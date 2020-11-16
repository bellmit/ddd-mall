package com.tactbug.mall.common.message.command.order.sellGoods;

import com.tactbug.mall.common.message.event.goods.GoodsEvent;
import lombok.Data;

import java.util.List;

@Data
public class SellCommand {
    private List<GoodsEvent> goodsEventList;
}
