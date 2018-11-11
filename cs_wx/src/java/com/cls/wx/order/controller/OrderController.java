package com.cls.wx.order.controller;

import com.cls.common.base.BaseController;
import com.cls.common.services.commodity.service.impl.CommodityServiceImpl;
import com.cls.common.services.order.service.impl.OrderServiceImpl;
import com.cls.common.services.shop.service.impl.ShopServiceImpl;
import com.cls.common.services.behavior.service.impl.OperationServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Project: cs_backend
 * @author Yue
 * @create 2018/4/29-9:52
 * Description：
 */
@RequestMapping(value = "/wx/ordr")
@Controller
public class OrderController extends BaseController{

    private static Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderServiceImpl orderService;

    @Autowired
    private OperationServiceImpl operationService;

    @Autowired
    private ShopServiceImpl shopService;

    @Autowired
    private CommodityServiceImpl commodityService;


}
