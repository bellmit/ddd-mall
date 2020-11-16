package com.tactbug.mall.common.enums;

import com.tactbug.mall.common.base.BaseInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum CommonResultEnum implements BaseInfo {
    OK("10000", "操作成功"),
    SYSTEM_ERROR("A0001", "系统异常"),
    JSON_ERROR("A0101", "json解析异常"),
    NETWORK_ERROR("A0102", "网络异常"),
    INTERFACE_ERROR("A0103", "接口访问异常"),
    JWT_ERROR("A0104", "jwt校验异常"),
    PARAMS_ERROR("A0105", "参数异常"),
    ENUM_ERROR("A0106", "枚举操作异常"),
    REDIS_ERROR("A0107", "redis操作异常"),
    ;

    @Override
    public String code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }

    private final String code;
    private final String message;

}
