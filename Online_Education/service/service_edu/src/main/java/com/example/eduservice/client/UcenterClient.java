package com.example.eduservice.client;

import com.example.commonutils.vo.UcenterMemberVo;
import com.example.eduservice.client.Impl.UcenterFileDegradeFeignClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Component
@FeignClient(name="service-ucenter",fallback = UcenterFileDegradeFeignClient.class)
public interface UcenterClient {

    //根据用户id获取用户信息
    @PostMapping("/educenter/member/getUserInfo/{memberId}")
    public UcenterMemberVo getMemberInfoById(@PathVariable("memberId") String memberId);
}