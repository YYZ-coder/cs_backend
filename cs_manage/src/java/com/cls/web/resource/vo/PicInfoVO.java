package com.cls.web.resource.vo;

import com.cls.common.base.BaseVO;
import lombok.Data;

/**
 * Project: cs_backend
 * @author Yue
 * @create 2018/5/17-0:58
 * Description：
 */
@Data
public class PicInfoVO extends BaseVO{

    private String url;

    /** 归属对象类型（0-商品|1-商铺|2-用户|3-评价） */
    private Integer type;

    private Integer objId;

    private Integer attribute;
}
