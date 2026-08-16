package com.example.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.example.oss.service.OssService;
import com.example.oss.utils.ConstantPropertiesUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Service
public class OssServiceImpl implements OssService {
     //上传头像至阿里云OSS
    @Override
    public String uploadFileAvatar(MultipartFile file, String hostName) {
        String endPoint = ConstantPropertiesUtils.END_POINT;
        String accessKeyId = ConstantPropertiesUtils.ACCESS_KEY_ID;
        String accessKeySecret = ConstantPropertiesUtils.ACCESS_KEY_SECRET;
        String bucketName = ConstantPropertiesUtils.BUCKET_NAME;


        try{
            // 创建OSS实例。
            OSS ossClient = new OSSClientBuilder().build(endPoint, accessKeyId, accessKeySecret);

            // 获取上传文件的输入流
            InputStream inputStream = file.getInputStream();

            String originalName = file.getOriginalFilename();// 获取原始文件名称
            String fileType = originalName.substring(originalName.lastIndexOf(".")); // 获取文件类型

            // 1.使用UUID在文件名称里面添加唯一值
            String uuid = UUID.randomUUID().toString().replace("-", ""); // UUID的'-'去掉
            // 拼接   3f7b80af6f674168aba879fed727cdd9helloWorld.jpg
            String filename = uuid + fileType; // UUID+文件类型作为新文件名，避免重复

            // 2.把文件按照日期进行分类
            String datePath = new DateTime().toString("yyyy/MM/dd"); // 上传日期作为文件夹路径
            String finalPath = hostName + "/" +datePath; // 模块名作为路径
            // 拼接     /avatar/2023/06/0769c67ba112df476eb8732b81216775a9.jpg
            String fileUrl = finalPath + "/" + filename; // 模块名/日期/uuid+文件类型

            //调用oss进行上传
            //第一个 参数BucketName  第二个参数 上传到oss文件的路径和名称  第三个参数 文件输入流
            ossClient.putObject(bucketName, fileUrl, inputStream);

            // 关闭OSSClient。
            ossClient.shutdown();

            // 把上传之后的文件路径返回
            // 需要吧上传到阿里云OSS路径手动拼接出来
            // https://online-education-chn.oss-cn-beijing.aliyuncs.com/2023/06/0769c67ba112df476eb8732b81216775a9.jpg
            String uploadUrl = "http://" + bucketName + "." + endPoint + "/" + fileUrl;
            return uploadUrl;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
}
