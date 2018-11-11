package com.cls.wx.evaluate.vo;

import com.cls.common.base.BaseVO;
import com.cls.common.services.resource.vo.PicInfoVO;
import lombok.Data;

import java.util.List;

/**
 * Project: cs_backend
 * @author Yue
 * @create 2018/4/27-0:17
 * Description：
 */
@Data
public class EvaluationVO extends BaseVO {

    /** 评价类型 */
    private Integer type;

    private String title;

    private String content;

    /** 匿名 */
    private String anonymityName;

    /** 评论对象id */
    private Integer objId;

    /** 用户名 */
    private String userName;

    /** 星级数 */
    private Integer starCount;

    /** 评论附带的图片资源 */
    private List<PicInfoVO> thumbPics;

    /** 评论附带大图资源 */
    private List<PicInfoVO> detailPics;

}
