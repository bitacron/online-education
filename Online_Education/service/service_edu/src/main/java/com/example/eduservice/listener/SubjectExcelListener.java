package com.example.eduservice.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.eduservice.entity.EduSubject;
import com.example.eduservice.entity.excel.SubjectData;
import com.example.eduservice.service.EduSubjectService;
import com.example.servicebase.exceptionhandler.CustomException;

public class SubjectExcelListener extends AnalysisEventListener<SubjectData> {

    // 因为SubjectExcelListener不能交给Spring进行管理，需要自己new，不能注入其他对象
    // 不能实现数据库操作
    public EduSubjectService subjectService;

    public SubjectExcelListener() {}
    public SubjectExcelListener(EduSubjectService subjectService) {
        this.subjectService = subjectService;
    }

    // 读取Excel内容，一行一行读取
    @Override
    public void invoke(SubjectData subjectData, AnalysisContext analysisContext) {
        if (subjectData == null) {
            throw new CustomException(20001,"文件数据为空");
        }
        // 一行一行读取，每次读取有两个值，第一个值一级分类，第二个值二级分类
        // 判断一级分类是否重复
        EduSubject oneSubject = this.existOneSubject(subjectService, subjectData.getOneSubjectName());
        if (oneSubject == null) { // 没有一级分类，进行添加
            oneSubject = new EduSubject();
            oneSubject.setParentId("0");
            oneSubject.setTitle(subjectData.getOneSubjectName()); // 一级分类名称
            subjectService.save(oneSubject);
        }
        // 添加二级分类
        // 判断二级分类是否重复
        String pid = oneSubject.getId(); // 获取一级分类的id值
        EduSubject twoSubject = this.existTwoSubject(subjectService, subjectData.getTwoSubjectName(), pid);
        if (twoSubject == null) { // 没有一级分类，进行添加
            twoSubject = new EduSubject();
            twoSubject.setParentId(pid);
            twoSubject.setTitle(subjectData.getTwoSubjectName()); // 一级分类名称
            subjectService.save(twoSubject);
        }
    }

    // 判断一级分类不能重复添加
    private EduSubject existOneSubject(EduSubjectService subjectService, String name) {
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("title", name);
        wrapper.eq("parent_id", "0");
        EduSubject oneSubject= subjectService.getOne(wrapper);
        return oneSubject;
    }
    // 判断二级分类不能重复添加
    private EduSubject existTwoSubject(EduSubjectService subjectService, String name, String pid) {
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("title", name);
        wrapper.eq("parent_id", pid);
        EduSubject twoSubject= subjectService.getOne(wrapper);
        return twoSubject;
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
