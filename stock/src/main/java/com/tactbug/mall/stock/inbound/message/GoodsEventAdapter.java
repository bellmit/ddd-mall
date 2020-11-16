package com.tactbug.mall.stock.inbound.message;

import com.fasterxml.jackson.core.type.TypeReference;
import com.tactbug.mall.common.message.event.EventMessage;
import com.tactbug.mall.common.message.event.goods.GoodsEvent;
import com.tactbug.mall.common.message.event.goods.GoodsEventTypeEnum;
import com.tactbug.mall.common.utils.JacksonUtil;
import com.tactbug.mall.stock.service.StockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GoodsEventAdapter {

    @Autowired
    private StockService stockService;

    public void onGoodsCreated(String data) {
        EventMessage<GoodsEventTypeEnum, GoodsEvent> eventMessage = JacksonUtil.stringToObject(data, new TypeReference<EventMessage<GoodsEventTypeEnum, GoodsEvent>>() {
        });
        GoodsEvent eventBody = eventMessage.getEventBody();
        stockService.createStockBySeller(eventBody.getSellerId(), eventBody.getGoodsId(), eventBody.getQuantity());
    }

    public void onGoodsBaned(String data) {
        EventMessage<GoodsEventTypeEnum, GoodsEvent> eventMessage = JacksonUtil.stringToObject(data, new TypeReference<EventMessage<GoodsEventTypeEnum, GoodsEvent>>() {
        });
        GoodsEvent eventBody = eventMessage.getEventBody();
        stockService.banStockBySeller(eventBody.getGoodsId());
    }

    public void onQuantityUpdated(String data) {
        EventMessage<GoodsEventTypeEnum, GoodsEvent> eventMessage = JacksonUtil.stringToObject(data, new TypeReference<EventMessage<GoodsEventTypeEnum, GoodsEvent>>() {
        });
        GoodsEvent eventBody = eventMessage.getEventBody();
        stockService.updateStockQuantityBySeller(eventBody.getSellerId(), eventBody.getGoodsId(), eventBody.getQuantity());
    }

}
