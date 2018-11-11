package com.cls.common.stat;

/**
 * Project: wx_cshop_end
 * @author Yue
 * @create 2018/3/25-15:37
 * Description：
 */
public enum LogStat implements Stat{

    LOG_FAILED("登录失败",10000),

    NO_AUTHC("无权限",10010),

    NO_LOGIN("没有登录", 10001),

    ERROR_PASSWORD("密码错误", 10002),

    NOTEXIST_ACCOUNT("账户不存在", 10003),

    NO_ROLES("无角色", 10004),

    NO_TOKEN("无token", 10005),

    EXPIRED_TOKEN("token 过期",10006),

    ERROR_PARAM("参数错误", 10007),

    REG_FASE("注册失败", 10008),

    ACCOUNT_NAME_REPEAT("账户名已存在",10009),

    SHOP_NAME_REPEAT("商铺名已存在", 10011),

    QUERY_FAILED("查询失败", 40001),

    DATA_FALSE("数据错误", 40002),

    INSERT_FAILED("数据插入失败", 40003),

    OPERAT_REPEAT("操作重复", 40004),

    NO_THUMBPIC("无缩略图", 40005),

    PARAM_LEAK("参数不完整", 40006),

    NOTEXIST_SHOP("商铺不存在", 40007),

    NO_OBJ("对象不存在", 40008),

    NOTEXIST_COMMODITY("商品不存在",40009),

    NO_DATA("查询不到数据", 40010),

    NO_EVALUATION("没有评论", 40011),

    UPDATE_FAILED("更新失败", 40012),

    NO_PICS("没有图片资源", 40013);


    //-------------------------

    public String message;

    public int code;

    LogStat(String message,int code){
        this.message = message;
        this.code = code;
    }


}
