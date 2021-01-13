package com.tactbug.mall.order.aggregate;

import com.tactbug.mall.order.aggregate.valueObject.OrderItem;
import com.tactbug.mall.order.aggregate.valueObject.OrderStatus;
import com.tactbug.mall.order.aggregate.valueObject.Price;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class Order {

    private Long id;
    private Integer version;

    private List<OrderItem> orderItemList;
    private Price price;
    private OrderStatus orderStatus;

    private Date createTime;
    private Date updateTime;
}
