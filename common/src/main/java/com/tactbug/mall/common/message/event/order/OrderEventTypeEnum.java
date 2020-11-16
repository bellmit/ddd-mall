package com.tactbug.mall.common.message.event.order;

import com.tactbug.mall.common.exceptions.TactEnumException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.EnumSet;

@AllArgsConstructor
@Getter
public enum OrderEventTypeEnum {
    SELLER_SELLING,
    ;

    public static OrderEventTypeEnum getEventType(String type){
        EnumSet<OrderEventTypeEnum> set = EnumSet.allOf(OrderEventTypeEnum.class);
        for (OrderEventTypeEnum g :
                set) {
            if (g.toString().equals(type)){
                return g;
            }
        }
        throw new TactEnumException("订单服务事件类型[" + type +"]不存在或不在监听中");
    }
}
