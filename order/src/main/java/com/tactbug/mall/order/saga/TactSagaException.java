package com.tactbug.mall.order.saga;

public class TactSagaException extends RuntimeException{
    public TactSagaException(String message){
        super(message);
    }
}
