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
package com.shulie.instrument.simulator.agent.api.model;

import java.io.Serializable;
import java.util.Map;

/**
 * 应用配置
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/11/17 9:17 下午
 */
public class AppConfig implements Serializable {
    private final static long serialVersionUID = 1L;
    /**
     * agent 版本号
     */
    private String agentVersion;

    /**
     * 是否运行状态
     */
    private boolean isRunning;

    /**
     * 插件配置
     * moduleId: ModuleConfig
     */
    private Map<String/*moduleId*/, ModuleConfig> moduleConfigs;

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public String getAgentVersion() {
        return agentVersion;
    }

    public void setAgentVersion(String agentVersion) {
        this.agentVersion = agentVersion;
    }

    public Map<String, ModuleConfig> getModuleConfigs() {
        return moduleConfigs;
    }

    public void setModuleConfigs(Map<String, ModuleConfig> moduleConfigs) {
        this.moduleConfigs = moduleConfigs;
    }
}
