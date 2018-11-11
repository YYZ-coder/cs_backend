package com.cls.web.shop.controller;

import com.cls.common.backdata.JsonBackData;
import com.cls.common.base.BaseController;
import com.cls.common.exception.ClsException;
import com.cls.common.search.Search;
import com.cls.common.services._enum.STATUS;
import com.cls.common.services.shop.entity.ShopInfo;
import com.cls.common.services.shop.service.impl.ShopServiceImpl;
import com.cls.common.services.user.entity.UserInfo;
import com.cls.common.services.user.service.impl.UserServiceImpl;
import com.cls.common.stat.LogStat;
import com.cls.common.utils.DateUtil;
import com.cls.common.utils.DozerUtil;
import com.cls.web.shop.entity.dto.ShopInfoDTO;
import com.cls.web.shop.entity.vo.ShopSimpleVO;
import com.cls.web.user.AuthcTool;
import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Project: cs_backend
 * @author Yue
 * @create 2018/5/17-0:25
 * Description：
 */
@RequestMapping("/web/shop")
@Controller
public class ShopWebController extends BaseController{

    private static Logger logger = LoggerFactory.getLogger(ShopWebController.class);

    @Autowired
    private ShopServiceImpl shopService;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private AuthcTool authcTool;

    /**
     * 查询所有商铺信息
     * @param page
     * @param size
     * @return JsonBackData
     */
    @RequestMapping(value = "/queryAll", method = RequestMethod.POST)
    @ResponseBody
    public JsonBackData queryAll(
            @RequestParam("page") @NotNull @Valid @Min(1) Integer page,
            @RequestParam("size") @NotNull @Valid @Min(1) Integer size
    ){
        JsonBackData jsonBackData = new JsonBackData(true,200);
        try{
            authcTool.verifyAdminAuthc();
            Search search = new Search();
            search.put("status",STATUS.YES.code);
            int offset = (page -1)*size;
            List<ShopInfo> shopInfos = shopService.queryAllByRowBounds(search,new RowBounds(offset, size));
            if(CollectionUtils.isEmpty(shopInfos)){
                throw new ClsException(LogStat.NO_DATA);
            }
            List<ShopSimpleVO> shopSimpleVOS = DozerUtil.mapToList(shopInfos, ShopSimpleVO.class);
            List<Integer> uids = shopSimpleVOS.stream().map(ShopSimpleVO::getUid).collect(Collectors.toList());
            Search search1 = new Search();
            search1.put("id_in",uids);
            List<UserInfo> userInfos = userService.queryByProperties(search1,"");
            if(CollectionUtils.isEmpty(userInfos)){
                throw new ClsException(LogStat.NO_DATA);
            }
            Map<Integer, UserInfo> userInfoMap = new HashMap<>();
            for(UserInfo userInfo : userInfos){
                userInfoMap.put(userInfo.getId(), userInfo);
            }
            for(ShopSimpleVO shopSimpleVO : shopSimpleVOS){
                shopSimpleVO.setAccountName(userInfoMap.get(shopSimpleVO.getUid()).getAccountName());
            }
            jsonBackData.setBackData(shopSimpleVOS);
        }catch(Throwable t){
            logger.warn(t.toString());
            wrapException(jsonBackData,t);
        }
        return jsonBackData;
    }


    /**
     * 新建店铺
     * @param shopInfoDTO
     * @return
     */
    @RequestMapping(value = "/new", method = RequestMethod.POST)
    @ResponseBody
    public JsonBackData newOne(
            @RequestBody @Valid @NotNull ShopInfoDTO shopInfoDTO
    ){
        JsonBackData jsonBackData = new JsonBackData(true,200);
        try{
            authcTool.verifyOwnerAuthc();
            ShopInfo shopInfo = DozerUtil.mapTo(shopInfoDTO, ShopInfo.class);
            shopInfo.setUid(getUID());
            shopInfo.setCreateUser(getUID());
            shopInfo.setCreateTime(DateUtil.getCurrentMili());
            shopInfo.setUid(getUID());
            shopService.addNew(shopInfo);
            jsonBackData.setBackData("新增店铺成功");
        }catch (Throwable e){
            logger.warn(e.toString());
            wrapException(jsonBackData,e);
        }
        return jsonBackData;
    }

