# Agent整体打包

## 一、环境依赖

### 1.1 jdk8

需要在环境变量中设置JAVA_8_HOME，如(mac)：
```
JAVA_8_HOME=/Library/Java/JavaVirtualMachines/jdk1.8.0_181.jdk/Contents/Home
CLASSPATH=.:$JAVA_8_HOME/lib/dt.jar:$JAVA_8_HOME/lib/tools.jar
PATH=$JAVA_8_HOME/bin:$PATH:
export JAVA_8_HOME
```

### 1.2 jdk9

需要在环境变量中设置JAVA_9_HOME，如(mac):

```aidl
JAVA_9_HOME=/Library/Java/JavaVirtualMachines/jdk-9.0.4.jdk/Contents/Home
CLASSPATH=.:$JAVA_9_HOME/lib/dt.jar:$JAVA_9_HOME/lib/tools.jar
PATH=$JAVA_9_HOME/bin:$PATH:
export JAVA_9_HOME
```

JAVA_HOME也必须要配置，如果有1.6版本的 jdk 请配置成1.6版本的，如果没有也可以使用 jdk8来替代

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



