package com.example.eduservice.controller.front;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.commonutils.JwtUtils;
import com.example.commonutils.R;
import com.example.eduservice.entity.EduArticle;
import com.example.eduservice.entity.EduCourse;
import com.example.eduservice.entity.chapter.ChapterVo;
import com.example.eduservice.entity.frontvo.ArticleFrontVo;
import com.example.eduservice.entity.frontvo.ArticleWebVo;
import com.example.eduservice.entity.frontvo.CourseFrontVo;
import com.example.eduservice.entity.frontvo.CourseWebVo;
import com.example.eduservice.service.EduArticleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Api(description = "文章前台管理")
@RestController
@RequestMapping("/eduservice/articlefront")
public class ArticleFrontController {
    @Autowired
    private EduArticleService articleService;
    // 1.条件查询带分页查询课程
    @ApiOperation(value = "分页课程列表")
    @PostMapping(value = "getFrontArticleList/{page}/{limit}")
    public R getFrontArticleList(@PathVariable Long page, @PathVariable Long limit, @RequestBody(required = false) ArticleFrontVo articleFrontVo){
        Page<EduArticle> pageParam = new Page<EduArticle>(page, limit);
        Map<String, Object> map = articleService.getArticleFrontList(pageParam, articleFrontVo);
        return R.ok().data(map);
    }

    // 2.课程详情的方法
    @ApiOperation(value = "根据ID查询文章")
    @GetMapping(value = "getFrontArticleInfo/{articleId}")
    public R getFrontArticleInfo(@PathVariable String articleId, HttpServletRequest request){
        // 根据文章id，编写课程语句查询文章信息和作者信息
         ArticleWebVo articleWebVo = articleService.getBaseArticleInfo(articleId);
        // 2.根据文章id,查询当前课程的章节信息
        //List<ChapterVo> chapterVideoList = articleService.getChapterVideoByCourseId(courseId);
        // 3.根据课程id和用户id，查询当前课程是否已经成功支付过了
        //boolean buyCourse = orderClient.isBuyCourse(courseId, JwtUtils.getMemberIdByJwtToken(request));
        return R.ok().data("articleWebVo", articleWebVo);
    }
}
