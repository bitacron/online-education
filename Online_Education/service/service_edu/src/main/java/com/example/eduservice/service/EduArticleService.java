package com.example.eduservice.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.commonutils.vo.ArticleElasticSearchModel;
import com.example.eduservice.entity.EduArticle;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.eduservice.entity.EduCourse;
import com.example.eduservice.entity.frontvo.ArticleFrontVo;
import com.example.eduservice.entity.frontvo.ArticleWebVo;
import com.example.eduservice.entity.vo.ArticleQuery;

import java.util.Map;

/**
 * <p>
 * 文章 服务类
 * </p>
 *
 * @author helloWorld
 * @since 2023-07-05
 */
public interface EduArticleService extends IService<EduArticle> {

    // 条件分页查询
    void pageQuery(Page<EduArticle> pageArticle, ArticleQuery articleQuery);
    // 条件查询带分页查询课程（前台调用）
    Map<String, Object> getArticleFrontList(Page<EduArticle> pageParam, ArticleFrontVo articleFrontVo);
    // 1.根据文章id，编写课程语句查询文章信息和作者信息
    ArticleWebVo getBaseArticleInfo(String articleId);

    void updatePageViewCount(String id);

    ArticleElasticSearchModel articleToModel(EduArticle article); // 代码复用
}
