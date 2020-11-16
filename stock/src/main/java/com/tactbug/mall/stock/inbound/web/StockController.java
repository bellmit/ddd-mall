package com.tactbug.mall.stock.inbound.web;

import com.tactbug.mall.common.base.TactException;
import com.tactbug.mall.common.enums.CommonResultEnum;
import com.tactbug.mall.common.vo.ResultResponse;
import com.tactbug.mall.stock.assist.exception.TactStockException;
import com.tactbug.mall.stock.service.StockService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@Api(tags = "仓库管理系统controller")
@RestController
@RequestMapping("/stock")
public class StockController {

    @Autowired
    private StockService stockService;

    @ApiOperation(value = "添加顶级仓库")
    @PostMapping("/warehouse")
    public ResultResponse createWarehouse(String name, Integer type) {
        stockService.createWarehouse(name, type);
        return ResultResponse.ok();
    }

    @ApiOperation(value = "添加子仓库")
    @PostMapping("/warehouse/child")
    public ResultResponse addChildWarehouse(Long parentId, Integer type) {
        stockService.addChild(parentId, type);
        return ResultResponse.ok();
    }

    @ApiOperation(value = "修改仓库名称")
    @PutMapping("/warehouse/name")
    public ResultResponse updateWarehouseName(Long warehouseId, String name) {
        stockService.updateWarehouseName(warehouseId, name);
        return ResultResponse.ok();
    }

    @ApiOperation(value = "移动仓库")
    @PutMapping("/warehouse")
    public ResultResponse moveWarehouse(Long sourceId, Long targetId) {
        stockService.moveWarehouse(sourceId, targetId);
        return ResultResponse.ok();
    }

    @ApiOperation(value = "设置仓库状态(0、禁用, 1、启用, 2、满载)")
    @PutMapping("/warehouse/status")
    public ResultResponse updateWarehouseStatus(Long warehouseId, Integer status) {
        switch (status){
            case 0:{
                stockService.makeWarehouseOff(warehouseId);
                return ResultResponse.ok();
            }
            case 1:{
                stockService.makeWarehouseOn(warehouseId);
                return ResultResponse.ok();
            }
            case 2:{
                stockService.makeWarehouseFull(warehouseId);
                return ResultResponse.ok();
            }
            default:
                throw new TactStockException("不存在的仓库状态[" + status + "]");
        }
    }

    @ApiOperation(value = "删除仓库")
    @DeleteMapping("/warehouse")
    public ResultResponse deleteWarehouse(Long warehouseId) {
        stockService.deleteWarehouse(warehouseId);
        return ResultResponse.ok();
    }

    @ApiOperation(value = "管理员手动入库")
    @PostMapping("/product")
    public ResultResponse putGoods(Long goodsId, Long warehouseId, Integer batch, Integer quantity) {
        stockService.putStockInByManager(goodsId, warehouseId, batch, quantity);
        return ResultResponse.ok();
    }

    @ApiOperation(value = "管理员手动出库")
    @PutMapping("/product")
    public ResultResponse getGoods(Long goodsId, Long warehouseId, Integer batch, Integer quantity) {
        stockService.getStockOutByManager(goodsId, warehouseId, batch, quantity);
        return ResultResponse.ok();
    }

    @GetMapping("/hello")
    public String hello(){
        return "hello";
    }
}
