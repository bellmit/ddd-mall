package com.tactbug.mall.order.saga.definition;

public interface SyncCompensatedSagaMethod extends SyncSagaMethod {
    boolean doCompensate();
}
