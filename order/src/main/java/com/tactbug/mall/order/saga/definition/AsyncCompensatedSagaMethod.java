package com.tactbug.mall.order.saga.definition;

public interface AsyncCompensatedSagaMethod extends AsyncSagaMethod {
    void doCompensate();
}
