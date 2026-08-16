package com.example.eduservice.mapper;

import com.example.eduservice.entity.EduArticle;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.eduservice.entity.frontvo.ArticleWebVo;

/**
 * <p>
 * 文章 Mapper 接口
 * </p>
 *
 * @author helloWorld
 * @since 2023-07-05
 */
public interface EduArticleMapper extends BaseMapper<EduArticle> {
    // 1.根据文章id，编写课程语句查询文章信息和作者信息
    ArticleWebVo getBaseArticleInfo(String articleId);
}
