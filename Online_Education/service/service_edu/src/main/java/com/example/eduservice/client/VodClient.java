package com.example.eduservice.client;

import com.example.commonutils.R;
import com.example.eduservice.client.Impl.VodFileDegradeFeignClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "service-vod", fallback = VodFileDegradeFeignClient.class)
@Component
public interface VodClient {

    // 定义调用的方法路径
    // 根据视频id删除阿里云视频
    // @PathVariable注解一定要知道参数名称，否则出错
    @DeleteMapping("/eduvod/video/removeAlyVideo/{videoId}")
    public R removeAlyVideo(@PathVariable("videoId") String videoId);

    // 删除多个阿里云视频的方法
    @DeleteMapping("/eduvod/video/deleteBatch")
    public R deleteBatch(@RequestParam("videoIdList") List<String> videoIdList);


}
