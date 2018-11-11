package com.cls.wx.commodity.controller;

import com.cls.common.backdata.JsonBackData;
import com.cls.common.base.BaseController;
import com.cls.common.context.WebSession;
import com.cls.common.exception.ClsException;
import com.cls.common.search.Search;
import com.cls.common.stat.LogStat;
import com.cls.common.utils.DateUtil;
import com.cls.common.utils.DozerUtil;
import com.cls.common.services.PublicService;
import com.cls.common.services._enum.ATTRIBUTE;
import com.cls.common.services._enum.BEHAVIOR;
import com.cls.common.services._enum.STATUS;
import com.cls.common.services.address.entity.AddressInfo;
import com.cls.common.services.address.service.impl.AddressServiceImpl;
import com.cls.wx.address.vo.AddressInfoVO;
import com.cls.wx.cart.vo.CartCommVO;
import com.cls.common.services.commodity.service.impl.CommodityServiceImpl;
import com.cls.common.services.commodity.entity.CommodityInfo;
import com.cls.common.services.commodity.vo.CommodityShortVO;
import com.cls.common.services.evaluate.entity.EvaluationInfo;
import com.cls.wx.evaluate.vo.EvaluationVO;
import com.cls.common.services.order.entity.OrderInfo;
import com.cls.common.services.order.service.impl.OrderServiceImpl;
import com.cls.wx.order.vo.OrderCommodityVO;
import com.cls.wx.order.vo.OrderVO;
import com.cls.common.services.resource.vo.PicInfoVO;
import com.cls.common.services.behavior.entity.UserOperation;
import com.cls.common.services.resource.entity.PicInfo;
import com.cls.common.services.evaluate.service.impl.EvaluationServiceImpl;
import com.cls.common.services.resource.service.impl.ResourceServiceImpl;
import com.cls.common.services.shop.entity.ShopInfo;
import com.cls.wx.commodity.vo.CommodityVO;
import com.cls.common.services._enum.TYPE;
import com.cls.common.services.shop.service.impl.ShopServiceImpl;
import com.cls.common.services.behavior.service.impl.OperationServiceImpl;
import com.google.common.collect.Lists;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Project: cs_backend
 * @author Yue
 * @create 2018/4/22-0:41
 * Description：
 *      商品操作
 */
@Controller
@RequestMapping("/wx/comm")
public class CommodityController extends BaseController{

    private static final Logger logger = LoggerFactory.getLogger(CommodityController.class);

    @Autowired
    private CommodityServiceImpl commodityService;

    @Autowired
    private OperationServiceImpl operationService;

    @Autowired
    private ResourceServiceImpl resourceService;

    @Autowired
    private ShopServiceImpl shopService;

    @Autowired
    private EvaluationServiceImpl evaluationService;

    @Autowired
    private OrderServiceImpl orderService;

    @Autowired
    private AddressServiceImpl addressService;


    /**
     * 包装资源对象
     * @param commodityVO
     */
    private void wrapResourceIn(CommodityVO commodityVO){
        Search search = new Search();
        search.put("type", TYPE.commodity.code);
        search.put("objId", commodityVO.getId());
        List<PicInfo> picInfos = resourceService.queryByProperties(search,"");
        if(CollectionUtils.isNotEmpty(picInfos)){
            List<PicInfoVO> picInfoVOS = DozerUtil.mapToList(picInfos, PicInfoVO.class);
            List<PicInfoVO> detailPics = picInfoVOS.stream().filter(picInfo -> TYPE.detailPic.code.equals(
                    picInfo.getAttribute())).collect(Collectors.toList());
            List<PicInfoVO> swiperPics = picInfoVOS.stream().filter(picInfo -> TYPE.swiperPic.code.equals(
                    picInfo.getAttribute())).collect(Collectors.toList());
            commodityVO.setSwiperPicUrls(swiperPics);
            commodityVO.setDetailPicUrls(detailPics);
            List<PicInfoVO> thumbPics = picInfoVOS.stream().filter(picInfoVO -> TYPE.thumbPic.code.equals(picInfoVO.getAttribute())).collect(Collectors.toList());
            commodityVO.setThumbPic(thumbPics);
        }
    }

