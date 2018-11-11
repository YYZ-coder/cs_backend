package com.cls.wx.shop.controller;

import com.cls.common.backdata.JsonBackData;
import com.cls.common.base.BaseController;
import com.cls.common.context.WebSession;
import com.cls.common.exception.ClsException;
import com.cls.common.search.Search;
import com.cls.common.stat.LogStat;
import com.cls.common.utils.DozerUtil;
import com.cls.common.services._enum.ATTRIBUTE;
import com.cls.common.services._enum.BEHAVIOR;
import com.cls.common.services._enum.STATUS;
import com.cls.common.services._enum.TYPE;
import com.cls.common.services.resource.entity.PicInfo;
import com.cls.common.services.resource.service.impl.ResourceServiceImpl;
import com.cls.common.services.resource.vo.PicInfoVO;
import com.cls.common.services.shop.entity.ShopInfo;
import com.cls.common.services.shop.service.impl.ShopServiceImpl;
import com.cls.wx.shop.vo.ShopInfoVO;
import com.cls.common.services.behavior.entity.UserOperation;
import com.cls.common.services.behavior.service.impl.OperationServiceImpl;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Project: cs_backend
 * @author Yue
 * @create 2018/4/22-3:23
 * Description：
 *      店铺操作
 */
@Controller
@RequestMapping("/wx/sh")
public class ShopController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(ShopController.class);

    @Autowired
    private ShopServiceImpl shopService;

    @Autowired
    private OperationServiceImpl operationService;

    @Autowired
    private ResourceServiceImpl resourceService;

    /**
     * 包装shop的资源文件
     * @param shopInfoVO
     */
    private void wrapResourceIn(ShopInfoVO shopInfoVO){
        Search search = new Search();
        search.put("type", TYPE.shop.code);
        search.put("objId", shopInfoVO.getId());
        List<PicInfo> picInfos = resourceService.queryByProperties(search,"");
        if(CollectionUtils.isNotEmpty(picInfos)){
            List<PicInfoVO> picInfoVOS = DozerUtil.mapToList(picInfos, PicInfoVO.class);
            List<PicInfoVO> detailPics = picInfoVOS.stream().filter(picInfo -> TYPE.detailPic.code.equals(
                    picInfo.getAttribute())).collect(Collectors.toList());
            List<PicInfoVO> thumbPics = picInfoVOS.stream().filter(picInfoVO -> TYPE.thumbPic.code.equals(
                    picInfoVO.getAttribute())).collect(Collectors.toList());
            shopInfoVO.setDetailPicUrls(detailPics);
            shopInfoVO.setThumbPics(thumbPics);
        }
    }

    /**
     *
     * @param shopInfoVOS
     */
    private void wrapResourceIn(List<ShopInfoVO> shopInfoVOS){
        List<Integer> ids = shopInfoVOS.stream().map(ShopInfoVO::getId).collect(Collectors.toList());
        Search search = new Search();
        search.put("objId_in", ids);
        search.put("type", TYPE.shop.code);
        search.put("attribute", ATTRIBUTE.THUMB_PIC.code);
        List<PicInfo> picInfos = resourceService.queryByProperties(search,"");
        if(CollectionUtils.isEmpty(picInfos)){
            throw new ClsException(LogStat.NO_PICS);
        }
        List<PicInfoVO> picInfoVOS = DozerUtil.mapToList(picInfos, PicInfoVO.class);
        Map<Integer, PicInfoVO> shopIdUrlMap = new HashMap<>();
        for(PicInfoVO picInfo : picInfoVOS){
            shopIdUrlMap.put(picInfo.getObjId(), picInfo);
        }
        for(ShopInfoVO shopInfoVO : shopInfoVOS){
            shopInfoVO.setThumbPics(Lists.newArrayList(shopIdUrlMap.get(shopInfoVO.getId())));
        }
    }


    /**
     * 查询收藏的商铺
     * @return
     */
    @RequestMapping(value = "/queryliked", method = RequestMethod.POST)
    @ResponseBody
    public JsonBackData queryLiked(){
        JsonBackData jsonBackData = new JsonBackData(true, 200);
        try{
            Integer uid = getUID();
            Search search = new Search();
            search.put("userId",uid);
            search.put("status", STATUS.YES.code);
            search.put("behavior", BEHAVIOR.LIKE.code);
            search.put("type", TYPE.shop.code);
            List<UserOperation> userOperations = operationService.queryByProperties(search,"");
            if(CollectionUtils.isEmpty(userOperations)){
                throw new ClsException(LogStat.NO_DATA);
            }
            List<Integer> ids = userOperations.stream().map(UserOperation::getObjId).collect(Collectors.toList());
            Search search1 = new Search();
            search1.put("id_in", ids);
            List<ShopInfo> shopInfos = shopService.queryByProperties(search1, "updateTime_desc");
            if(CollectionUtils.isEmpty(shopInfos)){
                throw new ClsException(LogStat.NO_DATA);
            }
            List<ShopInfoVO> shopInfoVOS = DozerUtil.mapToList(shopInfos, ShopInfoVO.class);
            wrapResourceIn(shopInfoVOS);
            jsonBackData.setBackData(shopInfoVOS);
        }catch (Throwable t){
            logger.warn(t.toString());
            wrapException(jsonBackData, t);
        }
        return jsonBackData;
    }

    /**
     * 查询店铺详细信息
     * @param shopId
     * @return JsonBackData
     */
    @RequestMapping(value = "/querysh", method = RequestMethod.POST)
    @ResponseBody
    public JsonBackData queryShop(
            @RequestParam("shopId") @Valid @NotNull @Min(1) Integer shopId
    ){
        JsonBackData jsonBackData = new JsonBackData(true,200);
        try{
            Integer cuid = (Integer)WebSession.getVal("cuid");
            ShopInfo shopInfo = shopService.queryById(shopId);
            if(shopInfo == null){
                throw new ClsException(LogStat.NO_DATA, "当前商品信息异常");
            }
            Integer liked = 0;//不喜欢
            if(cuid != null && !cuid.equals(0)){
                List<UserOperation> userOperationList = operationService.queryByProperties(
                        new Search().put("objId",shopId).put("userId",cuid).put("type",TYPE.shop.code).put("behavior", BEHAVIOR.LIKE.code),"");
                if(CollectionUtils.isNotEmpty(userOperationList)){
                    liked = 1;//喜欢
                }
            }
            ShopInfoVO shopInfoVO = DozerUtil.mapTo(shopInfo, ShopInfoVO.class);
            wrapResourceIn(shopInfoVO);
            shopInfoVO.setLiked(liked);
            jsonBackData.setBackData(shopInfoVO);
        }catch (Throwable e){
            logger.warn(e.getCause().toString());
            wrapException(jsonBackData,e);
        }
        return jsonBackData;
    }


    //-----------------------------Back-------------------------------


    //----------------------------------------------------------------
}
