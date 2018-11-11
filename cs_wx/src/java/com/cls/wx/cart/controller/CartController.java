package com.cls.wx.cart.controller;

import com.cls.common.backdata.JsonBackData;
import com.cls.common.base.BaseController;
import com.cls.common.exception.ClsException;
import com.cls.common.search.Search;
import com.cls.common.stat.LogStat;
import com.cls.common.utils.DateUtil;
import com.cls.common.utils.DozerUtil;
import com.cls.common.services._enum.ATTRIBUTE;
import com.cls.common.services._enum.BEHAVIOR;
import com.cls.common.services._enum.STATUS;
import com.cls.common.services._enum.TYPE;
import com.cls.wx.cart.vo.CartCommVO;
import com.cls.common.services.commodity.CommodityDTO;
import com.cls.common.services.behavior.entity.UserOperation;
import com.cls.common.services.behavior.service.impl.OperationServiceImpl;
import com.cls.common.services.commodity.entity.CommodityInfo;
import com.cls.common.services.commodity.service.impl.CommodityServiceImpl;
import com.cls.wx.commodity.vo.CommodityVO;
import com.cls.common.services.resource.entity.PicInfo;
import com.cls.common.services.resource.service.impl.ResourceServiceImpl;
import com.cls.common.services.resource.vo.PicInfoVO;
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
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Project: cs_backend
 * @author Yue
 * @create 2018/4/22-0:35
 * Description：
 *      购物车操作请求处理
 */
@Controller
@RequestMapping("/wx/cart")
public class CartController extends BaseController{

    private static final Logger logger = LoggerFactory.getLogger(CartController.class);

    @Autowired
    private OperationServiceImpl operationService;

    @Autowired
    private CommodityServiceImpl commodityService;

    @Autowired
    private ResourceServiceImpl resourceService;

    /**
     * 加载购物车商品
     * @return
     */
    @RequestMapping(value = "/loadcomms", method = RequestMethod.POST)
    @ResponseBody
    public JsonBackData loadCart(){
        JsonBackData jsonBackData = new JsonBackData(true,200);
        List<CartCommVO> cartCommVOS = Lists.newArrayList();
        Map<Integer, CommodityVO> commodityInfoMap = new HashMap<>();
        Map<Integer, List<PicInfoVO>> commIdPicListMap = new HashMap<>();
        try{
            Integer cuid = getUID();
            Search search = new Search();
            search.put("status", STATUS.YES.code);
            search.put("behavior", BEHAVIOR.INCART.code);
            search.put("type", TYPE.commodity.code);
            search.put("userId",cuid);
            List<UserOperation> userOperations = operationService.queryByDetail(search,"updateTime_desc");
            if(CollectionUtils.isEmpty(userOperations)){
                throw new ClsException(LogStat.NO_DATA);
            }
            List<Integer> objIds = userOperations.stream().map(UserOperation::getObjId).collect(Collectors.toList());
            List<CommodityInfo> commodityInfos = commodityService.queryByObjIds(objIds);
            if(CollectionUtils.isEmpty(commodityInfos)){
                throw new ClsException(LogStat.NO_DATA);
            }
            Search searchPic = new Search();
            searchPic.put("type",TYPE.commodity.code);
            searchPic.put("attribute", ATTRIBUTE.THUMB_PIC.code);
            searchPic.put("objId_in",objIds);
            List<PicInfo> picInfos = resourceService.queryByProperties(searchPic,"");
            if(CollectionUtils.isEmpty(picInfos)){
                throw new ClsException(LogStat.NO_THUMBPIC);
            }
            for(PicInfo picInfo : picInfos){
                List<PicInfoVO> picInfoVOS = commIdPicListMap.get(picInfo.getObjId());
                PicInfoVO picInfoVO = DozerUtil.mapTo(picInfo, PicInfoVO.class);
                if(CollectionUtils.isEmpty(picInfoVOS)){
                    commIdPicListMap.put(picInfo.getObjId(), Lists.newArrayList(picInfoVO));
                }else{
                    picInfoVOS.add(picInfoVO);
                }
            }
            for(CommodityInfo commodityInfo : commodityInfos){
                CommodityVO commodityVO = DozerUtil.mapTo(commodityInfo, CommodityVO.class);
                commodityInfoMap.put(commodityInfo.getId(), commodityVO);
            }
            for(UserOperation userOperation : userOperations){
                CommodityVO commodityInfo = commodityInfoMap.get(userOperation.getObjId());
                CartCommVO cartCommVO = DozerUtil.mapTo(commodityInfo, CartCommVO.class);
                cartCommVO.setCount(userOperation.getCount());
                cartCommVO.setThumbPic(commIdPicListMap.get(commodityInfo.getId()));
                cartCommVOS.add(cartCommVO);
            }
            jsonBackData.setBackData(cartCommVOS);
        }catch (Throwable e){
            logger.info(e.toString());
            wrapException(jsonBackData, e);
        }
        return jsonBackData;
    }

