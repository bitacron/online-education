package com.example.statistic.schedule;

import com.example.statistic.service.StatisticsDailyService;
import com.example.statistic.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ScheduledTask {

    @Autowired
    private StatisticsDailyService staService;

    // 0/5 * * * * ?表示每五秒执行一次这个方法。Cron表达式
    //@Scheduled(cron = "0/5 * * * * ?")
    //public void task1() {
    //    System.out.println("*********++++++++++++*****执行了");
    //}

    /**
     *
     */
    // 每天凌晨1点，定时执行方法，把前一天的统计数据查询并进行添加
    @Scheduled(cron = "0 0 1 * * ?") // cron表达式
    public void task2() {
        // 获取上一天的日期，查询前一天的统计数据并添加
        String day = DateUtil.formatDate(DateUtil.addDays(new Date(), -1));
        staService.registerCount(day);
    }
}
