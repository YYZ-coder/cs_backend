package com.cls.wx.user.vo;

import com.cls.common.base.BaseVO;
import com.cls.common.services.commodity.vo.CommodityShortVO;
import com.cls.wx.shop.vo.ShopShortVO;
import lombok.Data;

import java.util.List;

/**
 * Project: cs_backend
 * @author Yue
 * @create 2018/4/26-2:05
 * Description：
 */
@Data
public class SearchCombiResultVO extends BaseVO {

    private List<CommodityShortVO> commodityShortVOS;

    private List<ShopShortVO> shopShortVOS;
}
