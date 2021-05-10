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

import com.shulie.instrument.simulator.api.ExtensionModule;
import com.shulie.instrument.simulator.api.ModuleException;
import com.shulie.instrument.simulator.api.ModuleInfo;
import com.shulie.instrument.simulator.api.ModuleSpec;
import com.shulie.instrument.simulator.api.resource.ModuleManager;
import com.shulie.instrument.simulator.core.CoreModule;
import com.shulie.instrument.simulator.core.manager.CoreModuleManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * 默认ModuleManager实现
 */
public class DefaultModuleManager implements ModuleManager {

    private final CoreModuleManager coreModuleManager;

    public DefaultModuleManager(CoreModuleManager coreModuleManager) {
        this.coreModuleManager = coreModuleManager;
    }

    @Override
    public void flush(boolean isForce) throws ModuleException {
        coreModuleManager.flush(isForce);
    }

    @Override
    public void reset() throws ModuleException {
        coreModuleManager.reset();
    }

    @Override
    public void unload(String uniqueId) throws ModuleException {
        final CoreModule coreModule = coreModuleManager.getThrowsExceptionIfNull(uniqueId);
        coreModuleManager.unload(coreModule, false);
    }

    @Override
    public void load(File file) throws ModuleException {
        coreModuleManager.load(file);
    }

    @Override
    public void active(String uniqueId) throws ModuleException {
        final CoreModule coreModule = coreModuleManager.getThrowsExceptionIfNull(uniqueId);
        coreModuleManager.active(coreModule);
    }

    @Override
    public void frozen(String uniqueId) throws ModuleException {
        final CoreModule coreModule = coreModuleManager.getThrowsExceptionIfNull(uniqueId);
        coreModuleManager.frozen(coreModule, false);
    }

    @Override
    public Collection<ExtensionModule> list() {
        final Collection<ExtensionModule> modules = new ArrayList<ExtensionModule>();
        for (final CoreModule coreModule : coreModuleManager.list()) {
            modules.add(coreModule.getModule());
        }
        return modules;
    }

    @Override
    public Collection<ModuleSpec> listModuleSpecs() {
        Set<ModuleSpec> set = new HashSet<ModuleSpec>();
        Collection<ExtensionModule> modules = list();
        for (ExtensionModule module : modules) {
            final ModuleInfo info = module.getClass().getAnnotation(ModuleInfo.class);
            ModuleSpec moduleSpec = getModuleSpec(info.id());
            if (moduleSpec != null) {
                set.add(moduleSpec);
            }

        }

        return set;
    }

    @Override
    public ModuleSpec getModuleSpec(String moduleId) {
        CoreModule coreModule = coreModuleManager.get(moduleId);
        if (coreModule == null) {
            return null;
        }
        return coreModule.getModuleSpec();
    }

    @Override
    public ExtensionModule get(String uniqueId) {
        final CoreModule coreModule = coreModuleManager.get(uniqueId);
        return null == coreModule
                ? null
                : coreModule.getModule();
    }

    @Override
    public int getEffectClassCount(String uniqueId) throws ModuleException {
        return coreModuleManager.getThrowsExceptionIfNull(uniqueId).getEffectClassCount();
    }

    @Override
    public int getEffectMethodCount(String uniqueId) throws ModuleException {
        return coreModuleManager.getThrowsExceptionIfNull(uniqueId).getEffectMethodCount();
    }

    @Override
    public boolean isActivated(String uniqueId) throws ModuleException {
        return coreModuleManager.getThrowsExceptionIfNull(uniqueId).isActivated();
    }

    @Override
    public boolean isLoaded(String uniqueId) throws ModuleException {
        return coreModuleManager.getThrowsExceptionIfNull(uniqueId).isLoaded();
    }

    @Override
    public File getJarFile(String uniqueId) throws ModuleException {
        return coreModuleManager.getThrowsExceptionIfNull(uniqueId).getJarFile();
    }

}
