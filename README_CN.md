# 架构守护者
[![许可证](https://img.shields.io/github/license/pingcap/tidb.svg)](https://github.com/pingcap/tidb/blob/master/LICENSE)
[![语言](https://img.shields.io/badge/Language-Java-blue.svg)](https://www.java.com/)

架构守护者是一款基于Java的开源系统，可嵌入到各个服务节点，实现生产环境的全链路性能测试，尤其适合面向微服务架构系统。通过架构守护者，系统中的中间件和应用可以在生产环境识别真实流量和测试流量，保证它们进入不同的数据库，实现真实和测试流量的现网隔离。

## 我们为什么需要做生产环境的性能测试？
微服务架构在现代系统架构中已被普遍使用，与此同时，随着业务的扩张和微服务数量的增加，它使系统变得非常复杂以至于人无法理解，而且，很多业务逻辑本身也非常复杂。业务复杂性和系统复杂性使保证和维持整个系统的高可用性非常困难，同时，它对研发效率也产生负面影响。<br/>
为了保证系统的高可用性，我们通常对测试环境或生产环境的单一服务进行性能测试，但是，测试环境与在生产环境区别很大，单个服务也不能代表整个服务链路，因此，它们都不能保证系统的高可用，通常也无法给出准确的容量评估结果。

**微服务很复杂**<br/>
和单体架构相比，微服务架构增加了业务系统的复杂性，因为它的子服务数量更多，并且涉及更多的不同技术栈和框架。

**业务系统也很复杂**<br/>
很多业务本身的业务逻辑也很复杂，其中很多业务涉及比较长的业务流程，例如电商业务。

**服务与服务之间的调用关系也很复杂**<br/>
在微服务架构的系统中，服务之间的调用关系非常复杂，每次服务的发布和更新都可能影响整个系统的可用性，并使开发人员难以频繁发布新版本。

# Quick Start Instruction（快速入门说明）

镜像安装：
- 获取容器镜像：docker pull registry.cn-hangzhou.aliyuncs.com/sl-docker-hub/docker-hub:sl-ky1.0.0
- 运行容器镜像：docker run -d -p 80:80 -p 2181:2181 -p 3306:3306 -p 6379:6379 -p 8086:8086 -p 9000:9000 -p 10032:10032 -p 6628:6628 -p 8000:8000 -p 6627:6627 -p 8888:8888 -p 29900-29999:29900-29999 imgaeID或者name:tag
- 参数解释：-d是后台启动，-p是需要开放的端口，容器运行初始化的时候需要安装一些必要的组件需要十分钟样子，-d可以忽略后台组件的安装信息，如果想要查看安装信息可以去除-d参数，如果不使用-p的方式开放端口，也可以使用--net=host的方式与宿主机共用一套网络。

安装完成后：
see [Quick Start](https://github.com/shulieTech/ArchGuadian/blob/main/controllePlatform/doc/QuickStart.md)

# 使用说明
#### 架构守护者结构
<img src="https://raw.githubusercontent.com/shulieTech/Images/main/DaYuX_Architecture2.png" width="70%" height="70%"><br/>
架构守护者由代理、控制平台与脉冲数据构成。

- [使用说明]()

## 探针
详见 [Agent](https://github.com/shulieTech/AgentX)

## 脉冲数据
详见 [Surge Data](https://github.com/shulieTech/ArchGuadian/blob/main/data/surge-data/README.md?_blank)

## 控制平台
详见 [Controlle Platform](https://github.com/shulieTech/ArchGuadian/blob/main/controllePlatform/doc/QuickStart.md)

# 社区
邮件地址: Mail to shulie@shulie.io<br/>
微信群<br/>
<img src="https://raw.githubusercontent.com/shulieTech/Images/main/code1.png" width="30%" height="30%">
<br/>
QQ群: **118098566**<br/>
群二维码：<br/>
<img src="https://raw.githubusercontent.com/shulieTech/Images/main/qq_group2.png" width="30%" height="30%">
<br/>
微信公众号：<br/>
<img src="https://raw.githubusercontent.com/shulieTech/Images/main/shulie.png" width="30%" height="30%">

## 在官方论坛提问
[官方论坛](https://news.shulie.io/?page_id=2477)

# 许可证
架构守护者遵循 the Apache 2.0 许可证. 详见 the [LICENSE](https://github.com/shulieTech/ArchGuadian/blob/main/LICENSE) file for details.
