package com.tactbug.mall.order.saga;

import com.tactbug.mall.order.saga.definition.SagaDefinition;
import lombok.Data;

@Data
public class SagaExecutor {

    private SagaDefinitions sagaDefinitions;

    public void execute(){
        SagaDefinition next = sagaDefinitions.next();
        SagaState sagaState = sagaDefinitions.getSagaState();
        SagaStatus status = sagaState.getStatus();
        switch (status){

        }
    }
}
