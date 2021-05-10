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
import java.util.Arrays;
import java.util.List;

/**
 * ModuleSupport 工厂，用于构建ModuleSupport
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/12/9 8:23 下午
 */
public class ModuleSupportFactory {
    public ModuleSupportFactory() {
    }

    public ModuleSupport newModuleSupport(Instrumentation instrumentation) {
        List<String> allowedProviders = Arrays.asList(
                "io.grpc.NameResolverProvider",
                "io.grpc.ManagedChannelProvider",
                "com.shulie.instrument.simulator.api.ExtensionModule",
                "com.shulie.instrument.simulator.spi.ModuleJarLoadingChain",
                "com.shulie.instrument.simulator.spi.ModuleLoadingChain"
        );

        return new ModuleSupport(instrumentation, allowedProviders);
    }
}
