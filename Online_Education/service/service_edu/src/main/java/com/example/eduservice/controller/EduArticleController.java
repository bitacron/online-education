package com.example.eduservice.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.commonutils.R;
import com.example.commonutils.vo.ArticleElasticSearchModel;
import com.example.eduservice.client.SearchClient;
import com.example.eduservice.entity.EduArticle;
import com.example.eduservice.entity.EduCourse;
import com.example.eduservice.entity.vo.ArticleQuery;
import com.example.eduservice.entity.vo.CourseInfoVo;
import com.example.eduservice.entity.vo.CourseQuery;
import com.example.eduservice.service.EduArticleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 文章 前端控制器
 * </p>
 *
 * @author helloWorld
 * @since 2023-07-05
 */
@Api(description = "文章后台管理")
@RestController
@RequestMapping("/eduservice/article")
public class EduArticleController {
    @Autowired
    private EduArticleService articleService;
    @Autowired
    private SearchClient searchClient;

    // 1.条件分页查询
    @ApiOperation(value = "条件查询分页方法")
    @PostMapping("pageArticleCondition/{current}/{limit}")
    public R getArticleList(@PathVariable Long current, @PathVariable Long limit,
                           @RequestBody(required = false) ArticleQuery articleQuery) {
        Page<EduArticle> pageArticle = new Page<>(current, limit);
        articleService.pageQuery(pageArticle, articleQuery);
        long total = pageArticle.getTotal();
        List<EduArticle> records = pageArticle.getRecords();
        return R.ok().data("total", total).data("rows", records);
    }

    // 2.添加课程信息的方法
    @PostMapping("addArticle")
    public R addArticleInfo(@RequestBody EduArticle article) {
        boolean save = articleService.save(article);
        ArticleElasticSearchModel searchArticle = articleService.articleToModel(article);
        //ArticleElasticSearchModel searchArticle = new ArticleElasticSearchModel();
        //searchArticle.setId(article.getId());
        //searchArticle.setContent(article.getContent());
        //searchArticle.setTitle(article.getTitle());
        //searchArticle.setMemberId(article.getMemberId());
        //searchArticle.setMemberName(article.getMemberName());
        //searchArticle.setViewCount(article.getViewCount()== null ? 0: article.getViewCount());
        //searchArticle.setLikeCount(article.getViewCount()== null ? 0: article.getLikeCount());
        //searchArticle.setCommentCount(article.getViewCount()== null ? 0: article.getCommentCount());
        //searchArticle.setGmtCreate(article.getGmtCreate());
        //searchArticle.setCateId(article.getCateId());
        System.out.println(searchArticle);
        searchClient.addDocument(searchArticle);
        if (save) {
            return R.ok();
        } else {
            return R.error();
        }
    }

    // 3.根据文章id查询文章基本信息
    @ApiOperation(value = "根据Id查询文章")
    @GetMapping("getArticle/{id}")
    public R getArticleInfo(@PathVariable String id){
        EduArticle articleInfo = articleService.getById(id);
        return R.ok().data("articleInfo", articleInfo);
    }

    // 修改文章信息
    @ApiOperation(value = "更新文章")
    @PostMapping("updateArticle")
    public R updateArticleInfo(@RequestBody EduArticle article){
        articleService.updateById(article);
        ArticleElasticSearchModel searchModel = articleService.articleToModel(article);
        searchClient.updateIndexDocument(searchModel);
        return R.ok();
    }
    // 逻辑删除文章方法
    @ApiOperation(value = "逻辑删除文章")
    @DeleteMapping("deleteArticle/{id}")
    public R removeArticle(@ApiParam(name = "id", value = "文章id", required = true) @PathVariable String id) {
        boolean flag = articleService.removeById(id);
        searchClient.deleteIndexDocument(id);
        if (flag) {
            return R.ok();
        } else {
            return R.error();
        }
    }
}

