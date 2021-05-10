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

import com.shulie.instrument.simulator.api.ExtensionModule;
import com.shulie.instrument.simulator.api.resource.SimulatorConfig;
import com.shulie.instrument.simulator.spi.ModuleLoadingChain;
import org.kohsuke.MetaInfServices;

import java.io.File;

@MetaInfServices(ModuleLoadingChain.class)
public class EmptyModuleLoadingChain implements ModuleLoadingChain {

    @Override
    public void loading(final SimulatorConfig simulatorConfig, final Class moduleClass, final ExtensionModule module, final File moduleJarFile, final ClassLoader moduleClassLoader) throws Throwable {

    }
}
