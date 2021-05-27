

# 控制平台项目介绍
- 项目结构如下
  - tro：tro-web 和 tro-cloud 抽象工程，包含工具类，agent交互通道，异常处理模块等
  - tro-web-ui:前端工程
  - tro-web:用于配置压测相关参数，例如应用管理，业务活动，脚本管理，场景管理，报告管理等
  - tro-cloud:用于启动引擎包，目前只支持本地启动引擎包  
## 一、环境依赖

### 1.1 jdk8
需要在环境变量中设置JAVA_HOME，如(mac)：
```

```

### 1.2 maven
需要在环境变量中设置maven，如(mac)：

### 1.3 node.js

## 二、打包

1. 进入工程`simulator-agent`目录`bin`，执行脚本`agent-packages.sh`
2. 进入工程`simulator-agent`目录`bin`，执行脚本`agent-packages.sh`
3. 进入工程`instrument-simulator`目录`bin`，执行脚本`simulator-packages.sh`
4. 进入工程`instrument-modules`目录`bin`，执行脚本`packages.sh`
5. 拷贝工程`instrument-modules`目录`target`下`modules`与`bootstrap`拷贝至工程`instrument-simulator`目录`target`下`simulator`中
6. 拷贝工程`instrument-modules`目录`target`下的`biz-classloader-jars/`目录下的内容拷贝至工程`instrument-simulator`目录`target`下`simulator`目录下的`biz-classloader-jars`目录中
7. 拷贝工程`instrument-modules`目录`target`下的`bootstrap/`目录下的内容拷贝至工程`instrument-simulator`目录`target`下`simulator`目录下的`bootstrap`目录中
8. 将工程`instrument-simulator`目录`target`下`simulator`拷贝至工程`simulator-agent`下的目录`target/simulator-agent/agent`中

> 拷贝的目标目录不存在则直接新建即可



