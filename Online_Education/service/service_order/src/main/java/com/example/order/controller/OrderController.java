package com.example.order.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.commonutils.JwtUtils;
import com.example.commonutils.R;
import com.example.order.entity.Order;
import com.example.order.entity.vo.OrderQuery;
import com.example.order.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 订单 前端控制器
 * </p>
 *
 * @author helloWorld
 * @since 2023-06-26
 */
@RestController
@RequestMapping("/eduorder/order")
//@CrossOrigin
@Api(description = "订单管理")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // 1.生成订单的方法
    @ApiOperation(value = "生成订单")
    @PostMapping("createOrder/{courseId}")
    public R saveOrder(@PathVariable String courseId, HttpServletRequest request) {
        // 获取下单者的用户id
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        // 创建订单，返回订单号
        String orderNo = orderService.createOrder(courseId, memberId);
        return R.ok().data("orderId", orderNo);
    }

    // 2.根据订单Id查询订单信息
    @ApiOperation(value = "查询订单信息")
    @GetMapping("getOrderInfo/{orderId}")
    public R getOrderInfo(@PathVariable String orderId) {
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("order_no",orderId);
        Order order = orderService.getOne(wrapper);
        return R.ok().data("item", order);
    }

    // 根据课程id和用户id查询订单表中的订单状态，判断此课程是够被该用户购买过
    @GetMapping("isBuyCourse/{courseId}/{memberId}")
    public boolean isBuyCourse(@PathVariable String courseId, @PathVariable String memberId) {
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("member_id", memberId);
        wrapper.eq("course_id", courseId);
        wrapper.eq("status", 1);// 订单支付状态，1表示支付成功
        int count = orderService.count(wrapper);
        if(count > 0) { // 能查出记录，说明该用户已经成功支付购买过该课程
            return true;
        } else {
            return false;
        }
    }
    // 1.分页查询订单
    @ApiOperation(value = "分页查询订单")
    @PostMapping("pageOrder/{current}/{limit}")
    public R pageOrder(@PathVariable Long current, @PathVariable Long limit,
                       @RequestBody(required = false) OrderQuery orderQuery) {
        // 创建page
        Page<Order> pageOrder = new Page<>(current, limit);
        // 调用方法，实现分页查询
        orderService.pageQuery(pageOrder, orderQuery);
        long total = pageOrder.getTotal(); // 获取总记录数
        List<Order> records = pageOrder.getRecords(); // 获取分页后的list集合
        return R.ok().data("total", total).data("rows", records);
    }
    // 2.逻辑删除订单方法
    @ApiOperation(value = "逻辑删除订单")
    @DeleteMapping("{id}")
    public R removeTeacher(@ApiParam(name = "id", value = "订单id", required = true) @PathVariable String id) {
        boolean flag = orderService.removeById(id);
        if (flag) {
            return R.ok();
        } else {
            return R.error();
        }
    }
}

