package com.example.center.controller;


import com.example.center.entity.UcenterMember;
import com.example.center.entity.vo.LoginVo;
import com.example.center.entity.vo.RegisterVo;
import com.example.center.service.UcenterMemberService;
import com.example.commonutils.JwtUtils;
import com.example.commonutils.R;
import com.example.commonutils.vo.UcenterMemberVo;
import com.example.servicebase.exceptionhandler.CustomException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author helloWorld
 * @since 2023-06-21
 */
@RestController
@RequestMapping("/educenter/member")
//@CrossOrigin // 跨域
@Api(description = "用户登录与注册")
public class UcenterMemberController {

    @Autowired
    private UcenterMemberService memberService;

    @ApiOperation(value = "会员登录")
    @PostMapping("login")
    // 为了减轻网络负担，同时不暴露数据库字段，所以新建实体类LoginVo，只有手机号和密码两个属性
    public R login(@RequestBody LoginVo loginVo) {
        // member对象封装手机号和密码
        // 调用service方法是实现登录
        // 返回token值，使用jwt生成
        String token = memberService.login(loginVo);
        return R.ok().data("token", token);
    }

    // 注册的方法
    @ApiOperation(value = "会员注册")
    @PostMapping("register")
    public R registerUser(@RequestBody RegisterVo registerVo){
        memberService.register(registerVo);
        return R.ok();
    }

    // 根据token获取用户信息
    @ApiOperation(value = "根据token获取登录信息")
    @GetMapping("getMemberInfo")
    public R getMemberInfo(HttpServletRequest request){
        try {
            // 调用jwt工具类，根据request对象获取头信息，返回用户id
            String memberId = JwtUtils.getMemberIdByJwtToken(request);
            // 查询数据库根据用户id获取用户信息
            UcenterMember loginVo = memberService.getLoginInfo(memberId);
            //LoginVo loginVo = memberService.getLoginInfo(memberId);
            return R.ok().data("userInfo", loginVo);
        }catch (Exception e){
            e.printStackTrace();
            throw new CustomException(20001,"error");
        }
    }

    // 根据用户id获取用户信息(发表评论功能、生成订单功能会远程调用这个方法)
    @ApiOperation(value = "根据用户id获取用户信息")
    @PostMapping("getUserInfo/{memberId}")
    public UcenterMemberVo getInfo(@PathVariable String memberId) {
        //根据用户id获取用户信息
        UcenterMember member = memberService.getById(memberId);
        // 把member对象里面的信息复制到UcenterMemberVo对象
        UcenterMemberVo memberVo = new UcenterMemberVo();
        BeanUtils.copyProperties(member, memberVo);
        return memberVo;
    }
    // 查询某一天的注册人数
    @ApiOperation(value = "查询某一天的注册人数")
    @GetMapping(value = "countRegister/{day}")
    public R registerCount(@PathVariable String day){
        Integer count = memberService.countRegisterDay(day);
        return R.ok().data("countRegister", count);
    }

}
