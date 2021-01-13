package com.tactbug.mall.stock.service.impl;

import com.tactbug.mall.common.message.command.CallBackMessage;
import com.tactbug.mall.common.message.command.order.sellGoods.SellCallBack;
import com.tactbug.mall.common.message.event.EventMessage;
import com.tactbug.mall.common.message.event.stock.WarehouseEvent;
import com.tactbug.mall.common.message.event.stock.WarehouseEventTypeEnum;
import com.tactbug.mall.common.utils.CodeUtil;
import com.tactbug.mall.common.utils.RedisUtil;
import com.tactbug.mall.stock.aggregate.Goods;
import com.tactbug.mall.stock.aggregate.Seller;
import com.tactbug.mall.stock.aggregate.Warehouse;
import com.tactbug.mall.stock.aggregate.factory.WarehouseFactory;
import com.tactbug.mall.stock.aggregate.root.StockRoot;
import com.tactbug.mall.stock.aggregate.valueObject.WarehouseStatusEnum;
import com.tactbug.mall.stock.aggregate.valueObject.WarehouseTypeEnum;
import com.tactbug.mall.stock.assist.exception.TactStockException;
import com.tactbug.mall.stock.assist.model.GoodsSellInfo;
import com.tactbug.mall.stock.outbound.publisher.WarehouseEventPublisher;
import com.tactbug.mall.stock.outbound.repository.goods.GoodsRepository;
import com.tactbug.mall.stock.outbound.repository.seller.SellerRepository;
import com.tactbug.mall.stock.outbound.repository.warehouse.WarehouseRepository;
import com.tactbug.mall.stock.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class StockServiceImpl implements StockService {

    private static final Integer SELLER_BATCH = 1;

    private static final Long SELLER_WAREHOUSE = 100L;

    private static final Long SELF_ID = 9527L;

    private static final String REDUCE_TAG = "reduce_stock";

    @Value("${spring.application.name}")
    private String applicationName;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private WarehouseEventPublisher warehouseEventPublisher;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public void createWarehouse(String name, Integer type) {
        Warehouse warehouse = WarehouseFactory.createTopWarehouse(name, type);
        EventMessage<WarehouseEventTypeEnum, WarehouseEvent> warehouseCreatedEventMessage = warehouse.warehouseCreated();
        warehouseRepository.putWarehouseIn(warehouse);
        warehouseEventPublisher.warehouseCreatedEvent(warehouseCreatedEventMessage);
    }

    @Override
    public void addChild(Long parentId, Integer warehouseType) {
        Warehouse parent = warehouseRepository.getSimple(parentId);
        warehouseRepository.assembleChildren(parent);
        Warehouse child = WarehouseFactory.createChild(parent, warehouseType);
        EventMessage<WarehouseEventTypeEnum, WarehouseEvent> childAddedEventMessage = parent.addChild(child);
        warehouseRepository.putWarehouseIn(child);
        warehouseEventPublisher.childAddEvent(childAddedEventMessage);
    }

    @Override
    public void updateWarehouseName(Long warehouseId, String newName) {
        Warehouse warehouse = warehouseRepository.getSimple(warehouseId);
        warehouseRepository.assembleChildren(warehouse);
        EventMessage<WarehouseEventTypeEnum, WarehouseEvent> eventMessage = warehouse.updateName(newName);
        warehouseRepository.putWarehouseIn(warehouse);
        warehouseEventPublisher.warehouseNameUpdatedEvent(eventMessage);
    }

    @Override
    public void moveWarehouse(Long sourceId, Long targetId) {
        Warehouse child = warehouseRepository.getSimple(sourceId);
        warehouseRepository.assembleChildren(child);
        Warehouse parent = warehouseRepository.getSimple(targetId);
        warehouseRepository.assembleChildren(parent);
        EventMessage<WarehouseEventTypeEnum, WarehouseEvent> eventMessage = parent.acceptChild(child);
        warehouseRepository.putWarehouseIn(parent);
        warehouseEventPublisher.warehouseMovedEvent(eventMessage);
    }

    @Override
    public void makeWarehouseFull(Long warehouseId) {
        Warehouse warehouse = warehouseRepository.getSimple(warehouseId);
        warehouseRepository.assembleStockList(warehouse);
        EventMessage<WarehouseEventTypeEnum, WarehouseEvent> eventMessage = warehouse.makeFull();
        warehouseRepository.putWarehouseIn(warehouse);
        warehouseEventPublisher.warehouseFullEvent(eventMessage);
    }

    @Override
    public void deleteWarehouse(Long warehouseId) {
        Warehouse warehouse = warehouseRepository.getSimple(warehouseId);
        warehouseRepository.assembleChildrenAndStockList(warehouse);

        EventMessage<WarehouseEventTypeEnum, WarehouseEvent> eventMessage = warehouse.delete();

        warehouseRepository.delete(warehouse);
        warehouseEventPublisher.warehouseDeleteEvent(eventMessage);
    }

    @Override
    public void makeWarehouseOff(Long warehouseId) {
        Warehouse warehouse = warehouseRepository.getSimple(warehouseId);
        warehouseRepository.assembleChildrenAndStockList(warehouse);
        EventMessage<WarehouseEventTypeEnum, WarehouseEvent> eventMessage = warehouse.makeOff();
        warehouseRepository.putWarehouseIn(warehouse);
        warehouseEventPublisher.warehouseOffEvent(eventMessage);
    }

    @Override
    public void makeWarehouseOn(Long warehouseId) {
        Warehouse target = warehouseRepository.getSimple(warehouseId);
        warehouseRepository.assembleChildren(target);
        List<Warehouse> list = warehouseRepository.parentList(target);
        for (Warehouse p :
                list) {
            if (p.getWarehouseStatus().equals(WarehouseStatusEnum.OFF)){
                throw new TactStockException("上级仓库[" + warehouseId + "]还处于禁用状态");
            }
        }
        EventMessage<WarehouseEventTypeEnum, WarehouseEvent> eventMessage = target.makeActive();
        warehouseRepository.putWarehouseIn(target);
        warehouseEventPublisher.warehouseActiveEvent(eventMessage);
    }

    @Override
    public void putStockInByManager(Long goodsId, Long warehouseId, Integer batchNumber, Integer quantity) {
        StockRoot stock = new StockRoot(goodsId, warehouseId, batchNumber, quantity);
        Goods goods;
        if (goodsRepository.exists(goodsId)){
            goods = goodsRepository.getById(goodsId);
        }else {
            goods = Goods.create(goodsId);
        }
        goods.addStock(stock);
        goodsRepository.putGoodsIn(goods);
    }

    @Override
    public void createStockBySeller(Long sellerId, Long goodsId, Integer quantity) {
        if (!sellerRepository.exists(sellerId)){
            addAreaBySellerOpeningAShop(sellerId);
        }
        Seller seller = sellerRepository.getById(sellerId);
        Warehouse store = warehouseRepository.getSimple(seller.getAreaId());
        Goods goods = Goods.create(goodsId);
        StockRoot stock = new StockRoot(goodsId, store.getId(), SELLER_BATCH, quantity);
        goods.getStocks().add(stock);
        goodsRepository.putGoodsIn(goods);
    }

    @Override
    public void addAreaBySellerOpeningAShop(Long sellerId) {
        Warehouse warehouse = warehouseRepository.getSimple(SELLER_WAREHOUSE);
        warehouseRepository.assembleChildren(warehouse);

        if (sellerRepository.exists(sellerId)){
            Seller seller = sellerRepository.getById(sellerId);
            Long areaId = seller.getAreaId();
            List<Warehouse> children = warehouse.getChildren();
            List<Long> areaIdList = children.stream()
                    .map(Warehouse::getId)
                    .collect(Collectors.toList());
            if (null == seller.getAreaId() || !areaIdList.contains(areaId)){
                Warehouse area = WarehouseFactory.createChild(warehouse, WarehouseTypeEnum.AREA.getType());
                warehouse.addChild(area);
                warehouseRepository.putWarehouseIn(area);
                seller.setAreaId(area.getId());
                sellerRepository.putSellerIn(seller);
            }
        }else {
            Warehouse area = WarehouseFactory.createChild(warehouse, WarehouseTypeEnum.AREA.getType());
            warehouse.addChild(area);
            warehouseRepository.putWarehouseIn(area);
            Seller seller = new Seller(sellerId, area.getId());
            sellerRepository.putSellerIn(seller);
        }

    }

    @Override
    public void banStockBySeller(Long goodsId) {
        Goods goods = goodsRepository.getById(goodsId);
        goodsRepository.delete(goods);
    }

    @Override
    public void getStockOutByManager(Long goodsId, Long warehouseId, Integer batch, Integer quantity) {
        Goods goods = goodsRepository.getById(goodsId);
        goods.reduceStockQuantity(warehouseId, batch, quantity);
        goodsRepository.putGoodsIn(goods);
    }

    @Override
    public CallBackMessage<List<SellCallBack>> getStockOutBySelling(List<GoodsSellInfo> goodsSellInfoList) {
        List<GoodsSellInfo> selfGoods = goodsSellInfoList.stream()
                .filter(g -> g.getSellerId().equals(SELF_ID))
                .collect(Collectors.toList());
        List<GoodsSellInfo> sellerGoods = goodsSellInfoList.stream()
                .filter(g -> !g.getSellerId().equals(SELF_ID))
                .collect(Collectors.toList());
        List<SellCallBack> list = new ArrayList<>();
        if (!selfGoods.isEmpty()){
            for (GoodsSellInfo g :
                    selfGoods) {
                redisUtil.tryLock(REDUCE_TAG + g.getGoodsId());
                Goods goods = goodsRepository.getById(g.getGoodsId());
                SellCallBack sellCallBack = goods.selfSelling(g.getQuantity());
                list.add(sellCallBack);
                goodsRepository.putGoodsIn(goods);
                redisUtil.unlock(REDUCE_TAG + g.getGoodsId());
            }
        }
        if (!sellerGoods.isEmpty()){
            for (GoodsSellInfo g :
                    sellerGoods) {
                if (goodsRepository.exists(g.getGoodsId())){
                    redisUtil.tryLock(REDUCE_TAG + g.getGoodsId());
                    Goods goods = goodsRepository.getById(g.getGoodsId());
                    SellCallBack sellCallBack = goods.sellerSelling(g.getQuantity());
                    list.add(sellCallBack);
                    goodsRepository.putGoodsIn(goods);
                    redisUtil.unlock(REDUCE_TAG + g.getGoodsId());
                }
            }
        }
        CallBackMessage<List<SellCallBack>> success = CallBackMessage.success(list);
        success.setMessageId(CodeUtil.nextId(applicationName));
        return success;
    }

    @Override
    public void updateStockQuantityBySeller(Long sellerId, Long goodsId, Integer quantity) {
        if (goodsRepository.exists(goodsId)){
            Goods goods = goodsRepository.getById(goodsId);
            goods.sellerSetQuantity(quantity);
            goodsRepository.putGoodsIn(goods);
        }else {
            createStockBySeller(sellerId, goodsId, quantity);
        }

    }

    @Override
    public void sellerCloseStore(Long sellerId) {
        if (sellerRepository.exists(sellerId)){
            Seller seller = sellerRepository.getById(sellerId);
            sellerRepository.delete(seller);
        }
    }

}
