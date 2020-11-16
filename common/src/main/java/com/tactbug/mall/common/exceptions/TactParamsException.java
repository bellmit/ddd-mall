package com.tactbug.mall.common.exceptions;

import com.tactbug.mall.common.base.TactException;
import com.tactbug.mall.common.enums.CommonResultEnum;

public class TactParamsException extends TactException {

    public TactParamsException(String info){
        super(
                CommonResultEnum.PARAMS_ERROR.code(),
                CommonResultEnum.PARAMS_ERROR.message() + ": " + info
        );
    }

    @Override
    public String toString(){
        return "TactParamsException: {" +
                "code: " + this.code() +
                ", message: " + this.message() + "}";
    }

}
