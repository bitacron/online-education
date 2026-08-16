package com.example.statistic.client;

import com.example.commonutils.R;
import org.springframework.stereotype.Component;

@Component
public class UcenterFileDegradeFeignClient implements UcenterClient{
    @Override
    public R registerCount(String day) {
        return R.error().message("time out");
    }
}
