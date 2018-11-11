package com.cls.common.services.user.entity;

import com.cls.common.base.BaseEntity;
import lombok.Data;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Project: cs_backend
 * @author Yue
 * @create 2018/1/22-23:42
 * Description：
 *  用户User实体
 *
 */
@Data
@Entity
@Table(name="cs_user_info")
public class UserInfo extends BaseEntity implements Serializable{

    @Column(name = "status")
    private Integer status;

    /**
     * 头像url
     */
    @Column(name = "avatar_url")
    private String avatarUrl;

    /**
     * 账户名
     */
    @Column(name = "account_name")
    private String accountName;

    /**
     * 用户唯一标识
     */
    @Column(name = "open_id")
    private String openId;

    /**
     * 职业id
     */
    @Column(name = "profession_id")
    private Integer professionId;

    /**
     * 角色Id
     */
    @Column(name = "role_id")
    private Integer roleId;

    /**
     * nickName 昵称
     */
    @Column(name = "nick_name")
    private String nickName;

    /**
     * 密码
     */
    @Column(name = "pass_word")
    private String passWord;

    /**
     * 电话号码
     */
    @Column(name = "phone_num")
    private String phoneNum;

    /**
     * 用户邮箱
     */
    @Column(name = "email")
    private String email;

    /**
     * 年龄
     */
    @Column(name = "age")
    private Integer age;

    /**
     * 城市
     */
    @Column(name = "city")
    private String city;

    /**
     * 用户所属省份
     */
    @Column(name = "province")
    private String province;

    /**
     * 用户所属国家
     */
    @Column(name = "country")
    private String country;

    /**
     * 用户性别: 0-女 | 1-男
     */
    @Column(name = "gender")
    private Integer gender;

    /**
     * 语言
     */
    @Column(name = "language")
    private String language;

    /**
     * 登录次数
     */
    @Column(name = "log_count")
    private Integer logCount;

    /**
     * 用户锁定
     */
    @Column(name = "locked")
    private Integer locked;

    public UserInfo(){}

    public UserInfo(String openId,
                    Integer professionId,
                    Integer roleId,
                    String nickName,
                    String phoneNum,
                    String email,
                    Integer age,
                    String avatarUrl,
                    String city,
                    String province,
                    String country,
                    Integer gender,
                    String language) {
        this.openId = openId;
        this.professionId = professionId;
        this.roleId = roleId;
        this.nickName = nickName;
        this.phoneNum = phoneNum;
        this.email = email;
        this.age = age;
        this.avatarUrl = avatarUrl;
        this.city = city;
        this.province = province;
        this.country = country;
        this.gender = gender;
        this.language = language;
    }
}
