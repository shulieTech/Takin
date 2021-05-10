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

import com.shulie.instrument.simulator.api.CommandResponse;

import java.util.Map;

/**
 * 模块命令调用者
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/11/3 3:27 下午
 */
public interface ModuleCommandInvoker {
    /**
     * 带参数的模块命令调用
     *
     * @param uniqueId 模块 ID
     * @param command  命令名称
     * @param args     参数
     * @return
     */
    <T> CommandResponse<T> invokeCommand(String uniqueId, String command, final Map<String, String> args);

    /**
     * 模块调用命令，不带参数
     * @param uniqueId
     * @param command
     * @param <T>
     * @return
     */
    <T> CommandResponse<T> invokeCommand(String uniqueId, String command);
}
