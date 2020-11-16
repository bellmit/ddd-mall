package com.tactbug.mall.common.message.command.order.sellGoods;

import lombok.Data;

import java.util.List;

@Data
public class SellCallBack {
    private Long goodsId;
    private List<StockReduceItem> stockItems;
}
