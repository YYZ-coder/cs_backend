package com.cls.common.services;

/**
 * Project: cs_backend
 * @author Yue
 * @create 2018/4/21-18:22
 * Description：
 *      全局enum
 */
public enum AppEnum {

    /** 当前登录用户id */
    CUID(99,"cuid"),

    /** 默认创建者 */
    AUTO_USER(13333),

    /** 操作 */
    DELETE(010101,"delete"),

    ADD(010110,"add"),

    MODIFY(010111,"modify"),

    QUERY(010100,"query"),

    /** 操作目标 */
    COMMODITY(011001,"comm"),

    SHOP(011011,"shop"),

    USER(011010,"user"),

    RECORD(011110,"record"),

    EVALUATION(010010,"evaluation");

    //------------------------------

    public Integer getCode(){
        return this.code;
    }

    public String getMess(){
        return this.mess;
    }

    private Integer code;

    private String mess;

    AppEnum(Integer code){
        this.code = code;
    }

    AppEnum(Integer code, String mess){
        this.code = code;
        this.mess = mess;
    }
}
