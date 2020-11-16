package com.tactbug.mall.common.message.event.order;

import com.tactbug.mall.common.message.event.goods.GoodsEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderEvent {
    private Long orderId;
    private List<GoodsEvent> goodsEventList;
}
