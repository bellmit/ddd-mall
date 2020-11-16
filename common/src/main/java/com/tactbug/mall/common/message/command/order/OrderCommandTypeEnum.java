package com.tactbug.mall.common.message.command.order;

import com.tactbug.mall.common.exceptions.TactEnumException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.EnumSet;

@AllArgsConstructor
@Getter
public enum OrderCommandTypeEnum {
    SELLING,
    ;

    public static OrderCommandTypeEnum getCommandType(String type){
        EnumSet<OrderCommandTypeEnum> set = EnumSet.allOf(OrderCommandTypeEnum.class);
        for (OrderCommandTypeEnum g :
                set) {
            if (g.toString().equals(type)){
                return g;
            }
        }
        throw new TactEnumException("订单服务命令类型[" + type +"]不存在或不参与此类事务");
    }
}
