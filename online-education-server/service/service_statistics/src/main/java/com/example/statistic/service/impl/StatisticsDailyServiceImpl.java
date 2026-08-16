package com.example.statistic.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.commonutils.R;
import com.example.statistic.client.UcenterClient;
import com.example.statistic.entity.StatisticsDaily;
import com.example.statistic.mapper.StatisticsDailyMapper;
import com.example.statistic.service.StatisticsDailyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务实现类
 * </p>
 *
 * @author helloWorld
 * @since 2023-06-27
 */
@Service
public class StatisticsDailyServiceImpl extends ServiceImpl<StatisticsDailyMapper, StatisticsDaily> implements StatisticsDailyService {
    @Autowired
    private UcenterClient ucenterClient;

    // 统计某一天的注册人数
    @Override
    public void registerCount(String day) {
        //删除已存在的统计对象
        QueryWrapper<StatisticsDaily> dayQueryWrapper = new QueryWrapper<>();
        dayQueryWrapper.eq("date_calculated", day);
        baseMapper.delete(dayQueryWrapper);

        // 远程调用，得到某一天的注册人数
        R registerR = ucenterClient.registerCount(day);
        Integer countRegister = (Integer) registerR.getData().get("countRegister");
        Integer loginNum = RandomUtils.nextInt(100, 200); //TODO 统计每日登录数未实现，暂时用随机数
        Integer videoViewNum = RandomUtils.nextInt(100, 200); //TODO 统计每日观看视频数未实现，暂时用随机数
        Integer courseNum = RandomUtils.nextInt(100, 200); //TODO 统计每日新增课程数未实现，暂时用随机数
        // 把获取数据添加数据库的统计分析表里面
        StatisticsDaily sta = new StatisticsDaily();
        sta.setRegisterNum(countRegister); // 注册人数
        sta.setDateCalculated(day); // 统计的日期
        sta.setLoginNum(loginNum);
        sta.setVideoViewNum(videoViewNum);
        sta.setCourseNum(courseNum);
        baseMapper.insert(sta);
    }

    // 图表显示，返回两部分数据，日期json数组，数量json数组
    @Override
    public Map<String, Object> getShowData(String type, String begin, String end) {
        QueryWrapper<StatisticsDaily> wrapper = new QueryWrapper<>();
        wrapper.between("date_calculated", begin, end); // 日期在begin和end之间
        wrapper.select("date_calculated", type); // 查询日期和type两列
        List<StatisticsDaily> statisticsList = baseMapper.selectList(wrapper);
        // 因为返回有两部分数据，日期和日期对应数量
        // 前端要求数组json结构，对一个后端代码是list集合
        // 创建两个List集合，一个日期List，一个数量List
        List<String> dateCalculatedList = new ArrayList<String>(); // 日期List
        List<Integer> numDataList = new ArrayList<Integer>(); // 数量List

        // 遍历查询所有的数据List集合，进行封装
        for (int i = 0; i < statisticsList.size(); i++) {
            StatisticsDaily daily = statisticsList.get(i);
            dateCalculatedList.add(daily.getDateCalculated()); // 日期List中添加数据
            switch (type) { // 数量List中添加数据
                case "register_num":
                    numDataList.add(daily.getRegisterNum());
                    break;
                case "login_num":
                    numDataList.add(daily.getLoginNum());
                    break;
                case "video_view_num":
                    numDataList.add(daily.getVideoViewNum());
                    break;
                case "course_num":
                    numDataList.add(daily.getCourseNum());
                    break;
                default:
                    break;
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("dateList", dateCalculatedList);
        map.put("dataList", numDataList);
        return map;
    }

}
