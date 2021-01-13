package com.tactbug.mall.order.saga.definition;

import com.tactbug.mall.order.saga.SagaType;
import lombok.Data;

@Data
public class SagaDefinition<T extends SagaMethod> {
    T t;
    private SagaType sagaType;

    public static<T extends SagaMethod> SagaDefinition<T> define(SagaType sagaType, T t){
        SagaDefinition<T> definition = new SagaDefinition<>();
        definition.setSagaType(sagaType);
        definition.setT(t);
        return definition;
    }
}
