# ddd-mall
采用领域驱动设计(ddd)设计的一个模拟B/C2C的电商项目，此项目采用微服务架构开发，旨在研究ddd在微服务下的适配性，
同时实地了解微服务场景下各种分布式技术的使用

## 项目简介
---
这是一个微服务工程，意在通过模拟一个完整的电商流程来研究业务的领域驱动设计，进程间通信，分布式锁，分布式事务等前沿技术在
大规模分布式场景下的使用情况，分析得失，指导将来的工作，提升自己的业务能力
  
项目的服务模块主要由以下几块: 
- parent：所有服务的父pom工程，定义了相关依赖的版本
- common：一个二方库，里面有一些各服务通用的工具类，枚举类，以及dto
- goods：商品服务，还未开发，只提供了一个模拟商品服务领域事件广播的controller
- order：订单服务，还未开发，只提供了一个模拟订单服务命令消息的controller
- seller：卖家服务，还未开发，只提供了一个模拟卖家服务领域事件广播的controller
- stock：库存服务，<a href = "https://github.com/tactbug/ddd-mall/tree/master/stock">详见此处</a>
  
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
git clone https://github.com/tactbug/ddd-mall.git
cd mall/parent
maven install
```
启动方式: 每个服务独立启动

## 版本内容
---
### 1.0.0
---
初次提交
## 协议
---
apache 2.0