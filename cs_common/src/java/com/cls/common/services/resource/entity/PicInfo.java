package com.cls.common.services.resource.entity;

import com.cls.common.base.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Project: cs_backend
 * @author Yue
 * @create 2018/4/22-17:29
 * Description：
 */
@Data
@Entity
@Table(name= "cs_resource_pic")
public class PicInfo extends BaseEntity implements Serializable {

    private String url;

    /** 归属对象类型（0-商品|1-商铺|2-用户|3-评价） */
    private Integer type;

    @Column(name = "obj_id")
    private Integer objId;

    /** 属性（0-缩略图|1-详情图片|2-avatar图片|3-swiper图片） */
    private Integer attribute;

}
