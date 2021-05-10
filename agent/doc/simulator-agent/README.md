这个项目的职责主要是负责与控制台进行交互，负责 agent 的升级、加载、卸载、模块升级等操作，需要注意的是这个模块不能直接升级，如果需要升级则需要重新安装。

### 工程介绍

- **bin**  该目录下包含所有的 agent 相关的配置文件和打包脚本
- **simulator-agent-api** agent 工程的 api 模块
- **simulator-agent-core** agent 工程的核心模块，包括 agent 启动的核心流程实现
- **simulator-agent-provider** agent 工程的默认 spi 实现，如果外部需要实现自定义spi 的实现可以将此 jar 包直接替换成自定义实现的 jar
- **simulator-agent-spi agent** 工程默认的 spi 定义，目前只定义了 agent 加载的 spi
- **simulator-launcher-embedded** agent 启动模式之内嵌模式实现，目前还未实现
- **simulator-launcher-insturment** agent 启动模块之指定命令行启动实现，目前已经测试通过的实现方式
- **simulator-launcher-standalone** agent 启动模式之独立进程的启动实现，目前已经有实现但是还未测试通过

