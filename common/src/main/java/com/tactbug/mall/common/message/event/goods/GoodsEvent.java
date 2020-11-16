package com.tactbug.mall.common.message.event.goods;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodsEvent {
    private Long sellerId;
    private Long goodsId;
    private Integer quantity;
}
