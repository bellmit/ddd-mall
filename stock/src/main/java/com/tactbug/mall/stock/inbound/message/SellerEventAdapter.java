package com.tactbug.mall.stock.inbound.message;

import com.fasterxml.jackson.core.type.TypeReference;
import com.tactbug.mall.common.message.event.EventMessage;
import com.tactbug.mall.common.message.event.seller.SellerEvent;
import com.tactbug.mall.common.message.event.seller.SellerEventTypeEnum;
import com.tactbug.mall.common.utils.JacksonUtil;
import com.tactbug.mall.stock.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SellerEventAdapter {

    @Autowired
    private StockService stockService;

    public void onShopOpened(String data) {
        EventMessage<SellerEventTypeEnum, SellerEvent> eventMessage = JacksonUtil.stringToObject(data, new TypeReference<EventMessage<SellerEventTypeEnum, SellerEvent>>() {
        });
        stockService.addAreaBySellerOpeningAShop(eventMessage.getEventBody().getSellerId());
    }

    public void onStoreClosed(String data) {
        EventMessage<SellerEventTypeEnum, SellerEvent> eventMessage = JacksonUtil.stringToObject(data, new TypeReference<EventMessage<SellerEventTypeEnum, SellerEvent>>() {
        });
        stockService.sellerCloseStore(eventMessage.getEventBody().getSellerId());
    }
}
