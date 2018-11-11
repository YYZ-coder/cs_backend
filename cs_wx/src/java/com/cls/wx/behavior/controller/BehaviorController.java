package com.cls.wx.behavior.controller;

import com.alibaba.fastjson.JSONArray;
import com.cls.common.backdata.JsonBackData;
import com.cls.common.base.BaseController;
import com.cls.common.exception.ClsException;
import com.cls.common.search.Search;
import com.cls.common.stat.LogStat;
import com.cls.common.utils.DateUtil;
import com.cls.common.services._enum.BEHAVIOR;
import com.cls.common.services._enum.STATUS;
import com.cls.common.services._enum.TYPE;
import com.cls.common.services.behavior.entity.UserOperation;
import com.cls.common.services.behavior.service.impl.OperationServiceImpl;
import com.cls.common.services.commodity.entity.CommodityInfo;
import com.cls.common.services.commodity.service.impl.CommodityServiceImpl;
import com.cls.common.services.order.entity.OrderInfo;
import com.cls.common.services.order.service.impl.OrderServiceImpl;
import com.cls.common.services.shop.entity.ShopInfo;
import com.cls.common.services.shop.service.impl.ShopServiceImpl;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Project: cs_backend
 * @author Yue
 * @create 2018/4/27-1:59
 * Description：
 *      行为操作：
 *          1. 未解决重复请求问题
 */
