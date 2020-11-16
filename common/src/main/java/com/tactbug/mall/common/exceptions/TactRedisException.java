package com.tactbug.mall.common.exceptions;

import com.tactbug.mall.common.base.TactException;
import com.tactbug.mall.common.enums.CommonResultEnum;

public class TactRedisException extends TactException {

    public TactRedisException(String info) {
        super(
                CommonResultEnum.REDIS_ERROR.code(),
                CommonResultEnum.REDIS_ERROR.message() + ": " + info
        );
    }

    @Override
    public String toString(){
        return "TactRedisLockException: {" +
                "code: " + this.code() +
                ", message: " + this.message() + "}";
    }
}
