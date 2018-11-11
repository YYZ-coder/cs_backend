package com.cls.common.services.address.entity;

import com.cls.common.base.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Project: cs_backend
 * @author Yue
 * @create 2018/4/22-13:47
 * Description：
 *      地址信息
 */
@Data
@Entity
@Table(name= "cs_address_info")
public class AddressInfo extends BaseEntity implements Serializable {

    /** 归属者id：用户/商铺 */
    @Column(name= "pid")
    private Integer pid;

    /** 0-普通用户地址信息|1-商铺地址信息 */
    @Column(name= "type")
    private Integer type;

    @Column(name= "province")
    private String province;

    @Column(name= "county")
    private String county;

    @Column(name= "city")
    private String city;

    /** 收货人名 */
    @Column(name= "consignee_name")
    private String consigneeName;

    /** 收货人手机号 */
    @Column(name= "phone_num")
    private String phoneNum;

    /** 收货人详细地址/商铺详细地址 */
    @Column(name= "detail_location")
    private String detailLocation;

    /** 是否为用户的默认收货地址：0-不默认|1-默认 */
    @Column(name= "is_default")
    private Integer isDefault;

    /** 0-未删除|1-已删除 */
    @Column(name = "status")
    private Integer status;
}
