package com.example.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.commonutils.vo.CourseOrderVo;
import com.example.commonutils.vo.UcenterMemberVo;
import com.example.order.client.EduClient;
import com.example.order.client.UcenterClient;
import com.example.order.entity.Order;
import com.example.order.entity.vo.OrderQuery;
import com.example.order.mapper.OrderMapper;
import com.example.order.service.OrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.order.utils.OrderNoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author helloWorld
 * @since 2023-06-26
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {
    @Autowired
    private EduClient eduClient;

    @Autowired
    private UcenterClient ucenterClient;

    // 1.生成订单的方法
    @Override
    public String createOrder(String courseId, String memberId) {
        // 通过远程调用根据用户id获取用户信息
        UcenterMemberVo memberInfoById = ucenterClient.getMemberInfoById(memberId);
        // 通过远程调用根据课程id获取课程信息
        CourseOrderVo courseInfoOrder = eduClient.getCourseInfoOrder(courseId);
        // 创建订单，创建Order对象，向Order对象里设置需要数据
        Order order = new Order();
        order.setOrderNo(OrderNoUtil.getOrderNo()); // 订单号，用OrderNoUtil工具类根据一定规则自动生成订单号
        order.setCourseId(courseId); // 课程Id
        order.setCourseTitle(courseInfoOrder.getTitle()); // 课程标题
        order.setCourseCover(courseInfoOrder.getCover());// 课程封面
        order.setTeacherName(courseInfoOrder.getTeacherName());
        order.setTotalFee(courseInfoOrder.getPrice());
        order.setMemberId(memberId);
        order.setMobile(memberInfoById.getMobile());
        order.setNickname(memberInfoById.getNickname());
        order.setStatus(0); // 订单状态
        order.setPayType(1); // 支付类型，微信
        baseMapper.insert(order);
        return order.getOrderNo(); // 返回订单号
    }

    @Override
    public void pageQuery(Page<Order> pageOrder, OrderQuery orderQuery) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("gmt_create");
        if (orderQuery == null){
            baseMapper.selectPage(pageOrder, queryWrapper);
            return;
        }
        String orderNo = orderQuery.getOrderNo();
        String courseId = orderQuery.getCourseId();
        String teacherName = orderQuery.getTeacherName();
        String nickname = orderQuery.getNickname();
        Integer status = orderQuery.getStatus();
        Integer payType = orderQuery.getPayType();
        String begin = orderQuery.getBegin();
        String end = orderQuery.getEnd();
        if (!StringUtils.isEmpty(orderNo)) {
            queryWrapper.eq("order_no", orderNo);
        }
        if (!StringUtils.isEmpty(courseId) ) {
            queryWrapper.eq("course_id", courseId);
        }
        if (!StringUtils.isEmpty(teacherName)) {
            queryWrapper.like("teacher_name", teacherName);
        }
        if (!StringUtils.isEmpty(nickname)) {
            queryWrapper.like("nickname", nickname);
        }
        if (!StringUtils.isEmpty(status)) {
            queryWrapper.eq("status", status);
        }
        if (!StringUtils.isEmpty(payType)) {
            queryWrapper.eq("pay_type", payType);
        }
        if (!StringUtils.isEmpty(begin)) {
            queryWrapper.ge("gmt_create", begin); // ge大于
        }
        if (!StringUtils.isEmpty(end)) {
            queryWrapper.le("gmt_create", end); // le小于
        }
        baseMapper.selectPage(pageOrder, queryWrapper);
    }

}
