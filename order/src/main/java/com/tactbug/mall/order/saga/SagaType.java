package com.tactbug.mall.order.saga;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SagaType {

    COMPENSATED(1, "可补偿事务"),
    DECISIVE(2, "关键事务"),
    MUST_BE_SUCCESS(3, "必定成功事务"),
    ;

    private final Integer type;
    private final String info;
}
