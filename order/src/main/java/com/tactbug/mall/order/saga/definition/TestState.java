package com.tactbug.mall.order.saga.definition;

import com.tactbug.mall.order.saga.SagaState;
import com.tactbug.mall.order.saga.SagaStatus;

public class TestState implements SagaState {

    @Override
    public SagaStatus getStatus() {
        return SagaStatus.EXECUTE;
    }

    @Override
    public void setStatus(SagaStatus status) {

    }
}
