package com.tactbug.mall.stock.aggregate;

import com.google.common.base.Objects;
import com.tactbug.mall.common.message.event.EventMessage;
import com.tactbug.mall.common.message.event.stock.WarehouseEvent;
import com.tactbug.mall.common.message.event.stock.WarehouseEventTypeEnum;
import com.tactbug.mall.stock.aggregate.function.IterateChildren;
import com.tactbug.mall.stock.aggregate.root.StockRoot;
import com.tactbug.mall.stock.aggregate.specification.WarehouseSpecification;
import com.tactbug.mall.stock.aggregate.valueObject.WarehouseStatusEnum;
import com.tactbug.mall.stock.aggregate.valueObject.WarehouseTypeEnum;
import com.tactbug.mall.stock.assist.exception.TactStockException;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class Warehouse {

    private Long id;

    //乐观锁控制
    private Integer version;

    private String name;
    private Integer warehouseIndex;
    private WarehouseTypeEnum warehouseType;
    private WarehouseStatusEnum warehouseStatus;
    private List<StockRoot> stockList = new ArrayList<>();
    private List<Warehouse> children = new ArrayList<>();
    private Long parent;

    private Date createTime;
    private Date updateTime;

    //聚合快照，用来判断聚合是否修改过
    private Warehouse snapshot;

    public EventMessage<WarehouseEventTypeEnum, WarehouseEvent> warehouseCreated(){
        return new EventMessage<>(id, WarehouseEventTypeEnum.WAREHOUSE_CREATED, generateEvent());
    }

    public EventMessage<WarehouseEventTypeEnum, WarehouseEvent> addChild(Warehouse child){
        children.add(child);
        return new EventMessage<>(child.getId(), WarehouseEventTypeEnum.CHILD_ADDED, generateEvent());
    }

    public EventMessage<WarehouseEventTypeEnum, WarehouseEvent> updateName(String name){

        this.name = name;
        iterateChildren(this, (parent, target) -> {
            target.setName(parent.getName() + "第" + target.getWarehouseIndex() + target.getWarehouseType().getMessage());
            sync(target);
        });
        sync(this);

        WarehouseSpecification.checkWarehouse(this);
        return new EventMessage<>(id, WarehouseEventTypeEnum.WAREHOUSE_NAME_UPDATED, generateEvent());
    }

    public EventMessage<WarehouseEventTypeEnum, WarehouseEvent> acceptChild(Warehouse child){

        if (children.isEmpty()){
            child.setWarehouseIndex(1);
        }else {
            List<Integer> sameTypeList = children.stream()
                    .filter(c -> c.getWarehouseType().equals(child.warehouseType))
                    .map(Warehouse::getWarehouseIndex)
                    .sorted()
                    .collect(Collectors.toList());
            if (sameTypeList.isEmpty()){
                child.setWarehouseIndex(1);
            }else {
                int index = 1;
                for (Integer i :
                        sameTypeList) {
                    if (i.equals(index)){
                        index += 1;
                    }else {
                        break;
                    }
                }
                child.setWarehouseIndex(index);
            }
        }
        child.updateName(name + "第" + child.getWarehouseIndex() + child.getWarehouseType().getMessage());
        child.setParent(this.id);
        children.add(child);

        WarehouseSpecification.checkWarehouse(this);
        return new EventMessage<>(child.getId(), WarehouseEventTypeEnum.WAREHOUSE_MOVED, generateEvent());
    }

    public EventMessage<WarehouseEventTypeEnum, WarehouseEvent> makeFull(){

        if (stockList.isEmpty()){
            throw new TactStockException("仓库[" + id + "]库存为空");
        }
        warehouseStatus = WarehouseStatusEnum.ENOUGH;
        sync(this);

        WarehouseSpecification.checkWarehouse(this);
        return new EventMessage<>(id, WarehouseEventTypeEnum.WAREHOUSE_FULL, generateEvent());
    }

    public EventMessage<WarehouseEventTypeEnum, WarehouseEvent> makeOff(){

        if (!stockList.isEmpty()){
            throw new TactStockException("仓库[" + id + "]还有库存商品未清空");
        }
        iterateChildren(this, (parent, target) -> {
            if (!target.getStockList().isEmpty()){
                throw new TactStockException("仓库[" + target.getId() + "]还有库存商品未清空");
            }
            target.setWarehouseStatus(WarehouseStatusEnum.OFF);
            sync(target);
        });
        warehouseStatus = WarehouseStatusEnum.OFF;
        sync(this);

        WarehouseSpecification.checkWarehouse(this);
        return new EventMessage<>(id, WarehouseEventTypeEnum.WAREHOUSE_OFF, generateEvent());
    }

    public EventMessage<WarehouseEventTypeEnum, WarehouseEvent> makeActive(){

        iterateChildren(this, (parent, target) -> {
            target.setWarehouseStatus(WarehouseStatusEnum.ACTIVE);
            sync(target);
        });
        warehouseStatus = WarehouseStatusEnum.ACTIVE;
        sync(this);

        WarehouseSpecification.checkWarehouse(this);
        return new EventMessage<>(id, WarehouseEventTypeEnum.WAREHOUSE_ACTIVE, generateEvent());
    }

    public EventMessage<WarehouseEventTypeEnum, WarehouseEvent> delete(){
        return new EventMessage<>(id, WarehouseEventTypeEnum.WAREHOUSE_DELETED, generateEvent());
    }

    private void iterateChildren(Warehouse root, IterateChildren<Warehouse, Warehouse> iterate){
        if (!root.children.isEmpty()){
            for (Warehouse w :
                    root.children) {
                iterate.accept(root, w);
                iterateChildren(w, iterate);
            }
        }
    }

    private void sync(Warehouse warehouse){
        if (!warehouse.equals(snapshot)){
            warehouse.setUpdateTime(new Date());
        }
    }

    public WarehouseEvent generateEvent(){
        WarehouseEvent warehouseEvent = generateEventBody();
        assembleChildrenEvent(this, warehouseEvent);
        return warehouseEvent;
    }

    private void assembleChildrenEvent(Warehouse root, WarehouseEvent rootEvent){
        if (!root.children.isEmpty()){
            List<WarehouseEvent> childrenEvent = new ArrayList<>();
            for (Warehouse w :
                    root.children) {
                WarehouseEvent childEvent = w.generateEventBody();
                childrenEvent.add(childEvent);
                assembleChildrenEvent(w, childEvent);
            }
            rootEvent.setChildren(childrenEvent);
        }
    }

    private WarehouseEvent generateEventBody(){
        WarehouseEvent warehouseEvent = new WarehouseEvent();
        warehouseEvent.setWarehouseId(id);
        warehouseEvent.setParentId(parent);
        warehouseEvent.setName(name);
        warehouseEvent.setWarehouseIndex(warehouseIndex);
        warehouseEvent.setWarehouseStatus(warehouseStatus.getStatus());
        warehouseEvent.setWarehouseStatusInfo(warehouseStatus.getMessage());
        warehouseEvent.setWarehouseType(warehouseType.getType());
        warehouseEvent.setWarehouseTypeInfo(warehouseType.getMessage());
        return warehouseEvent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Warehouse warehouse = (Warehouse) o;
        return Objects.equal(id, warehouse.id) &&
                Objects.equal(name, warehouse.name) &&
                Objects.equal(warehouseIndex, warehouse.warehouseIndex) &&
                warehouseType == warehouse.warehouseType &&
                warehouseStatus == warehouse.warehouseStatus &&
                Objects.equal(parent, warehouse.parent);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, name, warehouseIndex, warehouseType, warehouseStatus, parent);
    }
}
