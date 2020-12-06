package com.zhi.service.impl;

import com.zhi.entity.Order;
import com.zhi.mapper.OrderMapper;
import com.zhi.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description
 * @Author WenZhiLuo
 * @Date 2020-12-06 16:08
 */
@Service("orderService")
public class OrderServiceImpl implements IOrderService {

    @Autowired
    private OrderMapper mapper;

    @Override
    public int[] batchInsert(final List<Order> orders) {
        return mapper.batchInsert(orders);
    }

    @Override
    public int insert(final Order order) {
        return mapper.insert(order);
    }

    @Override
    public Order selectById(int id) {
        return mapper.selectById(id);
    }
}
