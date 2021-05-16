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
package com.shulie.instrument.simulator.agent.core.config;

import com.shulie.instrument.simulator.agent.spi.config.AgentConfig;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/11/17 8:29 下午
 */
public class AgentConfigImpl implements AgentConfig {
    private CoreConfig coreConfig;
    private final String version;

    public AgentConfigImpl(CoreConfig coreConfig) {
        this.coreConfig = coreConfig;
        this.version = getAgentVersion0();
    }

    @Override
    public boolean getBooleanProperty(String propertyName, boolean defaultValue) {
        return coreConfig.getBooleanProperty(propertyName, defaultValue);
    }

    @Override
    public int getIntProperty(String propertyName, int defaultValue) {
        return coreConfig.getIntProperty(propertyName, defaultValue);
    }

    @Override
    public long getLongProperty(String propertyName, long defaultValue) {
        return coreConfig.getLongProperty(propertyName, defaultValue);
    }

    @Override
    public String getProperty(String propertyName, String defaultValue) {
        return coreConfig.getProperty(propertyName, defaultValue);
    }

    @Override
    public String getAgentHome() {
        return coreConfig.getAgentHome();
    }

    @Override
    public String getConfigFilePath() {
        return coreConfig.getConfigFilePath();
    }

    @Override
    public String getSpiFilePath() {
        return coreConfig.getProviderFilePath();
    }

    @Override
    public String getSimulatorHome() {
        return coreConfig.getSimulatorHome();
    }

    @Override
    public String getAgentJarPath() {
        return coreConfig.getSimulatorJarPath();
    }

    @Override
    public String getNamespace() {
        return coreConfig.getNamespace();
    }

    @Override
    public String getToken() {
        return coreConfig.getToken();
    }

    @Override
    public String getZkServers() {
        return coreConfig.getZkServers();
    }

    @Override
    public String getRegisterPath() {
        return coreConfig.getRegisterPath();
    }

    @Override
    public int getZkConnectionTimeout() {
        return coreConfig.getZkConnectionTimeout();
    }

    @Override
    public int getZkSessionTimeout() {
        return coreConfig.getZkSessionTimeout();
    }

    @Override
    public String getAppName() {
        return coreConfig.getAppName();
    }

    @Override
    public String getAgentId() {
        return coreConfig.getAgentId();
    }

    @Override
    public String getUserAppKey() {
        return coreConfig.getUserAppKey();
    }

    @Override
    public String getAgentResultFilePath() {
        return coreConfig.getAgentResultFilePath();
    }

    @Override
    public InputStream getLogConfigFile() {
        return coreConfig.getLogConfigFile();
    }

    @Override
    public boolean isAgentProcessListUploadSupported() {
        String uploadAgentProcesslistUrl = coreConfig.getProperty("agent.processlist.url", null);
        return uploadAgentProcesslistUrl != null;
    }

    @Override
    public String getUploadAgentProcesslistUrl() {
        return coreConfig.getProperty("agent.processlist.url", null);
    }

    @Override
    public long getAttachId() {
        return coreConfig.getAttachId();
    }

    @Override
    public String getAttachName() {
        return coreConfig.getAttachName();
    }

    @Override
    public String getLogPath() {
        return coreConfig.getLogPath();
    }

    @Override
    public String getLogLevel() {
        return coreConfig.getLogLevel();
    }

    @Override
    public String getAgentVersion() {
        return version;
    }

    /**
     * 获取 agent版本号
     *
     * @return
     */
    public String getAgentVersion0() {
        final InputStream is = getClass().getClassLoader().getResourceAsStream("com/shulie/instrument/simulator/agent/version");
        try {
            return IOUtils.toString(is);
        } catch (IOException e) {
            // impossible
            return "UNKNOW_VERSION";
        } finally {
            IOUtils.closeQuietly(is);
        }
    }
}
