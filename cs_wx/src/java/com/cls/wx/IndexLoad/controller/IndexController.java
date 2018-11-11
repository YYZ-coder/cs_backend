package com.cls.wx.IndexLoad.controller;

import com.cls.common.backdata.JsonBackData;
import com.cls.common.base.BaseController;
import com.cls.common.context.WebSession;
import com.cls.common.exception.ClsException;
import com.cls.common.search.Search;
import com.cls.common.stat.LogStat;
import com.cls.common.utils.DozerUtil;
import com.cls.common.services.PublicService;
import com.cls.common.services._enum.ATTRIBUTE;
import com.cls.common.services._enum.TYPE;
import com.cls.common.services.behavior.service.impl.OperationServiceImpl;
import com.cls.common.services.commodity.entity.CommodityInfo;
import com.cls.common.services.resource.entity.PicInfo;
import com.cls.common.services.shop.entity.ShopInfo;
import com.cls.common.services.commodity.vo.CommodityShortVO;
import com.cls.wx.IndexLoad.vo.IndexDataVO;
import com.cls.wx.shop.vo.ShopShortVO;
import com.cls.common.services.commodity.service.impl.CommodityServiceImpl;
import com.cls.common.services.resource.service.impl.ResourceServiceImpl;
import com.cls.common.services.shop.service.impl.ShopServiceImpl;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Project: cs_backend
 * @author Yue
 * @create 2018/4/6-19:14
 * Description：
 *      加载主页数据
 */
@Controller
@RequestMapping("/wx/index")
public class IndexController extends BaseController{

    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    private CommodityServiceImpl commodityService;

    @Autowired
    private ShopServiceImpl shopService;

    @Autowired
    private ResourceServiceImpl resourceService;

    @Autowired
    private OperationServiceImpl operationService;

    /**
     * 加载非推荐系统推荐主页数据: 保证不重复
     * @param swiperCount
     * @param hotsCount
     * @param newsCount
     * @param shopCount
     * @return IndexDataVO
     */
    private IndexDataVO loadNonRecommend(Integer swiperCount, Integer hotsCount, Integer newsCount, Integer shopCount){
        IndexDataVO indexDataVO = new IndexDataVO();
        List<CommodityInfo> commodityInfoNews = commodityService.queryByNews(1,newsCount);
        List<CommodityInfo> commodityInfoHots = commodityService.queryByViews(1,hotsCount);
        List<ShopInfo> shopInfos = shopService.queryHotShops(shopCount);
        //constructor Map
        Map<Integer, PicInfo> commIdPicMap = new HashMap<>();
        Map<Integer, PicInfo> shopIdPicMap = new HashMap<>();
        //查出所有商品的缩略图和对应类型、id
        List<Integer> hotCommIds = commodityInfoHots.stream().map(CommodityInfo::getId).collect(Collectors.toList());
        Set<Integer> commIds = new HashSet<>(hotCommIds);
        List<Integer> newsCommIds = commodityInfoNews.stream().map(CommodityInfo::getId).collect(Collectors.toList());
        List<Integer> shopIds = shopInfos.stream().map(ShopInfo::getId).collect(Collectors.toList());
        for(Integer id : newsCommIds){
            commIds.add(id);
        }
        //商铺的pic url
        Search search = new Search();
        search.put("objId_in",shopIds);
        search.put("type",TYPE.shop.code);
        List<PicInfo> shopPic = resourceService.queryByProperties(search,"");
        //comm的pic url
        Search chsearch = new Search();
        chsearch.put("type",TYPE.commodity.code);
        chsearch.put("objId_in",Lists.newArrayList(commIds));
        List<PicInfo> commPic = resourceService.queryByProperties(chsearch,"");
        if(CollectionUtils.isNotEmpty(commPic)){
            for(PicInfo picInfo : commPic){
                commIdPicMap.put(picInfo.getObjId(), picInfo);
            }
        }
        if(CollectionUtils.isNotEmpty(shopPic)){
            for(PicInfo picInfo : shopPic){
                if(picInfo.getAttribute().equals(ATTRIBUTE.THUMB_PIC.code)){
                    shopIdPicMap.put(picInfo.getObjId(), picInfo);
                }
            }
        }
        List<CommodityShortVO> commodityShortNews = DozerUtil.mapToList(commodityInfoNews, CommodityShortVO.class);
        List<CommodityShortVO> commodityShortHot = DozerUtil.mapToList(commodityInfoHots, CommodityShortVO.class);
        List<ShopShortVO> shopShort = DozerUtil.mapToList(shopInfos, ShopShortVO.class);
        for(CommodityShortVO commodityShortVO : commodityShortNews){
            PicInfo picInfo = commIdPicMap.get(commodityShortVO.getId());
            if(picInfo != null){
                commodityShortVO.setThumbPicUrl(picInfo.getUrl());
            }
        }
        for(CommodityShortVO commodityShortVO : commodityShortHot){
            PicInfo picInfo = commIdPicMap.get(commodityShortVO.getId());
            if(picInfo != null){
                commodityShortVO.setThumbPicUrl(picInfo.getUrl());
            }
        }
        for(ShopShortVO shopShortVO : shopShort){
            PicInfo p = shopIdPicMap.get(shopShortVO.getId());
            if(p != null){
                shopShortVO.setThumbPicUrl(p.getUrl());
            }
        }
        indexDataVO.setShops(shopShort);
        indexDataVO.setNewsObjects(commodityShortNews);
        indexDataVO.setHotObjects(commodityShortHot);
        return indexDataVO;
    }

