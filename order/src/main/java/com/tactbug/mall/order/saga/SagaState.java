package com.tactbug.mall.order.saga;

public interface SagaState {
    SagaStatus getStatus();
    void setStatus(SagaStatus status);
}
