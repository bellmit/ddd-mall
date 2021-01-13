package com.tactbug.mall.order.saga;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SagaStatus {
    EXECUTE, COMPENSATED, FINISHED,
    ;
}
