package com.tactbug.mall.stock.outbound.repository.seller;


import com.tactbug.mall.stock.aggregate.Seller;

public interface SellerRepository {
    boolean exists(Long id);
    Seller getById(Long id);
    void putSellerIn(Seller seller);
    void delete(Seller seller);
}
