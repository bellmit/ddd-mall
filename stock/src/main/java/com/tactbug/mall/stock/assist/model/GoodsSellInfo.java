package com.tactbug.mall.stock.assist.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodsSellInfo {
    private Long sellerId;
    private Long goodsId;
    private Integer quantity;
}
