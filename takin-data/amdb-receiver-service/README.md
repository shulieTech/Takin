# amdb

Takin的数据服务

项目为springboot项目，使用springmvc，主要为控制台提供数据查询服务

## 模块说明

### controller

核心开放三部分：应用、实时流量查询、调用链路查询

### adapter

定时同步探针注册的应用数据，主要包括实例数据

## 工程打包

### 命令

mvn clean package

### 运行包路径

amdb-receiver-service/target/amdb-app-1.0.4.1-SNAPSHOT.jar

## 运行

java -jar amdb-app-1.0.4.1-SNAPSHOT.jar