@RequestMapping(value = "/wx/behavior")
@Controller
public class BehaviorController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(BehaviorController.class);

    @Autowired
    private OperationServiceImpl operationService;

    @Autowired
    private ShopServiceImpl shopService;

    @Autowired
    private CommodityServiceImpl commodityService;

    @Autowired
    private OrderServiceImpl orderService;


    /**
     * 改变库存
     * @param uid
     * @param ids
     */
    private void changeInventory(Integer uid, List<Integer> ids, Map<Integer, Integer> commIdCountMap){
        List<CommodityInfo> commodityInfos = commodityService.queryByObjIds(ids);
        for(CommodityInfo commodityInfo : commodityInfos){
            Search sea = new Search();
            sea.put("id", commodityInfo.getId());
            commodityInfo.setUpdateUser(uid);
            commodityInfo.setUpdateTime(DateUtil.getCurrentMili());
            commodityInfo.setInventory(commodityInfo.getInventory() - commIdCountMap.get(commodityInfo.getId()));
            commodityService.updateInfoByExample(commodityInfo,sea);
        }
    }

    /**
     * 到已购买状态
     * @param oid orderId
     * @param aid addressId
     * @param tid transwayId
     * @return
     */
    @RequestMapping(value = "/tobought", method = RequestMethod.POST)
    @ResponseBody
    public JsonBackData toBought(
            @RequestParam("oid") @NotNull @Valid Integer oid,
            @RequestParam("aid") @NotNull @Valid Integer aid,
            @RequestParam("tid") @NotNull @Valid Integer tid,
            @RequestParam("sumPrice") @NotNull @Valid BigDecimal sumPrice,
            @RequestParam("transPrice") @NotNull @Valid BigDecimal transPrice
    ){
        JsonBackData jsonBackData = new JsonBackData(true, 200);
        try{
            Integer uid = getUID();
            Integer boughtCount = 0;
            //切换UserOperation状态
            Search search = new Search();
            search.put("status",STATUS.YES.code);
            search.put("userId", uid);
            search.put("behavior",BEHAVIOR.BUYING.code);
            search.put("type", TYPE.commodity.code);
            search.put("orderId",oid);
            List<UserOperation> userOperations = operationService.queryByProperties(search,"");
            if(CollectionUtils.isEmpty(userOperations)){
                throw new ClsException(LogStat.NO_DATA);
            }
            for(UserOperation userOperation : userOperations){
                boughtCount += userOperation.getCount();
            }
            UserOperation userOperation = new UserOperation();
            userOperation.setBehavior(BEHAVIOR.BOUGHT.code);
            userOperation.setUpdateTime(DateUtil.getCurrentMili());
            userOperation.setUpdateUser(uid);
            operationService.updateInfoByExample(userOperation, search);
            //切换order状态
            Search search1 = new Search();
            search1.put("id",oid);
            OrderInfo orderInfo = new OrderInfo();
            orderInfo.setBoughtStatus(1);//已购买
            orderInfo.setAddressId(aid);
            orderInfo.setBoughtCount(boughtCount);
            orderInfo.setSumPrice(sumPrice);
            orderInfo.setTransPrice(transPrice);
            orderInfo.setTransWay(tid);
            orderInfo.setUpdateUser(uid);
            orderInfo.setUpdateTime(DateUtil.getCurrentMili());
            orderService.updateInfoByExample(orderInfo, search1);
            //为各自归属店铺增加销量
            List<Integer> ids = userOperations.stream().map(UserOperation::getObjId).collect(Collectors.toList());
            Search search2 = new Search();
            search2.put("id_in",ids);
            List<CommodityInfo> commodityInfos = commodityService.queryByProperties(search2,"");
            if(CollectionUtils.isEmpty(commodityInfos)){
                throw new ClsException(LogStat.NO_DATA);
            }
            List<Integer> shopIds = commodityInfos.stream().map(CommodityInfo::getShopId).collect(Collectors.toList());
            Search search3 = new Search();
            search3.put("id_in", shopIds);
            List<ShopInfo> shopInfos = shopService.queryByProperties(search3, "");
            if(CollectionUtils.isEmpty(shopInfos)){
                throw new ClsException(LogStat.NO_DATA);
            }
            Map<Integer, ShopInfo> idShopInfoMap = new HashMap<>();
            Map<Integer, Integer> commIdShopIdMap = new HashMap<>();
            Map<Integer, Integer> shopIdCountMap = new HashMap<>();
            for(ShopInfo shopInfo : shopInfos){
                idShopInfoMap.put(shopInfo.getId(), shopInfo);
            }
            for(CommodityInfo commodityInfo : commodityInfos){
                commIdShopIdMap.put(commodityInfo.getId(), commodityInfo.getShopId());
            }
            for(ShopInfo shopInfo : shopInfos){
                shopIdCountMap.put(shopInfo.getId(), shopInfo.getSellsCount());
            }
            for(UserOperation userOperation1 : userOperations){
                Integer sid = commIdShopIdMap.get(userOperation1.getObjId());
                Integer count =  shopIdCountMap.get(sid) + userOperation1.getCount();
                shopIdCountMap.put(sid, count);
            }
            for(ShopInfo shopInfo : shopInfos){
                Search search4 = new Search();
                search4.put("id", shopInfo.getId());
                shopInfo.setSellsCount(shopIdCountMap.get(shopInfo.getId()));
                shopService.updateInfoByExample(shopInfo, search4);
            }
            jsonBackData.setBackData("购买成功");
        }catch(Throwable t){
            logger.info(t.toString());
            wrapException(jsonBackData, t);
        }
        return jsonBackData;
    }

    /**
     * 立即购买
     * @param id
     * @return
     */
    @RequestMapping(value = "/immbuy", method = RequestMethod.POST)
    @ResponseBody
    public JsonBackData immToBuy(
            @RequestParam("id") @NotNull @Valid Integer id
    ){
        JsonBackData jsonBackData = new JsonBackData(true,200);
        try{
            Integer uid = getUID();
            //生成orderId，并置状态为未购买
            OrderInfo orderInfo = new OrderInfo();
            orderInfo.setBoughtStatus(STATUS.YES.code);//未购买
            orderInfo.setUserId(uid);
            orderInfo.setCreateTime(DateUtil.getCurrentMili());
            orderInfo.setCreateUser(uid);
            orderInfo.setUpdateTime(DateUtil.getCurrentMili());
            orderInfo.setUpdateUser(uid);
            orderService.addNew(orderInfo);
            Integer orderId = orderInfo.getId();
            Search search = new Search();
            search.put("type",TYPE.commodity.code);
            search.put("userId",uid);
            search.put("objId",id);
            search.put("status",STATUS.YES.code);
            //改变库存
            Map<Integer, Integer> commIdCountMap = new HashMap<>();
            commIdCountMap.put(id, 1);//立即购买--默认为一个
            List<Integer> idss = Lists.newArrayList(id);
            changeInventory(uid, idss, commIdCountMap);
            UserOperation userOperation = new UserOperation();
            userOperation.setBehavior(BEHAVIOR.BUYING.code);
            userOperation.setOrderId(orderId);
            userOperation.setObjId(id);
            userOperation.setType(TYPE.commodity.code);
            if(0 == operationService.addNew(userOperation)){
                jsonBackData.setBackData("服务接口异常");
            }else{
                jsonBackData.setBackData(orderId);
            }
        }catch(Throwable t){
            logger.warn(t.toString());
            wrapException(jsonBackData,t);
        }
        return jsonBackData;
    }

    /**
     * 到正在购买状态....
     * @param ids
     * @return
     */
    @RequestMapping(value = "/toBuying", produces = "application/json" ,method = RequestMethod.POST)
    @ResponseBody
    public JsonBackData toBuying(
            @RequestParam("ids") @NotNull @Valid String ids
    ){
        JsonBackData jsonBackData = new JsonBackData(true, 200);
        try{
            List<Integer> idss = JSONArray.parseArray(ids, Integer.class);
            //转换状态(cart->buying)
            Integer uid = getUID();
            //生成orderId，并置状态为未购买
            OrderInfo orderInfo = new OrderInfo();
            orderInfo.setBoughtStatus(STATUS.YES.code);//未购买
            orderInfo.setUserId(uid);
            orderInfo.setCreateTime(DateUtil.getCurrentMili());
            orderInfo.setCreateUser(uid);
            orderInfo.setUpdateTime(DateUtil.getCurrentMili());
            orderInfo.setUpdateUser(uid);
            orderService.addNew(orderInfo);
            Integer orderId = orderInfo.getId();
            Search search = new Search();
            search.put("type",TYPE.commodity.code);
            search.put("behavior",BEHAVIOR.INCART.code);
            search.put("userId",uid);
            search.put("objId_in",idss);
            search.put("status",STATUS.YES.code);
            //改变库存
            Map<Integer, Integer> commIdCountMap = new HashMap<>();
            List<UserOperation> userOperations = operationService.queryByProperties(search,"");
            if(CollectionUtils.isEmpty(userOperations)){
                throw new ClsException(LogStat.NO_DATA);
            }
            for(UserOperation userOperation : userOperations){
                commIdCountMap.put(userOperation.getObjId(), userOperation.getCount());
            }
            changeInventory(uid, idss, commIdCountMap);
            UserOperation userOperation = new UserOperation();
            userOperation.setBehavior(BEHAVIOR.BUYING.code);
            userOperation.setOrderId(orderId);
            userOperation.setUpdateUser(uid);
            userOperation.setUpdateTime(DateUtil.getCurrentMili());
            if(0 == operationService.updateInfoByExample(userOperation, search)){
                jsonBackData.setBackData("服务接口异常");
            }else{
                jsonBackData.setBackData(orderId);
            }
        }catch(Throwable t){
            logger.info(t.toString());
            wrapException(jsonBackData, t);
        }
        return jsonBackData;
    }

    /**
     * 验证对象存在性
     * @param type
     * @param objId
     */
    private void verifyExist(Integer type, Integer objId){
        if(TYPE.shop.code.equals(type) && shopService.queryById(objId) == null){
            throw new ClsException(LogStat.NOTEXIST_SHOP);
        }else if(TYPE.commodity.code.equals(type) && commodityService.queryById(objId) == null){
            throw new ClsException(LogStat.NOTEXIST_COMMODITY);
        }else if(!TYPE.commodity.code.equals(type) && !TYPE.shop.code.equals(type)){//针对恶意参数
            throw new ClsException(LogStat.NO_DATA);
        }
    }

    /**
     * 增加行为:收藏、浏览
     * @param objId
     * @param type
     * @param behavior
     * @return JsonBackData
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public JsonBackData add(
            @RequestParam("objId") @Valid @NotNull @Min(1) Integer objId,
            @RequestParam("type") @Valid @NotNull @Min(0) @Max(1) Integer type,
            @RequestParam("behavior") @Valid @NotNull @Min(0) Integer behavior
    ){
        JsonBackData jsonBackData = new JsonBackData(true,200);
        try{
            verifyExist(type,objId);
            Integer cuid = getUID();
            if(behavior.equals(BEHAVIOR.VIEW.code)){
                operationService.addNew(new UserOperation(behavior,STATUS.YES.code,cuid,type,objId));
            }else{
                Search search = new Search();
                search.put("type",type);
                search.put("objId",objId);
                search.put("userId",cuid);
                search.put("behavior",behavior);
                List<UserOperation> userOperationList = operationService.queryByDetail(search,"");
                if(CollectionUtils.isEmpty(userOperationList)){
                    operationService.addNew(new UserOperation(behavior, STATUS.YES.code, cuid, type, objId));
                }else{
                    userOperationList.get(0).setStatus(STATUS.YES.code);
                    operationService.updateByDetail(userOperationList.get(0),type,behavior,cuid,objId);
                }
            }
            jsonBackData.setBackData("操作成功");
        }catch(Throwable e){
            logger.warn(e.toString());
            wrapException(jsonBackData,e);
        }
        return jsonBackData;
    }

    /**
     * 取消行为:收藏、浏览
     * @param objId
     * @param type
     * @param behavior
     * @return
     */
    @RequestMapping(value = "/cancel")
    @ResponseBody
    public JsonBackData cancel(
            @RequestParam("objId") @Valid @NotNull @Min(1) Integer objId,
            @RequestParam("type") @Valid @NotNull @Min(0) @Max(1) Integer type,
            @RequestParam("behavior") @Valid @NotNull @Min(0) Integer behavior
    ){
        JsonBackData jsonBackData = new JsonBackData(true,200);
        try{
            verifyExist(type,objId);
            Integer cuid = getUID();
            Search search = new Search();
            search.put("type",type);
            search.put("behavior",behavior);
            search.put("userId", cuid);
            search.put("objId",objId);
            List<UserOperation> userOperations = operationService.queryByDetail(search,"");
            if(CollectionUtils.isEmpty(userOperations)){
                throw new ClsException(LogStat.NO_DATA);
            }
            userOperations.get(0).setStatus(STATUS.NO.code);
            if(0 == operationService.updateByDetail(userOperations.get(0), type, behavior, cuid, objId)){
                jsonBackData.setBackData("取消失败");
            }else{
                jsonBackData.setBackData("取消成功");
            }
        }catch(Throwable e){
            logger.warn(e.toString());
            wrapException(jsonBackData,e);
        }
        return jsonBackData;
    }

}