    /**
     * 包装ShopInfo
     * @param commodityVO
     */
    private void wrapShopInfo(CommodityVO commodityVO){
        ShopInfo shopInfo = shopService.queryById(commodityVO.getShopId());
        if(shopInfo != null){
            commodityVO.setShopName(shopInfo.getShopName());
        }
    }

    /**
     * 包装评论信息
     * @param commodityVO
     */
    private void wrapEvaluationInfo(CommodityVO commodityVO){
        commodityVO.setEvaluateCount(evaluationService.sumCountById(commodityVO.getId()));
        Search search = new Search();
        search.put("objId",commodityVO.getId());
        search.put("type", TYPE.commodity.code);
        search.put("status",STATUS.YES.code);
        List<EvaluationInfo> evaluationInfos = evaluationService.queryByProperties(search,"updateTime_desc");
        if(CollectionUtils.isNotEmpty(evaluationInfos)){
            EvaluationVO evaluationVO = DozerUtil.mapTo(evaluationInfos.get(0), EvaluationVO.class);
            commodityVO.setEvaluationVO(evaluationVO);
            //TODO:将评论数据放入缓存
            //TODO:未考虑评论数据过多的问题
        }
    }

    /**
     * commodities资源包装
     * @param cartCommVOS
     */
    private void wrapThumbsIn(List<CartCommVO> cartCommVOS){
        List<Integer> commIds = cartCommVOS.stream().map(CartCommVO::getId).collect(Collectors.toList());
        Search search = new Search();
        search.put("objId_in",commIds);
        search.put("attribute", ATTRIBUTE.THUMB_PIC.code);
        search.put("type",TYPE.commodity.code);
        List<PicInfo> picInfos = resourceService.queryByProperties(search,"");
        List<PicInfoVO> picInfoVOS = DozerUtil.mapToList(picInfos, PicInfoVO.class);
        Map<Integer, PicInfoVO> commIdPicMap = new HashMap<>();
        for(PicInfoVO picInfo : picInfoVOS){
            commIdPicMap.put(picInfo.getObjId(), picInfo);
        }
        for(CartCommVO cartCommVO : cartCommVOS){
            cartCommVO.setThumbPic(Lists.newArrayList(commIdPicMap.get(cartCommVO.getId())));
        }
    }

    /**
     * 包装资源文件
     * @param orderCommodityVOS
     */
    private void wrapResourceIn(List<OrderCommodityVO> orderCommodityVOS){
        List<Integer> ids = orderCommodityVOS.stream().map(OrderCommodityVO::getId).collect(Collectors.toList());
        Search search = new Search();
        search.put("objId_in", ids);
        search.put("type",TYPE.commodity.code);
        search.put("attribute", ATTRIBUTE.THUMB_PIC.code);
        List<PicInfo> picInfos = resourceService.queryByProperties(search,"");
        if(CollectionUtils.isEmpty(picInfos)){
            return;
        }
        Map<Integer, PicInfo> picInfoMap = new HashMap<>();
        for(PicInfo picInfo : picInfos){
            picInfoMap.put(picInfo.getObjId(),picInfo);
        }
        for(OrderCommodityVO commodityVO : orderCommodityVOS){
            commodityVO.setThumbPic(picInfoMap.get(commodityVO.getId()).getUrl());
        }
    }

