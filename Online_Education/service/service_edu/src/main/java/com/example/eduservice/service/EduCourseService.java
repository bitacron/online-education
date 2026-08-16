package com.example.eduservice.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.eduservice.entity.EduCourse;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.eduservice.entity.frontvo.CourseFrontVo;
import com.example.eduservice.entity.frontvo.CourseWebVo;
import com.example.eduservice.entity.vo.CourseInfoVo;
import com.example.eduservice.entity.vo.CoursePublishVo;
import com.example.eduservice.entity.vo.CourseQuery;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author helloWorld
 * @since 2023-06-10
 */
public interface EduCourseService extends IService<EduCourse> {

    // 添加课程信息的方法
    String saveCourseInfo(CourseInfoVo courseInfoVo);

    // 根据课程id查询课程基本信息，实现"上一步"功能需要的课程信息回显
    CourseInfoVo getCourseInfoById(String courseId);

    // 修改课程信息
    void updateCourseInfo(CourseInfoVo courseInfoVo);

    // 根据课程id查询课程确认信息
    CoursePublishVo publishCourseInfo(String id);

    // 课程最终发布, 修改课程状态
    boolean publishCourse(String id);

    // 条件查询分页方法
    void pageQuery(Page<EduCourse> pageCourse, CourseQuery courseQuery);

    // 删除课程 按照video、chapter、course_description、course的顺序依次删除4个表的数据
    void removeCourse(String courseId);

    // 查询前8条热门课程
    List<EduCourse> getHotCourse();

    // 根据讲师id查询这个讲师的课程列表
    List<EduCourse> selectByTeacherId(String teacherId);

    // 条件查询带分页查询课程
    Map<String, Object> getCourseFrontList(Page<EduCourse> pageParam, CourseFrontVo courseFrontVo);

    // 根据课程id，编写sql语句查询课程信息和讲师信息
    CourseWebVo getBaseCourseInfo(String courseId);

    // 更新课程浏览数
    void updatePageViewCount(String id);
}
