package com.cls.web.shop.entity.dto;

import com.cls.common.base.BaseDTO;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * Project: cs_backend
 * @author Yue
 * @create 2018/5/17-0:40
 * Description：
 */
@Data
public class ShopInfoDTO extends BaseDTO {

    @NotNull
    private String shopName;

    /** 店铺简介 */
    @NotNull
    private String shortIntroduction;

    /** 店铺类型：1-衣|2-食|3-其它 */
    @NotNull
    private Integer type;

    /** 总销量 */
    private Integer sellsCount;

    /** 店主id */
    @NotNull
    private Integer uid;

    /** 店铺联系电话 */
    @Length(min = 11, max = 11)
    private String phoneNum;

    /** 店铺地址信息 */
    private Integer addressId;

    /** 店铺详细介绍 */
    @NotNull @Length(min = 20, max = 180)
    private String introduction;

}
