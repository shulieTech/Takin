# 安装步骤
## 拉Docker镜像
https://github.com/shulieTech/Takin/blob/main/README_CN.md

### 打开putty登陆10.15.10.191 用户名/密码: root/*********** 

### Centos7设备如果是新装，需要安装一些工具，全部复制进去执行即可
yum update -y &&\
yum install -y gcc curl make unzip gettext git &&\
yum install vim -y && \
yum install wget -y &&\
yum install net-tools -y &&\
mkdir -p /home/hunter/ && \
yum install zlib zlib-devel -y &&\
yum install openssl-devel -y &&\
yum update –y

### 新开一个putty窗口登陆10.15.10.190 用户名/密码: root/***********

### 上传docker要运行的配置文件docker-compose.yml 到/opt目录

### 删除旧的docker
yum remove docker  docker-client  docker-client-latest  docker-common  docker-latest  docker-latest-logrotate  docker-logrotate  docker-selinux  docker-engine-selinux  docker-engine docker-ce -y

### 删除旧的docker文件
rm -rf /var/lib/docker

 ###	安装所需要的依赖包
yum install -y yum-utils device-mapper-persistent-data lvm2

### 用阿里源下载docker
yum-config-manager --add-repo http://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo

### 打开docker下载列表
yum list docker-ce --showduplicates | sort -r

###	选择一个docker版本安装
yum install docker-ce-18.06.1.ce –y

### 启动docker
systemctl start docker

### 如果docker配置没有设置阿里云docker source
vim /etc/docker/daemon.json
### 添加如下配置：
{
  "registry-mirrors": ["https://q2gr04ke.mirror.aliyuncs.com"]
}
### 重启：
systemctl daemon-reload

### 拉docker镜像
docker run -p 80:80 -p 2181:2181 -p 3306:3306 -p 6379:6379 -p 8086:8086 -p 9000:9000 -p 10032:10032 -p 6628:6628 -p 8000:8000 -p 6627:6627 -p 8888:8888 -p 29900-29999:29900-29999 registry.cn-hangzhou.aliyuncs.com/forcecop/forcecop:v1.0.0
（这个过程执行完以后 请务必等待10分钟）
### 查看docker进程
 docker ps –a

### 进入容器
docker exec -it 97c3af70a5af /bin/bash

### 编辑/data/index.html文件 # 修改serverUrl的IP为服务本机IP
vi /data/apps/dist/tro/index.html

### 重启Nginx服务：
nginx -s reload

### kill掉sugre-deploy应用
ps –ef|grep sugre  kill -9 pid 
到/data/install.sh里面复制启动sugre-deploy的脚本 再复制出来执行
nohup java -jar surge-deploy-1.0-jar-with-dependencies.jar '{"172.17.0.2":"你的机器IP"}' > surge.out  2>&1 &
### 注意:首次注册需要1-2分钟

### 打开页面 http://你的机器IP/tro
默认账号密码：
账号:admin  
密码:pamirs@2020


# 修改 tro-web application.yml

## 部署位置
troweb.path: ~/data/apps/tro-web
## 数据库配置
resource:
  ## mysql
  mysql:
    host: 127.0.0.1
    port: 3306
    username: root
    password: shulie@2020
## redis配置
  ## redis
  redis:
    host: 127.0.0.1
    port: 6379
    password: pamirs@2020
## influxdb配置
  ## influxdb
  influxdb:
    url: http://127.0.0.1:32709
    user: pradar
    password: pamirs@db2020
    database: base

## tro-cloud 访问url配置 具体配置看引入
trocloud.out.url: 127.0.0.1:10010
## 大数据配置
### AMDB 组件提供的请求地址 ###
amdb.url.prdar: 127.0.0.1:8080
amdb.url.amdb: http://127.0.0.1:10032
## 配置zk地址 用与agent交互
tro.config.zk.addr: 127.0.0.1:2181


# 修改tro-cloud application-dev.yml

resource:
  ## mysql
  mysql:
    host: 127.0.0.1
    port: 3306
    username: root
    password: shulie@2020
  ## redis
  redis:
    host: 127.0.0.1
    port: 6379
    password: pamirs@2020
  ## influxdb
  influxdb:
    url: http://127.0.0.1:8086
    user: pradar
    password: pamirs@db2020
    database: jmeter
## 引擎包位置  引擎包可在github下载
pressure.engine.install.dir: /Users/shulie/engine/pressure-engine.tar.gz
## 引擎包工作目录 记录引擎包工作的配置文件 记录引擎包生成的日志（jtl,log）
pressure.engine.task.dir: /Users/shulie/engine
## 脚本管理 临时文件目录
script.temp.path: /data/apps/tro-cloud/script/temp
## 脚本管理 脚本文件目录
script.path: /data/apps/tro-cloud/script/

## 配置cloud地址 即当前系统部署的url
console.url: http://127.0.0.1:10010/tro-cloud
## influx 时间间隔分组
report.aggregation.interval: 5s
#微服务类型，目前不支持支持k8s 支持本地线程启动引擎包
micro.type: localThread
## 压测包工作jvm
pressure.engine.memSetting: -Xmx512m -Xms512m -Xss256K -XX:MaxMetaspaceSize=256m


# Agent部署
https://github.com/shulieTech/LinkAgent/blob/main/doc/QuickStartInEnglish.md

## 打开putty登陆10.15.10.191 用户名/密码: root/***********  

## 安装JDK8 配置环境变量

## 获取开源Agent 到/data 目录 没有则mkdir data
wget https://shulie-main-pass.oss-cn-hangzhou.aliyuncs.com/open-source/simulator-agent.tar

## 解压tar包
 tar --no-same-owner -xvf simulator-agent.tar
 
## 修改配置文件 agent.properties
simulator.zk.servers=127.0.0.1:2181 Zookeeper地址配置，改成你的zookeeper地址。
simulator.log.path=/data/log日志的输出地址。

## 修改配置文件 simulator.properties 
tro.web.url 改成docker takin平台的IP 
user.app.key=5b06060a-17cb-4588-bb71-edd7f65035af

## 上传打好的jar包 也就是测试用的demo

## 编辑一个.sh 的启动文件
nohup java -javaagent:/data/simulator-agent-ky/bootstrap/transmittable-thread-local-2.12.1.jar -Xbootclasspath/a:/usr/soft/jdk1.8.0_291/lib/tools.jar -javaagent:/data/simulator-agent-ky/simulator-launcher-instrument.jar -Dpradar.project.name=ceshi003-test -Djdk.attach.allowAttachSelf=true -Dsimulator.agentId=ceshi001 -Dsimulator.delay=1 -jar /data/demo-0.0.1-SNAPSHOT.jar > test.out  2>&1 &

## 	启动 可以查看test.out 看jar包是否运行成功 然后再去检查agent日志 

