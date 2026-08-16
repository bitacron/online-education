package com.example.eduservice.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.commonutils.R;
import com.example.eduservice.entity.EduTeacher;
import com.example.eduservice.entity.vo.TeacherQuery;
import com.example.eduservice.service.EduTeacherService;
import com.example.servicebase.exceptionhandler.CustomException;
import com.example.servicebase.exceptionhandler.GlobalExceptionHandler;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author helloWorld
 * @since 2023-05-31
 */
@Api(description = "讲师管理")
@RestController
@RequestMapping("/eduservice/teacher")
//@CrossOrigin // 解决跨域问题
public class EduTeacherController {
    // 访问地址  http://localhost:8001/eduservice/teacher/findAll
    // 把service注入
    @Autowired
    private EduTeacherService teacherService;

    //1. 查询讲师所有数据
    // restful风格
    @ApiOperation(value = "所有讲师列表")
    @GetMapping("findAll")
    public R findAllTeacher() {
        // 调用service的方法实现查询所有的操作
        List<EduTeacher> list = teacherService.list(null);
        return R.ok().data("items", list);
    }

    // 2.逻辑删除讲师方法
    @ApiOperation(value = "逻辑删除讲师")
    @DeleteMapping("{id}")
    public R removeTeacher(@ApiParam(name = "id", value = "讲师id", required = true) @PathVariable String id) {
        boolean flag = teacherService.removeById(id);
        if (flag) {
            return R.ok();
        } else {
            return R.error();
        }
    }

    // 3.分页查询讲师
    @ApiOperation(value = "分页查询讲师")
    @GetMapping("pageTeacher/{current}/{limit}")
    public R pageTeacher(@PathVariable Long current, @PathVariable Long limit) {
        // 创建page
        Page<EduTeacher> pageTeacher = new Page<>(current, limit);

        // 调用方法，把所有数据封装到pageTeacher中
        teacherService.page(pageTeacher, null);
        long total = pageTeacher.getTotal(); // 获取总记录数
        List<EduTeacher> records = pageTeacher.getRecords(); // 获取分页后的list集合
        return R.ok().data("total", total).data("rows", records);
        //HashMap<String, Object> map = new HashMap<>(); //也可以用HashMap返回，但是不建议使用
        //map.put("total", total);
        //map.put("rows", records);
        //return R.ok().data(map);
    }

    // 4.添加查询带分页的方法
    @ApiOperation(value = "条件查询分页方法")
    @PostMapping("pageTeacherCondition/{current}/{limit}")
    public R pageTeacherCondition(@PathVariable Long current, @PathVariable Long limit,
                                  @RequestBody(required = false) TeacherQuery teacherQuery) {
        // 创建page
        Page<EduTeacher> pageTeacher = new Page<>(current, limit);
        // 调用方法，实现分页查询
        teacherService.pageQuery(pageTeacher, teacherQuery);
        long total = pageTeacher.getTotal(); // 获取总记录数
        List<EduTeacher> records = pageTeacher.getRecords(); // 获取分页后的list集合
        return R.ok().data("total", total).data("rows", records);
    }

    @ApiOperation("添加讲师")
    @PostMapping("addTeacher")
    public R addTeacher(@RequestBody EduTeacher eduTeacher) {
        boolean save = teacherService.save(eduTeacher);
        if (save) {
            return R.ok();
        } else
            return R.error();
    }

    @ApiOperation("根据ID查询讲师")
    @GetMapping("getTeacher/{id}")
    public R getTeacher(@PathVariable String id) {
        EduTeacher byId = teacherService.getById(id);
        return R.ok().data("teacher", byId);
    }

    @ApiOperation("修改讲师")
    @PostMapping("updateTeacher")
    public R updateTeacher(@RequestBody EduTeacher eduTeacher) {
        boolean b = teacherService.updateById(eduTeacher);
        if (b) {
            return R.ok();
        } else
            return R.error();
    }

}
