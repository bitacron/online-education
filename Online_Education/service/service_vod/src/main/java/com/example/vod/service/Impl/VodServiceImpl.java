package com.example.vod.service.Impl;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.aliyuncs.vod.model.v20170321.DeleteVideoResponse;
import com.example.servicebase.exceptionhandler.CustomException;
import com.example.vod.service.VodService;
import com.example.vod.utils.ConstantVodUtils;
import com.example.vod.utils.InitVodClient;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class VodServiceImpl implements VodService {

    // 上传视频到阿里云
    @Override
    public String uploadAly(MultipartFile file) {
        try {
            String originalFileName = file.getOriginalFilename();
            String title = originalFileName.substring(0, originalFileName.lastIndexOf("."));
            InputStream inputStream = file.getInputStream();
            /**
             * accessKeyId, accessKeySecret
             * title 上传之后显示名称
             * originalFileName  上传文件原始名称
             * inputStream 上传文件输入流
             */
            UploadStreamRequest request = new UploadStreamRequest(ConstantVodUtils.ACCESS_KEY_ID, ConstantVodUtils.ACCESS_KEY_SECRET, title, originalFileName, inputStream);
            UploadVideoImpl uploader = new UploadVideoImpl();
            UploadStreamResponse response = uploader.uploadStream(request);
            //如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。
            // 其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因
            String videoId = response.getVideoId();
            if (!response.isSuccess()) {
                String errorMessage = "阿里云上传错误：" + "code：" + response.getCode() + ", message：" + response.getMessage();
                //log.warn(errorMessage);
                if(StringUtils.isEmpty(videoId)){
                    throw new CustomException(20001, errorMessage);
                }
            }
            return videoId;
        } catch (IOException e) {
            throw new CustomException(20001, "vod 服务上传失败");
        }
    }

    // 根据视频id删除阿里云视频
    @Override
    public void removeVideo(String videoId) {
        try {
            //创建初始化对象
            DefaultAcsClient client = InitVodClient.initVodClient(ConstantVodUtils.ACCESS_KEY_ID, ConstantVodUtils.ACCESS_KEY_SECRET);
            //创建获取凭证的request和response对象
            DeleteVideoRequest request = new DeleteVideoRequest();
            //向request对象中设置视频id
            request.setVideoIds(videoId);
            //调用方法获得凭证
            DeleteVideoResponse response = client.getAcsResponse(request);
            System.out.print("RequestId = " + response.getRequestId() + "\n");
        } catch (ClientException e) {
            e.printStackTrace();
            throw new CustomException(20001, "视频删除失败");
        }
    }

    // 删除多个阿里云视频的方法
    @Override
    public void removeAlyVideo(List videoIdList) {
        try {
            //创建初始化对象
            DefaultAcsClient client = InitVodClient.initVodClient(ConstantVodUtils.ACCESS_KEY_ID, ConstantVodUtils.ACCESS_KEY_SECRET);
            //创建获取凭证的request和response对象
            DeleteVideoRequest request = new DeleteVideoRequest();
            // videoList值转换成1,2,3
            String videoIdString = org.apache.commons.lang.StringUtils.join(videoIdList.toArray(), ",");
            //向request对象中设置视频id
            request.setVideoIds(videoIdString);
            //调用方法获得凭证
            DeleteVideoResponse response = client.getAcsResponse(request);
            System.out.print("RequestId = " + response.getRequestId() + "\n");
        } catch (ClientException e) {
            e.printStackTrace();
            throw new CustomException(20001, "视频删除失败");
        }
    }

}
