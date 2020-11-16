package com.tactbug.mall.order.inbound.web;

import com.tactbug.mall.common.message.command.CallBackTopics;
import com.tactbug.mall.common.message.command.CommandMessage;
import com.tactbug.mall.common.message.command.order.OrderCommandTypeEnum;
import com.tactbug.mall.common.message.command.order.sellGoods.SellCommand;
import com.tactbug.mall.common.message.event.goods.GoodsEvent;
import com.tactbug.mall.common.utils.JacksonUtil;
import com.tactbug.mall.order.assist.utils.OrderCodeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "模拟订单服务事件广播以及命令式消息")
@Controller
@RequestMapping("/order/mock")
public class MockOrderEventAndCommand {

    @Autowired
    private KafkaTemplate<String, String> commandKafkaTemplate;

    private static final Long SELF_ID = 9527L;


    @Value("${topic.order.command}")
    private String orderCommand;

    @Value("${topic.order.callback}")
    private String orderCommandCallback;

    @ApiOperation(value = "模拟订单服务商品销售命令")
    @PostMapping("/selling")
    public void ourSelling(@RequestBody List<GoodsEvent> goodsEvents) {

        SellCommand sellCommand = new SellCommand();
        sellCommand.setGoodsEventList(goodsEvents);

        CallBackTopics callBackTopics =
                CallBackTopics.create(orderCommandCallback, OrderCommandTypeEnum.SELLING.toString());

        CommandMessage<OrderCommandTypeEnum, SellCommand> goodsCommand =
                new CommandMessage<>(OrderCodeUtil.nextId(), OrderCommandTypeEnum.SELLING, sellCommand, callBackTopics);

        String message = JacksonUtil.objectToString(goodsCommand);

        commandKafkaTemplate.send(orderCommand, OrderCommandTypeEnum.SELLING.toString(), message);
    }

}
