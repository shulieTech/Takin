# instrument-simulator

instrument-simulator是探针的框架，负责整个探针和模块的生命周期管理，并且提供内置的一些工具命令

## 工程介绍
- **instrument-simulator-agent** simulator 框架启动的入口，其中核心类启动类为 AgentLauncher
- **instrument-simulator-agent-module** simulator 框架启动的入口模块(jdk9及以上)支持
- **instrument-simulator-api** simulator 所有扩展模块需要依赖的 api，通过这个 api 实现中间件的增强
- **instrument-simulator-base-api** simulator所有扩展模块依赖的基础 api, 被 instrument-simulator-api依赖
- **instrument-simulator-compatible** simulator 适配模块，用来解决jdk 多版本及 jvm 多版本的适配问题
- **instrument-simulator-core** simulator 的核心类，实现框架的核心加载流程以及扩展模块的管理
- **instrument-simulator-jdk9-module** simulator jdk9以上的module支持
- **instrument-simulator-management-provider** simulator 内部默认的 spi 实现，可以对模块 jar 包加载时进行自定义逻辑
- **instrument-simulator-messager** simulator所有在字节码中增强涉及到的类都定义在这个模块中，并且这个模块定义了增强代码执行的钩子以及异常处理等
- **instrument-simulator-messager-api** instrument-simulator-messager暴露出来的 api,目前只暴露了对于 bootstrap 的资源加载的定义
- **instrument-simulator-messager-jdk9** jdk9的对于 boostrap 的资源加载的实现
- **instrument-simulator-spi** simulator 的 spi 定义
- **system-modules** simulator 内置的工具模块的实现，实现了部分 arthas 的功能，提供 http 的方式进行访问