    /**
     * 包装正在/已经购买信息List
     * @param orderVOS
     * @param userOperations
     * @param commodityInfos
     * @param orderInfos
     */
    private void wrapBuyList(List<OrderVO> orderVOS,
                             List<UserOperation> userOperations,
                             List<CommodityInfo> commodityInfos,
                             List<OrderInfo> orderInfos,
                             Integer type
    ) throws InvocationTargetException, IllegalAccessException {
        List<OrderCommodityVO> orderCommodityVOS = DozerUtil.mapToList(commodityInfos, OrderCommodityVO.class);
        wrapResourceIn(orderCommodityVOS);
        Map<Integer, List<OrderCommodityVO>> orderIdCommsMap = new HashMap<>();
        Map<Integer, OrderCommodityVO> idCommMap = new HashMap<>();
        Map<Integer, AddressInfoVO> addIdAddressInfoMap = new HashMap<>();
        Map<Integer, EvaluationInfo> commIdEvaluationMap = new HashMap<>();
        boolean hasEval = false;
        //查找地址信息
        if(type.equals(1)){//已购买订单
            List<Integer> addIds = orderVOS.stream().map(OrderVO::getAddressId).collect(Collectors.toList());
            Search search = new Search();
            search.put("id_in",addIds);
            List<AddressInfo> addressInfos = addressService.queryByProperties(search,"");
            List<AddressInfoVO> addressInfoVOS = DozerUtil.mapToList(addressInfos, AddressInfoVO.class);
            for(AddressInfoVO addressInfo : addressInfoVOS){
                addIdAddressInfoMap.put(addressInfo.getId(), addressInfo);
            }
            List<Integer> commIds = orderCommodityVOS.stream().map(OrderCommodityVO::getId).collect(Collectors.toList());
            List<Integer> orderIds = orderInfos.stream().map(OrderInfo::getId).collect(Collectors.toList());
            Search search1 = new Search();
            search1.put("type", TYPE.commodity.code);
            search1.put("status",STATUS.YES.code);
            search1.put("objId_in", commIds);
            search1.put("orderId_in", orderIds);
            List<EvaluationInfo> evaluationInfos = evaluationService.queryByProperties(search1,"");
            if(CollectionUtils.isNotEmpty(evaluationInfos)){
                for(EvaluationInfo evaluationInfo : evaluationInfos){
                    commIdEvaluationMap.put(evaluationInfo.getObjId(), evaluationInfo);
                }
                hasEval = true;
            }
        }
        for(OrderCommodityVO commodityInfo : orderCommodityVOS){
            idCommMap.put(commodityInfo.getId(), commodityInfo);
        }
        for(UserOperation userOperation : userOperations){
            List<OrderCommodityVO> orderCommodityVOList = orderIdCommsMap.get(userOperation.getOrderId());
            OrderCommodityVO commodityvo = idCommMap.get(userOperation.getObjId());
            OrderCommodityVO commodityVO = new OrderCommodityVO();
            BeanUtils.copyProperties(commodityVO, commodityvo);
            commodityVO.setCount(userOperation.getCount());
            commodityVO.setOrderId(userOperation.getOrderId());
            if(hasEval &&
                    commIdEvaluationMap.get(commodityVO.getId()) != null &&
                    commIdEvaluationMap.get(commodityVO.getId()).getOrderId().equals(userOperation.getOrderId())){
                commodityVO.setHaveEvaluated(1);//有评论
            }else{
                commodityVO.setHaveEvaluated(0);//没有评论
            }
            if(CollectionUtils.isEmpty(orderCommodityVOList)){
                orderIdCommsMap.put(userOperation.getOrderId(), Lists.newArrayList(commodityVO));
            }else{
                orderCommodityVOList.add(commodityVO);
            }
        }
        for(OrderInfo orderInfo : orderInfos){
            OrderVO orderVO = DozerUtil.mapTo(orderInfo, OrderVO.class);
            List<OrderCommodityVO> commodityInfos1 = orderIdCommsMap.get(orderInfo.getId());
            orderVO.setCommodityVOList(commodityInfos1);
            if(type.equals(1)){//已购买订单
                orderVO.setAddressInfoVO(addIdAddressInfoMap.get(orderVO.getAddressId()));
            }
            orderVOS.add(orderVO);
        }
    }

