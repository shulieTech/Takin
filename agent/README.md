# Agent

DaYuX的agent探针

## QuickStart

see [QuickStart](https://github.com/shulieTech/DaYuX/blob/main/agent/doc/QuickStart.md)

## 模块说明

### simulator-agent
这个项目的职责主要是负责与控制台进行交互，负责 agent 的升级、加载、卸载、模块升级等操作，需要注意的是这个模块不能直接升级，如果需要升级则需要重新安装。

see [simulator-agent](https://github.com/shulieTech/DaYuX/blob/main/agent/doc/simulator-agent/README.md)

### instrument-simulator
探针框架，负责整个探针和模块的生命周期管理，并且提供内置的一些工具命令
see [instrument-simulator](https://github.com/shulieTech/DaYuX/blob/main/agent/doc/instrument-simulator/README.md)

### instrument-modules
所有用户的自定义模块。所有我们支持的中间件列表全都放在这个工程目录下。

see [instrument-modules](https://github.com/shulieTech/DaYuX/blob/main/agent/doc/instrument-modules/README.md)

## 如何构建

see [HowToBuild](https://github.com/shulieTech/DaYuX/blob/main/agent/doc/HowToBuild.md)

## 注意事项

see [Notice](Notice.md)

## FQA

see [FQA](FQA.md)
