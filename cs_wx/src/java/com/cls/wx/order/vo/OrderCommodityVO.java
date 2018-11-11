package com.cls.wx.order.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Project: cs_backend
 * @author Yue
 * @create 2018/4/23-1:47
 * Description：
 */
@Data
public class OrderCommodityVO implements Serializable{

    private Integer orderId;

    private Integer id;

    /** 商铺id */
    private Integer shopId;

    /** 商铺名 */
    private String shopName;

    /** 物品名称 */
    private String commodityName;

    /** 购买数量 */
    private Integer count;

    /** 物品单价 */
    private BigDecimal price;

    /** 物品规格 */
    private String specifications;

    /** 物品缩略图 */
    private String thumbPic;

    /** 是否已评论过：0-无|1-有 */
    private Integer haveEvaluated;

}
