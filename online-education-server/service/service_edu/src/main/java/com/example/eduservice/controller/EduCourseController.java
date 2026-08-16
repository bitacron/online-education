package com.example.eduservice.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.commonutils.R;
import com.example.commonutils.vo.CourseOrderVo;
import com.example.eduservice.entity.EduCourse;
import com.example.eduservice.entity.EduTeacher;
import com.example.eduservice.entity.frontvo.CourseWebVo;
import com.example.eduservice.entity.vo.CourseInfoVo;
import com.example.eduservice.entity.vo.CoursePublishVo;
import com.example.eduservice.entity.vo.CourseQuery;
import com.example.eduservice.service.EduCourseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author helloWorld
 * @since 2023-06-10
 */
@Api(description = "课程后台管理")
@RestController
@RequestMapping("/eduservice/course")
//@CrossOrigin // 解决跨域问题
public class EduCourseController {

    @Autowired
    private EduCourseService courseService;

    // 课程列表 基本实现
    // TODO 完善条件查询带分页
    @ApiOperation(value = "条件查询分页方法")
    @PostMapping("pageCourseCondition/{current}/{limit}")
    public R getCourseList(@PathVariable Long current, @PathVariable Long limit,
                           @RequestBody(required = false) CourseQuery courseQuery) {
        Page<EduCourse> pageCourse = new Page<>(current, limit);
        courseService.pageQuery(pageCourse, courseQuery);
        long total = pageCourse.getTotal();
        List<EduCourse> records = pageCourse.getRecords();
        return R.ok().data("total", total).data("rows", records);
    }

    // 添加课程信息的方法
    @PostMapping("addCourseInfo")
    public R addCourseInfo(@RequestBody CourseInfoVo courseInfoVo) {
        // 返回添加之后的课程id，为了后面添加大纲使用
        String courseId = courseService.saveCourseInfo(courseInfoVo);
        return R.ok().data("courseId", courseId);
    }

    // 根据课程id查询课程基本信息，实现"上一步"功能需要的课程信息回显
    @ApiOperation(value = "根据ID查询课程")
    @GetMapping("getCourseInfo/{courseId}")
    public R getCourseInfo(@PathVariable String courseId){
        CourseInfoVo courseInfoVo = courseService.getCourseInfoById(courseId);
        return R.ok().data("courseInfoVo", courseInfoVo);
    }

    // 修改课程信息
    @ApiOperation(value = "更新课程")
    @PostMapping("updateCourseInfo")
    public R updateCourseInfo(@RequestBody CourseInfoVo courseInfoVo){
        courseService.updateCourseInfo(courseInfoVo);
        return R.ok();
    }

    // 根据课程id查询课程确认信息
    @ApiOperation(value = "确认课程信息")
    @GetMapping("getPublishCourseInfo/{id}")
    public R getPublishCourseInfo(@PathVariable String id){
        CoursePublishVo publishCourseVo = courseService.publishCourseInfo(id);
        return R.ok().data("publishCourse", publishCourseVo);
    }

    // 课程最终发布
    // 修改课程状态
    @ApiOperation(value = "课程最终发布")
    @PutMapping("publishCourse/{id}")
    public R publishCourse(@PathVariable String id) {
        courseService.publishCourse(id);  // 设置课程发布状态。Normal已发布；Draft未发布
        return R.ok();
    }

    // 删除课程 按照video、chapter、course_description、course的顺序依次删除4个表的数据
    @DeleteMapping("{courseId}")
    public R deleteCourse(@PathVariable String courseId) {
        courseService.removeCourse(courseId); // 依次删除video、chapter、course_description、course四个表中的数据
        return R.ok();
    }
    // 查询课程所有数据
    @ApiOperation(value = "所有课程列表")
    @GetMapping("findAll")
    public R findAllCourse() {
        // 调用service的方法实现查询所有的操作
        List<EduCourse> list = courseService.list(null);
        return R.ok().data("items", list);
    }

    // 根据课程id获取课程信息(生成订单方法远程调用)
    @GetMapping("getCourseInfoOrder/{courseId}")
    public CourseOrderVo getCourseInfoOrder(@PathVariable String courseId) {
        CourseWebVo courseInfo = courseService.getBaseCourseInfo(courseId);
        CourseOrderVo courseOrderVo = new CourseOrderVo();
        BeanUtils.copyProperties(courseInfo, courseOrderVo);
        return courseOrderVo;
    }

}

