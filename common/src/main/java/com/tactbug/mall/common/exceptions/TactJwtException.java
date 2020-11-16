package com.tactbug.mall.common.exceptions;

import com.tactbug.mall.common.base.TactException;
import com.tactbug.mall.common.enums.CommonResultEnum;

public class TactJwtException extends TactException {

    public TactJwtException(String info){
        super(
                CommonResultEnum.JWT_ERROR.code(),
                CommonResultEnum.JWT_ERROR.message() + ": " + info
        );
    }

    @Override
    public String toString(){
        return "TactJwtException: {" +
                "code: " + this.code() +
                ", message: " + this.message() + "}";
    }

}
