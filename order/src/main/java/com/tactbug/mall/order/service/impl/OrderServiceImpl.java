package com.tactbug.mall.order.service.impl;

import com.tactbug.mall.order.aggregate.valueObject.OrderItem;
import com.tactbug.mall.order.aggregate.valueObject.Price;
import com.tactbug.mall.order.service.OrderService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Override
    public void createOrder(List<OrderItem> orderItems, Price price) {
        //1. 创建order聚合
        //2. 创建orderCreated命令式Saga
    }
}