    /**
     * 包装图片资源
     * @param commodityVOS
     */
    private void wrapResourceIn(Collection<CommodityShortVO> commodityVOS){
        List<Integer> ids = commodityVOS.stream().map(CommodityShortVO::getId).collect(Collectors.toList());
        Search search = new Search();
        search.put("type", TYPE.commodity.code);
        search.put("attribute", ATTRIBUTE.THUMB_PIC.code);
        search.put("objId_in",ids);
        List<PicInfo> picInfos = resourceService.queryByProperties(search,"");
        if(CollectionUtils.isEmpty(picInfos)){
            return;
        }
        List<PicInfoVO> picInfoVOS = DozerUtil.mapToList(picInfos, PicInfoVO.class);
        Map<Integer, PicInfoVO> commIdPicMap = new HashMap<>();
        for(PicInfoVO picInfoVO : picInfoVOS){
            commIdPicMap.put(picInfoVO.getObjId(), picInfoVO);
        }
        for(CommodityShortVO commodityVO : commodityVOS){
            commodityVO.setThumbPicUrl(commIdPicMap.get(commodityVO.getId()).getUrl());
        }
    }


    /**
     * 取消订单
     * @param oid
     * @return JsonBackData
     */
    @RequestMapping(value = "/cancelBuying", method = RequestMethod.POST)
    @ResponseBody
    public JsonBackData cancelBuying(
            @RequestParam("oid") @NotNull @Valid @Min(1) Integer oid
    ){
        JsonBackData jsonBackData = new JsonBackData(true, 200);
        try{
            Integer uid = getUID();
            Search search = new Search();
            search.put("status",STATUS.YES.code);
            search.put("userId", uid);
            search.put("id",oid);
            search.put("createUser",uid);
            search.put("boughtStatus",0);//未购买
            List<OrderInfo> orderInfos = orderService.queryByProperties(search,"");
            if(CollectionUtils.isEmpty(orderInfos)){
                throw new ClsException(LogStat.NO_DATA);
            }
            //更新order的status
            OrderInfo orderInfo = orderInfos.get(0);
            orderInfo.setStatus(STATUS.NO.code);
            orderInfo.setUpdateUser(uid);
            orderInfo.setUpdateTime(DateUtil.getCurrentMili());
            orderService.updateInfoByExample(orderInfo,search);
            //更新order的commodityOperation,从正在购买删除
            Search search1 = new Search();
            search1.put("userId",uid);
            search1.put("status", STATUS.YES.code);
            search1.put("orderId",oid);
            UserOperation userOperation = new UserOperation();
            userOperation.setStatus(STATUS.NO.code);
            userOperation.setUpdateUser(uid);
            userOperation.setUpdateTime(DateUtil.getCurrentMili());
            operationService.updateInfoByExample(userOperation, search1);
            //恢复库存

        }catch (Throwable t){
            logger.warn(t.toString());
            wrapException(jsonBackData,t);
        }
        return jsonBackData;
    }

    /**
     * 查询已收藏的商品
     * @return JsonBackData
     */
    @RequestMapping(value = "/queryliked", method = RequestMethod.POST)
    @ResponseBody
    public JsonBackData queryLikedComm(){
        JsonBackData jsonBackData = new JsonBackData(true, 200);
        try{
            Integer uid = getUID();
            Search search = new Search();
            search.put("userId",uid);
            search.put("status", STATUS.YES.code);
            search.put("behavior", BEHAVIOR.LIKE.code);
            search.put("type", TYPE.commodity.code);
            List<UserOperation> userOperations = operationService.queryByProperties(search,"");
            if(CollectionUtils.isEmpty(userOperations)){
                throw new ClsException(LogStat.NO_DATA);
            }
            List<Integer> ids = userOperations.stream().map(UserOperation::getObjId).collect(Collectors.toList());
            Search search1 = new Search();
            search1.put("id_in",ids);
            List<CommodityInfo> commodityInfos = commodityService.queryByProperties(search1 ,"updateTime_desc");
            if(CollectionUtils.isEmpty(commodityInfos)){
                throw new ClsException(LogStat.NO_DATA);
            }
            List<CommodityShortVO> commodityVOS = DozerUtil.mapToList(commodityInfos, CommodityShortVO.class);
            wrapResourceIn(commodityVOS);
            PublicService.wrapSellsCount(operationService,commodityVOS);
            jsonBackData.setBackData(commodityVOS);
        }catch(Throwable t){
            logger.warn(t.toString());
            wrapException(jsonBackData, t);
        }
        return jsonBackData;
    }

