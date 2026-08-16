package com.example.oss.controller;

import com.example.commonutils.R;
import com.example.oss.service.OssService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/eduoss/fileoss")
//@CrossOrigin // 解决跨域问题
public class OssController {

    @Autowired
    private OssService ossService;

    // 上传头像的方法
    @PostMapping("upload")
    public R uploadOssFile(MultipartFile file, @RequestParam(value="host") String hostName) {
        // 获取上传文件 MultipartFile
        // 返回上传到
        String url = ossService.uploadFileAvatar(file, hostName);
        return R.ok().data("url",url);
    }

}
