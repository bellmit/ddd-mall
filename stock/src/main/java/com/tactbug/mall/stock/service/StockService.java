package com.tactbug.mall.stock.service;


import com.tactbug.mall.common.message.command.CallBackMessage;
import com.tactbug.mall.common.message.command.order.sellGoods.SellCallBack;
import com.tactbug.mall.stock.assist.model.GoodsSellInfo;

import java.util.List;

public interface StockService {

    void createWarehouse(String name, Integer type);
    void addChild(Long parentId, Integer warehouseType);
    void updateWarehouseName(Long warehouseId, String newName);
    void moveWarehouse(Long sourceId, Long targetId);
    void makeWarehouseFull(Long warehouseId);
    void deleteWarehouse(Long warehouseId);
    void makeWarehouseOff(Long warehouseId);
    void makeWarehouseOn(Long warehouseId);
    void putStockInByManager(Long goodsId, Long warehouseId, Integer batch, Integer quantity);
    void createStockBySeller(Long sellerId, Long goodsId, Integer quantity);
    void addAreaBySellerOpeningAShop(Long sellerId);
    void banStockBySeller(Long goodsId);
    void getStockOutByManager(Long goodsId, Long warehouseId, Integer batch, Integer quantity);
    CallBackMessage<List<SellCallBack>> getStockOutBySelling(List<GoodsSellInfo> sellInfoList);
    void updateStockQuantityBySeller(Long sellerId, Long goodsId, Integer quantity);
    void sellerCloseStore(Long sellerId);

}
