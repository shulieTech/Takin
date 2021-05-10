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
package com.shulie.instrument.simulator.agent.module;

import java.lang.instrument.Instrumentation;
import java.net.URL;

/**
 * java模块启动加载器
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/12/10 2:55 下午
 */
public class ModuleBootLoader {

    private final Instrumentation instrumentation;

    private ModuleSupport moduleSupport;

    public ModuleBootLoader(Instrumentation instrumentation) {
        this.instrumentation = instrumentation;
    }

    /**
     * 加载模块支持
     */
    public void loadModuleSupport() {
        try {
            ModuleSupportFactory factory = new ModuleSupportFactory();
            this.moduleSupport = factory.newModuleSupport(instrumentation);
            this.moduleSupport.init();
        } catch (Exception e) {
            throw new IllegalStateException("moduleSupport load fail Caused by:" + e.getMessage(), e);
        }
    }

    /**
     * 定义 agent 模块
     *
     * @param classLoader agent 类加载器
     * @param jarFileList jar 列表
     */
    public void defineAgentModule(ClassLoader classLoader, URL[] jarFileList) {
        if (moduleSupport == null) {
            throw new IllegalStateException("moduleSupport not loaded");
        }
        try {
            this.moduleSupport.defineAgentModule(classLoader, jarFileList);
        } catch (Exception ex) {
            throw new IllegalStateException("defineAgentPackage fail: Caused by:" + ex.getMessage(), ex);
        }
    }
}
