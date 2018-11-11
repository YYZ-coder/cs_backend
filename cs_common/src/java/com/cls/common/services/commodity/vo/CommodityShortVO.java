package com.cls.common.services.commodity.vo;

import com.cls.common.base.ShortVO;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Project: cs_backend
 * @author Yue
 * @create 2018/4/25-1:21
 * Description：
 *      简短的商品数据
 */
@Data
public class CommodityShortVO extends ShortVO {

    private String commodityName;

    private Integer inventory;

    /** 商品价格 */
    private BigDecimal price;

    private Integer sellsCount;

    private Integer shopId;

    private String shopName;

}
