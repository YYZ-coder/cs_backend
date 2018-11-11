package com.cls.wx.commodity.vo;

import com.cls.common.base.BaseVO;
import com.cls.wx.evaluate.vo.EvaluationVO;
import com.cls.common.services.resource.vo.PicInfoVO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Project: cs_backend
 * @author Yue
 * @create 2018/4/22-17:16
 * Description：
 *
 */
@Data
public class CommodityVO extends BaseVO {

    //-----------shopInfo------------

    private String shopName;

    //----------userOperation--------

    /** 用户是否liked */
    private boolean liked;

    //-------------------------------

    //--------commodityInfo----------

    /** 归属的商铺信息 */
    private Integer shopId;

    /** 商品名 */
    private String commodityName;

    /** 商品shortName */
    private String shortIntroduction;

    /** 商品详细介绍 */
    private String introduction;

    /** 单价 */
    private BigDecimal price;

    /** 规格：个/条/袋/斤/千克 */
    private String specifications;

    /** 库存（总数） */
    private Integer inventory;

    /** 类型：0-衣|1-食|2-其它 */
    private Integer classes;

    /** 种类 */
    private Integer categoryId;

    //---------------------------------

    //------------resource-------------

    /** 缩略图 */
    private List<PicInfoVO> thumbPic;

    /** 详情图片urls */
    private List<PicInfoVO> detailPicUrls;

    /** swiper的图片urls */
    private List<PicInfoVO> swiperPicUrls;

    //---------------------------------

    private EvaluationVO evaluationVO;

    /** 评论总数 */
    private Integer evaluateCount;

    private Integer sellsCount;

}
