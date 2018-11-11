package com.cls.wx.address.vo;

import com.cls.common.base.BaseVO;
import lombok.Data;

/**
 * Project: cs_backend
 * @author Yue
 * @create 2018/4/25-22:02
 * Description：
 */
@Data
public class AddressInfoVO extends BaseVO{

    private String province;

    private String county;

    private String city;

    /** 收货人名 */
    private String consigneeName;

    /** 收货人手机号 */
    private String phoneNum;

    /** 收货人详细地址/商铺详细地址 */
    private String detailLocation;

    /** 是否为用户的默认收货地址：0-不默认|1-默认 */
    private Integer isDefault;

}