    /**
     * 转换函数CommodityInfo -> CommodityShortInfo
     * @param commodityInfos
     * @return
     */
    private List<CommodityShortVO> wrapToCommodityShortVO(List<CommodityInfo> commodityInfos){
        logger.info("wrapToCommodityShortVO");
        List<CommodityShortVO> commodityShortVOS = Lists.newArrayList();
        for(CommodityInfo commodityInfo : commodityInfos){
            CommodityShortVO commodityShortVO = DozerUtil.mapTo(commodityInfo, CommodityShortVO.class);
            commodityShortVO.setType(TYPE.commodity.code);
            List<PicInfo> picInfos = resourceService.queryByDetail(TYPE.commodity.code,commodityInfo.getId(), ATTRIBUTE.THUMB_PIC.code);
            commodityShortVO.setThumbPicUrl(picInfos.get(0).getUrl());
            commodityShortVOS.add(commodityShortVO);
        }
        return commodityShortVOS;
    }

    /**
     * 转换函数 ShopInfo -> ShopShortInfo
     * @param shopInfos
     * @return
     */
    private List<ShopShortVO> wrapToShopShortVO(List<ShopInfo> shopInfos){
        logger.info("wrapToShopShortVO");
        List<ShopShortVO> shopShortVOS = Lists.newArrayList();
        for(ShopInfo shopInfo : shopInfos){
            ShopShortVO shopShortVO = DozerUtil.mapTo(shopInfo, ShopShortVO.class);
            shopShortVO.setType(TYPE.shop.code);
            List<PicInfo> picInfos = resourceService.queryByDetail(TYPE.shop.code,shopInfo.getId(), ATTRIBUTE.THUMB_PIC.code);
            shopShortVO.setThumbPicUrl(picInfos.get(0).getUrl());
            shopShortVOS.add(shopShortVO);
        }
        return shopShortVOS;
    }

    /**
     * 加载wx主页个人推荐系统信息(所有)
     * @return JsonBackData
     */
    @RequestMapping(value = "/load", method = RequestMethod.POST)
    @ResponseBody
    public JsonBackData loadIndex(
            @RequestParam(name = "swiperCount") @Valid @NotNull @Min(1) Integer swiperCount,
            @RequestParam(name = "hotCount") @Valid @NotNull  @Min(3) Integer hotsCount,
            @RequestParam(name = "newsCount") @Valid @NotNull @Min(3) Integer newsCount,
            @RequestParam(name = "shopsCount") @Valid @NotNull @Min(1) Integer shopCount
    ){
        JsonBackData jsonBackData = new JsonBackData(true,null,200);
        Integer uid = (Integer)WebSession.getVal("cuid");
        try{
            IndexDataVO indexDataVO = loadNonRecommend(swiperCount, hotsCount, newsCount, shopCount);
            if(indexDataVO == null || indexDataVO.getHotObjects() == null ||
                    indexDataVO.getNewsObjects() == null || indexDataVO.getShops() == null ){
                throw new ClsException(LogStat.QUERY_FAILED, "主页数据加载失败，请重试");
            }
            jsonBackData.setBackData(indexDataVO);
            if(uid == null || uid.equals(0)){

            } else {
                //TODO:加载推荐系统推荐数据
            }
        }catch (Throwable ex){
            logger.warn("loadIndex throw Exception :{}",ex.toString());
            wrapException(jsonBackData,ex);
        }
        return jsonBackData;
    }


