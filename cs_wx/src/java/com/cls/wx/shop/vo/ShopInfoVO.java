package com.cls.wx.shop.vo;

import com.cls.common.base.BaseVO;
import com.cls.common.services.resource.vo.PicInfoVO;
import lombok.Data;

import java.util.List;

/**
 * Project: cs_backend
 * @author Yue
 * @create 2018/4/22-14:15
 * Description：
 */
@Data
public class ShopInfoVO extends BaseVO{

    private String shopName;

    /** 店铺简介 */
    private String shortIntroduction;

    /** 店铺类型：1-衣|2-食|3-其它 */
    private Integer type;

    /** 总销量 */
    private Integer sellsCount;

    /** 店主id */
    private Integer uid;

    /** 星级 */
    private Integer starLevel;

    /** 店铺联系电话 */
    private String phoneNum;

    private Integer liked;

    private Integer commodityCount;

    /** 店铺地址信息 */
    private Integer addressId;

    /** 店铺详细介绍 */
    private String introduction;

    /** 缩略图 */
    private List<PicInfoVO> thumbPics;

    /** 详情图片urls */
    private List<PicInfoVO> detailPicUrls;
}
