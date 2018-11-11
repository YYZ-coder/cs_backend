package com.cls.wx.IndexLoad.vo;

import com.cls.common.base.BaseVO;
import com.cls.common.base.ShortVO;
import com.cls.common.services.commodity.vo.CommodityShortVO;
import com.cls.wx.shop.vo.ShopShortVO;
import lombok.Data;

import java.util.List;

/**
 * Project: cs_backend
 * @author Yue
 * @create 2018/4/25-1:30
 * Description：
 *      主页显示的数据
 */
@Data
public class IndexDataVO extends BaseVO{

    /** swiper滚动控件的数据 */
    private List<ShortVO> swipers;

    /** 最热商品 block */
    private List<CommodityShortVO> hotObjects;

    /** 最新商品 block */
    private List<CommodityShortVO> newsObjects;

    /** 商铺 block */
    private List<ShopShortVO> shops;

}
