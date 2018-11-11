package com.cls.web.user.entity.vo;

import com.cls.common.base.BaseVO;
import lombok.Data;

/**
 * Project: cs_backend
 * @author Yue
 * @create 2018/5/15-20:42
 * Description：
 */
@Data
public class UserInfoVO extends BaseVO {

    private Integer locked;

    /**
     * 头像url
     */
    private String avatarUrl;

    /**
     * 账户名
     */
    private String accountName;


    /**
     * 角色Id
     */
    private Integer roleId;

    /**
     * nickName 昵称
     */
    private String nickName;

    /**
     * 电话号码
     */
    private String phoneNum;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 城市
     */
    private String city;

    /**
     * 用户所属省份
     */
    private String province;

    /**
     * 用户所属国家
     */
    private String country;

    /**
     * 用户性别: 0-女 | 1-男
     */
    private Integer gender;

    private String sex;

    /**
     * 语言
     */
    private String language;

    /**
     * 登录次数
     */
    private Integer logCount;

    /**
     * 角色信息
     */
    private RoleInfoVO roleInfo;
}
