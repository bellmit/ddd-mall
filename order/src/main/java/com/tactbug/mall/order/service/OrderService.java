package com.tactbug.mall.order.service;

import com.tactbug.mall.order.aggregate.valueObject.OrderItem;
import com.tactbug.mall.order.aggregate.valueObject.Price;

import java.util.List;

public interface OrderService {
    void createOrder(List<OrderItem> orderItems, Price price);
}
