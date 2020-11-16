package com.tactbug.mall.stock.outbound.repository.seller.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SellerEntityDao extends JpaRepository<SellerEntity, Long> {

}
