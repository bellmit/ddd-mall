package com.tactbug.mall.stock.aggregate.valueObject;

import com.tactbug.mall.common.exceptions.TactEnumException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.EnumSet;

@AllArgsConstructor
@Getter
public enum WarehouseTypeEnum {
    WAREHOUSE(1, "仓库"),
    AREA(2, "区域"),
    SHELVE(3, "货架"),
    POSITION(4, "货位"),
    ;

    private final Integer type;
    private final String message;

    public static WarehouseTypeEnum getType(Integer type){
        EnumSet<WarehouseTypeEnum> set = EnumSet.allOf(WarehouseTypeEnum.class);
        for (WarehouseTypeEnum w :
                set) {
            if (w.getType().equals(type)){
                return w;
            }
        }
        throw new TactEnumException("仓库类型[" + type + "]不存在");
    }
}
