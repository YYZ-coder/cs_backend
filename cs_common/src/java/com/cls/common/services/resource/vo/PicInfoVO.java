package com.cls.common.services.resource.vo;

import lombok.Data;

/**
 * Project: cs_backend
 * @author Yue
 * @create 2018/4/27-20:13
 * Description：
 */
@Data
public class PicInfoVO {

    private Integer id;

    private String url;

    /** 归属对象类型（0-商品|1-商铺|2-用户|3-评价） */
    private Integer type;

    private Integer objId;

    private Integer attribute;

}
