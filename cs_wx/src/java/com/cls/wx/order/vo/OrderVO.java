package com.cls.wx.order.vo;

import com.cls.common.base.BaseVO;
import com.cls.wx.address.vo.AddressInfoVO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Project: cs_backend
 * @author Yue
 * @create 2018/4/23-1:16
 * Description：
 *
 */
@Data
public class OrderVO extends BaseVO {

    /** 商品s */
    private List<OrderCommodityVO> commodityVOList;

    /** 总价 */
    private BigDecimal sumPrice;

    /** 订单绑定的地址信息 */
    private AddressInfoVO addressInfoVO;

    private Integer addressId;

    /** 运输方式：0-普通快递|1-顺丰 */
    private Integer transWay;

    /** 运费 */
    private BigDecimal transPrice;

}