    /**
     * 加载最新List
     * @param page
     * @param size
     * @return
     */
    @RequestMapping(value = "/morenews",method = RequestMethod.POST)
    @ResponseBody
    public JsonBackData moreNews(
            @RequestParam(name = "page") @Valid @NotNull @Min(1) Integer page,
            @RequestParam(name = "size") @Valid @NotNull @Min(1) Integer size){
        logger.info("com.cls.wx.IndexLoad.controller.IndexController$moreNews(page:{},size:{})",page,size);
        JsonBackData jsonBackData = new JsonBackData(true,200);
        Integer cuid = (Integer)WebSession.getVal("cuid");
        try{
            List<CommodityInfo> commodityInfos = commodityService.queryByNews(page,size);
            if(CollectionUtils.isEmpty(commodityInfos)){
                throw new ClsException(LogStat.NO_DATA, "无数据");
            }
            List<CommodityShortVO> commodityShortVOS = wrapToCommodityShortVO(commodityInfos);
            PublicService.wrapSellsCount(operationService, commodityShortVOS);
            jsonBackData.setBackData(commodityShortVOS);
            if(cuid == null || cuid.equals(0)){

            }else{
                //TODO:推荐系统推荐数据
            }
        }catch (Throwable e){
            logger.warn("morenews throw Exception:{}",e.toString());
            wrapException(jsonBackData,e);
        }
        return jsonBackData;
    }


    /**
     * 加载最热List
     * @param page
     * @param size
     * @return
     */
    @RequestMapping(value = "/morehots", method = RequestMethod.POST)
    @ResponseBody
    public JsonBackData moreHots(
            @RequestParam(name = "page") @Valid @NotNull @Min(1) Integer page,
            @RequestParam(name = "size") @Valid @NotNull @Min(1) Integer size){
        logger.info("com.cls.wx.IndexLoad.controller.IndexController$moreHots(page:{},size:{})",page,size);
        JsonBackData jsonBackData = new JsonBackData(true,200);
        Integer cuid = (Integer) WebSession.getVal("cuid");
        try{
            List<CommodityInfo> commodityInfolist = commodityService.queryByViews(page,size);
            if(CollectionUtils.isEmpty(commodityInfolist)){
                throw new ClsException(LogStat.NO_DATA, "查不到最热列表");
            }
            List<CommodityShortVO> commodityShortVOS = wrapToCommodityShortVO(commodityInfolist);
            PublicService.wrapSellsCount(operationService, commodityShortVOS);
            jsonBackData.setBackData(commodityShortVOS);
            if(cuid == null || cuid.equals(0)){

            }else{
                //TODO: 加载推荐系统数据

            }
       }catch(Throwable t){
            logger.warn("moreHots throw Exception:{}",t.toString());
            wrapException(jsonBackData,t);
        }
        return jsonBackData;
    }


    /**
     *  加载商铺List
    * @param size
     * @return
     */
    @RequestMapping(value = "/moreshops", method = RequestMethod.POST)
    @ResponseBody
    public JsonBackData moreShops(
            @RequestParam(name = "page") @Valid @NotNull @Min(1) int page,
            @RequestParam(name = "size") @Valid @NotNull @Min(1) int size){
        JsonBackData jsonBackData = new JsonBackData(true,200);
        try{
            Integer cuid = (Integer) WebSession.getVal("cuid");
            List<ShopInfo> shopInfos = shopService.querySellsShops(page,size);
            if(CollectionUtils.isEmpty(shopInfos)){
                throw new ClsException(LogStat.NO_DATA, "查不到商铺列表");
            }
            jsonBackData.setBackData(wrapToShopShortVO(shopInfos));
            if(cuid == null || cuid.equals(0)){

            }else{
                //TODO:加载推荐系统推荐数据
            }
        }catch(Throwable e){
            logger.warn("moreShops throws Exception :{}", e.toString());
            wrapException(jsonBackData, e);
        }
        return jsonBackData;
    }
}
