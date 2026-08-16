package com.example.msm.controller;

import com.example.commonutils.R;
import com.example.msm.service.MsmService;
import com.example.msm.utils.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/edumsm/msm")
//@CrossOrigin //跨域
public class MsmController {
    @Autowired
    private MsmService msmService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    // 发送短信的方法
    @GetMapping(value = "/send/{phone}")
    public R code(@PathVariable String phone) {
        // 1.从redis获取验证码，如果获取到直接返回
        String code = redisTemplate.opsForValue().get(phone);
        if(!StringUtils.isEmpty(code)) {
            return R.ok();
        }
        // 2.如果redis获取不到，进行阿里云发送
        // 生成随机值，传递给阿里云进行发送
        code = RandomUtil.getFourBitRandom();
        Map<String,Object> param = new HashMap<>();
        param.put("code", code);
        // 调用service发送短信的方法
        boolean isSend = msmService.send(param, phone);
        if(isSend) {
            // 发送成功，把发送成功的验证码放到redis里面。设置有效时间5分钟
            redisTemplate.opsForValue().set(phone, code,5, TimeUnit.MINUTES);
            return R.ok();
        } else {
            return R.error().message("发送短信失败");
        }
    }
}
