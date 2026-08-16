package com.example.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.commonutils.vo.ArticleElasticSearchModel;
import com.example.eduservice.entity.EduArticle;
import com.example.eduservice.entity.frontvo.ArticleFrontVo;
import com.example.eduservice.entity.frontvo.ArticleWebVo;
import com.example.eduservice.entity.vo.ArticleQuery;
import com.example.eduservice.mapper.EduArticleMapper;
import com.example.eduservice.service.EduArticleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 文章 服务实现类
 * </p>
 *
 * @author helloWorld
 * @since 2023-07-05
 */
@Service
public class EduArticleServiceImpl extends ServiceImpl<EduArticleMapper, EduArticle> implements EduArticleService {

    @Override
    public void pageQuery(Page<EduArticle> pageArticle, ArticleQuery articleQuery) {
        QueryWrapper<EduArticle> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("gmt_create");
        if (articleQuery == null){
            baseMapper.selectPage(pageArticle, queryWrapper);
            return;
        }
        String title = articleQuery.getTitle();
        String cateId = articleQuery.getCateId();
        String memberName = articleQuery.getMemberName();
        String begin = articleQuery.getBegin();
        String end = articleQuery.getEnd();
        if (!StringUtils.isEmpty(title)) {
            queryWrapper.like("title", title);
        }
        if (!StringUtils.isEmpty(memberName)) {
            queryWrapper.like("member_name", memberName);
        }
        if (!StringUtils.isEmpty(cateId) ) {
            queryWrapper.eq("cate_id", cateId);
        }
        if (!StringUtils.isEmpty(begin)) {
            queryWrapper.ge("gmt_create", begin); // ge大于
        }
        if (!StringUtils.isEmpty(end)) {
            queryWrapper.le("gmt_create", end); // le小于
        }
        baseMapper.selectPage(pageArticle, queryWrapper);
    }

    // 条件查询带分页查询课程（前台调用）
    @Override
    public Map<String, Object> getArticleFrontList(Page<EduArticle> pageParam, ArticleFrontVo articleFrontVo) {
        QueryWrapper<EduArticle> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(articleFrontVo.getCateId())) { // 一级分类
            queryWrapper.eq("cate_id", articleFrontVo.getCateId());
        }
        if (!StringUtils.isEmpty(articleFrontVo.getViewSort())) { // 关注度
            queryWrapper.orderByDesc("view_count");
        }
        if (!StringUtils.isEmpty(articleFrontVo.getGmtCreateSort())) { // 最新
            queryWrapper.orderByDesc("gmt_create");
        }
        baseMapper.selectPage(pageParam, queryWrapper);
        List<EduArticle> records = pageParam.getRecords();
        long current = pageParam.getCurrent();
        long pages = pageParam.getPages();
        long size = pageParam.getSize();
        long total = pageParam.getTotal();
        boolean hasNext = pageParam.hasNext();
        boolean hasPrevious = pageParam.hasPrevious();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("items", records);
        map.put("current", current);
        map.put("pages", pages);
        map.put("size", size);
        map.put("total", total);
        map.put("hasNext", hasNext);
        map.put("hasPrevious", hasPrevious);
        return map;
    }

    // 1.根据文章id，编写课程语句查询文章信息和作者信息
    @Override
    public ArticleWebVo getBaseArticleInfo(String articleId) {
        this.updatePageViewCount(articleId); // 更新浏览数
        return baseMapper.getBaseArticleInfo(articleId);
    }

    // 更新浏览数+1
    @Override
    public void updatePageViewCount(String id) {
        EduArticle article = baseMapper.selectById(id);
        article.setViewCount(article.getViewCount() + 1);
        baseMapper.updateById(article);
    }

    public ArticleElasticSearchModel articleToModel(EduArticle article) {
        ArticleElasticSearchModel searchArticle = new ArticleElasticSearchModel();
        searchArticle.setId(article.getId());
        searchArticle.setContent(article.getContent());
        searchArticle.setTitle(article.getTitle());
        searchArticle.setMemberId(article.getMemberId());
        searchArticle.setMemberName(article.getMemberName());
        searchArticle.setViewCount(article.getViewCount()== null ? 0: article.getViewCount());
        searchArticle.setLikeCount(article.getViewCount()== null ? 0: article.getLikeCount());
        searchArticle.setCommentCount(article.getViewCount()== null ? 0: article.getCommentCount());
        searchArticle.setGmtCreate(article.getGmtCreate());
        searchArticle.setCateId(article.getCateId());
        return searchArticle;
    }
}
