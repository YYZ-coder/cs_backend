package com.cls.common.services._enum;

/**
 * Project: cs_backend
 * @author Yue
 * @create 2018/4/26-1:06
 * Description：
 * (0-缩略图|1-详情图片|2-avatar图片|3-swiper图片）
 */
public enum ATTRIBUTE {

    THUMB_PIC(0),
    DETAIL_PIC(1),
    AVATAR_PIC(2),
    SWIPER_PIC(3);

    public Integer code;

    ATTRIBUTE(Integer code){
        this.code =code;
    }
}
