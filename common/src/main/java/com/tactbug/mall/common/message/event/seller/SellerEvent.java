package com.tactbug.mall.common.message.event.seller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SellerEvent {
    private Long sellerId;
}
