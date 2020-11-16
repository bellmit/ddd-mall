package com.tactbug.mall.common.exceptions;

import com.tactbug.mall.common.base.TactException;
import com.tactbug.mall.common.enums.CommonResultEnum;

public class TactJsonException extends TactException {

    public TactJsonException(String info){
        super(
                CommonResultEnum.JSON_ERROR.code(),
                CommonResultEnum.JSON_ERROR.message() + ": " + info
        );
    }

    @Override
    public String toString(){
        return "TactJsonException: {" +
                "code: " + this.code() +
                ", message: " + this.message() + "}";
    }
}
