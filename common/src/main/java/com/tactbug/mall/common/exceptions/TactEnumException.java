package com.tactbug.mall.common.exceptions;

import com.tactbug.mall.common.base.TactException;
import com.tactbug.mall.common.enums.CommonResultEnum;

public class TactEnumException extends TactException {

    public TactEnumException(String info){
        super(
                CommonResultEnum.ENUM_ERROR.code(),
                CommonResultEnum.ENUM_ERROR.message() + ": " + info
        );
    }

    @Override
    public String toString(){
        return "TactEnumException: {" +
                "code: " + this.code() +
                ", message: " + this.message() + "}";
    }
}
