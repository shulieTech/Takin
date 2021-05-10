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
package com.shulie.instrument.simulator.agent.api;

import com.shulie.instrument.simulator.agent.api.model.AppConfig;

import java.io.File;
import java.util.List;

/**
 * 配置提供者，所有与配置中心交互的全部由此类完成
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/11/23 7:13 下午
 */
public interface ConfigProvider {
    /**
     * 下载 agent包
     *
     * @param downloadPath 下载后存放的目录
     * @return 返回 agent 包的文件
     */
    File downloadAgent(String downloadPath);

    /**
     * 下载模块包
     *
     * @param moduleId      模块 ID
     * @param moduleVersion 模块版本
     * @param downloadPath  下载存放路径
     * @return
     */
    File downloadModule(String moduleId, String moduleVersion, String downloadPath);

    /**
     * 获取 app 配置
     *
     * @return
     * @throws RuntimeException 如果获取不到配置或者解析配置失败，则会抛出 RuntimeException
     */
    AppConfig getAppConfig() throws RuntimeException;

    /**
     * 获取 agent 扫描的进程名称列表
     *
     * @return 返回 agent 扫描的所有进程列表
     */
    List<String> getAgentProcessList();
}
