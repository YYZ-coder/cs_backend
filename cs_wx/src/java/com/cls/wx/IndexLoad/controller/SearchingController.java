package com.cls.wx.IndexLoad.controller;

import com.cls.common.backdata.JsonBackData;
import com.cls.common.base.BaseController;
import com.cls.common.context.WebSession;
import com.cls.common.services.behavior.service.impl.OperationServiceImpl;
import com.cls.common.services.commodity.service.impl.CommodityServiceImpl;
import com.cls.common.services.resource.service.impl.ResourceServiceImpl;
import com.cls.common.services.shop.service.impl.ShopServiceImpl;
import org.hibernate.validator.constraints.Length;
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

/**
 * Project: cs_backend
 * @author Yue
 * @create 2018/4/26-1:56
 * Description：
 *      搜索接口们
 */
@RequestMapping("/wx/srch")
@Controller
public class SearchingController extends BaseController{

    private static final Logger logger = LoggerFactory.getLogger(SearchingController.class);

    @Autowired
    private ShopServiceImpl shopService;

    @Autowired
    private CommodityServiceImpl commodityService;

    @Autowired
    private ResourceServiceImpl resourceService;

    @Autowired
    private OperationServiceImpl operationService;

    /**
     * 组合查询：包括商铺 & 商品
     * @param key
     * @return
     */
    @RequestMapping(value = "/combine",method = RequestMethod.POST)
    @ResponseBody
    public JsonBackData searchCombination(
            @RequestParam(name = "key") @Valid @NotNull @Length(min = 1) String key
    ){
        JsonBackData jsonBackData = new JsonBackData(true, 200);
        Integer cuid = (Integer) WebSession.getVal("cuid");
        try{
            if(cuid == null || cuid.equals(0)){

            }else{
                //TODO:推荐系统数据
            }
        }catch (Throwable t){
            logger.warn(t.toString());
            wrapException(jsonBackData,t);
        }
        return jsonBackData;
    }

    /**
     * 按搜索条件查询物品List
     * @param prop
     * @return JsonBackData
     */
    @RequestMapping(value = "/searchComms",method = RequestMethod.POST)
    @ResponseBody
    public JsonBackData searchComm(
            @RequestParam("prop") @Valid @NotNull String prop
    ){
        JsonBackData jsonBackData = new JsonBackData(true,200);
        try{

        }catch (Throwable t){
            logger.warn(t.toString());
            wrapException(jsonBackData,t);
        }
        return jsonBackData;
    }

    /**
     * 搜索shops
     * @param prop
     * @return
     */
    @RequestMapping(value = "/searchSh", method = RequestMethod.POST)
    @ResponseBody
    public JsonBackData searchShops(
            @RequestParam("prop") @Valid @NotNull String prop
    ){
        JsonBackData jsonBackData = new JsonBackData(true,200);
        try{

        }catch (Throwable t){
            logger.warn(t.toString());
            wrapException(jsonBackData,t);
        }
        return jsonBackData;
    }

}
