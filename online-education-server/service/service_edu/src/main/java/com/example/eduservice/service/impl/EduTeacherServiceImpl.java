package com.example.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.eduservice.entity.EduTeacher;
import com.example.eduservice.entity.vo.TeacherQuery;
import com.example.eduservice.mapper.EduTeacherMapper;
import com.example.eduservice.service.EduTeacherService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 服务实现类
 * </p>
 *
 * @author helloWorld
 * @since 2023-05-31
 */
@Service
public class EduTeacherServiceImpl extends ServiceImpl<EduTeacherMapper, EduTeacher> implements EduTeacherService {

    // 条件查询分页方法
    @Override
    public void pageQuery(Page<EduTeacher> pageTeacher, TeacherQuery teacherQuery) {
        // 构建条件
        QueryWrapper<EduTeacher> queryWrapper = new QueryWrapper<>();
        if (teacherQuery == null){
            baseMapper.selectPage(pageTeacher, queryWrapper);
            return;
        }
        // 多条件组合查询
        // MyBatis的动态sql
        String name = teacherQuery.getName();
        Integer level = teacherQuery.getLevel();
        String begin = teacherQuery.getBegin();
        String end = teacherQuery.getEnd();

        //判断条件是否为空，如果不为空拼接条件
        if (!StringUtils.isEmpty(name)) {
            // 构建条件
            queryWrapper.like("name", name); // like模糊查询
        }
        if (!StringUtils.isEmpty(level)) {
            queryWrapper.eq("level", level); // eq等于
        }
        if (!StringUtils.isEmpty(begin)) {
            queryWrapper.ge("gmt_create", begin); // ge大于
        }
        if (!StringUtils.isEmpty(end)) {
            queryWrapper.le("gmt_create", end); // le小于
        }
        // 排序
        queryWrapper.orderByDesc("gmt_create");
        baseMapper.selectPage(pageTeacher, queryWrapper);
    }

    // 查询前4条名师
    @Cacheable(key = "'selectTeacherList'", value = "teacher")
    @Override
    public List<EduTeacher> getHotTeacher() {
        //查询前4条名师
        QueryWrapper<EduTeacher> wrapperTeacher = new QueryWrapper<>();
        wrapperTeacher.orderByAsc("sort");
        wrapperTeacher.last("limit 4");
        List<EduTeacher> hotTeacherList = baseMapper.selectList(wrapperTeacher);
        return hotTeacherList;
    }

    // 前台分页查询讲师的方法
    @Override
    public Map<String, Object> getTeacherFrontList(Page<EduTeacher> pageParam) {
        QueryWrapper<EduTeacher> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("sort");
        // 把分页数据封装到pageTeacher里面
        baseMapper.selectPage(pageParam, queryWrapper);
        // 把分页数据获取出来，放到map集合
        List<EduTeacher> records = pageParam.getRecords();
        long current = pageParam.getCurrent();
        long pages = pageParam.getPages();
        long size = pageParam.getSize();
        long total = pageParam.getTotal();
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
}
