package com.example.center.controller;

import com.example.center.entity.UcenterMember;
import com.example.center.service.UcenterMemberService;
import com.example.center.utils.ConstantPropertiesUtil;
import com.example.center.utils.HttpClientUtils;
import com.example.commonutils.JwtUtils;
import com.example.servicebase.exceptionhandler.CustomException;
import com.google.gson.Gson;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

@Controller//注意这里没有配置 @RestController 指向请求地址，不需要返回数据
@RequestMapping("/api/ucenter/wx")
//@CrossOrigin // 跨域
@Api(description = "微信登录")
public class WxApiController {
    @Autowired
    private UcenterMemberService memberService;

    //http://localhost:8160/api/ucenter/wx/login
    // 1.生成微信登录二维码
    @GetMapping("login")
    public String getWxQrCode(HttpSession session) {
        // 固定地址，后面接参数
        //String url = "https://open.weixin.qq.com/"+"connect/qrconnect?appid=" +ConstantPropertiesUtil.WX_OPEN_APP_ID;
        // 微信开放平台授权baseUrl，%s相当于?是占位符
        String baseUrl = "https://open.weixin.qq.com/connect/qrconnect" +
                "?appid=%s" +
                "&redirect_uri=%s" +
                "&response_type=code" +
                "&scope=snsapi_login" +
                "&state=%s" +
                "#wechat_redirect";
        // 回调地址 对redirect_url进行URLEncode编码
        String redirectUrl = ConstantPropertiesUtil.WX_OPEN_REDIRECT_URL; //获取业务服务器重定向地址
        try {
            redirectUrl = URLEncoder.encode(redirectUrl, "UTF-8"); //url编码
        } catch (UnsupportedEncodingException e) {
            throw new CustomException(20001, e.getMessage());
        }

        // 防止csrf攻击（跨站请求伪造攻击）
        //String state = UUID.randomUUID().toString().replaceAll("-", "");//一般情况下会使用一个随机数
        String state = "example"; // 为了让大家能够使用我搭建的外网的微信回调跳转服务器，这里填写你在ngrok的前置域名

        //生成qrcodeUrl
        String qrcodeUrl = String.format(
                baseUrl,
                ConstantPropertiesUtil.WX_OPEN_APP_ID,
                redirectUrl,
                state);
        // 重定向到请求地址中去
        return "redirect:" + qrcodeUrl;
    }

    // 获取扫描人信息，添加数据
    @GetMapping("callback")
    public String callback(String code, String state, HttpSession session) {
        // 1.获取code值，临时票据，类似于验证码
        System.out.println(code);
        System.out.println(state);

        // 2.拿着code请求微信固定的地址，得到两个值access_token和open_id
        String baseAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token" +
                "?appid=%s" +
                "&secret=%s" +
                "&code=%s" +
                "&grant_type=authorization_code";

        // 拼接三个参数 id 秘钥和code值
        String accessTokenUrl = String.format(baseAccessTokenUrl,
                ConstantPropertiesUtil.WX_OPEN_APP_ID,
                ConstantPropertiesUtil.WX_OPEN_APP_SECRET,
                code);
        // 请求这个拼接好的地址，得到返回两个值access_token和open_id
        String accessTokenInfo = null;
        try {
            // 使用httpclient发送请求，得到返回结果
            accessTokenInfo = HttpClientUtils.get(accessTokenUrl);
            System.out.println("accessTokenInfo->" + accessTokenInfo);
        } catch (Exception e) {
            throw new CustomException(20001, "获取access_token失败");
        }

        // 从accessTokenInfo字符串取出来两个值access_token和openid
        // 把accessTokenInfo字符串转换map集合，根据map里面key获取对应值
        // 解析json字符串，使用Gson
        Gson gson = new Gson();
        HashMap map = gson.fromJson(accessTokenInfo, HashMap.class);
        String accessToken = (String)map.get("access_token");
        String openid = (String)map.get("openid");

        // 根据openid判断用户是否否曾经使用过微信登录
        UcenterMember member = memberService.getOpenIdMember(openid);
        if(member == null){ // member为null，说明表中没有这条数据，即没注册过
            System.out.println("新用户注册");
            // 3.拿着得到access_token和openid，再去请求微信提供的固定地址，获取到扫描人信息
            // 访问微信的资源服务器，获取用户信息
            String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                    "?access_token=%s" +
                    "&openid=%s";
            String userInfoUrl = String.format(baseUserInfoUrl, accessToken, openid);
            String resultUserInfo = null;
            try {
                // 发送请求
                resultUserInfo = HttpClientUtils.get(userInfoUrl);
                System.out.println("resultUserInfo->" + resultUserInfo);
            } catch (Exception e) {
                throw new CustomException(20001, "获取用户信息失败");
            }
            // 解析json，获取返回resultUserInfo字符串信息
            HashMap<String, Object> mapUserInfo = gson.fromJson(resultUserInfo, HashMap.class);
            String nickname = (String)mapUserInfo.get("nickname"); // 昵称
            String headimgurl = (String)mapUserInfo.get("headimgurl"); // 头像
            // 向数据库中插入一条记录
            member = new UcenterMember();
            member.setNickname(nickname);
            member.setOpenid(openid);
            member.setAvatar(headimgurl);
            memberService.save(member);
        }
        // 使用jwt根据member对象生成token字符串
        String jwtToken = JwtUtils.getJwtToken(member.getId(), member.getNickname());
        // 返回首页，通过路径传递token字符串
        return "redirect:http://localhost:3000?token="+jwtToken;
    }

}
