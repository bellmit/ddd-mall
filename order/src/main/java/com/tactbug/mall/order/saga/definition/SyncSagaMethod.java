package com.tactbug.mall.order.saga.definition;

@FunctionalInterface
public interface SyncSagaMethod extends SagaMethod {
    boolean doCommand();
}
