package com.example.vod.controller;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.example.commonutils.R;
import com.example.servicebase.exceptionhandler.CustomException;
import com.example.vod.service.VodService;
import com.example.vod.utils.ConstantVodUtils;
import com.example.vod.utils.InitVodClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/eduvod/video")
//@CrossOrigin // 跨域
public class VodController {

    @Autowired
    private VodService vodService;

    // 上传视频到阿里云
    @PostMapping("uploadAlyVideo")
    public R uploadAlyVideo(MultipartFile file) {
        // 返回上传视频id
        String videoId = vodService.uploadAly(file);
        return R.ok().data("videoId", videoId);
    }

    // 根据视频id删除阿里云视频
    @DeleteMapping("removeAlyVideo/{videoId}")
    public R removeAlyVideo(@PathVariable String videoId) {
        vodService.removeVideo(videoId);
        return R.ok().message("视频删除成功");
    }

    // 删除多个阿里云视频的方法
    // 参数多个视频id List videoIdList
    @DeleteMapping("deleteBatch")
    public R deleteBatch(@RequestParam("videoIdList") List videoIdList) {
        vodService.removeAlyVideo(videoIdList);
        return R.ok();
    }

    // 根据视频id获取视频凭证
    @GetMapping("getPlayAuth/{videoId}")
    public R getVideoPlayAuth(@PathVariable("videoId") String videoId) {
        //获取阿里云存储相关常量
        String accessKeyId = ConstantVodUtils.ACCESS_KEY_ID;
        String accessKeySecret = ConstantVodUtils.ACCESS_KEY_SECRET;
        // 创建初始化对象
        DefaultAcsClient client = InitVodClient.initVodClient(accessKeyId, accessKeySecret);
        // 创建获取凭证request和response对象
        GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
        // 向request设置视频Id
        request.setVideoId(videoId);
        //响应
        GetVideoPlayAuthResponse response = null;
        try {
           response = client.getAcsResponse(request);
        } catch (ClientException e) {
            e.printStackTrace();
            throw new CustomException(20001, "获取凭证失败");
        }
        //得到播放凭证
        String playAuth = response.getPlayAuth();
        //返回结果
        return R.ok().message("获取凭证成功").data("playAuth", playAuth);
    }

}