    /**
     * 移除已加入购物车的物品
     * @param id
     * @return JsonBackData
     */
    @RequestMapping(value = "/rmcomm", method = RequestMethod.POST)
    @ResponseBody
    public JsonBackData deleteFromCart(
            @RequestParam("id") @Valid @NotNull Integer id
    ){
        JsonBackData jsonBackData = new JsonBackData(true,200);
        try{
            Integer uid = getUID();
            Search search = new Search();
            search.put("status",STATUS.YES.code);
            search.put("behavior",BEHAVIOR.INCART.code);
            search.put("type",TYPE.commodity.code);
            search.put("userId",uid);
            search.put("objId",id);
            List<UserOperation> userOperations = operationService.queryByDetail(search, "");
            if(CollectionUtils.isEmpty(userOperations)){
                throw new ClsException(LogStat.NO_DATA);
            }
            UserOperation userOperation = userOperations.get(0);
            userOperation.setStatus(STATUS.NO.code);
            userOperation.setUpdateUser(uid);
            userOperation.setUpdateTime(DateUtil.getCurrentMili());
            operationService.updateInfoByExample(userOperation,search);
        }catch(Throwable e){
            logger.info(e.toString());
            wrapException(jsonBackData,e);
        }
        return jsonBackData;
    }

    /**
     * 加入购物车
     * @param id
     * @return JsonBackData
     */
    @RequestMapping(value = "/addtoc", method = RequestMethod.POST)
    @ResponseBody
    public JsonBackData addToCart(
            @RequestParam("id") @Valid @NotNull Integer id
    ){
        JsonBackData jsonBackData = new JsonBackData(true,200);
        try{
            Integer uid = getUID();
            Search search = new Search();
            search.put("status",STATUS.YES.code);
            search.put("behavior",BEHAVIOR.INCART.code);
            search.put("type",TYPE.commodity.code);
            search.put("userId",uid);
            search.put("objId",id);
            List<UserOperation> userOperations = operationService.queryByProperties(search,"");
            if(CollectionUtils.isNotEmpty(userOperations)){
                UserOperation userOperation = userOperations.get(0);
                userOperation.setCount(userOperation.getCount()+1);
                userOperation.setUpdateUser(uid);
                userOperation.setUpdateTime(DateUtil.getCurrentMili());
                Search sear = new Search();
                sear.put("id",userOperation.getId());
                operationService.updateInfoByExample(userOperation,sear);
            }else{
                UserOperation userOperation = new UserOperation();
                userOperation.setBehavior(BEHAVIOR.INCART.code);
                userOperation.setType(TYPE.commodity.code);
                userOperation.setObjId(id);
                operationService.addNew(userOperation);
            }
            jsonBackData.setBackData("加入购物车成功");
        }catch(Throwable e){
            logger.info(e.toString());
            wrapException(jsonBackData,e);
        }
        return jsonBackData;
    }

    /**
     * 减1
     * @param id
     * @return JsonBackData
     */
    @RequestMapping(value = "/reduce", method = RequestMethod.POST)
    @ResponseBody
    public JsonBackData reduceCartComm(
            @RequestParam(value = "id") @Valid @NotNull Integer id
    ){
        JsonBackData jsonBackData = new JsonBackData(true, 200);
        try{
            Integer uid = getUID();
            Search search = new Search();
            search.put("type", TYPE.commodity.code);
            search.put("behavior", BEHAVIOR.INCART.code);
            search.put("status",STATUS.YES.code);
            search.put("objId", id);
            search.put("userId", uid);
            List<UserOperation> userOperations = operationService.queryByDetail(search, "");
            if(CollectionUtils.isEmpty(userOperations)){
                throw new ClsException(LogStat.NO_DATA);
            }
            UserOperation userOperation = userOperations.get(0);
            userOperation.setUpdateTime(DateUtil.getCurrentMili());
            userOperation.setUpdateUser(uid);
            userOperation.setCount(userOperation.getCount()-1);
            if(userOperation.getCount().equals(0)){
                userOperation.setStatus(STATUS.NO.code);
            }
            operationService.updateInfoByExample(userOperation, search);
        }catch(Throwable t){
            logger.info(t.toString());
            wrapException(jsonBackData, t);
        }
        return jsonBackData;
    }


    /**
     * 更新购物车商品
     * @param commodityDTOs
     * @return JsonBackData
     */
    @RequestMapping(value = "/updtcomm", method = RequestMethod.POST)
    @ResponseBody
    public JsonBackData updateCart(
            @RequestParam("commodities") @Valid @NotNull List<CommodityDTO> commodityDTOs
    ){
        JsonBackData jsonBackData = new JsonBackData(true,200);
        try{

        }catch(Throwable e){
            wrapException(jsonBackData,e);
        }
        return jsonBackData;
    }



}
