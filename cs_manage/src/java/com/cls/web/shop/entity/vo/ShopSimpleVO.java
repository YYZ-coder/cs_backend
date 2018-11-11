package com.cls.web.shop.entity.vo;

import com.cls.common.base.BaseVO;
import lombok.Data;

/**
 * Project: cs_backend
 * @author Yue
 * @create 2018/5/17-1:07
 * Description：
 */
@Data
public class ShopSimpleVO extends BaseVO {

    private String shopName;

    /** 店铺简介 */
    private String shortIntroduction;

    /** 店铺类型：1-衣|2-食|3-其它 */
    private Integer type;

    /** 总销量 */
    private Integer sellsCount;

    /** 店主id */
    private Integer uid;

    /** 店主账户 */
    private String accountName;

    private String phoneNum;

    /** 星级 */
    private Integer starLevel;

    private Integer commodityCount;

    /** 店铺地址信息 */
    private Integer addressId;

    /** 店铺详细介绍 */
    private String introduction;
}
