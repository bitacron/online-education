package com.example.eduservice.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.eduservice.entity.EduSubject;
import com.example.eduservice.entity.excel.SubjectData;
import com.example.eduservice.entity.subject.OneSubject;
import com.example.eduservice.entity.subject.TwoSubject;
import com.example.eduservice.listener.SubjectExcelListener;
import com.example.eduservice.mapper.EduSubjectMapper;
import com.example.eduservice.service.EduSubjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.servicebase.exceptionhandler.CustomException;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author helloWorld
 * @since 2023-06-08
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {

    // 添加课程分类
    @Override
    public void saveSubject(MultipartFile file, EduSubjectService eduSubjectService) {
        try {
            //1 获取文件输入流
            InputStream inputStream = file.getInputStream();
            // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
            EasyExcel.read(inputStream, SubjectData.class, new SubjectExcelListener(eduSubjectService)).sheet().doRead();
        }catch(Exception e) {
            e.printStackTrace();
            throw new CustomException(20002,"添加课程分类失败");
        }
    }

    // 课程分类列表（树形） 二级分类是重点难点
    @Override
    public List<OneSubject> getAllOneTwoSubject() {
        // 1.查询出所有的一级分类  parentId=0是一级分类
        QueryWrapper<EduSubject> wrapperOne = new QueryWrapper<>();
        wrapperOne.eq("parent_id", 0);// 查询parent_id == 0的
        wrapperOne.orderByAsc("sort","id"); // 排序
        List<EduSubject> oneSubjectList = baseMapper.selectList(wrapperOne);

        // 2.查询出所有的二级分类
        QueryWrapper<EduSubject> wrapperTwo = new QueryWrapper<>();
        wrapperOne.ne("parent_id", 0); // 查询parent_id != 0的
        wrapperOne.orderByAsc("sort","id"); // 排序
        List<EduSubject> twoSubjectList = baseMapper.selectList(wrapperTwo);

        // 创建list集合，存储最终封装工具
        List<OneSubject> finalSubjectList = new ArrayList<>();

        // 3.封装一级分类
        // 把查询出来的所有的一级分类list集合遍历，得到每个一级分类对象，获取每个一级分类对象值
        // 封装到需求的list集合里面finalSubjectList
        for (int i = 0; i < oneSubjectList.size(); i++) {
            //得到oneSubjectList每个eduSubject对象
            EduSubject oneEduSubject = oneSubjectList.get(i);
            // 把eduSubject里面值获取出来，放到OneSubject对象里面
            // 多个OneSubject放到finalSubjectList里面
            OneSubject oneSubject = new OneSubject();
            //oneSubject.setId(eduSubject.getId());
            //oneSubject.setTitle(eduSubject.getTitle());
            BeanUtils.copyProperties(oneEduSubject, oneSubject); // 等于上面注释掉的set
            finalSubjectList.add(oneSubject);

            // 4.封装二级分类
            // 在一级分类循换遍历查询所有的二级分类
            // 创建list集合封装每个一级分类的二报分类
            List<TwoSubject> twoFinalSubjectList = new ArrayList<>();
            // 遍历二级分类list集合
            for (int j = 0; j < twoSubjectList.size(); j++) {
                //获取每个二级分类
                EduSubject twoEduSubject = twoSubjectList.get(j);
                // 判断二级分类的parentId是否和一级分类的Id一样
                if (twoEduSubject.getParentId().equals(oneEduSubject.getId())) {
                    // 把tSubject值复制到TwoSubject里面，放到twoFinalSubjectList里面
                    TwoSubject twoSubject = new TwoSubject();
                    BeanUtils.copyProperties(twoEduSubject, twoSubject);
                    twoFinalSubjectList.add(twoSubject);
                }
            }
            // 把一级分类下面所有二级分类放到一级分类里面
            oneSubject.setChildren(twoFinalSubjectList);
        }
        return finalSubjectList;
    }
}
