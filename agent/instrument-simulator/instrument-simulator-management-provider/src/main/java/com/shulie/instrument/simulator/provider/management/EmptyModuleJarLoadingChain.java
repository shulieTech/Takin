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
package com.shulie.instrument.simulator.provider.management;

import com.shulie.instrument.simulator.api.ModuleSpec;
import com.shulie.instrument.simulator.api.resource.SimulatorConfig;
import com.shulie.instrument.simulator.spi.ModuleJarLoadingChain;
import org.kohsuke.MetaInfServices;

import java.io.File;

/**
 * 空实现
 */
@MetaInfServices(ModuleJarLoadingChain.class)
public class EmptyModuleJarLoadingChain implements ModuleJarLoadingChain {

    @Override
    public void loading(final SimulatorConfig simulatorConfig, final ModuleSpec moduleSpec, final File moduleJarFile) throws Throwable {

    }

}
