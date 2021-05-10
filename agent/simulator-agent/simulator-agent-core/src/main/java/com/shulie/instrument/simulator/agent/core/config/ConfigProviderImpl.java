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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.shulie.instrument.simulator.agent.api.ConfigProvider;
import com.shulie.instrument.simulator.agent.api.model.AppConfig;
import com.shulie.instrument.simulator.agent.api.model.ModuleConfig;
import com.shulie.instrument.simulator.agent.api.model.Result;
import com.shulie.instrument.simulator.agent.core.util.ConfigUtils;
import com.shulie.instrument.simulator.agent.core.util.DownloadUtils;
import com.shulie.instrument.simulator.agent.spi.config.AgentConfig;
import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/11/23 7:21 下午
 */
public class ConfigProviderImpl implements ConfigProvider {
    private final static Logger logger = LoggerFactory.getLogger(ConfigProviderImpl.class);

    private AgentConfig agentConfig;
    private AtomicBoolean isWarnAlready;

    public ConfigProviderImpl(AgentConfig agentConfig) {
        this.agentConfig = agentConfig;
        isWarnAlready = new AtomicBoolean(false);
    }

    @Override
    public File downloadAgent(String downloadPath) {
        String agentDownloadUrl = agentConfig.getProperty("agent.load.url", null);
        if (StringUtils.isNotBlank(agentDownloadUrl)) {
            if (StringUtils.indexOf(agentDownloadUrl, '?') != -1) {
                agentDownloadUrl += "&appName=" + agentConfig.getAppName();
            } else {
                agentDownloadUrl += "?appName=" + agentConfig.getAppName();
            }
            return DownloadUtils.download(agentDownloadUrl, downloadPath);
        }
        return null;
    }

    @Override
    public File downloadModule(String moduleId, String moduleVersion, String downloadPath) {
        String agentDownloadUrl = agentConfig.getProperty("agent.load.url", null);
        if (StringUtils.isNotBlank(agentDownloadUrl)) {
            if (StringUtils.indexOf(agentDownloadUrl, '?') != -1) {
                agentDownloadUrl += "&appName=" + agentConfig.getAppName() + "&moduleId=" + moduleId + "&moduleVersion=" + moduleVersion;
            } else {
                agentDownloadUrl += "?appName=" + agentConfig.getAppName() + "&moduleId=" + moduleId + "&moduleVersion=" + moduleVersion;
            }
            return DownloadUtils.download(agentDownloadUrl, downloadPath);
        }
        return null;
    }

    @Override
    public AppConfig getAppConfig() throws RuntimeException {
        if (logger.isInfoEnabled()) {
            logger.info("prepare to get app config.");
        }
        String agentConfigUrl = agentConfig.getProperty("agent.config.url", null);
        if (StringUtils.isBlank(agentConfigUrl)) {
            if (isWarnAlready.compareAndSet(false, true)) {
                logger.warn("AGENT: agent.config.url is not assigned.");
            }
            /**
             * 如果没有配置则给一个默认值
             */
            AppConfig appConfig = new AppConfig();
            appConfig.setRunning(true);
            appConfig.setAgentVersion("1.0.0");
            appConfig.setModuleConfigs(new HashMap<String, ModuleConfig>());
            return appConfig;
        }
        String url = agentConfigUrl;
        if (StringUtils.indexOf(url, '?') != -1) {
            url += "&appName=" + agentConfig.getAppName() + "&agentId=" + agentConfig.getAgentId();
        } else {
            url += "?appName=" + agentConfig.getAppName() + "&agentId=" + agentConfig.getAgentId();
        }

        String resp = ConfigUtils.doConfig(url, agentConfig.getUserAppKey());
        if (StringUtils.isBlank(resp)) {
            logger.error("AGENT: fetch agent config got a err response. {}", url);
            throw new RuntimeException("fetch agent config got a err response. " + url);
        }

        try {
            Type type = new TypeReference<Result<AppConfig>>() {
            }.getType();
            Result<AppConfig> response = JSON.parseObject(resp, type);
            if (!response.isSuccess()) {
                logger.error("fetch agent config got a fault response. resp={}", resp);
                throw new RuntimeException(response.getErrorMsg());
            }
            if (logger.isInfoEnabled()) {
                logger.info("load agent lib successful.");
            }
            return response.getResult();
        } catch (Throwable e) {
            logger.error("AGENT: parse app config err. {}", resp, e);
            throw new RuntimeException("AGENT: parse app config err.. " + resp, e);
        }
    }

    @Override
    public List<String> getAgentProcessList() {
        String url = agentConfig.getUploadAgentProcesslistUrl();
        if (StringUtils.isBlank(url)) {
            return Collections.EMPTY_LIST;
        }

        List<String> processlist = new ArrayList<String>();
        final List<VirtualMachineDescriptor> list = VirtualMachine.list();
        for (VirtualMachineDescriptor descriptor : list) {
            processlist.add(descriptor.displayName());
        }
        return processlist;
    }
}
