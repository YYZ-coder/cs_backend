package com.cls.web.user;

/**
 * Project: cs_backend
 * @author Yue
 * @create 2018/5/16-9:33
 * Description：
 */
public enum ROLE {

    WECHAT_USER(1,"微信用户"),
    SHOP_OWNER(2,"店主"),
    ADMIN(3,"管理员");

    public Integer code;
    public String name;

    ROLE(Integer code, String name){
        this.code = code;
        this.name = name;
    }
}
