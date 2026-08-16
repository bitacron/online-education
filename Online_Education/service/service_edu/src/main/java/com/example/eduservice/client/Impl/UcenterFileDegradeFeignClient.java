package com.example.eduservice.client.Impl;

import com.example.commonutils.vo.UcenterMemberVo;
import com.example.eduservice.client.UcenterClient;
import org.springframework.stereotype.Component;

@Component
public class UcenterFileDegradeFeignClient implements UcenterClient {
    @Override
    public UcenterMemberVo getMemberInfoById(String memberId) {
        return null;
    }
}