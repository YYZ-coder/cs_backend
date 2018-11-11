package com.cls.web.user.entity.dto;

import com.cls.common.base.BaseDTO;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * Project: cs_backend
 *
 * @author Yue
 * @create 2018/5/15-21:40
 * Description：
 */
@Data
public class UserInfoDTO extends BaseDTO{

    /**
     * 账户名
     */
    @NotNull @Length(max = 14)
    private String accountName;


    /**
     * nickName 昵称
     */
    @NotNull @Length(max = 14)
    private String nickName;

    /**
     * 电话号码
     */
    @NotNull @Length(max = 11)
    private String phoneNum;

    /**
     * 密码
     */
    @NotNull
    private String passWord;

    /**
     * 用户邮箱
     */
    @NotNull
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
     * 性别
     */
    private String sex;

    /**
     * 语言
     */
    private String language;


}
