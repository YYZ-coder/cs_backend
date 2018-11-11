package com.cls.wx.cart.vo;

import com.cls.common.services.resource.vo.PicInfoVO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Project: cs_backend
 * @author Yue
 * @create 2018/4/29-13:06
 * Description：
 */
@Data
public class CartCommVO {

    private Integer id;

    /** 总数 */
    private Integer count;

    /** 缩略图 */
    private List<PicInfoVO> thumbPic;

    /** 价格 */
    private BigDecimal price;

    /** 商品名 */
    private String commodityName;

    /** 库存 */
    private Integer inventory;
}
