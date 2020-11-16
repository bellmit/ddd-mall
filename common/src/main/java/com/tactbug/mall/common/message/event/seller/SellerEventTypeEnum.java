package com.tactbug.mall.common.message.event.seller;

import com.tactbug.mall.common.exceptions.TactEnumException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.EnumSet;

@AllArgsConstructor
@Getter
public enum SellerEventTypeEnum {
    CLOSE_STORE, OPEN_SHOP,
    ;

    public static SellerEventTypeEnum getEventType(String key){
        EnumSet<SellerEventTypeEnum> set = EnumSet.allOf(SellerEventTypeEnum.class);
        for (SellerEventTypeEnum s :
                set) {
            if (s.toString().equals(key)){
                return s;
            }
        }
        throw new TactEnumException("卖家服务事件类型[" + key + "]不存在或不在监听中");
    }
}
