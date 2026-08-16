package com.example.statistic.controller;


import com.example.commonutils.R;
import com.example.statistic.service.StatisticsDailyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 前端控制器
 * </p>
 *
 * @author helloWorld
 * @since 2023-06-27
 */
@RestController
@RequestMapping("/edusta/sta")
//@CrossOrigin
@Api(description = "统计日常信息")
public class StatisticsDailyController {
    @Autowired
    private StatisticsDailyService staService;

    // 1.统计某一天的注册人数
    @ApiOperation(value = "统计某一天的注册人数")
    @PostMapping("registerCount/{day}")
    public R registerCount(@PathVariable String day) {
        staService.registerCount(day);
        return R.ok();
    }

    // 2.图表显示，返回两部分数据，日期json数组，数量json数组
    @ApiOperation(value = "统计结果图表展示")
    @GetMapping("showData/{type}/{begin}/{end}")
    public R showChart(@PathVariable String type, @PathVariable String begin, @PathVariable String end){
        Map<String, Object> map = staService.getShowData(type, begin, end);
        return R.ok().data(map);
    }

}

