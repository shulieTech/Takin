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
package com.shulie.instrument.simulator.core.manager;

import com.shulie.instrument.simulator.api.ModuleRepositoryMode;

import java.io.File;

/**
 * 模块加载器，负责加载模块文件
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/10/27 3:35 下午
 */
public interface ModuleLoader {
    /**
     * 支持的模块类型
     *
     * @return
     */
    ModuleRepositoryMode supportMode();

    /**
     * 加载模块类库文件
     *
     * @param appName 应用名称
     * @param paths   路径
     * @return 返回模块类库文件列表
     */
    File[] loadModuleLibs(String appName, String[] paths);
}
