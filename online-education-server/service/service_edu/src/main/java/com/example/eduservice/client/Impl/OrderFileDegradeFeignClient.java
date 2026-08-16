package com.example.eduservice.client.Impl;

import com.example.eduservice.client.OrderClient;
import org.springframework.stereotype.Component;

@Component
public class OrderFileDegradeFeignClient implements OrderClient {
    @Override
    public boolean isBuyCourse(String courseId, String memberId) {
        return false;
    }
}
