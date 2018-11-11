package com.cls.web.user.controller;

import com.cls.common.backdata.JsonBackData;
import com.cls.common.base.BaseController;
import com.cls.common.context.WebSession;
import com.cls.common.exception.ClsException;
import com.cls.common.search.Search;
import com.cls.common.services._enum.STATUS;
import com.cls.common.services._enum.TYPE;
import com.cls.common.services.commodity.service.impl.CommodityServiceImpl;
import com.cls.common.services.role.entity.RoleInfo;
import com.cls.common.services.role.service.impl.RoleServiceImpl;
import com.cls.common.services.shop.entity.ShopInfo;
import com.cls.common.services.shop.service.impl.ShopServiceImpl;
import com.cls.common.services.user.entity.UserInfo;
import com.cls.common.services.user.service.impl.UserServiceImpl;
import com.cls.common.stat.LogStat;
import com.cls.common.utils.DateUtil;
import com.cls.common.utils.DozerUtil;
import com.cls.web.user.AuthcTool;
import com.cls.web.user.ROLE;
import com.cls.web.user.entity.vo.RoleInfoVO;
import com.cls.web.user.entity.vo.UserInfoVO;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.json.Json;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Project: cs_backend
 * @author Yue
 * @create 2018/5/15-19:46
 * Description：
 */
@RequestMapping("/web/user")
@Controller
public class UserController extends BaseController{

    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private RoleServiceImpl roleService;

    @Autowired
    private ShopServiceImpl shopService;

    @Autowired
    private CommodityServiceImpl commodityService;

    @Autowired
    private AuthcTool authcTool;

    /**
     * 包装用户信息
     * @param userInfoVOList
     */
    private void wrapUserInfo(List<UserInfoVO> userInfoVOList){
        List<Integer> roleIds = userInfoVOList.stream().map(UserInfoVO::getRoleId).collect(Collectors.toList());
        Search search = new Search();
        search.put("id_in", roleIds);
        search.put("status", STATUS.YES.code);
        List<RoleInfo> roleInfos = roleService.queryByProperties(search,"");
        if(CollectionUtils.isEmpty(roleInfos)){
            throw new ClsException(LogStat.NO_ROLES);
        }
        List<RoleInfoVO> roleInfoVOS = DozerUtil.mapToList(roleInfos, RoleInfoVO.class);
        Map<Integer, RoleInfoVO> idRoleInfoMap = new HashMap<>();
        for(RoleInfoVO roleInfo : roleInfoVOS){
            idRoleInfoMap.put(roleInfo.getId(), roleInfo);
        }
        for(UserInfoVO userInfoVO : userInfoVOList) {
            userInfoVO.setRoleInfo(idRoleInfoMap.get(userInfoVO.getRoleId()));
            if(userInfoVO.getGender() != null && userInfoVO.getGender().equals(TYPE.MAN.code)){
                userInfoVO.setSex("男");
            }else if(userInfoVO.getGender() != null && userInfoVO.getGender().equals(TYPE.WOMAN.code)){
                userInfoVO.setSex("女");
            }
        }
    }

    /**
     * 性别转换
     * @param key
     * @return
     */
    private Integer transferSex(String key){
        String strmed = key.trim();
        if(strmed == null || strmed.equals("")){
            throw new ClsException(LogStat.ERROR_PARAM);
        }
        if(strmed.equals("男")){
            return TYPE.MAN.code;
        }else if(strmed.equals("女")){
            return TYPE.WOMAN.code;
        }
        return null;
    }

