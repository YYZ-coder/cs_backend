package com.cls.common.services.commodity;

import com.cls.common.base.BaseDTO;
import com.cls.common.services.resource.vo.PicInfoVO;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * Project: cs_backend
 * @author Yue
 * @create 2018/4/22-1:52
 * Description：
 *      商品DTO
 */
@Data
public class CommodityDTO extends BaseDTO{

    /** 商品名 */
    @NotNull @Length(min = 1, max = 20)
    private String commodityName;

    /** 商品shortName */
    @NotNull @Length(min = 1, max = 50)
    private String shortIntroduction;

    /** 商品详细介绍 */
    @NotNull @Length(min = 1, max = 200)
    private String introduction;

    /** 单价 */
    @NotNull
    private BigDecimal price;

    /** 规格：个/条/袋/斤/千克 */
    private String specifications;

    /** 库存（总数） */
    @NotNull @Min(1)
    private Integer inventory;

    /** 商铺id */
    @NotNull @Min(1)
    private Integer shopId;

    /** 类型：商品所属类别：0-默认|1-衣|2-食|3-其它 */
    private Integer classes;

    /** 种类: 在商铺中的类别 */
    private Integer categoryId;

    //--------------------------------

    /** 缩略图 */
    private List<PicInfoVO> thumbPic;

    /** 详情图片urls */
    private List<PicInfoVO> detailPicUrls;

    /** swiper的图片urls */
    private List<PicInfoVO> swiperPicUrls;

}
