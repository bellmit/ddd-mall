package com.tactbug.mall.stock.inbound.message;

import com.fasterxml.jackson.core.type.TypeReference;
import com.tactbug.mall.common.message.command.CallBackMessage;
import com.tactbug.mall.common.message.command.CommandMessage;
import com.tactbug.mall.common.message.command.order.OrderCommandTypeEnum;
import com.tactbug.mall.common.message.command.order.sellGoods.SellCallBack;
import com.tactbug.mall.common.message.command.order.sellGoods.SellCommand;
import com.tactbug.mall.common.utils.JacksonUtil;
import com.tactbug.mall.stock.assist.model.GoodsSellInfo;
import com.tactbug.mall.stock.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderCommandAdapter {

    @Autowired
    private StockService stockService;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void onOurSelling(String data) {
        CommandMessage<OrderCommandTypeEnum, SellCommand> commandMessage = JacksonUtil.stringToObject(data, new TypeReference<CommandMessage<OrderCommandTypeEnum, SellCommand>>() {
        });
        SellCommand sellCommand = commandMessage.getCommandMessage();
        List<GoodsSellInfo> list = sellCommand.getGoodsEventList().stream()
                .map(g -> new GoodsSellInfo(g.getSellerId(), g.getGoodsId(), g.getQuantity()))
                .collect(Collectors.toList());
        CallBackMessage<List<SellCallBack>> callBackMessage = stockService.getStockOutBySelling(list);
        String callBack = JacksonUtil.objectToString(callBackMessage);
        kafkaTemplate.send(
                commandMessage.getCallBackTopics().getName(),
                commandMessage.getCallBackTopics().getKey(),
                callBack
        );
    }
}
