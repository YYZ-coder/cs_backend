package com.cls.common.services.evaluate.entity;

import com.cls.common.base.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Project: cs_backend
 * @author Yue
 * @create 2018/4/13-0:33
 * Description：
 *  评价
 */
@Data
@Entity
@Table(name = "cs_evaluate_info")
public class EvaluationInfo extends BaseEntity implements Serializable {

    /** 评价类型:0-商品/1-商铺 */
    @Column(name = "type")
    private Integer type;

    /** 0-未删|1-删除 */
    @Column(name= "status")
    private Integer status;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    /** 评价对象id */
    @Column(name = "obj_id")
    private Integer objId;

    /** 用户id */
    @Column(name = "user_id")
    private Integer userId;

    /** 匿名 */
    @Column(name= "anonymity_name")
    private String anonymityName;

    /** 星级数 */
    @Column(name = "star_count")
    private Integer starCount;

    /** 订单id */
    @Column(name = "order_id")
    private Integer orderId;
}
