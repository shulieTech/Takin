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
package com.shulie.instrument.simulator.spi;

import com.shulie.instrument.simulator.api.resource.SimulatorConfig;

/**
 * 仿真器启动接口类
 * 仿真器的生命周期接口
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/11/16 9:54 上午
 */
public interface SimulatorLifecycle {
    /**
     * 启动时调用
     *
     * @param namespace
     * @param simulatorConfig
     */
    void onStart(String namespace, SimulatorConfig simulatorConfig);

    /**
     * 关闭时调用
     *
     * @param namespace
     * @param simulatorConfig
     */
    void onShutdown(String namespace, SimulatorConfig simulatorConfig);
}
