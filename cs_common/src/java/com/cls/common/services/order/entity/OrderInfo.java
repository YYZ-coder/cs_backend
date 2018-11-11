package com.cls.common.services.order.entity;

import com.cls.common.base.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Project: cs_backend
 * @author Yue
 * @create 2018/4/24-0:51
 * Description：
 *      订单信息
 */
@Data
@Entity
@Table(name= "cs_order_info")
public class OrderInfo extends BaseEntity implements Serializable {

    /** 地址信息id */
    @Column(name = "address_id")
    private Integer addressId;

    /** 用户id */
    @Column(name = "user_id")
    private Integer userId;

    /** 购买次数 */
    @Column(name = "bought_count")
    private Integer boughtCount;

    /** 商品总数 */
    @Column(name = "comm_count")
    private Integer commCount;

    /** 状态：0-未删除|1-已删除 */
    @Column(name = "status")
    private Integer status;

    /** 购买状态：0-未购买|1-已购买 */
    @Column(name = "bought_status")
    private Integer boughtStatus;

    /** 订单总价格 */
    @Column(name= "sum_price")
    private BigDecimal sumPrice;

    /** 配送方式：0-普通快递|1-顺丰快递*/
    @Column(name= "trans_way")
    private Integer transWay;

    /** 配送价格 */
    @Column(name = "trans_price")
    private BigDecimal transPrice;

}
