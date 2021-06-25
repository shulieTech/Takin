# QuickStart

## 一、安装包

下载 `tro-web` 安装包：[download](https://shulie-main-pass.oss-cn-hangzhou.aliyuncs.com/open-source/tro-web.zip)

下载 `tro-cloud` 安装包：[download](https://shulie-main-pass.oss-cn-hangzhou.aliyuncs.com/open-source/tro-cloud.zip)

下载 `引擎包` 引擎包：[download](https://shulie-main-pass.oss-cn-hangzhou.aliyuncs.com/open-source/pressure-engine.tar.gz)

或者你可以自己构建：

see [HowToBuild](HowToBuild.md)

### 二、配置修改

### 引擎包校验json `engine_pack_version.json`
> 这里 引擎包下载下来，求下md5值 ：md5sum pressure-engine.tar.gz
```
[
    {
        "version":"v1.0.0",
        "md5":"5bc2c6e747c6ccec11354a91443b0f41",
        "description":"引擎包1.0.0版本，支持jmeter压测"
    }
]
```

### tro-web  `application.yml`
```
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
```

### tro-cloud `application-dev.yml`
```
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
```
### nginx配置 
- 安装nginx  [nginx安装部署-1.20.0](https://install-pkg.oss-cn-hangzhou.aliyuncs.com/alone-pkg/nginx-1.20.1.tar.gz)

- nginx配置  nginx.conf
```
user root;
worker_processes auto;
error_log /var/log/nginx/error.log;
pid /run/nginx.pid;

include /usr/share/nginx/modules/*.conf;

events {
    worker_connections 1024;
}

http {
    access_log  /var/log/nginx/access.log;
    proxy_read_timeout 240s;
    sendfile            on;
    tcp_nopush          on;
    tcp_nodelay         on;
    keepalive_timeout   65;
    types_hash_max_size 2048;
 
    include             /usr/local/nginx/conf/mime.types;
    default_type        application/octet-stream;

    gzip on;
    gzip_comp_level 3;
    gzip_buffers 320 320k;
    gzip_min_length 40960;
    gzip_types text/plain text/style application/javascript application/x-javascript text/javascript text/css application/json;

    server {
        listen       80;
        server_name  localhost;

        client_max_body_size 200M;

        #前端访问配置
        location / {
            root   /data/apps/dist;
            index  index.html index.htm;
        }

        #web后端
        location /tro-web {
            proxy_pass http://127.0.0.1:10008/tro-web;
        }

        #cloud后端
        location /tro-cloud {
            proxy_pass http://127.0.0.1:10010/tro-cloud;
        }

        ##白名单
        location /tro-web/api/confcenter/wbmnt/query{
            add_header Cache-Control no-store;
            root /opt/tro/conf;
        }

        error_page 500 502 503 504  /50x.html;
        location = /50x.html {
            root   html;
        }
    }
}
```
