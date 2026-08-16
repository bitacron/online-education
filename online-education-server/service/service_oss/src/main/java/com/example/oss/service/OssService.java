package com.example.oss.service;

import org.springframework.web.multipart.MultipartFile;

public interface OssService {
    // 上传头像至阿里云OSS
    String uploadFileAvatar(MultipartFile file, String hostName);
}
