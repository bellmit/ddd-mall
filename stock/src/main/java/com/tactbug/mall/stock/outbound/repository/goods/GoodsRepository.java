package com.tactbug.mall.stock.outbound.repository.goods;

import com.tactbug.mall.stock.aggregate.Goods;

public interface GoodsRepository {
    boolean exists(Long id);
    Goods getById(Long id);
    void putGoodsIn(Goods goods);
    void delete(Goods goods);
}
