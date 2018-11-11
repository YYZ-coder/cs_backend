package com.cls.wx.evaluate.dto;

import com.cls.common.base.BaseDTO;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Project: cs_backend
 * @author Yue
 * @create 2018/4/22-0:50
 * Description：
 *      添加的评论
 */
@Data
public class EvaluationDTO extends BaseDTO{

    /** 评论标题 */
    @NotNull
    @Length(max = 11)
    private String title;

    /** 评论内容 */
    @NotNull
    @Length(max = 200)
    private String content;

    /** 0-商品 */
    @NotNull
    @Min(0)
    private Integer type;

    /** 评论用户id */
    private Integer userId;

    /** 评论的商品id */
    @NotNull
    private Integer objId;

    @NotNull
    private Integer orderId;

    /** 星级数 */
    @NotNull @Min(1)
    private Integer starCount;


}
