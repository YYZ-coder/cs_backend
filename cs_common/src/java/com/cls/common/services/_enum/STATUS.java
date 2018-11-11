package com.cls.common.services._enum;

/**
 * Project: cs_backend
 * @author Yue
 * @create 2018/4/25-2:33
 * Description：
 */
public enum STATUS {

    YES(0),//不删除
    NO(1);//删除

    public Integer code;

    STATUS(Integer code){
        this.code = code;
    }
}