    /**
     * 批量删除用户
     * @param uids
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public JsonBackData deleteUser(@RequestBody @NotNull @Valid List<Integer> uids){
        JsonBackData jsonBackData = new JsonBackData(true,200);
        try{
            authcTool.verifyAdminAuthc();
            Integer uid = getUID();
            Search sea = new Search();
            sea.put("id_in",uids);
            sea.put("status",STATUS.YES.code);
            List<UserInfo> userInfos = userService.queryUserInfo(sea);
            if(CollectionUtils.isEmpty(userInfos)){
                throw new ClsException(LogStat.NO_DATA);
            }
            //如果是店主
            for(UserInfo uis : userInfos){
                if(uis.getRoleId().equals(ROLE.SHOP_OWNER.code)){
                    //删除店主下所有商铺.商铺下所有商品
//                    Search searchsh = new Search();
//                    searchsh.put("uid",uis.getId());
//                    List<ShopInfo> shopInfos = shopService.queryByProperties(searchsh,"");
                }
            }
            Search search = new Search();
            search.put("id_in",uids);
            UserInfo userInfo = new UserInfo();
            userInfo.setUpdateUser(uid);
            userInfo.setUpdateTime(DateUtil.getCurrentMili());
            userInfo.setStatus(STATUS.NO.code);
            userService.updateInfoByExample(userInfo, search);
            jsonBackData.setBackData("删除成功");
        }catch(Throwable t){
            logger.warn(t.toString());
            wrapException(jsonBackData,t);
        }
        return jsonBackData;
    }

    /**
     * 搜索base accountName、gender
     * @param key
     * @return JsonBackData
     */
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    @ResponseBody
    public JsonBackData search(@RequestParam("key") String key,
                               @RequestParam("type") Integer type){
        JsonBackData jsonBackData = new JsonBackData(true, 200);
        try{
            authcTool.verifyAdminAuthc();
            Search search = new Search();
            if(type.equals(TYPE.GENDER.code)){
                Integer gender = transferSex(key);
                if(gender == null){
                    throw new ClsException(LogStat.ERROR_PARAM);
                }
                search.put("gender", gender);
            }else if(type.equals(TYPE.ACCOUNT_NAME.code)){
                search.put("accountName_like","%" + key + "%");
            }
            List<UserInfo> userInfos = userService.queryByRestriction("accountName","%"+ key +"%");
            if(CollectionUtils.isEmpty(userInfos)){
                throw new ClsException(LogStat.NO_DATA);
            }
            List<UserInfoVO> userInfoVOS = DozerUtil.mapToList(userInfos, UserInfoVO.class);
            wrapUserInfo(userInfoVOS);
            jsonBackData.setBackData(userInfoVOS);
        }catch(Throwable t){
            logger.warn(t.toString());
            wrapException(jsonBackData, t);
        }
        return jsonBackData;
    }

    /**
     * 查询全部User数据
     * @return
     */
    @RequestMapping(value = "/queryAll", method = RequestMethod.POST)
    @ResponseBody
    public JsonBackData queryAllUser(){
        JsonBackData jsonBackData = new JsonBackData(true,200);
        try{
            //鉴权--是否为管理员
            Integer uid = (Integer)WebSession.getVal("cuid");
            Integer rid = (Integer)WebSession.getVal("rid");
            Search search1 = new Search();
            search1.put("id", uid);
            search1.put("status",STATUS.YES.code);
            List<UserInfo> userInfoList = userService.queryUserInfo(search1);
            if(CollectionUtils.isEmpty(userInfoList)){
                throw new ClsException(LogStat.NOTEXIST_ACCOUNT);
            }
            //用户id与rid匹配
            if(!rid.equals(userInfoList.get(0).getRoleId())){
                throw new ClsException(LogStat.DATA_FALSE);
            }
            if(!userInfoList.get(0).getRoleId().equals(ROLE.ADMIN.code)){
                throw new ClsException(LogStat.NO_AUTHC);
            }
            Search search = new Search();
            search.put("status", STATUS.YES.code);
            List<UserInfo> userInfos = userService.queryUserInfo(search);
            if(CollectionUtils.isEmpty(userInfos)){
                throw new ClsException(LogStat.NO_DATA);
            }
            List<UserInfoVO> userInfoVOS = DozerUtil.mapToList(userInfos, UserInfoVO.class);
            wrapUserInfo(userInfoVOS);
            jsonBackData.setBackData(userInfoVOS);
        }catch(Throwable t){
            logger.warn(t.toString());
            wrapException(jsonBackData, t);
        }
        return  jsonBackData;
    }

}
