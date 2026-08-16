package com.example.commonutils.vo;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/*
PUT article
{
  "mappings": {
    "properties": {
      "id":{
        "type": "long"
      },
       "title":{
        "type": "text",
        "analyzer": "ik_smart"
      },
      "content":{
        "type": "text",
        "analyzer": "ik_smart"
      },
      "memberId":{
        "type": "long"
      },
      "memberName":{
        "type": "text"
      },
      "likeCount":{
        "type": "long"
      },
      "commentCount":{
        "type": "long"
      },
      "viewCount":{
        "type": "long"
      },
      "cateId":{
        "type": "long"
      },
      "gmtCreate":{
        "type": "date"
      }
    }
  }
}
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="SearchArticle对象", description="文章整合ElasticSearch实现高亮搜索")
@AllArgsConstructor
@NoArgsConstructor
public class ArticleElasticSearchModel {
    @ApiModelProperty(value = "文章Id")
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    @ApiModelProperty(value = "文章标题")
    private String title;

    @ApiModelProperty(value = "文章内容")
    private String content;

    @ApiModelProperty(value = "发表用户")
    private String memberId;

    @ApiModelProperty(value = "发表用户昵称")
    private String memberName;

    @ApiModelProperty(value = "点赞数")
    private Long likeCount;

    @ApiModelProperty(value = "评论数")
    private Long commentCount;

    @ApiModelProperty(value = "浏览数")
    private Long viewCount;

    @ApiModelProperty(value = "文章分类Id")
    private String cateId;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

}

