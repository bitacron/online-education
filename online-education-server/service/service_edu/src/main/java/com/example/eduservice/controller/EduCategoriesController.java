package com.example.eduservice.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.commonutils.R;
import com.example.eduservice.entity.EduArticle;
import com.example.eduservice.entity.EduCategories;
import com.example.eduservice.entity.EduCourse;
import com.example.eduservice.entity.EduTeacher;
import com.example.eduservice.entity.vo.CategoriesInfo;
import com.example.eduservice.service.EduArticleService;
import com.example.eduservice.service.EduCategoriesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 文章分类 前端控制器
 * </p>
 *
 * @author helloWorld
 * @since 2023-07-05
 */
@Api(description = "文章类别管理")
@RestController
@RequestMapping("/eduservice/categories")
public class EduCategoriesController {
    @Autowired
    private EduCategoriesService categoriesService;
    @Autowired
    private EduArticleService articleService;

    // 查询分类所有数据
    @ApiOperation(value = "所有分类列表")
    @GetMapping("findAll")
    public R findAllCategories() {
        // 调用service的方法实现查询所有的操作
        List<EduCategories> list = categoriesService.list(null);
        return R.ok().data("list", list);
    }

    // 查询所有分类数据
    @ApiOperation(value = "所有分类列表")
    @GetMapping("getCategoriesInfoList")
    public R getCategoriesInfoList() {
        // 调用service的方法实现查询所有的操作
        List<CategoriesInfo> list = categoriesService.getCategoriesInfoList();
        return R.ok().data("items", list);
    }

    // 添加分类
    @ApiOperation(value = "添加分类")
    @PostMapping("addCategories")
    public R addCategories(@RequestBody EduCategories categories) {
        categoriesService.save(categories);
        return R.ok();
    }

    // 修改分类信息
    @ApiOperation("修改分类信息")
    @PostMapping("updateCategories")
    public R updateCategories(@RequestBody EduCategories categories) {
        boolean b = categoriesService.updateById(categories);
        if (b) {
            return R.ok();
        } else
            return R.error();
    }

    // 根据Id查询分类
    @ApiOperation("根据Id查询分类")
    @GetMapping("getCategories/{id}")
    public R getCategories(@PathVariable String id) {
        EduCategories categories = categoriesService.getById(id);
        return R.ok().data("categories", categories);
    }

    // 删除分类方法 删除前先判断该分类是否还有文章，有文章就无法删除
    @ApiOperation(value = "删除分类")
    @DeleteMapping("deleteCategories/{id}")
    public R deleteCategories(@ApiParam(name = "id", value = "类别id", required = true) @PathVariable String id) {
        int flag = categoriesService.deleteCategories(id);
        if (flag > 0) {
            return R.ok();
        } else if (flag == -1){
            return R.error().message("该分类还有文章，请删除文章后再尝试");
        } else {
            return R.error();
        }
    }
}

