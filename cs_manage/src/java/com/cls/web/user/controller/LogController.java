package com.cls.web.user.controller;

import com.cls.common.backdata.JsonBackData;
import com.cls.common.base.BaseController;
import com.cls.common.context.WebSession;
import com.cls.common.exception.ClsException;
import com.cls.common.search.Search;
import com.cls.common.services._enum.BEHAVIOR;
import com.cls.common.services._enum.STATUS;
import com.cls.common.services._enum.TYPE;
import com.cls.common.services.behavior.entity.UserOperation;
import com.cls.common.services.behavior.service.impl.OperationServiceImpl;
import com.cls.common.services.shop.entity.ShopInfo;
import com.cls.common.services.shop.service.impl.ShopServiceImpl;
import com.cls.common.services.user.entity.UserInfo;
import com.cls.common.services.user.service.impl.UserServiceImpl;
import com.cls.common.session.TokenSession;
import com.cls.common.stat.LogStat;
import com.cls.common.utils.*;
import com.cls.web.shop.entity.dto.ShopInfoDTO;
import com.cls.web.user.ROLE;
import com.cls.web.user.entity.dto.UserInfoDTO;
import com.cls.web.user.entity.vo.UserInfoVO;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.validator.constraints.Length;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * Project: cs_backend
 * @author Yue
 * @create 2018/5/14-16:43
 * Description：
 */
@RequestMapping("/web/log")
@Controller
public class LogController extends BaseController{

    private static Logger logger = LoggerFactory.getLogger(LogController.class);

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private ShopServiceImpl shopService;

    @Autowired
    private OperationServiceImpl operationService;

    /**
     * 注册
     * @param userInfoDTO
     * @return
     */
    @RequestMapping(value = "/regin", method = RequestMethod.POST)
    @ResponseBody
    public JsonBackData reginUser(@RequestBody @Valid @NotNull UserInfoDTO userInfoDTO){
        JsonBackData jsonBackData = new JsonBackData(true, 200);
        try{
            //检查账户名是否已重复
            String acName = userInfoDTO.getAccountName();
            List<UserInfo> userInfos = userService.queryByProperties(new Search().put("accountName",acName),"");
            if(!CollectionUtils.isEmpty(userInfos)){
                throw new ClsException(LogStat.ACCOUNT_NAME_REPEAT);
            }
            UserInfo userInfo = DozerUtil.mapTo(userInfoDTO, UserInfo.class);
            userInfo.setCreateTime(DateUtil.getCurrentMili());
            userInfo.setCreateUser(0);
            userInfo.setUpdateTime(DateUtil.getCurrentMili());
            userInfo.setUpdateUser(0);
            userInfo.setRoleId(ROLE.SHOP_OWNER.code);
            userInfo.setPassWord(EncryUtil.encryptByMD5(userInfo.getPassWord()));
            Integer uid = userService.addUser(userInfo);
            if(0 == uid){
                throw new ClsException(LogStat.REG_FASE);
            }
            //创建token并返回
            Integer rid = userInfo.getRoleId();
            String token = EncryUtil.encryptByMD5(DateUtil.getCurrentMili().toString() + userInfo.getId());
            TokenSession.saveSession(token, userInfo.getId()+"_"+rid , String.class);
            jsonBackData.setBackData(token);
        }catch(Throwable t){
            logger.warn(t.toString());
            wrapException(jsonBackData, t);
        }
        return jsonBackData;
    }

