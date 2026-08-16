package com.example.order.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.order.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.order.entity.vo.OrderQuery;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author helloWorld
 * @since 2023-06-26
 */
public interface OrderService extends IService<Order> {

    // 1.生成订单的方法
    String createOrder(String courseId, String memberId);

    // 条件查询
    void pageQuery(Page<Order> pageOrder, OrderQuery orderQuery);
}
