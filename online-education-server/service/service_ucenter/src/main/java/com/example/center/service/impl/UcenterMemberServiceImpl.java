package com.example.center.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.center.entity.UcenterMember;
import com.example.center.entity.vo.LoginVo;
import com.example.center.entity.vo.RegisterVo;
import com.example.center.mapper.UcenterMemberMapper;
import com.example.center.service.UcenterMemberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.commonutils.JwtUtils;
import com.example.commonutils.MD5;
import com.example.servicebase.exceptionhandler.CustomException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author helloWorld
 * @since 2023-06-21
 */
@Service
public class UcenterMemberServiceImpl extends ServiceImpl<UcenterMemberMapper, UcenterMember> implements UcenterMemberService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    // 登录的方法
    @Override
    public String login(LoginVo loginVo) {
        // 获取登录手机号和密码
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();
        // 手机号和密码非空判断
        if(StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password)) {
            throw new CustomException(20001,"登录失败");
        }
        // 手机号是否正确
        QueryWrapper<UcenterMember> queryWrapper = new QueryWrapper<UcenterMember>();
        queryWrapper.eq("mobile", mobile);
        UcenterMember mobileMember = baseMapper.selectOne(queryWrapper); // 根据手机号查用户信息
        if(mobileMember == null) { // 没有这个手机号
            throw new CustomException(20001,"您的手机号不存在！");
        }
        // 判断密码
        // 因为存储到数据库密码肯定是加密的
        // 把输入的密码进行加密，再和数据库进行比较
        // 加密方式 MD5
        if(!MD5.encrypt(password).equals(mobileMember.getPassword())) {
            throw new CustomException(20001,"您的密码输入错误！");
        }
        // 判断用户是否被禁用
        if(mobileMember.getIsDisabled()) {
            throw new CustomException(20001,"您的账户已被禁用！");
        }
        // 登录成功
        // 生成token字段，使用jwt工具类
        String jwtToken = JwtUtils.getJwtToken(mobileMember.getId(), mobileMember.getNickname());
        return jwtToken;
    }

    // 注册的方法
    @Override
    public void register(RegisterVo registerVo) {
        // 获取注册的数据
        String code = registerVo.getCode(); // 验证码
        String mobile = registerVo.getMobile(); // 手机号
        String nickname = registerVo.getNickname(); // 昵称
        String password = registerVo.getPassword(); // 密码
        // 非空判断
        if(StringUtils.isEmpty(code) || StringUtils.isEmpty(mobile) || StringUtils.isEmpty(nickname) || StringUtils.isEmpty(password)) {
            throw new CustomException(20001,"注册失败");
        }

        // 判断验证码
        // 获取redis里面的验证码
        String redisCode = redisTemplate.opsForValue().get(mobile);
        if (!code.equals(redisCode)) {
            throw new CustomException(20001, "验证码错误，注册失败");
        }

        // 判断手机号是否重复，表里面存在相同的手机号不能注册添加
        QueryWrapper<UcenterMember> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mobile", mobile);
        Integer count = baseMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new CustomException(20001, "该手机号已经注册过，注册失败");
        }

        // 数据添加数据库中
        UcenterMember member = new UcenterMember();
        member.setNickname(nickname);
        member.setMobile(mobile);
        member.setPassword(MD5.encrypt(password));
        member.setIsDisabled(false); // 用户不禁用
        member.setIsDeleted(false); // 未被逻辑删除
        member.setAvatar("https://online-education-chn.oss-cn-beijing.aliyuncs.com/avatar/default.jpg");
        baseMapper.insert(member);
    }

    // 根据token获取用户信息
    @Override
    public UcenterMember getLoginInfo(String memberId) {
        UcenterMember member = baseMapper.selectById(memberId);
        return member;
        //LoginVo loginVo = new LoginVo();
        //BeanUtils.copyProperties(member, loginVo);
        //System.out.println("====>"+loginVo);
        //return loginVo;
    }

    // 根据openid查询用户是否存在
    @Override
    public UcenterMember getOpenIdMember(String openid) {
        QueryWrapper<UcenterMember> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("openid", openid);
        UcenterMember member = baseMapper.selectOne(queryWrapper);
        return member;
    }

    // 查询某一天的注册人数
    @Override
    public Integer countRegisterDay(String day) {
        return baseMapper.countRegisterDay(day);
    }
}
