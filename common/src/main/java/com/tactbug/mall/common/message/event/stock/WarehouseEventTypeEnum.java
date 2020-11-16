package com.tactbug.mall.common.message.event.stock;

import com.tactbug.mall.common.exceptions.TactEnumException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.EnumSet;

@AllArgsConstructor
@Getter
public enum WarehouseEventTypeEnum{
    WAREHOUSE_CREATED, CHILD_ADDED, WAREHOUSE_NAME_UPDATED, WAREHOUSE_MOVED, WAREHOUSE_FULL,
    WAREHOUSE_OFF, WAREHOUSE_ACTIVE, WAREHOUSE_DELETED;

    public static WarehouseEventTypeEnum getEventType(String type){
        EnumSet<WarehouseEventTypeEnum> set = EnumSet.allOf(WarehouseEventTypeEnum.class);
        for (WarehouseEventTypeEnum w :
                set) {
            if (type.equals(w.toString())){
                return w;
            }
        }
        throw new TactEnumException("仓库事件变更类型[" + type + "]不存在或不在监听中");
    }
}
