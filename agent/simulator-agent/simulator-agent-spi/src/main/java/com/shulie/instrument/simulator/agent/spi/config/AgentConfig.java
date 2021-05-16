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
package com.shulie.instrument.simulator.agent.spi.config;


import java.io.InputStream;

/**
 * agent 配置
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/11/17 8:09 下午
 */
public interface AgentConfig {

    /**
     * 获取 boolean类型的属性
     *
     * @param propertyName 属性名称
     * @param defaultValue 默认值
     * @return property value
     */
    boolean getBooleanProperty(String propertyName, boolean defaultValue);

    /**
     * 获取 int 类型的属性
     *
     * @param propertyName 属性名称
     * @param defaultValue 默认值
     * @return property value
     */
    int getIntProperty(String propertyName, int defaultValue);

    /**
     * 获取 long 类型的属性
     *
     * @param propertyName 属性名称
     * @param defaultValue 默认值
     * @return property value
     */
    long getLongProperty(String propertyName, long defaultValue);

    /**
     * 获取属性配置值
     *
     * @param propertyName 属性名称
     * @param defaultValue 默认值
     * @return property value
     */
    String getProperty(String propertyName, String defaultValue);

    /**
     * 获取 agent home 目录地址
     *
     * @return agent home
     */
    String getAgentHome();

    /**
     * 获取配置文件路径
     *
     * @return config file path
     */
    String getConfigFilePath();

    /**
     * 获取 spi 目录路径
     *
     * @return spi file path
     */
    String getSpiFilePath();

    /**
     * 获取 agent 目录路径
     *
     * @return agent file path
     */
    String getSimulatorHome();

    /**
     * 获取 agent jar 路径
     *
     * @return agent jar path
     */
    String getAgentJarPath();

    /**
     * 获取 namespace
     *
     * @return namespace
     */
    String getNamespace();

    /**
     * 获取 token
     *
     * @return token
     */
    String getToken();

    /**
     * 获取 zk 地址
     *
     * @return
     */
    String getZkServers();

    /**
     * 获取 zk 注册路径
     *
     * @return
     */
    String getRegisterPath();

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
     * 获取 app 名称
     *
     * @return app 名称
     */
    String getAppName();

    /**
     * 获取 agentId
     *
     * @return agentId
     */
    String getAgentId();

    /**
     * 获取 user app key
     *
     * @return
     */
    String getUserAppKey();

    /**
     * 获取 agent结果文件路径
     *
     * @return agent 结果文件路径
     */
    String getAgentResultFilePath();

    /**
     * 获取 log 配置文件路径
     *
     * @return
     */
    InputStream getLogConfigFile();

    /**
     * 是否支持 agent扫描进程列表的上报
     *
     * @return true/false
     */
    boolean isAgentProcessListUploadSupported();

    /**
     * 返回上传 agent 扫描的进程名称列表 url
     *
     * @return
     */
    String getUploadAgentProcesslistUrl();

    /**
     * 获取 attachId
     *
     * @return attach id -> pid
     */
    long getAttachId();

    /**
     * 获取 attach 的进程名称
     *
     * @return attach 进程名称
     */
    String getAttachName();

    /**
     * 获取日志目录
     *
     * @return 日志目录
     */
    String getLogPath();

    /**
     * 获取日志级别
     *
     * @return 日志级别
     */
    String getLogLevel();

    /**
     * 获取 agent 版本号
     *
     * @return
     */
    String getAgentVersion();
}
