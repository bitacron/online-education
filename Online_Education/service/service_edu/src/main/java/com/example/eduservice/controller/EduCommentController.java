package com.example.eduservice.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.commonutils.JwtUtils;
import com.example.commonutils.R;
import com.example.commonutils.vo.UcenterMemberVo;
import com.example.eduservice.client.UcenterClient;
import com.example.eduservice.entity.EduComment;
import com.example.eduservice.service.EduCommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 评论 前端控制器
 * </p>
 *
 * @author helloWorld
 * @since 2023-06-25
 */
@Api(description = "评论前台管理")
@RestController
@RequestMapping("/eduservice/comment")
//@CrossOrigin
public class EduCommentController {
    @Autowired
    private EduCommentService commentService;
    @Autowired
    private UcenterClient ucenterClient;

    // 根据课程id分页查询评论列表
    @ApiOperation(value = "评论分页列表")
    @GetMapping("{page}/{limit}")
    public R getCommentFrontList(@PathVariable Long page, @PathVariable Long limit, String courseId) {
        Page<EduComment> pageParam = new Page<>(page, limit);
        Map<String, Object> map = commentService.getCommentFrontList(pageParam, courseId);
        // 返回分页所有数据
        return R.ok().data(map);
    }

    // 添加评论
    @ApiOperation(value = "添加评论")
    @PostMapping("auth/save")
    public R save(@RequestBody EduComment comment, HttpServletRequest request) {
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        // 判断用户是否登录
        if(StringUtils.isEmpty(memberId)) {
            return R.error().code(28004).message("请登录");
        }
        comment.setMemberId(memberId);
        // 通过远程调用ucenter根据用户id获取用户信息
        UcenterMemberVo memberVo = ucenterClient.getMemberInfoById(memberId);
        // 保存评论发送者的昵称头像
        comment.setNickname(memberVo.getNickname());
        comment.setAvatar(memberVo.getAvatar());
        // 保存评论
        commentService.save(comment);
        return R.ok();
    }


}

