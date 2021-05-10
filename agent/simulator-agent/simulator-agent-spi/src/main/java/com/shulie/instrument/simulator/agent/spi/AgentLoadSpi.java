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
package com.shulie.instrument.simulator.agent.spi;

import com.shulie.instrument.simulator.agent.spi.config.AgentConfig;

/**
 * agent 加载的 spi
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/11/16 8:53 下午
 */
public interface AgentLoadSpi {
    /**
     * 加载 agent lib
     *
     * @param agentConfig agent 配置
     */
    void loadAgentLib(AgentConfig agentConfig);

    /**
     * 添加执行器, 执行器会触发命令的执行
     * 目前支持的命令有 Start/Stop
     *
     * @param agentConfig     agent 配置
     * @param commandExecutor 执行器
     */
    void addCommandExecutor(AgentConfig agentConfig, CommandExecutor commandExecutor);
}
