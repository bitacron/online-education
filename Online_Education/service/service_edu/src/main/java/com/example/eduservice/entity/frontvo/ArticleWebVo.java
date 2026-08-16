package com.example.eduservice.entity.frontvo;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@ApiModel(value="文章信息", description="网站文章详情页和列表页需要的相关字段")
@Data
public class ArticleWebVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "文章Id")
    private String id;

    @ApiModelProperty(value = "发表用户")
    private String memberId;

    @ApiModelProperty(value = "发表用户昵称")
    private String memberName;

    @ApiModelProperty(value = "用户头像")
    private String avatar;

    @ApiModelProperty(value = "文章标题")
    private String title;

    @ApiModelProperty(value = "文章内容")
    private String content;

    @ApiModelProperty(value = "文章状态 Draft未发布  Normal已发布")
    private String state;

    @ApiModelProperty(value = "点赞数")
    private Long likeCount;

    @ApiModelProperty(value = "评论数")
    private Long commentCount;

    @ApiModelProperty(value = "浏览数")
    private Long viewCount;

    @ApiModelProperty(value = "文章分类Id")
    private String cateId;

    @ApiModelProperty(value = "文章分类名")
    private String cate;

    @ApiModelProperty(value = "创建时间")
    private Date gmtCreate;

    @ApiModelProperty(value = "更新时间")
    private Date gmtModified;

}
