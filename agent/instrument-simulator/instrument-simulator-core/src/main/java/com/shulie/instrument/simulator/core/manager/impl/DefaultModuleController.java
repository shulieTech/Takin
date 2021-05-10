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
package com.shulie.instrument.simulator.core.manager.impl;

import com.shulie.instrument.simulator.api.ModuleException;
import com.shulie.instrument.simulator.api.resource.ModuleController;
import com.shulie.instrument.simulator.api.resource.ReleaseResource;
import com.shulie.instrument.simulator.core.CoreModule;
import com.shulie.instrument.simulator.core.manager.CoreModuleManager;

/**
 * 仿真器模块控制器
 */
class DefaultModuleController implements ModuleController {

    private final CoreModule coreModule;
    private final CoreModuleManager coreModuleManager;

    DefaultModuleController(final CoreModule coreModule,
                            final CoreModuleManager coreModuleManager) {
        this.coreModule = coreModule;
        this.coreModuleManager = coreModuleManager;
    }

    @Override
    public void active() throws ModuleException {
        coreModuleManager.active(coreModule);
    }

    @Override
    public void frozen() throws ModuleException {
        coreModuleManager.frozen(coreModule, false);
    }

    @Override
    public <T> void addReleaseResource(ReleaseResource<T> releaseResource) {
        this.coreModule.append(releaseResource);
    }
}
