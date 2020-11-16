package com.tactbug.mall.stock.outbound.repository.seller.jpa;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Entity
@Table(name = "seller")
public class SellerEntity {

    @Id
    private Long id;

    private Long areaId;

    private Date createTime;
    private Date updateTime;
}
