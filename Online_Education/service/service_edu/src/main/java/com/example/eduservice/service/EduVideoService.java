package com.example.eduservice.service;

import com.example.eduservice.entity.EduVideo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 课程视频 服务类
 * </p>
 *
 * @author helloWorld
 * @since 2023-06-10
 */
public interface EduVideoService extends IService<EduVideo> {

    // 根据课程id删除课程小节
    void removeVideoByCourseId(String courseId);

}
