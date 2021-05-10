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
package com.pamirs.attach.plugin.common.web;

import com.shulie.instrument.simulator.api.ExtensionModule;
import com.shulie.instrument.simulator.api.ModuleInfo;
import com.shulie.instrument.simulator.api.ModuleLifecycleAdapter;
import org.kohsuke.MetaInfServices;

/**
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/10/1 1:19 上午
 */
@MetaInfServices(ExtensionModule.class)
@ModuleInfo(id = "servlet-common", version = "1.0.0", author = "xiaobin@shulie.io",description = "servlet 通用模式，提供给其他的 web 容器模块依赖")
public class ServletCommonModule extends ModuleLifecycleAdapter implements ExtensionModule {

    @Override
    public void onActive() throws Throwable {
        //将simulator home路径和plugin相关的配置全部导入到system property中
        String home = simulatorConfig.getSimulatorHome();
        if (home != null) {
            System.setProperty("simulator.home", home);
        }
        Integer requestSize = simulatorConfig.getIntProperty("plugin.request.size");
        if (requestSize != null) {
            System.setProperty("plugin.request.size", String.valueOf(requestSize));
        }

        Integer responseSize = simulatorConfig.getIntProperty("plugin.response.size");
        if (responseSize != null) {
            System.setProperty("plugin.response.size", String.valueOf(responseSize));
        }

        Boolean requestOn = simulatorConfig.getBooleanProperty("plugin.request.on");
        if (requestOn != null) {
            System.setProperty("plugin.request.on", String.valueOf(requestOn));
        }

        Boolean responseOn = simulatorConfig.getBooleanProperty("plugin.response.on");
        if (responseOn != null) {
            System.setProperty("plugin.response.on", String.valueOf(responseOn));
        }
    }
}
