package com.example.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.eduservice.entity.EduCourse;
import com.example.eduservice.entity.EduCourseDescription;
import com.example.eduservice.entity.frontvo.CourseFrontVo;
import com.example.eduservice.entity.frontvo.CourseWebVo;
import com.example.eduservice.entity.vo.CourseInfoVo;
import com.example.eduservice.entity.vo.CoursePublishVo;
import com.example.eduservice.entity.vo.CourseQuery;
import com.example.eduservice.mapper.EduCourseMapper;
import com.example.eduservice.service.EduChapterService;
import com.example.eduservice.service.EduCourseDescriptionService;
import com.example.eduservice.service.EduCourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.eduservice.service.EduVideoService;
import com.example.servicebase.exceptionhandler.CustomException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author helloWorld
 * @since 2023-06-10
 */
@Service
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {

    // 课程描述注入
    @Autowired
    private EduCourseDescriptionService courseDescriptionService;

    @Autowired
    private EduVideoService videoService;

    @Autowired
    private EduChapterService chapterService;

    // 添加课程信息的方法
    @Override
    public String saveCourseInfo(CourseInfoVo courseInfoVo) {
        // 1、向课程表了添加课程基本信息
        // CourseInfoVo对象转换为EduCourse对象，才能insert到数据库中
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoVo, eduCourse); // 工具类的方法，相当于吧CourseInfoVo的对象set到EduCourse中
        int insertRows = baseMapper.insert(eduCourse);
        if (insertRows <= 0) {
            // 添加失败
            throw new CustomException(20001, "课程添加失败");
        }

        // 获取课程Id，方便课程表与课程描述表对应上
        String courseId = eduCourse.getId();

        // 2、向课程简介表里添加课程简介
        EduCourseDescription eduCourseDescription = new EduCourseDescription();
        eduCourseDescription.setDescription(courseInfoVo.getDescription());
        eduCourseDescription.setId(courseId); // 课程描述Id要与课程Id对应上
        courseDescriptionService.save(eduCourseDescription);
        return courseId;
    }

    // 根据课程id查询课程基本信息，实现"上一步"功能需要的课程信息回显
    @Override
    public CourseInfoVo getCourseInfoById(String courseId) {
        // 1.查询课程表
        EduCourse eduCourse = baseMapper.selectById(courseId);
        CourseInfoVo courseInfoVo = new CourseInfoVo();
        BeanUtils.copyProperties(eduCourse, courseInfoVo);
        // 2.查询课程信息表
        EduCourseDescription courseDescription = courseDescriptionService.getById(courseId);
        courseInfoVo.setDescription(courseDescription.getDescription());
        return courseInfoVo;
    }

    // 修改课程信息
    @Override
    public void updateCourseInfo(CourseInfoVo courseInfoVo) {
        // 1.修改课程表
        EduCourse course = new EduCourse();
        BeanUtils.copyProperties(courseInfoVo, course);
        int updateRows = baseMapper.updateById(course);
        if(updateRows == 0){
            throw new CustomException(20001, "课程信息保存失败");
        }
        // 2. 修改课程描述表
        EduCourseDescription courseDescription = new EduCourseDescription();
        courseDescription.setId(course.getId());
        courseDescription.setDescription(courseInfoVo.getDescription());
        courseDescriptionService.updateById(courseDescription);
    }

    // 根据课程id查询课程确认信息
    @Override
    public CoursePublishVo publishCourseInfo(String id) {
        // 调用mapper
        CoursePublishVo publishCourseInfo = baseMapper.getPublishCourseInfo(id);
        return publishCourseInfo;
    }

    // 课程最终发布, 修改课程状态
    @Override
    public boolean publishCourse(String id) {
        EduCourse eduCourse = new EduCourse();
        eduCourse.setId(id);
        eduCourse.setStatus("Normal"); // 设置课程发布状态。Normal已发布；Draft未发布
        Integer updateRows = baseMapper.updateById(eduCourse);
        return null != updateRows && updateRows > 0;
    }

    // 条件查询分页方法
    @Override
    public void pageQuery(Page<EduCourse> pageCourse, CourseQuery courseQuery) {
        QueryWrapper<EduCourse> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("gmt_create");
        if (courseQuery == null){
            baseMapper.selectPage(pageCourse, queryWrapper);
            return;
        }
        String title = courseQuery.getTitle();
        String teacherId = courseQuery.getTeacherId();
        String subjectParentId = courseQuery.getSubjectParentId();
        String subjectId = courseQuery.getSubjectId();
        if (!StringUtils.isEmpty(title)) {
            queryWrapper.like("title", title);
        }
        if (!StringUtils.isEmpty(teacherId) ) {
            queryWrapper.eq("teacher_id", teacherId);
        }
        if (!StringUtils.isEmpty(subjectParentId)) {
            queryWrapper.eq("subject_parent_id", subjectParentId);
        }
        if (!StringUtils.isEmpty(subjectId)) {
            queryWrapper.eq("subject_id", subjectId);
        }
        baseMapper.selectPage(pageCourse, queryWrapper);
    }

    // 删除课程。按照video、chapter、course_description、course的顺序依次删除4个表的数据
    @Override
    public void removeCourse(String courseId) {
        // 1.根据课程id删除课程小节 即删除video表中的数据
        videoService.removeVideoByCourseId(courseId);
        // 2.根据课程id删除课程章节 即删除chapter表中的数据
        chapterService.removeChapterByCourseId(courseId);
        // 3.根据课程id删除课程描述 即删除course_description表中的数据
        courseDescriptionService.removeById(courseId);
        // 4.根据课程id删除课程本身 即删除course表中的数据
        int deleteRows = baseMapper.deleteById(courseId);
        if (deleteRows == 0) { // 失败返回
            throw new CustomException(20001, "删除失败");
        }

    }

    // 查询前8条热门课程
    @Cacheable(key = "'selectHotCourseList'", value = "course")
    @Override
    public List<EduCourse> getHotCourse() {
        //查询前8条热门课程
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("view_count");
        wrapper.last("limit 8");
        List<EduCourse> HotCourseList = baseMapper.selectList(wrapper);
        return HotCourseList;
    }

    @Override
    public List<EduCourse> selectByTeacherId(String teacherId) {
        QueryWrapper<EduCourse> queryWrapper = new QueryWrapper<EduCourse>();
        queryWrapper.eq("teacher_id", teacherId);
        //按照最后更新时间倒序排列
        queryWrapper.orderByDesc("gmt_modified");
        List<EduCourse> courses = baseMapper.selectList(queryWrapper);
        return courses;
    }

    // 条件查询带分页查询课程
    @Override
    public Map<String, Object> getCourseFrontList(Page<EduCourse> pageParam, CourseFrontVo courseFrontVo) {
        QueryWrapper<EduCourse> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(courseFrontVo.getSubjectParentId())) { // 一级分类
            queryWrapper.eq("subject_parent_id", courseFrontVo.getSubjectParentId());
        }

        if (!StringUtils.isEmpty(courseFrontVo.getSubjectId())) { // 二级分类
            queryWrapper.eq("subject_id", courseFrontVo.getSubjectId());
        }

        if (!StringUtils.isEmpty(courseFrontVo.getBuyCountSort())) { // 关注度
            queryWrapper.orderByDesc("buy_count");
        }

        if (!StringUtils.isEmpty(courseFrontVo.getGmtCreateSort())) { // 最新
            queryWrapper.orderByDesc("gmt_create");
        }

        if (!StringUtils.isEmpty(courseFrontVo.getPriceSort())) { // 价格
            queryWrapper.orderByDesc("price");
        }

        baseMapper.selectPage(pageParam, queryWrapper);

        List<EduCourse> records = pageParam.getRecords();
        long current = pageParam.getCurrent(); // 当前是第几页
        long pages = pageParam.getPages(); // 总页数
        long size = pageParam.getSize(); // 每页数据数目
        long total = pageParam.getTotal(); // 总计结果数
        boolean hasNext = pageParam.hasNext(); // 是否有下一页
        boolean hasPrevious = pageParam.hasPrevious(); // 是否有上一页
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

    // 根据课程id，编写sql语句查询课程信息和讲师信息
    @Override
    public CourseWebVo getBaseCourseInfo(String courseId) {
        this.updatePageViewCount(courseId);
        return baseMapper.getBaseCourseInfo(courseId);
    }

    @Override
    public void updatePageViewCount(String id) {
        EduCourse course = baseMapper.selectById(id);
        course.setViewCount(course.getViewCount() + 1);
        baseMapper.updateById(course);
    }
}
