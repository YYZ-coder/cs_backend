package com.cls.wx.shop.vo;

import com.cls.common.base.ShortVO;
import lombok.Data;

/**
 * Project: cs_backend
 * @author Yue
 * @create 2018/4/25-1:33
 * Description：
 *
 */
@Data
public class ShopShortVO extends ShortVO {

    private String shopName;

    private Integer starLevel;

    private Integer sellsCount;

}