    /**
     * 查询某一个商品详情
     * @param id
     * @return JsonBackData
     */
    @RequestMapping(value = "/queryComm", method = RequestMethod.POST)
    @ResponseBody
    public JsonBackData queryCommodity(
            @RequestParam(value = "id") @Valid @NotNull String id
    ){
        JsonBackData jsonBackData = new JsonBackData(true,200);
        try{
            Integer commId = new Integer(id);
            if(commId.equals(0)){throw new ClsException(LogStat.DATA_FALSE);}
            CommodityInfo commodityInfo = commodityService.queryById(commId);
            if(commodityInfo == null){
                throw new ClsException(LogStat.QUERY_FAILED,"查询commodity Detail失败");
            }
            UserOperation userOperation = null;
            CommodityVO commodityVO = DozerUtil.mapTo(commodityInfo,CommodityVO.class);
            Integer cuid = (Integer)WebSession.getVal("cuid");
            if(cuid != null && cuid != 0){
                Search search = new Search();
                search.put("type",TYPE.commodity.code);
                search.put("behavior",BEHAVIOR.LIKE.code);
                search.put("userId",cuid);
                search.put("objId",commId);
                search.put("status",STATUS.YES.code);
                List<UserOperation> userOperations = operationService.queryByDetail(search,"");
                if(CollectionUtils.isNotEmpty(userOperations)){
                    userOperation = userOperations.get(0);
                }
            }
            if(userOperation != null){
                commodityVO.setLiked(true);
            }else{
                commodityVO.setLiked(false);
            }
            wrapResourceIn(commodityVO);
            wrapShopInfo(commodityVO);
            wrapEvaluationInfo(commodityVO);
            jsonBackData.setBackData(commodityVO);
        }catch(Throwable e){
            logger.warn(e.toString());
            wrapException(jsonBackData,e);
        }
        return jsonBackData;
    }

    /**
     * 查找正在购买状态的订单
     * @return JsonBackData
     */
    @RequestMapping(value = "/queryBuying", method = RequestMethod.POST)
    @ResponseBody
    public JsonBackData queryBuying(
            @RequestParam("oid") @Valid @NotNull Integer oid
    ){
        JsonBackData jsonBackData = new JsonBackData(true, 200);
        try{
            Integer uid = getUID();
            Search search = new Search();
            search.put("type", TYPE.commodity.code);
            search.put("behavior",BEHAVIOR.BUYING.code);
            search.put("userId",uid);
            search.put("status",STATUS.YES.code);
            search.put("orderId",oid);
            List<UserOperation> userOperations = operationService.queryByProperties(search, "");
            if(CollectionUtils.isEmpty(userOperations)){
                throw new ClsException(LogStat.NO_DATA);
            }
            List<Integer> ids = userOperations.stream().map(UserOperation::getObjId).collect(Collectors.toList());
            List<CommodityInfo> commodityInfos = commodityService.queryByObjIds(ids);
            List<CartCommVO> cartCommVOS = DozerUtil.mapToList(commodityInfos, CartCommVO.class);
            wrapThumbsIn(cartCommVOS);
            Map<Integer, UserOperation> commIdOperationMap = new HashMap<>();
            for(UserOperation userOperation : userOperations){
                commIdOperationMap.put(userOperation.getObjId(), userOperation);
            }
            for(CartCommVO cartCommVO : cartCommVOS){
                cartCommVO.setCount(commIdOperationMap.get(cartCommVO.getId()).getCount());
            }
            jsonBackData.setBackData(cartCommVOS);
        }catch(Throwable t){
            logger.warn(t.toString());
            wrapException(jsonBackData, t);
        }
        return jsonBackData;
    }


