package com.example.vod.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface VodService {

    // 上传视频到阿里云
    String uploadAly(MultipartFile file);

    // 根据视频id删除阿里云视频
    void removeVideo(String videoId);

    // 删除多个阿里云视频的方法
    void removeAlyVideo(List videoIdList);
}
