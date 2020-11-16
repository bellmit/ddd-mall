package com.tactbug.mall.stock.outbound.repository.seller.jpa;

import com.tactbug.mall.stock.aggregate.Seller;
import com.tactbug.mall.stock.aggregate.Warehouse;
import com.tactbug.mall.stock.assist.exception.TactStockException;
import com.tactbug.mall.stock.outbound.repository.seller.SellerRepository;
import com.tactbug.mall.stock.outbound.repository.stock.StockRepository;
import com.tactbug.mall.stock.outbound.repository.warehouse.WarehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SellerRepositoryJpaImpl implements SellerRepository {

    @Autowired
    private SellerEntityDao sellerEntityDao;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private StockRepository stockRepository;

    @Override
    public boolean exists(Long id) {
        return sellerEntityDao.existsById(id);
    }

    @Override
    public Seller getById(Long id) {
        SellerEntity sellerEntity = sellerEntityDao.findById(id)
                .orElseThrow(() -> new TactStockException("卖家用户[" + id + "]不存在"));
        return entityToAggregate(sellerEntity);
    }

    @Override
    public void putSellerIn(Seller seller) {
        if (null == seller.getAreaId()){
            sellerEntityDao.delete(aggregateToEntity(seller));
        }else {
            sellerEntityDao.save(aggregateToEntity(seller));
        }
    }

    @Override
    public void delete(Seller seller) {
        if (warehouseRepository.exists(seller.getAreaId())){
            Warehouse area = warehouseRepository.getSimple(seller.getAreaId());
            warehouseRepository.assembleStockList(area);
            stockRepository.delete(area.getStockList());
            warehouseRepository.delete(area);
        }
        sellerEntityDao.delete(aggregateToEntity(seller));
    }

    private Seller entityToAggregate(SellerEntity sellerEntity){
        Seller seller = new Seller();
        seller.setId(sellerEntity.getId());
        seller.setAreaId(sellerEntity.getAreaId());
        seller.setCreateTime(sellerEntity.getCreateTime());
        seller.setUpdateTime(sellerEntity.getUpdateTime());
        return seller;
    }

    private SellerEntity aggregateToEntity(Seller seller){
        SellerEntity sellerEntity = new SellerEntity();
        sellerEntity.setId(seller.getId());
        sellerEntity.setAreaId(seller.getAreaId());
        sellerEntity.setCreateTime(seller.getCreateTime());
        sellerEntity.setUpdateTime(seller.getUpdateTime());
        return sellerEntity;
    }
}
