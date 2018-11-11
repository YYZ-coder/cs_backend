package com.cls.wx.cart.dto;

import com.cls.common.base.BaseDTO;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Project: cs_backend
 * @author Yue
 * @create 2018/4/22-0:38
 * Description：
 *      用户购买的东西
 */
@Data
public class BuyThingDTO extends BaseDTO{

    /** 物品名称 */
    @NotNull
    private String name;

    /** 物品购买总数 */
    @Length(min = 1) @NotNull
    private Integer count;

    /** 物品总价格 */
    @NotNull @Min(1)
    private Integer amountPrice;

    /** 物品收货地址信息 */
    @NotNull @Min(1)
    private Integer addressId;

    /** 物品购买的用户 */
    @NotNull @Min(1)
    private Integer userId;
}
