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
package com.shulie.instrument.simulator.core.manager.impl;

import com.shulie.instrument.simulator.api.LoadMode;
import com.shulie.instrument.simulator.api.ModuleRepositoryMode;
import com.shulie.instrument.simulator.api.resource.SimulatorConfig;
import com.shulie.instrument.simulator.core.CoreConfigure;
import com.shulie.instrument.simulator.core.server.ProxyCoreServer;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.instrument.Instrumentation;
import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 默认配置信息实现
 */
class DefaultSimulatorConfig implements SimulatorConfig {

    private final CoreConfigure config;
    private final String version;

    public DefaultSimulatorConfig(CoreConfigure config) {
        this.config = config;
        this.version = getVersion0();
    }

    @Override
    public String getNamespace() {
        return config.getNamespace();
    }

    @Override
    public LoadMode getMode() {
        return config.getLaunchMode();
    }

    @Override
    public ModuleRepositoryMode getModuleRepositoryMode() {
        return config.getModuleRepositoryMode();
    }

    @Override
    public boolean isEnableUnsafe() {
        return config.isEnableUnsafe();
    }

    @Override
    public String getSimulatorHome() {
        return config.getSimulatorHome();
    }

    @Override
    public String getSimulatorMd5() {
        return config.getSimulatorMd5();
    }

    @Override
    public String getSystemModuleLibPath() {
        return config.getSystemModuleLibPath();
    }

    @Override
    public String getSystemProviderLibPath() {
        return config.getProviderLibPath();
    }

    @Override
    public String[] getUserModuleLibPaths() {
        return config.getUserModuleLibPaths();
    }

    @Override
    public Map<String, List<File>> getBizClassLoaderInjectFiles() {
        return config.getBizClassLoaderInjectFiles();
    }

    @Override
    public Map<String, List<String>> getBizClassLoaderURLExternalForms() {
        return config.getBizClassLoaderURLExternalForms();
    }

    @Override
    public InetSocketAddress getServerAddress() {
        try {
            return ProxyCoreServer.getInstance().getLocal();
        } catch (Throwable cause) {
            return new InetSocketAddress("0.0.0.0", 0);
        }
    }

    @Override
    public int getConfigServerPort() {
        return config.getServerPort();
    }

    @Override
    public String getConfigServerIp() {
        return config.getServerIp();
    }

    @Override
    public String getServerCharset() {
        return config.getServerCharset().name();
    }

    @Override
    public String getAgentId() {
        return config.getAgentId();
    }

    @Override
    public String getAppName() {
        return config.getAppName();
    }

    @Override
    public String getZkServers() {
        return config.getZkServers();
    }

    @Override
    public String getHeartbeatPath() {
        return config.getHeartbeatPath();
    }

    @Override
    public int getZkConnectionTimeout() {
        return config.getZkConnectionTimeout();
    }

    @Override
    public int getZkSessionTimeout() {
        return config.getZkSessionTimeout();
    }

    /**
     * 获取服务配置
     *
     * @param key 配置key
     * @return 返回配置值
     */
    @Override
    public String getProperty(String key) {
        return config.getProperty(key);
    }

    /**
     * 获取服务配置
     *
     * @param key 配置key
     * @return 返回配置值
     */
    @Override
    public String getProperty(String key, String defaultValue) {
        return config.getProperty(key, defaultValue);
    }

    /**
     * 获取int配置
     *
     * @param key 配置key
     * @return 返回配置值
     */
    @Override
    public Integer getIntProperty(String key, Integer defaultValue) {
        return config.getIntProperty(key, defaultValue);
    }

    /**
     * 获取int配置
     *
     * @param key 配置key
     * @return 返回配置值
     */
    @Override
    public Integer getIntProperty(String key) {
        return config.getIntProperty(key);
    }

    /**
     * 获取long配置
     *
     * @param key 配置key
     * @return 返回配置值
     */
    @Override
    public Long getLongProperty(String key, Long defaultValue) {
        return config.getLongProperty(key, defaultValue);
    }

    /**
     * 获取int配置
     *
     * @param key 配置key
     * @return 返回配置值
     */
    @Override
    public Long getLongProperty(String key) {
        return config.getLongProperty(key);
    }

    /**
     * 获取long配置
     *
     * @param key
     * @return
     */
    @Override
    public Boolean getBooleanProperty(String key, Boolean defaultValue) {
        return config.getBooleanProperty(key, defaultValue);
    }

    /**
     * 获取int配置
     *
     * @param key
     * @return
     */
    @Override
    public Boolean getBooleanProperty(String key) {
        return config.getBooleanProperty(key);
    }

    /**
     * 获取 List 配置
     *
     * @param key       键
     * @param separator 分隔符
     * @return
     */
    @Override
    public List<String> getListProperty(String key, String separator) {
        return config.getListProperty(key, separator);
    }

    /**
     * 获取 List 配置
     *
     * @param key          键
     * @param separator    分隔符
     * @param defaultValue 默认值，如果不存在此 key 则返回此默认值
     * @return
     */
    @Override
    public List<String> getListProperty(String key, String separator, List<String> defaultValue) {
        return config.getListProperty(key, separator, defaultValue);
    }

    /**
     * 获取 List 配置
     *
     * @param key 键
     * @return
     */
    @Override
    public List<String> getListProperty(String key) {
        return config.getListProperty(key);
    }

    /**
     * 获取 List 配置
     *
     * @param key          键
     * @param defaultValue 默认值，如果不存在此 key 则返回此默认值
     * @return
     */
    @Override
    public List<String> getListProperty(String key, List<String> defaultValue) {
        return config.getListProperty(key, defaultValue);
    }

    /**
     * 获取所有禁用的模块列表
     *
     * @return
     */
    @Override
    public List<String> getDisabledModules() {
        return config.getDisabledModules();
    }

    /**
     * 获取所有匹配的 key
     *
     * @param prefix key 的前缀
     * @return
     */
    @Override
    public Set<String> getKeys(String prefix) {
        Set<String> result = new HashSet<String>();
        if ("*".equals(prefix)) {
            result.addAll(config.keys());
        } else {
            for (String key : config.keys()) {
                if (StringUtils.startsWith(key, prefix)) {
                    result.add(key);
                }
            }
        }
        return result;
    }

    /**
     * 获取仿真器的版本号
     *
     * @return
     */
    @Override
    public String getSimulatorVersion() {
        return config.getSimulatorVersion();
    }

    @Override
    public String getAgentVersion() {
        return config.getAgentVersion();
    }

    /**
     * 读取仿真器版本号
     *
     * @return
     */
    public String getVersion0() {
        final InputStream is = getClass().getClassLoader().getResourceAsStream("com/shulie/instrument/simulator/version");
        try {
            return IOUtils.toString(is);
        } catch (IOException e) {
            // impossible
            return "UNKNOW_VERSION";
        } finally {
            IOUtils.closeQuietly(is);
        }
    }

    @Override
    public Instrumentation getInstrumentation() {
        return config.getInstrumentation();
    }

    @Override
    public String getLogPath() {
        return config.getLogPath();
    }

    @Override
    public String getLogLevel() {
        return config.getLogLevel();
    }
}
