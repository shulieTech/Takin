# Takin
[![许可证](https://img.shields.io/github/license/pingcap/tidb.svg)](https://github.com/pingcap/tidb/blob/master/LICENSE)
[![语言](https://img.shields.io/badge/Language-Java-blue.svg)](https://www.java.com/)

Takin是一款基于Java的开源系统，可嵌入到各个服务节点，实现生产或测试环境的全链路性能测试，尤其适合面向微服务架构系统。通过Takin，系统中的中间件和应用可以在生产环境识别真实流量和测试流量，保证它们进入不同的数据库，实现真实和测试流量的现网隔离。

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

## 详细安装部署
-![image](https://user-images.githubusercontent.com/86357315/125920811-6be44d23-b6da-4c2e-b3fb-6913f4574bea.png)
 快速上手文档 ：https://docs.shulie.io/docs/opensource/opensource-1d40ib39m90bu
## 简易上手文档：
- 建议虚拟机内存 ： 3G以上(推荐8G)
- 镜像大小： 2.1G
> 建议修改 Docker 镜像地址为阿里云，详见 [官方镜像加速 - 阿里云文档](https://help.aliyun.com/document_detail/60750.html)
```
vim /etc/docker/daemon.json
添加：
{
  "registry-mirrors": ["https://q2gr04ke.mirror.aliyuncs.com"]
}
重启：
systemctl daemon-reload
```

- 获取docker镜像：docker pull registry.cn-hangzhou.aliyuncs.com/shulie-takin/takin:v1.0.1

- 启动docker镜像：docker run -e APPIP=your ip address -p 80:80 -p 2181:2181 -p 29900-29999:29900-29999 registry.cn-hangzhou.aliyuncs.com/shulie-takin/takin:v1.0.1

- 参数解释：
  -e:添加系统参数。APPIP:是运行容器所在的宿主机IP。默认surge-deploy是读取docker网卡的ip进行注册到zookeeper上的，这样会导致agent无法与容器中的surge-deploy进行通信，通过指定APPIP可以将宿   主机的IP注册到zookeeper上，这样agent就可以在容器   外通过宿主机IP与容器内的surge-deploy应用进行通信。如果使用的是本机docker部署，APPIP需要指定为docker网卡中的宿主机的IP。所以只需要把   APPIP的值替换成自己所使用的IP即可。docker网卡宿主机ip类似于：172.xxx.xxx.xxx
  
  -d:后台启动，如果不想查看部署日志可以在-e前面添加-d参数。
  
  -p:需要开放的端口
  前面的端口表示宿主机需要开放的端口，后面的表示容器中需要开放的端口。例如80:80指就是用宿主机的80端口映射到容器中的80端口。当然，你也可以使用其它的端口来与容器内的80端口进行映射，例如70:80，这   样也是可以的。但是，在访问的时候你就需要把70这个端口加上，例如使用2000:2181 agent在连接zookeeper的时候就需要把端口改成2000。其中80，2181，29900-29999这些端口是必须要开放的。如果你想连接   系统的redis,mysql你还可以开放6379和3306端口。
  
- 访问页面：http://AAPIP/web

- 特别说明：如果部署完成访问首页出现：错误代码：502 ，Bad Gateway/错误的网关!这是因为容器内的takin-web还未完全启动完成，只需要在等一下，刷新页面就好了。

安装完成后：
- see [Quick Start](takin-webapp/doc/QuickStart.md)
- see [系统各个服务的启动命令](docs/Service-CN.md)

# 使用说明
#### Takin结构
<img src="https://raw.githubusercontent.com/shulieTech/Images/main/DaYuX_Architecture2.png" width="70%" height="70%"><br/>
Takin由agent、控制平台与大数据构成。
- [快速上手](https://github.com/shulieTech/Takin/blob/main/takin-webapp/doc/QuickStart.md)
- [使用说明](https://news.shulie.io/?p=2987)

## Agent
- see [Agent](https://github.com/shulieTech/LinkAgent)

## 大数据模块
- see [Takin-surge-deploy](https://github.com/shulieTech/Takin-surge-deploy)
- see [Takin-amdb](https://github.com/shulieTech/Takin-amdb)

## Takin Web
- see [Takin-common](https://github.com/shulieTech/Takin-common)
- see [Takin-web](https://github.com/shulieTech/Takin-web)
- see [Takin-web-ui](https://github.com/shulieTech/Takin-web-ui)
- see [Takin-cloud](https://github.com/shulieTech/Takin-cloud)

## 压测引擎
- see [Takin-pressure-engine](https://github.com/shulieTech/Takin-pressure-engine)
- see [Takin-jmeter](https://github.com/shulieTech/Takin-jmeter)

# 社区
邮件地址: Mail to shulie@shulie.io<br/>
微信群<br/>

<img src="https://user-images.githubusercontent.com/86357315/128668906-afc506d8-79c9-4608-943e-6400f03a64c4.png" width="30%" height="30%">
<br/>
QQ群: **118098566**<br/>
群二维码：<br/>
<img src="https://raw.githubusercontent.com/shulieTech/Images/main/qq_group_2.jpg" width="30%" height="30%">
<br/>
钉钉群：<br/>
<img src="https://raw.githubusercontent.com/shulieTech/Images/main/dingding_group.jpg" width="30%" height="30%">
<br/>
微信公众号：<br/>
<img src="https://raw.githubusercontent.com/shulieTech/Images/main/shulie.png" width="30%" height="30%">

## 在官方论坛提问
[官方论坛](https://news.shulie.io/?page_id=2477)
## 他们都在用Takin
![image](https://user-images.githubusercontent.com/86357315/126733836-6486de9a-5b67-4486-b17f-102e974684b1.png)


# 许可证
Takin遵循 the Apache 2.0 许可证. 详见 the [LICENSE](https://github.com/shulieTech/Takin/blob/main/LICENSE) file for details.
