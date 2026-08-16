package com.example.eduservice.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.eduservice.entity.EduComment;
import com.example.eduservice.entity.EduCommentArticle;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 文章评论 服务类
 * </p>
 *
 * @author helloWorld
 * @since 2023-11-25
 */
public interface EduCommentArticleService extends IService<EduCommentArticle> {

    // 根据课程id分页查询评论列表
    Map<String, Object> getCommentFrontList(Page<EduCommentArticle> pageParam, String courseId);

}
