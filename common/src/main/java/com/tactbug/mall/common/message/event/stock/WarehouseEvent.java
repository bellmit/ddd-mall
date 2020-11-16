package com.tactbug.mall.common.message.event.stock;

import lombok.Data;

import java.util.List;

@Data
public class WarehouseEvent {

    private Long warehouseId;
    private Long parentId;
    private String name;
    private Integer warehouseIndex;
    private Integer warehouseType;
    private String warehouseTypeInfo;
    private Integer warehouseStatus;
    private String warehouseStatusInfo;
    private List<WarehouseEvent> children;

}
