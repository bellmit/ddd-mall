package com.tactbug.mall.order.saga;

import com.tactbug.mall.order.saga.definition.SagaDefinition;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SagaManager {

    private static final ExecutorService executorService;

    static {
        executorService = Executors.newCachedThreadPool();
    }

    public static void execute(SagaExecutor sagaExecutor){
        SagaDefinition next =
                sagaExecutor.getSagaDefinitions().next();

    }

}
