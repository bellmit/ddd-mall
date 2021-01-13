package com.tactbug.mall.order.saga.definition;

@FunctionalInterface
public interface AsyncSagaMethod extends SagaMethod {
    void doCommand();
}
