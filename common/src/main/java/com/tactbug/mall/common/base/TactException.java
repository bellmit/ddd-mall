package com.tactbug.mall.common.base;

public class TactException extends RuntimeException implements BaseInfo {
    private final String code;

    public TactException(String code, String message){
        super(message);
        this.code = code;
    }

    @Override
    public String code() {
        return code;
    }

    @Override
    public String message() {
        return this.getMessage();
    }
}
