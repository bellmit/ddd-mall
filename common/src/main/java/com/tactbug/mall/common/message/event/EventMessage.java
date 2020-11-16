package com.tactbug.mall.common.message.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventMessage<T, E> {

    private Long messageId;
    private T eventType;
    private E eventBody;

}
