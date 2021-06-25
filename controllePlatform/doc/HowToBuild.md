# 控制平台

***

## 项目介绍

- 代码结构

  ![控制平台](控制平台.png)

- 项目环境

    - 前端工程环境

        - nodeJs

            - [node](https://nodejs.org/en/)
            - [nvm](https://github.com/nvm-sh/nvm)

          ```
          首先得有 node【https://nodejs.org/en/】，并确保 node 版本是 10.13 或以上，建议使用版本14.16.1。
          （mac 下推荐使用 nvm【https://github.com/nvm-sh/nvm】 来管理 node 版本）
          推荐使用 yarn 管理 npm 依赖，并使用国内源
          # 国内源
          $ npm i yarn tyarn -g
          # 后面文档里的 yarn 换成 tyarn
          $ tyarn -v
          ```

    - 后端工程环境   --感谢文档提供者

        - jdk8  [Mac](https://www.cnblogs.com/mini-monkey/p/11593526.html)  [windows](https://www.runoob.com/w3cnote/windows10-java-setup.html)
        - mysql 版本5.7.24  [Linux](https://www.cnblogs.com/maidongdong/p/11469940.html)  [windows](https://www.cnblogs.com/zhangkanghui/p/9613844.html)
        - influxdb  版本 influxdb-1.7.10.x86_64.rpm  [Linux](https://blog.csdn.net/u013059060/article/details/112303586)
        - maven  [Mac](https://www.jianshu.com/p/191685a33786)  [windows](https://www.cnblogs.com/happyday56/p/8968328.html)
        - Redis [Mac](https://www.jianshu.com/p/3bdfda703552) [Linux](https://www.cnblogs.com/hunanzp/p/12304622.html) [windows](https://www.redis.com.cn/redis-installation.html)
        - Zookeeper [Mac](https://segmentfault.com/a/1190000022287477) [Linux](https://www.cnblogs.com/liuwei00125/p/12554744.html) [windows](https://blog.csdn.net/qq_36148847/article/details/80114283)
        - 引擎包 [download]()
***

## 安装部署

## 第一步 deploy

- deploy tro，两种方案

    - 打包jar,并deploy 架包

  ```maven
  // 第一步配置pom文件仓库地址
  // 第二步执行deploy
  mvn clean deploy -Dmaven.test.skip=true 
  ```

    - 使用公有仓库

- deploy tro-cloud，两种方案

    - 打包jar，并deploy架包

  ```
  // 第一步配置pom文件仓库地址
  // 第二步执行deploy
  mvn clean deploy -Dmaven.test.skip=true 
  ```

    - 使用公有仓库

## 第二步 package

### tro-web

进入工程 `tro-web` 根目录，执行脚本`mvn clean install -Dmaven.test.skip=true`

打包位置：在 `tro-web-app` 中 `target`

### tro-cloud

进入工程 `tro-cloud` 根目录，执行脚本`mvn clean install -Dmaven.test.skip=true`

打包位置：在 `tro-cloud-app` 中 `target`

***

# 环境配置

## 数据库环境

- Tro-web 项目 数据库名 `trodb` : [trodb_web_base.sql](trodb_web_base.sql)

- Tro-cloud项目 数据库名 `trodb_cloud`  [trodb_cloud_base.sql](trodb_cloud_base.sql)

- 低版本的Mysql，group by限制性比较小，在进行group by时，select的对象可包含多个，但是换成高版本5.6以上好像，使用group by 以后，select的对象必须也已经被聚合，否则查询不出来数据

  ```
  set sql_mode ='STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION';
  ```

## influxdb配置

- 设置账号密码

  ```
  resource：
   	influxdb:
      url: http://127.0.0.1:32709
      user: pradar
      password: root
      database: jmeter
  ```

- Tro-web 项目 手动创建数据库名 `trodb` :

  ```
  create database base
  ```

- Tro-cloud项目 手动创建数据库名 `jmeter`  :

  ```
  create database jmeter
  ```

## redis配置

- 设置密码 root