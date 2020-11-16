package com.tactbug.mall.common.message.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommandMessage<T, M> {

    private Long messageId;
    private T commandType;
    private M commandMessage;
    private CallBackTopics callBackTopics;

}
