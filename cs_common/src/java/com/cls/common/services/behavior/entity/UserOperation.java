package com.cls.common.services.behavior.entity;

import com.cls.common.base.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Project: cs_backend
 * @author Yue
 * @create 2018/4/13-1:07
 * Description：
 *      用户行为表
 */
@Data
@Entity
@Table(name= "cs_user_operation")
public class UserOperation extends BaseEntity implements Serializable {

    /** 状态：0-浏览|1-购物车|2-正在购买|3-已购买|4-收藏 */
    @Column(name = "behavior")
    private Integer behavior;

    /** 0-未删除|1-删除 */
    @Column(name= "status")
    private Integer status;

    @Column(name = "user_id")
    private Integer userId;

    /** 类型：0-商品详情|1-商铺详情 */
    @Column(name = "type")
    private Integer type;

    /** 对象id：商品/商铺 */
    @Column(name = "obj_id")
    private Integer objId;

    /** 购买ing/购物车/已购买个数 */
    @Column(name = "count")
    private Integer count;

    /** 订单id */
    @Column(name = "order_id")
    private Integer orderId;

    public UserOperation(){}

    public UserOperation(Integer behavior, Integer status, Integer userId, Integer type, Integer objId) {
        this.behavior = behavior;
        this.status = status;
        this.userId = userId;
        this.type = type;
        this.objId = objId;
    }
}
