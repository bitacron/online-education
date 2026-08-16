package com.example.eduservice.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.eduservice.entity.EduComment;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 评论 服务类
 * </p>
 *
 * @author helloWorld
 * @since 2023-06-25
 */
public interface EduCommentService extends IService<EduComment> {

    // 根据课程id分页查询评论列表
    Map<String, Object> getCommentFrontList(Page<EduComment> pageParam, String courseId);
}
