package com.zhi.datasource.service;

import com.zhi.datasource.entity.Order;

import java.util.List;

/**
 * @Description 订单服务
 * @Author WenZhiLuo
 * @Date 2020-12-06 16:07
 */
public interface IOrderService {

    /**
     * 批量插入订单数据
     */
    int[] batchInsert(final List<Order> orders);

    /**
     * 插入订单数据
     */
    int insert(final Order order);

    /**
     * 查询订单
     */
    Order selectById(int id);
}
