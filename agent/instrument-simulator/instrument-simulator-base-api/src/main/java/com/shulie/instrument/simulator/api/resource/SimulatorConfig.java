/**
 * Copyright 2021 Shulie Technology, Co.Ltd
 * Email: shulie@shulie.io
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.shulie.instrument.simulator.api.resource;

import com.shulie.instrument.simulator.api.LoadMode;
import com.shulie.instrument.simulator.api.ModuleRepositoryMode;

import java.io.File;
import java.lang.instrument.Instrumentation;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 仿真器配置信息
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/10/23 10:45 下午
 */
public interface SimulatorConfig {

    /**
     * 获取仿真器的命名空间
     *
     * @return 仿真器的命名空间
     */
    String getNamespace();

    /**
     * 获取仿真器的加载模式
     *
     * @return 仿真器加载模式
     */
    LoadMode getMode();

    /**
     * 获取模块仓库模式
     *
     * @return 模块仓库模块
     */
    ModuleRepositoryMode getModuleRepositoryMode();

    /**
     * 判断仿真器是否启用了unsafe
     * <p>unsafe功能启用之后，仿真器将能修改被BootstrapClassLoader所加载的类</p>
     * <p>在<b>${SIMULATOR_HOME}/config/simulator.properties#unsafe.enable</b>中进行开启关闭</p>
     *
     * @return true:功能启用;false:功能未启用
     */
    boolean isEnableUnsafe();

    /**
     * 获取仿真器的HOME目录(仿真器主程序目录)
     * 默认是在<b>${HOME}/.simulator</b>
     *
     * @return 仿真器HOME目录
     */
    String getSimulatorHome();

    /**
     * 获取 simulator md5值
     *
     * @return
     */
    String getSimulatorMd5();

    /**
     * 获取仿真器的系统模块目录地址
     * <p>仿真器将会从该模块目录中寻找并加载所有的模块</p>
     * <p>默认路径在<b>${SIMULATOR_HOME}/module</b>目录下</p>
     *
     * @return 系统模块目录地址
     */
    String getSystemModuleLibPath();

    /**
     * 获取仿真器内部服务提供库目录
     *
     * @return 仿真器内部服务提供库目录
     */
    String getSystemProviderLibPath();

    /**
     * 获取仿真器的用户模块目录地址(集合)
     *
     * @return 用户模块目录地址(集合)
     */
    String[] getUserModuleLibPaths();

    /**
     * 获取所有需要业务类加载加载的jar包
     *
     * @return 业务类加载器加载的jar包, key为className value为jar文件
     */
    Map<String, List<File>> getBizClassLoaderInjectFiles();

    /**
     * 获取所有需要业务类加载加载的jar包
     *
     * @return 业务类加载器加载的jar包, key为className value为jar url
     */
    Map<String, List<String>> getBizClassLoaderURLExternalForms();

    /**
     * 获取仿真器HTTP服务侦听地址
     * 如果服务器未能完成端口的绑定，则返回("0.0.0.0:0")
     *
     * @return 仿真器HTTP服务侦听地址
     */
    InetSocketAddress getServerAddress();

    /**
     * 返回配置的服务端口,如果没有配置则返回0
     *
     * @return 服务器配置的端口
     */
    int getConfigServerPort();

    /**
     * 返回服务器配置的服务 ip,如果没有配置则返回127.0.0.1
     *
     * @return 服务器配置的 ip
     */
    String getConfigServerIp();

    /**
     * 获取仿真器HTTP服务返回编码
     *
     * @return 仿真器HTTP服务返回编码
     */
    String getServerCharset();

    /**
     * 获取agentId
     *
     * @return 返回agentId
     */
    String getAgentId();

    /**
     * 获取应用名称
     *
     * @return 返回应用名称
     */
    String getAppName();

    /**
     * 获取 zk 地址
     *
     * @return
     */
    String getZkServers();

    /**
     * 获取心跳节点路径
     *
     * @return
     */
    String getHeartbeatPath();

    /**
     * 获取 zk 连接超时时间
     *
     * @return
     */
    int getZkConnectionTimeout();

    /**
     * 获取 zk session 超时时间
     *
     * @return
     */
    int getZkSessionTimeout();

    /**
     * 获取配置
     *
     * @param key 配置key
     * @return 返回配置值
     */
    String getProperty(String key);

    /**
     * 获取配置
     *
     * @param key          配置key
     * @param defaultValue 配置默认值
     * @return 返回配置值
     */
    String getProperty(String key, String defaultValue);

    /**
     * 获取int类型配置
     *
     * @param key          配置key
     * @param defaultValue 配置默认值
     * @return 返回配置值
     */
    Integer getIntProperty(String key, Integer defaultValue);

    /**
     * 获取int类型配置
     *
     * @param key 配置key
     * @return 返回配置值
     */
    Integer getIntProperty(String key);

    /**
     * 获取long类型配置
     *
     * @param key          配置key
     * @param defaultValue 配置值
     * @return 返回配置值
     */
    Long getLongProperty(String key, Long defaultValue);

    /**
     * 获取long类型配置
     *
     * @param key 配置key
     * @return 返回配置值
     */
    Long getLongProperty(String key);

    /**
     * 获取boolean类型配置
     *
     * @param key          配置key
     * @param defaultValue 默认值
     * @return 返回配置值
     */
    Boolean getBooleanProperty(String key, Boolean defaultValue);

    /**
     * 获取boolean类型配置
     *
     * @param key 配置key
     * @return 返回配置值
     */
    Boolean getBooleanProperty(String key);

    /**
     * 根据 key 获取 List 配置，值的多个配置中间以逗号分隔
     *
     * @param key 键
     * @return List
     */
    List<String> getListProperty(String key);

    /**
     * 根据 key 获取 List 配置，值的多个配置中间以逗号分隔
     *
     * @param key          键
     * @param defaultValue 默认值，如果不存在此 key 则返回此默认值
     * @return List
     */
    List<String> getListProperty(String key, List<String> defaultValue);

    /**
     * 根据 key 获取 List 配置，值的多个配置中间以逗号分隔
     *
     * @param key       键
     * @param separator 分隔符
     * @return List
     */
    List<String> getListProperty(String key, String separator);

    /**
     * 根据 key 获取 List 配置，值的多个配置中间以逗号分隔
     *
     * @param key          键
     * @param separator    分隔符
     * @param defaultValue 默认值，如果不存在此 key 则返回此默认值
     * @return List
     */
    List<String> getListProperty(String key, String separator, List<String> defaultValue);

    /**
     * 获取禁用的模块列表
     *
     * @return
     */
    List<String> getDisabledModules();

    /**
     * 获取以某开头的所有key,如果该 key 是以*开头的，则直接匹配
     *
     * @param prefix key 的前缀
     * @return 返回所有以某个前缀开头的 key
     */
    Set<String> getKeys(String prefix);

    /**
     * 获取仿真器版本号
     *
     * @return 仿真器版本号
     */
    String getSimulatorVersion();

    /**
     * 获取 agent 版本号
     *
     * @return
     */
    String getAgentVersion();

    /**
     * 返回Instrumentation
     *
     * @return Instrumentation
     */
    Instrumentation getInstrumentation();

    /**
     * 获取日志打印的目录
     *
     * @return 日志目录
     */
    String getLogPath();

    /**
     * 获取日志打印的级别
     *
     * @return 日志级别
     */
    String getLogLevel();
}
