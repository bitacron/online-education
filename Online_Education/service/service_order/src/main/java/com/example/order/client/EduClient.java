package com.example.order.client;

import com.example.commonutils.vo.CourseOrderVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(name="service-edu")
public interface EduClient {
    @GetMapping("/eduservice/course/getCourseInfoOrder/{courseId}")
    public CourseOrderVo getCourseInfoOrder(@PathVariable("courseId") String courseId);
}
