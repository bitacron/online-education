package com.example.eduservice.controller.front;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.commonutils.JwtUtils;
import com.example.commonutils.R;
import com.example.eduservice.client.OrderClient;
import com.example.eduservice.entity.EduCourse;
import com.example.eduservice.entity.chapter.ChapterVo;
import com.example.eduservice.entity.frontvo.CourseFrontVo;
import com.example.eduservice.entity.frontvo.CourseWebVo;
import com.example.eduservice.service.EduChapterService;
import com.example.eduservice.service.EduCourseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
@Api(description = "课程前台管理")
@RestController
@RequestMapping("/eduservice/coursefront")
//@CrossOrigin
public class CourseFrontController {

    @Autowired
    private EduCourseService courseService;
    @Autowired
    private EduChapterService chapterService;
    @Autowired
    private OrderClient orderClient;

    // 1.条件查询带分页查询课程
    @ApiOperation(value = "分页课程列表")
    @PostMapping(value = "getFrontCourseList/{page}/{limit}")
    public R getFrontCourseList(@PathVariable Long page, @PathVariable Long limit, @RequestBody(required = false) CourseFrontVo courseFrontVo){
        Page<EduCourse> pageParam = new Page<EduCourse>(page, limit);
        Map<String, Object> map = courseService.getCourseFrontList(pageParam, courseFrontVo);
        return R.ok().data(map);
    }

    // 2.课程详情的方法
    @ApiOperation(value = "根据ID查询课程")
    @GetMapping(value = "getFrontCourseInfo/{courseId}")
    public R getFrontCourseInfo(@PathVariable String courseId, HttpServletRequest request){
        // 1.根据课程id，编写课程语句查询课程信息和讲师信息
        CourseWebVo courseWebVo = courseService.getBaseCourseInfo(courseId);
        // 2.根据课程id,查询当前课程的章节信息
        List<ChapterVo> chapterVideoList = chapterService.getChapterVideoByCourseId(courseId);
        // 3.根据课程id和用户id，查询当前课程是否已经成功支付过了
        boolean buyCourse = orderClient.isBuyCourse(courseId, JwtUtils.getMemberIdByJwtToken(request));
        return R.ok().data("courseWebVo", courseWebVo).data("chapterVoList", chapterVideoList).data("isBuy", buyCourse);
    }
}
