# 服务列表
- MySQL
- Redis
- InfluxDB
- Zookeeper
- ClickHouse
- Strom
- AMDB
- surege-deploy
- tro-web
- tro-cloud

## 服务启动命令
### MySql
启动：
- 1.service mysql start
- 2.systemctl start mysqld（Centos7）
停止：
- 1.service mysql stop
- 2.systemctl stop mysqld（Centos7）
登录命令：
- mysql -uroot -pshulie@2020

### Redis
启动：
- /data/redis/redis-5.0.8/src/redis-server /data/redis/redis-5.0.8/redis.conf
停止：
- ./data/redis/redis-5.0.8/src/redis-cli shutdown
-（注：尽量不要使用kill命令强制杀进程，容易导致Redis持久化消失）
登录Redis：
- ./data/redis/redis-5.0.8/src/redis-cli -h IP地址 -p 端口号

常用命令：
- keys * ---查询redis中的所有key
- set name c ---创建一个名为name的key，值为c
- exists name ---判断name这个key是否存在
- get name ---获取指定名为name的key
- del name ---删除指定名为name的key

### InfluxDB（分布式时序数据库，常用于数据监控，配合图形化工具可制作折线图）
登录命令：
- influx
常用命令（InfluxDB和MySql命令相似）：
- show measurements; ---查询所有的measurement（测量指标，比如cpu_usage，即cpu使用率）

### Zookeeper
启动：
- ./data/apache-zookeeper-3.5.9-bin/bin/zkServer.sh start
停止：
- ./data/apache-zookeeper-3.5.9-bin/bin/zkServer.sh stop
登录命令：
- ./data/apache-zookeeper-3.5.9-bin/bin/zkCli.sh

### ClickHouse（列式数据库管理系统）
启动：
- service clickhouse-server.service start
- （注：华为云可能会启动失败，如果失败，使用systemctl start clickhouse-server.service）
停止：
- service clickhouse-server.service stop
登录命令：
- clickhouse-client -h 0.0.0.0 --port 9000 --password rU4zGjA/
常用命令：
-select *from t_trace_all where traceId = "traceId" format TSV; ---format表示格式化，TSV是 TabSeparated的缩写

### Strom
启动：
- nohup storm supervisor >/dev/null 2>&1 &
- nohup storm logviewer >/dev/null 2>&1 &
- nohup storm nimbus >/dev/null 2>&1 &
- nohup storm ui >/dev/null 2>&1 &

### amdb（非数据库，负责将测试中产生的各种数据分别写入InfluxDB、MySql、ClickHouse）
启动：
- ./data/amdb/restart.sh


### surege-deploy
启动：
- ./data/apache-storm-2.1.0/start_surge.sh
停止：
- ./data/apache-storm-2.1.0/stop_surge.sh

### tro
tro-web启动：
- sh /data/apps/tro-web/start.sh
tro-web停止：
- sh /data/apps/tro-web/stop.sh
tro-web日志：
- /data/apps/tro-web/service.log

tro-cloud启动：
- sh /data/apps/tro-cloud/start.sh
tro-cloud停止：
- sh /data/apps/tro-cloud/stop.sh
tro-cloud日志：
- /data/apps/tro-web/service.log
