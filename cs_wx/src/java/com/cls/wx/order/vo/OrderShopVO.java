package com.cls.wx.order.vo;

import lombok.Data;

import java.util.List;

/**
 * Project: cs_backend
 * @author Yue
 * @create 2018/4/27-21:31
 * Description：
 */
@Data
public class OrderShopVO {

    private Integer id;

    private String shopName;

    private List<OrderCommodityVO> commodityVOList;
}
