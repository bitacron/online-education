package com.example.eduservice.client.Impl;

import com.example.commonutils.R;
import com.example.eduservice.client.VodClient;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VodFileDegradeFeignClient implements VodClient {

    // 出错之后方法会执行
    @Override
    public R removeAlyVideo(String videoId) {
        return R.error().message("time out");
    }

    @Override
    public R deleteBatch(List<String> videoIdList) {
        return R.error().message("time out");
    }

}