    /**
     * 注册商铺信息
     * @param shopInfoDTO
     * @return
     */
    @RequestMapping(value = "/regshop", method = RequestMethod.POST)
    @ResponseBody
    public JsonBackData regInShop(@RequestBody @NotNull ShopInfoDTO shopInfoDTO){
        JsonBackData jsonBackData = new JsonBackData(true,200);
        try{
            Integer uid = getUID();
            //商铺名不重复
            List<ShopInfo> shopInfos = shopService.queryByProperties(new Search().put("shopName",shopInfoDTO.getShopName()),"");
            if(CollectionUtils.isNotEmpty(shopInfos)){
                throw new ClsException(LogStat.SHOP_NAME_REPEAT);
            }
            ShopInfo shopInfo = DozerUtil.mapTo(shopInfoDTO, ShopInfo.class);
            shopInfo.setCreateTime(DateUtil.getCurrentMili());
            shopInfo.setUpdateTime(DateUtil.getCurrentMili());
            shopInfo.setUid(uid);
            shopInfo.setUpdateUser(uid);
            shopInfo.setCreateUser(uid);
            shopInfo.setStarLevel(1);//默认一星
            if(0 == shopService.addNew(shopInfo)){
                throw new ClsException(LogStat.DATA_FALSE);
            }
            //插入一条浏览行为
            UserOperation userOperation = new UserOperation();
            userOperation.setType(TYPE.shop.code);
            userOperation.setObjId(shopInfo.getId());
            userOperation.setCreateTime(DateUtil.getCurrentMili());
            userOperation.setCreateUser(uid);
            userOperation.setUpdateTime(DateUtil.getCurrentMili());
            userOperation.setUpdateUser(uid);
            userOperation.setBehavior(BEHAVIOR.VIEW.code);
            userOperation.setUserId(uid);
            if(0 == operationService.addNew(userOperation)){
                throw new ClsException(LogStat.INSERT_FAILED);
            }
            jsonBackData.setBackData(shopInfo.getId());
        }catch(Throwable t){
            logger.warn(t.toString());
            wrapException(jsonBackData,t);
        }
        return jsonBackData;
    }

    /**
     * 登出
     * @return
     */
    @RequestMapping(value = "/out", method = RequestMethod.POST)
    @ResponseBody
    public JsonBackData logOut(HttpServletRequest request){
        JsonBackData jsonBackData = new JsonBackData(true,200);
        try{
            String token = request.getHeader("token");
            TokenSession.removeSession(token);
        }catch(Throwable t){
            logger.warn(t.toString());
            wrapException(jsonBackData, t);
        }
        return jsonBackData;
    }

    /**
     * web登录
     * @param username
     * @param password
     * @return
     */
    @RequestMapping(value = "/in", method = RequestMethod.POST)
    @ResponseBody
    public JsonBackData logIn(
            @RequestParam("uname") @NotNull @Valid @Length(min = 1) String username,
            @RequestParam("pword") @NotNull @Valid @Length(min = 5) String password
    ){
        JsonBackData jsonBackData = new JsonBackData(true, 200);
        try{
            //同时判断另一个token是否过期
            //没过期则禁止登录
            Integer roleId = (Integer) WebSession.getVal("rid");
            Search search = new Search();
            search.put("roleId", roleId);
            search.put("accountName", username);
            search.put("status", STATUS.YES.code);
            search.put("locked", 0);//未锁定
            List<UserInfo> userInfoList = userService.queryUserInfo(search);
            if(CollectionUtils.isEmpty(userInfoList)){
                throw new ClsException(LogStat.NOTEXIST_ACCOUNT);
            }
            if(userInfoList.size() > 1){
                throw new ClsException(LogStat.DATA_FALSE);
            }
            UserInfo userInfo = userInfoList.get(0);
            userInfo.setUpdateTime(DateUtil.getCurrentMili());
            userInfo.setUpdateUser(userInfo.getId());
            userInfo.setLogCount(userInfo.getLogCount() + 1);
            String pass = EncryUtil.encryptByMD5(password);
            if(!userInfo.getPassWord().equals(pass)){
                throw new ClsException(LogStat.ERROR_PASSWORD);
            }
            userService.updateWebLogRecord(userInfo);
            String token = EncryUtil.encryptByMD5(DateUtil.getCurrentMili().toString() + userInfo.getId());
            TokenSession.saveSession(token, userInfo.getId()+"_"+roleId , String.class);
            jsonBackData.setBackData(token);
        }catch(Throwable t){
            logger.warn(t.toString());
            wrapException(jsonBackData, t);
        }
        return jsonBackData;
    }

    /**
     * verify token
     * @param request
     * @return
     */
    @RequestMapping(value = "/verify", method = RequestMethod.POST)
    @ResponseBody
    public JsonBackData verifyToken(HttpServletRequest request){
        JsonBackData jsonBackData = new JsonBackData(true, 200);
        try{
            String token = request.getHeader("token");
            String v = TokenSession.getIfPresent(token);
            if(v == null || v.equals("")){
                throw new ClsException(LogStat.NO_AUTHC);
            }
        }catch(Throwable t){
            logger.warn(t.toString());
            wrapException(jsonBackData, t);
        }
        return jsonBackData;
    }
}
