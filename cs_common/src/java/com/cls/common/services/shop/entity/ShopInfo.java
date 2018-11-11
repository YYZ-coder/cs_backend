package com.cls.common.services.shop.entity;

import com.cls.common.base.BaseEntity;
import lombok.Data;
import org.springframework.stereotype.Component;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Project: cs_backend
 * @author Yue
 * @create 2018/4/13-0:47
 * Description：
 *      店铺信息
 */
@Data
@Entity
@Table(name= "cs_shop_info")
public class ShopInfo extends BaseEntity implements Serializable {

    @Column(name = "shop_name")
    private String shopName;

    /** 店铺简介 */
    @Column(name = "short_introduction")
    private String shortIntroduction;

    /** 店铺类型：1-衣|2-食|3-其它 */
    @Column(name = "type")
    private Integer type;

    /** 总销量 */
    @Column(name= "sells_count")
    private Integer sellsCount;

    /** 店主id */
    @Column(name = "uid")
    private Integer uid;

    /** 星级 */
    @Column(name = "star_level")
    private Integer starLevel;

    /** 店铺联系电话 */
    @Column(name = "phone_num")
    private String phoneNum;

    /** 商品总数 */
    @Column(name = "commodity_count")
    private Integer commodityCount;

    /** 店铺地址信息 */
    @Column(name = "address_id")
    private Integer addressId;

    /** 店铺详细介绍 */
    @Column(name = "introduction")
    private String introduction;

    /** 0-未删除|1-删除 */
    @Column(name = "status")
    private Integer status;
}
