package com.tactbug.mall.stock.outbound.repository.stock;


import com.tactbug.mall.stock.aggregate.root.StockRoot;

import java.util.List;

public interface StockRepository {
    void putStockIn(List<StockRoot> stockList);
    void delete(List<StockRoot> stockList);
    List<StockRoot> getByGoods(Long goodsId);
    List<StockRoot> getByWarehouse(Long warehouse);
}
