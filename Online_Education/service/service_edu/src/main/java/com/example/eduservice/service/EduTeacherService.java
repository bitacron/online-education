package com.example.eduservice.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.eduservice.entity.EduTeacher;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.eduservice.entity.vo.TeacherQuery;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 服务类
 * </p>
 *
 * @author helloWorld
 * @since 2023-05-31
 */
public interface EduTeacherService extends IService<EduTeacher> {

    // 条件查询分页方法
    void pageQuery(Page<EduTeacher> pageTeacher, TeacherQuery teacherQuery);

    // 查询前4条名师
    List<EduTeacher> getHotTeacher();

    // 前台分页查询讲师的方法
    Map<String, Object> getTeacherFrontList(Page<EduTeacher> pageParam);
}
