# data

ArchGuadian的实时数据处理和存储模块

使用guice管理依赖注入，主要为实时接收agent推送的数据

## 模块说明

### common

使用到的公共包

### runtime

运行时的统一管理

### supplier

与探针对接的数据接收端

### sink

数据处理完成后的数据写入

## 工程打包

### 命令

    mvn clean package -Dmaven.test.skip=true

### 运行包路径

    surge-data/target/surge-deploy-1.0-jar-with-dependencies.jar

### 运行

    # acquire docker container id
    docker ps
    # entry docker container
    docker exec -it ${container_id}  /bin/bash
    # run jar
    java -jar surge-deploy-1.0-jar-with-dependencies.jar {"${container_ip}":"${host_ip}"} 
    # example
    java -jar surge-deploy-1.0-jar-with-dependencies.jar {"172.17.0.2":"192.168.1.138"}