    /**
     * 搜索
     * @param key
     * @param page
     * @param size
     * @return
     */
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    @ResponseBody
    public JsonBackData search(
            @RequestParam("key") @NotNull @Valid String key,
            @RequestParam("page") @NotNull @Valid @Min(1) Integer page,
            @RequestParam("size") @NotNull @Valid @Min(1) Integer size
    ){
        JsonBackData jsonBackData = new JsonBackData(true,200);
        try{
            authcTool.verifyAdminAuthc();
            int offset = (page-1) * size;
            List<ShopInfo> shopInfos = shopService.queryByRestriction("shopName", "%"+ key +"%", new RowBounds(offset, size));
            if(CollectionUtils.isEmpty(shopInfos)){
                throw new ClsException(LogStat.NO_DATA);
            }
            List<Integer> uids = shopInfos.stream().map(ShopInfo::getUid).collect(Collectors.toList());
            Search search = new Search();
            search.put("id_in",uids);
            search.put("status",STATUS.YES.code);
            List<UserInfo> userInfos = userService.queryUserInfo(search);
            if(CollectionUtils.isEmpty(userInfos)){
                throw new ClsException(LogStat.NO_DATA);
            }
            Map<Integer, UserInfo> userInfoMap = new HashMap<>();
            for(UserInfo userInfo : userInfos){
                userInfoMap.put(userInfo.getId(), userInfo);
            }
            List<ShopSimpleVO> shopSimpleVOS = DozerUtil.mapToList(shopInfos, ShopSimpleVO.class);
            for(ShopSimpleVO shopSimpleVO : shopSimpleVOS){
                shopSimpleVO.setAccountName(userInfoMap.get(shopSimpleVO.getUid()).getAccountName());
            }
            jsonBackData.setBackData(shopSimpleVOS);
        }catch(Throwable t){
            logger.warn(t.toString());
            wrapException(jsonBackData,t);
        }
        return jsonBackData;
    }

    /**
     * 更新店铺信息
     * @param shopInfoDTO
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public JsonBackData updateShop(
            @RequestBody @Valid @NotNull ShopInfoDTO shopInfoDTO
    ){
        JsonBackData jsonBackData = new JsonBackData(true,200);
        try{
            //1. 更新店铺资源（图片）
            //2. 更新店铺信息
            ShopInfo shopInfo = DozerUtil.mapTo(shopInfoDTO, ShopInfo.class);
            Search search = new Search();
            search.put("id",shopInfoDTO.getId());
            shopService.updateInfoByExample(shopInfo, search);
        }catch (Throwable e){
            logger.warn(e.toString());
            wrapException(jsonBackData,e);
        }
        return jsonBackData;
    }

    /**
     * 删除店铺
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public JsonBackData deleteShop(
            @RequestBody @NotNull @Valid List<Integer> sids
    ){
        JsonBackData jsonBackData = new JsonBackData(true,200);
        try{
            authcTool.verifyAdminAuthc();
            Integer uid = getUID();
            Search search = new Search();
            search.put("id_in",sids);
            List<ShopInfo> shopInfos = shopService.queryByProperties(search,"updateTime_desc");
            if(CollectionUtils.isEmpty(shopInfos)){
                throw new ClsException(LogStat.NO_DATA);
            }
            ShopInfo shopInfo = new ShopInfo();
            shopInfo.setUpdateUser(uid);
            shopInfo.setStatus(STATUS.NO.code);
            shopInfo.setUpdateTime(DateUtil.getCurrentMili());
            shopService.updateInfoByExample(shopInfo,new Search().put("id_in",sids));
            jsonBackData.setBackData("删除店铺成功");
        }catch (Throwable e){
            logger.warn(e.toString());
            wrapException(jsonBackData,e);
        }
        return jsonBackData;
    }

}
