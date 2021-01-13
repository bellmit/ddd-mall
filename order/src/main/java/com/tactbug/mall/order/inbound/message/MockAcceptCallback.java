package com.tactbug.mall.order.inbound.message;

import com.tactbug.mall.common.message.command.order.OrderCommandTypeEnum;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
public class MockAcceptCallback {

    @KafkaListener(topics = "${topic.order.callback}", groupId = "order")
    public void accept(
            String data,
            @Header(name = KafkaHeaders.RECEIVED_MESSAGE_KEY) String key
    ){
        OrderCommandTypeEnum commandType = OrderCommandTypeEnum.getCommandType(key);
        if (commandType.equals(OrderCommandTypeEnum.SELLING)){
            System.out.println(data);
        }
    }
}
