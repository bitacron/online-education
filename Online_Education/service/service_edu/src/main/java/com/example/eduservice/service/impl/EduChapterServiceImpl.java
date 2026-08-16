package com.example.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.eduservice.entity.EduChapter;
import com.example.eduservice.entity.EduVideo;
import com.example.eduservice.entity.chapter.ChapterVo;
import com.example.eduservice.entity.chapter.VideoVo;
import com.example.eduservice.mapper.EduChapterMapper;
import com.example.eduservice.service.EduChapterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.eduservice.service.EduVideoService;
import com.example.servicebase.exceptionhandler.CustomException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author helloWorld
 * @since 2023-06-10
 */
@Service
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {
    @Autowired
    private EduVideoService videoService;// 注入小节service

    // 课程大纲列表，根据课程id进行查询 二级分类是重点难点
    @Override
    public List<ChapterVo> getChapterVideoByCourseId(String courseId) {
        // 1.根据课程id查询课程里面所有章节
        QueryWrapper<EduChapter> wrapperChapter = new QueryWrapper<>();
        wrapperChapter.eq("course_id", courseId);
        List<EduChapter> eduChapterList = baseMapper.selectList(wrapperChapter);
        // 2.根据课程id查询课程里面所有小节
        QueryWrapper<EduVideo> WrapperVideo = new QueryWrapper<>();
        WrapperVideo.eq("course_id", courseId);
        List<EduVideo> eduVideoList = videoService.list(WrapperVideo);

        // 创建list，用于最终数据封装
        List<ChapterVo> finalList = new ArrayList<>();
        // 3.遍历查询章节list集合进行封装
        // 遍历查询章节list集合
        for (int i = 0; i < eduChapterList.size(); i++) {
            EduChapter eduChapter = eduChapterList.get(i);
            // eduChapter对象值复制到chapterVo中
            ChapterVo chapterVo = new ChapterVo();
            BeanUtils.copyProperties(eduChapter, chapterVo);
            // 把chapterVo放入finalList集合
            finalList.add(chapterVo);
            List<VideoVo> videoVoList = new ArrayList<>();
            // 4.遍历查询小节list集合进行封装
            for (int j = 0; j < eduVideoList.size(); j++) {
                // 得到每个小节
                EduVideo eduVideo = eduVideoList.get(j);
                // 判断：小节里面chapterId和章节里面id是否一样
                if (eduVideo.getChapterId().equals(chapterVo.getId())) {
                    // 进行封装
                    VideoVo videoVo = new VideoVo();
                    BeanUtils.copyProperties(eduVideo, videoVo);
                    // 放到小节封装集合
                    videoVoList.add(videoVo);
                }
            }
            // 把封装之后的小节list集合，放到章节对象里面
            chapterVo.setChildren(videoVoList);
        }
        return finalList;
    }

    //删除章节的方法
    @Override
    public boolean deleteChapter(String chapterId) {
        // 很久chapterId章节id查询小节表，如果查询到数据，不进行删除
        QueryWrapper<EduVideo> eduVideoWrapper = new QueryWrapper<>();
        eduVideoWrapper.eq("chapter_id", chapterId);
        int count = videoService.count(eduVideoWrapper);
        // 判断
        if (count > 0) { // 能查询出小节，不进行删除
            throw new CustomException(20001, "有小节，无法删除");
        } else { // 不能查询出小节，进行删除
            // 删除章节
            int result = baseMapper.deleteById(chapterId);
            // 成功 1>0 0>0
            return result > 0;
        }
    }

    // 根据课程id删除课程章节
    @Override
    public void removeChapterByCourseId(String courseId) {
        QueryWrapper<EduChapter> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id", courseId);
        baseMapper.delete(queryWrapper);
    }
}
