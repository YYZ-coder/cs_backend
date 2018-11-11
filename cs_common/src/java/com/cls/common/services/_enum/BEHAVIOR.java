package com.cls.common.services._enum;

/**
 * Project: cs_backend
 * @author Yue
 * @create 2018/4/22-22:44
 * Description：
 *      用户行为枚举
 */
public enum BEHAVIOR {

    VIEW(0),
    INCART(1),
    BUYING(2),
    BOUGHT(3),
    LIKE(4);

    public Integer code;

    BEHAVIOR(Integer code){
        this.code = code;
    }

}
