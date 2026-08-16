package com.example.vodtest;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadVideoRequest;
import com.aliyun.vod.upload.resp.UploadVideoResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.GetPlayInfoRequest;
import com.aliyuncs.vod.model.v20170321.GetPlayInfoResponse;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import org.junit.Test;

import java.util.List;

public class TestVod {

    private String accessKeyId = "LTAI5tEEJmuaJ45oMVSNBPT6";
    private String accessKeySecret = "sPx5p04J6st18wUeMzRXtMp2MLmT8a";
    // 音视频上传-本地文件上传
    @Test
    public void testUploadVideo(){
        //1.音视频上传-本地文件上传
        //视频标题(必选)
        String title = "6 - What If I Want to Move Faster - upload by sdk"; // 上传之后文件名称
        String fileName = "D:/6 - What If I Want to Move Faster.mp4"; // 本地文件路径和名称
        // 上传视频的方法
        UploadVideoRequest request = new UploadVideoRequest(accessKeyId, accessKeySecret, title, fileName);
        /* 可指定分片上传时每个分片的大小，默认为1M字节 */
        request.setPartSize(1 * 1024 * 1024L);
        /* 可指定分片上传时的并发线程数，默认为1，(注：该配置会占用服务器CPU资源，需根据服务器情况指定）*/
        request.setTaskNum(1);
        request.setEnableCheckpoint(false);
        UploadVideoImpl uploader = new UploadVideoImpl();
        UploadVideoResponse response = uploader.uploadVideo(request);
        if (response.isSuccess()) {
            System.out.print("VideoId=" + response.getVideoId() + "\n");
        } else {
            /* 如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因 */
            System.out.print("VideoId=" + response.getVideoId() + "\n");
            System.out.print("ErrorCode=" + response.getCode() + "\n");
            System.out.print("ErrorMessage=" + response.getMessage() + "\n");
        }
    }

    // 根据视频id获取视频凭证
    @Test
    public void testGetVideoPlayAuth() throws Exception {

        // 创建初始化对象
        DefaultAcsClient client = InitObject.initVodClient(accessKeyId, accessKeySecret);
        // 创建获取视频地址request和response
        GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
        GetVideoPlayAuthResponse response = new GetVideoPlayAuthResponse();

        // 向request对象设置视频id
        request.setVideoId("db8936600a7871eeba530764b3ec0102");

        // 使用初始化对象里面的方法传递request
        response = client.getAcsResponse(request);
        // 输出视频凭证
        System.out.print("PlayAuth = " + response.getPlayAuth() + "\n");
    }

    // 根据视频id获取视频播放地址
    @Test
    public void testGetVideoPlayURL() throws Exception {
        // 创建初始化对象
        DefaultAcsClient client = InitObject.initVodClient(accessKeyId, accessKeySecret);
        // 创建获取视频地址request和response
        GetPlayInfoRequest request = new GetPlayInfoRequest();
        GetPlayInfoResponse response = new GetPlayInfoResponse();

        // 向request对象设置视频id
        request.setVideoId("db8936600a7871eeba530764b3ec0102");

        // 使用初始化对象里面的方法传递request
        response = client.getAcsResponse(request);
        List<GetPlayInfoResponse.PlayInfo> playInfoList = response.getPlayInfoList();

        //播放地址
        for (GetPlayInfoResponse.PlayInfo playInfo :playInfoList) {
            System.out.println("PlayInfo.PlayURL=" + playInfo.getPlayURL() + "\n");
        }
        // Base信息
        System.out.print("VideBase.Title = " + response.getVideoBase().getTitle() + "\n");
    }
}



















