package com.cls.common.services._enum;

/**
 * Project: cs_backend
 * @author Yue
 * @create 2018/4/25-2:15
 * Description：
 */
public enum TYPE {
    /** 搜索类型 */
    GENDER(1),
    ACCOUNT_NAME(2),

    /** 性别 */
    MAN(1),
    WOMAN(0),

    /** 地址对应对象 */
    ADD_USER(0),
    ADD_SHOP(1),

    /** 对象 */
    commodity(0),
    shop(1),
    user(2),
    evaluation(3),

    /** 图片资源 */
    thumbPic(0),
    detailPic(1),
    avatarPic(2),
    swiperPic(3);

    public Integer code;

    TYPE(Integer code){
        this.code = code;
    }
}
