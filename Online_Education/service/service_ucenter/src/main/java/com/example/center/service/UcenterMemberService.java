package com.example.center.service;

import com.example.center.entity.UcenterMember;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.center.entity.vo.LoginVo;
import com.example.center.entity.vo.RegisterVo;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author helloWorld
 * @since 2023-06-21
 */
public interface UcenterMemberService extends IService<UcenterMember> {
    // 登录的方法
    String login(LoginVo loginVo);
    // 注册的方法
    void register(RegisterVo registerVo);
    // 根据token获取用户信息
    UcenterMember getLoginInfo(String memberId);
    // 根据openid查询用户是否存在
    UcenterMember getOpenIdMember(String openid);
    // 查询某一天的注册人数
    Integer countRegisterDay(String day);
}
