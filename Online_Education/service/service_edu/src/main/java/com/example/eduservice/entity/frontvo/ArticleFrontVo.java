package com.example.eduservice.entity.frontvo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@ApiModel(value = "文章查询对象", description = "文章查询对象封装")
@Data
public class ArticleFrontVo implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "文章标题")
    private String title;

    @ApiModelProperty(value = "文章发布者")
    private String memberId;

    @ApiModelProperty(value = "文章类别id")
    private String cateId;

    @ApiModelProperty(value = "观看量排序")
    private String viewSort;

    @ApiModelProperty(value = "最新时间排序")
    private String gmtCreateSort;

}
