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
import com.shulie.instrument.simulator.core.manager.impl.LocalModuleLoader;
import com.shulie.instrument.simulator.core.manager.impl.RemoteModuleLoader;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/10/27 3:39 下午
 */
public class ModuleLoaders {
    private final static Map<ModuleRepositoryMode, ModuleLoader> caches = new HashMap<ModuleRepositoryMode, ModuleLoader>();

    static {
        caches.put(ModuleRepositoryMode.LOCAL, new LocalModuleLoader());
        /**
         * use simulator-agent instead of remote
         * simulator-agent managed lifecycle of agent and module
         */
        caches.put(ModuleRepositoryMode.REMOTE, new RemoteModuleLoader());
    }


    public static ModuleLoader getModuleLoader(ModuleRepositoryMode moduleRepositoryMode) {
        return caches.get(moduleRepositoryMode);
    }
}