    /**
     * 查找正在购买的所有订单
     * @return
     */
    @RequestMapping(value = "/queryList", method = RequestMethod.POST)
    @ResponseBody
    public JsonBackData queryList(
            @RequestParam("type") @NotNull @Valid Integer type
    ){
        JsonBackData jsonBackData = new JsonBackData(true, 200);
        List<OrderVO> orderVOList = Lists.newArrayList();
        try{
            Integer uid = getUID();
            Search search = new Search();
            Search search1 = new Search();
            if(type.equals(0)){
                search.put("boughtStatus", 0);//购买状态-未购买
                search1.put("behavior", BEHAVIOR.BUYING.code);
            }else if(type.equals(1)){
                search.put("boughtStatus", 1);//购买状态-已购买
                search1.put("behavior", BEHAVIOR.BOUGHT.code);
            }
            search.put("status",STATUS.YES.code);
            search.put("userId", uid);
            List<OrderInfo> orderInfos = orderService.queryByProperties(search, "updateTime_desc");
            if(CollectionUtils.isEmpty(orderInfos)){
                throw new ClsException(LogStat.NO_DATA);
            }
            List<Integer> orderIds = orderInfos.stream().map(OrderInfo::getId).collect(Collectors.toList());
            search1.put("type",TYPE.commodity.code);
            search1.put("status",STATUS.YES.code);
            search1.put("orderId_in",orderIds);
            List<UserOperation> userOperations = operationService.queryByProperties(search1,"");
            if(CollectionUtils.isEmpty(userOperations)){
                throw new ClsException(LogStat.NO_DATA);
            }
            List<Integer> commIds = userOperations.stream().map(UserOperation::getObjId).collect(Collectors.toList());
            List<CommodityInfo> commodityInfos = commodityService.queryByObjIds(commIds);
            wrapBuyList(orderVOList, userOperations, commodityInfos, orderInfos, type);
            jsonBackData.setBackData(orderVOList);
        }catch(Throwable t){
            logger.warn(t.toString());
            wrapException(jsonBackData, t);
        }
        return jsonBackData;
    }


    /**
     * 分页查询shopId商品List
     * @param shopId
     * @param page
     * @param size
     * @return JsonBackData
     */
    @RequestMapping(value = "/queryComms", method = RequestMethod.POST)
    @ResponseBody
    public JsonBackData queryCommodities(
            @RequestParam(name = "shopId") @Valid @NotNull @Min(1) Integer shopId,
            @RequestParam(name = "page") @Valid @NotNull @Min(0) Integer page,
            @RequestParam(name = "size") @Valid @NotNull @Min(1) Integer size
    ){
        JsonBackData jsonBackData = new JsonBackData(true,200);
        try{
            List<CommodityInfo> commodityInfoList = commodityService.queryAllList(shopId,page,size);
            if(CollectionUtils.isEmpty(commodityInfoList)){
                throw new ClsException(LogStat.NO_DATA,"当前商铺没有商品数据");
            }
            List<CommodityShortVO> commodityVOS = DozerUtil.mapToList(commodityInfoList, CommodityShortVO.class);
            wrapResourceIn(commodityVOS);
            PublicService.wrapSellsCount(operationService, commodityVOS);
            jsonBackData.setBackData(commodityVOS);
        }catch(Throwable e){
            logger.warn(e.toString());
            wrapException(jsonBackData,e);
        }
        return jsonBackData;
    }


    //-------------------------后台操作----------------------------



}
