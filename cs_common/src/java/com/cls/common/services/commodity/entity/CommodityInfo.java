package com.cls.common.services.commodity.entity;

import com.cls.common.base.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * Project: cs_backend
 * @author Yue
 * @create 2018/4/13-0:51
 * Description：
 *      商品信息
 */
@Data
@Entity
@Table(name= "cs_commodity_info")
public class CommodityInfo extends BaseEntity {

    @Column(name = "commodity_name")
    private String commodityName;

    @Column(name= "price")
    private BigDecimal price;

    @Column(name = "short_introduction")
    private String shortIntroduction;

    @Column(name = "status")
    private Integer status;

    @Column(name = "shop_id")
    private Integer shopId;

    /** 商品所属商铺中的类别 */
    @Column(name = "category_id")
    private Integer categoryId;

    /** 商品所属类别：0-衣|1-食|2-其它 */
    @Column(name = "classes")
    private Integer classes;

    @Column(name = "introduction")
    private String introduction;

    /** 库存数 */
    @Column(name = "inventory")
    private Integer inventory;

    /** 规格：个/条/袋/斤/千克 */
    @Column(name = "specifications")
    private String specifications;
}
