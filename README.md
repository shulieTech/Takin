# Takin
[![LICENSE](https://img.shields.io/github/license/pingcap/tidb.svg)](https://github.com/pingcap/tidb/blob/master/LICENSE)
[![Language](https://img.shields.io/badge/Language-Java-blue.svg)](https://www.java.com/)

English / [中文](README_CN.md)

## What is Takin?
Takin is an Java-based, open-source system designed to measure online environmental performance test for full-links, Especially for microservices. Through ArchGuadian, middlewares and applications can identify real online traffic and test traffic, ensure that they enter the right databases.

# Why should we do online environmental performance test
Microservices Architecture is used commonly nowadays and it always make system complex to understand for humans. Moreover, businesses are also very complex in huge system. Business complexity and system complexity make it difficult to :
- Keep entire system highly available
- Maintain Research & Development efficiency.

In order to keep system high available, we usually make performance test on test environment or online single-service. However, test environment is very different from online environment, single-service can't stand for the whole service-links. They can't guarantee system performance.

**Microservices Are Complex**<br/>
Compare with monolithic application, Microservices architecture increases complexity for business system. It may maintain multiple tools and frameworks.

**Business Systems Are Complex**<br/>
Businesses involve different sections and many of them are long-process and complicated, such as E-Commerce businesses.

**The Microservices Relation Is Complex**<br/>
In a microservices architecture system with a lot of business services, the calling relation between services is very complicated. Every change may affect the availability of the entire system and make developers difficult to release new versions Frequently.

<img src="https://raw.githubusercontent.com/shulieTech/Images/main/WX20210511-150735%402x.png" width="80%" height="80%">
<img src="https://raw.githubusercontent.com/shulieTech/Images/main/3.png" width="50%" height="50%">

# Quick Start Instruction

**docker:**<br/>
- VM memory requirement ： more than 4G
- Docker mirror size ： 1.8 G

If docker configuration doesn't set AliYun docker source :
```
vim /etc/docker/daemon.json
```
Add following configuration：
```
{
  "registry-mirrors": ["https://q2gr04ke.mirror.aliyuncs.com"]
}
```
restart service
```
systemctl daemon-reload
```

Pull docker
```shell
docker run -d -p 80:80 -p 2181:2181 -p 3306:3306 -p 6379:6379 -p 8086:8086 -p 9000:9000 -p 10032:10032 -p 6628:6628 -p 8000:8000 -p 6627:6627 -p 8888:8888 -p 29900-29999:29900-29999 registry.cn-hangzhou.aliyuncs.com/forcecop/forcecop:v1.0.0
```

- Parameter：-d start in background，-p port. <br/>
The Initiation of docker need about 10 mins because it need install necessary components. -d can ignore installment information of components in background. If you dont't want to open your server's port, you can use --net=host and make sure it and host server are in the same network。
- Modify the IP address of the index.html file to the server's local IP address.
    ```
    # Enter Docker
    vi /data/apps/dist/tro/index.html
    # Modify serverUrl to the server's local IP address
    # Restart Nginx：
    nginx -s reload
    
    Kill surge-deploy application
    nohup java -jar surge-deploy-1.0-jar-with-dependencies.jar '{"172.17.0.2":"Your own IP"}' > surge.out  2>&1 &
    Modify the Ip to your own IP, and restart.

    Open http://127.0.0.1/tro
    
    Default account and password：
    account:admin  
    password:pamirs@2020
    ```

after installation：
- see [Quick Start](takin-webapp/doc/QuickStart.md)
- [Instruction 使用说明](https://news.shulie.io/?p=2987)

# Instruction
#### Takin Architecture
<img src="https://raw.githubusercontent.com/shulieTech/Images/main/DaYuX_Architecture2.png" width="70%" height="70%"><br/>
Takin consists of Agent, Web App and Surge Data.

## Agent
see [Agent](https://github.com/shulieTech/LinkAgent)

## Surge Data
see [surge-data](takin-data/surge-data/README.md?_blank)

## Controller Platform
see [takin-webapp](takin-webapp/doc/QuickStart.md)

# Community
Mailing List: Mail to shulie@shulie.io<br/>
Wechat group<br/>
<img src="https://raw.githubusercontent.com/shulieTech/Images/main/wx_5.png" width="30%" height="30%">
<br/>
QQ group: **118098566**<br/>
QR code：<br/>
<img src="https://raw.githubusercontent.com/shulieTech/Images/main/qq_group_2.jpg" width="30%" height="30%">
<br/>
Dingding group：<br/>
<img src="https://raw.githubusercontent.com/shulieTech/Images/main/dingding_group.jpg" width="30%" height="30%">
<br/>
WeChat Official Account：<br/>
<img src="https://raw.githubusercontent.com/shulieTech/Images/main/shulie.png" width="30%" height="30%">
<br/>

## Ask Questions in Official Forum
[Official Forum](https://news.shulie.io/?page_id=2477)

# License
Takin is under the Apache 2.0 license. See the [LICENSE](LICENSE) file for details.
