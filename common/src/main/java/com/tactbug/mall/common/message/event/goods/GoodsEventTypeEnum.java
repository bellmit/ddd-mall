package com.tactbug.mall.common.message.event.goods;

import com.tactbug.mall.common.exceptions.TactEnumException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.EnumSet;

@AllArgsConstructor
@Getter
public enum GoodsEventTypeEnum {
    SELLER_BAN_GOODS, SELLER_CREATE_GOODS,
    SELLER_UPDATE_QUANTITY,
    ;

    public static GoodsEventTypeEnum getEventType(String type){
        EnumSet<GoodsEventTypeEnum> set = EnumSet.allOf(GoodsEventTypeEnum.class);
        for (GoodsEventTypeEnum g :
                set) {
            if (g.toString().equals(type)){
                return g;
            }
        }
        throw new TactEnumException("商品服务事件类型[" + type + "]不存在或不在监听中");
    }
}
