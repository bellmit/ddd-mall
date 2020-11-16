package com.tactbug.mall.goods.inbound.web;

import com.tactbug.mall.common.message.event.EventMessage;
import com.tactbug.mall.common.message.event.goods.GoodsEvent;
import com.tactbug.mall.common.message.event.goods.GoodsEventTypeEnum;
import com.tactbug.mall.common.utils.JacksonUtil;
import com.tactbug.mall.goods.assist.utils.GoodsCodeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Api(tags = "模拟商品服务事件广播")
@Controller
@RequestMapping("/goods/mock")
public class MockGoodsEvent {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value("${topic.goods.event}")
    private String goodsEvent;

    @ApiOperation("模拟商品服务创建商品事件")
    @GetMapping("/create")
    public void sellerCreateGoods(Long sellerId, Long goodsId, Integer quantity) {

        GoodsEvent goodsEventBody = new GoodsEvent();
        goodsEventBody.setSellerId(sellerId);
        goodsEventBody.setGoodsId(goodsId);
        goodsEventBody.setQuantity(quantity);

        EventMessage<GoodsEventTypeEnum, GoodsEvent> eventMessage = new EventMessage<>();
        eventMessage.setMessageId(GoodsCodeUtil.nextId());
        eventMessage.setEventBody(goodsEventBody);
        eventMessage.setEventType(GoodsEventTypeEnum.SELLER_CREATE_GOODS);
        String event = JacksonUtil.objectToString(eventMessage);
        kafkaTemplate.send(goodsEvent, eventMessage.getEventType().toString(), event);
    }

    @ApiOperation("模拟商品服务删除商品事件")
    @GetMapping("/delete")
    public void sellerBanGoods(Long sellerId, Long goodsId) {

        GoodsEvent goodsEventBody = new GoodsEvent();
        goodsEventBody.setSellerId(sellerId);
        goodsEventBody.setGoodsId(goodsId);
        goodsEventBody.setQuantity(0);

        EventMessage<GoodsEventTypeEnum, GoodsEvent> eventMessage = new EventMessage<>();
        eventMessage.setMessageId(GoodsCodeUtil.nextId());
        eventMessage.setEventBody(goodsEventBody);
        eventMessage.setEventType(GoodsEventTypeEnum.SELLER_BAN_GOODS);
        String message = JacksonUtil.objectToString(eventMessage);
        kafkaTemplate.send(goodsEvent, eventMessage.getEventType().toString(), message);
    }

    @ApiOperation("模拟商品服务修改商品库存数量事件")
    @GetMapping("/update")
    public void sellerUpdateGoodsQuantity(Long sellerId, Long goodsId, Integer quantity) {

        GoodsEvent goodsEventBody = new GoodsEvent();
        goodsEventBody.setSellerId(sellerId);
        goodsEventBody.setGoodsId(goodsId);
        goodsEventBody.setQuantity(quantity);

        EventMessage<GoodsEventTypeEnum, GoodsEvent> eventMessage = new EventMessage<>();
        eventMessage.setMessageId(GoodsCodeUtil.nextId());
        eventMessage.setEventBody(goodsEventBody);
        eventMessage.setEventType(GoodsEventTypeEnum.SELLER_UPDATE_QUANTITY);
        String message = JacksonUtil.objectToString(eventMessage);
        kafkaTemplate.send(goodsEvent, eventMessage.getEventType().toString(), message);
    }
}
