package com.example.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.eduservice.entity.EduComment;
import com.example.eduservice.entity.EduCommentArticle;
import com.example.eduservice.mapper.EduCommentArticleMapper;
import com.example.eduservice.service.EduCommentArticleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 文章评论 服务实现类
 * </p>
 *
 * @author helloWorld
 * @since 2023-11-25
 */
@Service
public class EduCommentArticleServiceImpl extends ServiceImpl<EduCommentArticleMapper, EduCommentArticle> implements EduCommentArticleService {
    // 根据课程id分页查询评论列表
    @Override
    public Map<String, Object> getCommentFrontList(Page<EduCommentArticle> pageParam, String articleId) {
        QueryWrapper<EduCommentArticle> wrapper = new QueryWrapper<>();
        wrapper.eq("article_id",articleId);
        baseMapper.selectPage(pageParam, wrapper);
        List<EduCommentArticle> commentList = pageParam.getRecords();
        Map<String, Object> map = new HashMap<>();
        map.put("items", commentList);
        map.put("current", pageParam.getCurrent());
        map.put("pages", pageParam.getPages());
        map.put("size", pageParam.getSize());
        map.put("total", pageParam.getTotal());
        map.put("hasNext", pageParam.hasNext());
        map.put("hasPrevious", pageParam.hasPrevious());
        return map;
    }
}
