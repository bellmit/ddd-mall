# ddd-stock
采用领域驱动设计(ddd)完成的一个模拟B/C2C的电商项目，这是模拟其中的库存管理服务端

## 项目简介
---
这是一个领域驱动设计思想指导下实现的服务端项目，意在研究ddd思想在实际使用中的优势及不足，项目采用标准六边形架构设计，囊括领域对象、服务、入站适配器、出站适配器等概念结构（具体项目结构<a href = "#目录结构">点击此处</a>）。
  
项目的服务内容从服务主体上主要分为三类: 
- 系统操作（管理员直接通过http restful接口进行操作）
  - createWarehouse(): 创建顶级仓库
  - addChild(): 添加子仓库单位
  - updateWarehouseName(): 修改仓库单位名称
  - moveWarehouse(): 移动仓库单位
  - makeWarehouseFull(): 设置仓库单位满载
  - makeWarehouseOff(): 设置仓库单位禁用
  - makeWarehouseActive(): 设置仓库单位启用
  - deleteWarehouse(): 删除仓库单位
  - putStockInByManager(): 管理员手动添加商品库存
  - getStockOutByManager(): 管理员手动进行商品出库
- 响应其他服务的命令式消息
  - getStockOutBySelling(): 商品销售的出库操作
- 监听其他服务的领域事件广播
  - createStockBySeller(): 商品服务中卖家创建商品库存
  - addAreaBySellerOpeningAShop(): 卖家服务中卖家开店
  - banStockBySeller(): 商品服务中卖家下架商品
  - updateStockQuantityBySeller(): 商品服务中卖家修改商品库存
  - sellerCloseStore(): 卖家服务中卖家关闭店铺
  
项目采用java开发，springboot 2.3.4作为技术基点，使用JPA做持久化处理(开启DDL模式)

## 环境依赖
---
- JDK: 11
- maven: 3.6.1
- mysql: 8.1
- kafka: 2.6.0
## 部署方式
---
项目采用maven管理，部署方式如下
```
git clone https://github.com/tactbug/ddd-stock.git
cd ./stock
maven install
```
启动方式
```
cd ./target
java -jar stock-1.0-SNAPSHOT.jar -Dxxx
```
-D后面的为jvm项目启动配置项，具体可配置的属性请参考项目application.yml文件
## [目录结构](#目录结构)
---
项目采用六边形结构建模，整体结构如下: 
![捕获.PNG](https://i.loli.net/2020/10/18/RWI14medlDEKF7A.png)
  
项目具体结构:
  - aggregate: 聚合相关
    - factory: 复杂聚合对象创建的工厂类，本项目只有warehouse需要用到
      - WarehouseFactory
    - function: 自定义的一些函数式接口
      - IterateChildren
    - root: 无聚合实体根，本项目只有stock
      - StockRoot
    - specification: 复杂聚合对象的合理性验证器，本项目只有warehouse需要用到
      - WarehouseSpecification
    - valueObject: 值对象
      - WarehouseStatusEnum
      - WarehouseTypeEnum
  - assist: 辅助类
    - config: springBean的配置类文件夹
      - KafkaConfig: kafka配置类
      - SwaggerConfig: swagger配置类
    - exception: 自定义异常
      - TactStockException
    - model: 处理消息体的dto
      - GoodsSellInfo: 商品售卖信息item
    - utils: 一些工具类
  - inbound: 入站适配器
    - message: 入站消息适配器
      - messageBox: 保证消息处理的事务性收件箱
      - MessageDispatch: 消息统一的调度器
      - MessageExceptionHandler: 消息统一的异常处理中心
      - OrderCommandAdapter: 订单服务命令式消息适配器
      - GoodsEventAdapter: 商品服务领域事件适配器
      - SellerEventAdapter: 卖家服务领域事件适配器
      - MockWarehouseEventConsumer: 模拟用, 仓库领域事件的消费者
    - web: http入站适配器
      - ExceptionController: http统一异常响应
      - StockController: spring mvc controller
  - outbound: 出站适配器
    - publisher: 领域事件发布器
      - WarehouseEventPublisher
    - repository: 各个实体根的持久化适配器
      - goods
      - seller
      - stock
      - warehouse
  - service: 系统服务
## 版本内容
---
### 1.0.0
---
完成了所有功能点的开发，做了集群环境的适配
## 协议
---
apache 2.0