package com.cls.common.base;

import lombok.Data;

/**
 * Project: cs_backend
 * @author Yue
 * @create 2018/4/25-1:31
 * Description：
 */
@Data
public class ShortVO extends BaseVO {

    /** 类型：0-商品|1-商铺 */
    private Integer type;

    /** 图片缩略图url */
    private String thumbPicUrl;

    /**  简短的介绍 */
    private String shortIntroduction;

}
