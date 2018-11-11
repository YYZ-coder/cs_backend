package com.cls.wx.address.dto;

import com.cls.common.base.BaseDTO;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Project: cs_backend
 * @author Yue
 * @create 2018/4/25-22:09
 * Description：
 */
@Data
public class AddressDTO extends BaseDTO{

    private Integer pid;

    @NotNull @Min(0)
    private Integer type;

    @NotNull
    private String province;

    @NotNull
    private String county;

    @NotNull
    private String city;

    /** 收货人名 */
    @NotNull @Length(max = 10, min = 1)
    private String consigneeName;

    /** 收货人手机号 */
    @NotNull @Length(max = 11,min = 11)
    private String phoneNum;

    /** 收货人详细地址/商铺详细地址 */
    private String detailLocation;

    /** 是否为用户的默认收货地址：0-不默认|1-默认 */
    private Integer isDefault;
}
