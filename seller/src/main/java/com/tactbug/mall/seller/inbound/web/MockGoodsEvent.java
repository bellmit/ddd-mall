package com.tactbug.mall.seller.inbound.web;

import com.tactbug.mall.common.message.event.EventMessage;
import com.tactbug.mall.common.message.event.seller.SellerEvent;
import com.tactbug.mall.common.message.event.seller.SellerEventTypeEnum;
import com.tactbug.mall.common.utils.JacksonUtil;
import com.tactbug.mall.seller.assist.utils.SellerCodeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Api(tags = "模拟卖家服务事件广播")
@Controller
@RequestMapping("/seller/mock")
public class MockGoodsEvent {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value("${topic.seller.event}")
    private String sellerEvent;

    @ApiOperation("模拟卖家服务创建店铺事件")
    @GetMapping("/openStore")
    public void sellerOpenShop(Long sellerId) {
        EventMessage<SellerEventTypeEnum, SellerEvent> eventMessage = new EventMessage<>();
        SellerEvent sellerEventBody = new SellerEvent();
        sellerEventBody.setSellerId(sellerId);
        eventMessage.setMessageId(SellerCodeUtil.nextId());
        eventMessage.setEventBody(sellerEventBody);
        eventMessage.setEventType(SellerEventTypeEnum.OPEN_SHOP);
        String message = JacksonUtil.objectToString(eventMessage);
        kafkaTemplate.send(sellerEvent, eventMessage.getEventType().toString(), message);
    }

    @ApiOperation("模拟卖家服务关闭店铺事件")
    @GetMapping("/closeStore")
    public void sellerDeleteShop(Long sellerId) {
        EventMessage<SellerEventTypeEnum, SellerEvent> eventMessage = new EventMessage<>();
        SellerEvent sellerEventBody = new SellerEvent();
        sellerEventBody.setSellerId(sellerId);
        eventMessage.setMessageId(SellerCodeUtil.nextId());
        eventMessage.setEventType(SellerEventTypeEnum.CLOSE_STORE);
        eventMessage.setEventBody(sellerEventBody);
        String message = JacksonUtil.objectToString(eventMessage);
        kafkaTemplate.send(sellerEvent, eventMessage.getEventType().toString(), message);
    }
}
