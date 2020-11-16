package com.tactbug.mall.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DeleteTagEnum {
    DELETE(0, "已删除"),
    ACTIVE(1, "活动中")
    ;

    private final Integer tag;
    private final String message;
}
