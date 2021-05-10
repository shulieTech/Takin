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


import com.shulie.instrument.simulator.agent.module.exception.ModuleException;
import com.shulie.instrument.simulator.agent.module.utils.RedefineModuleUtils;

import java.lang.instrument.Instrumentation;
import java.lang.module.ModuleDescriptor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Java9 模块模型
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/12/9 7:54 下午
 */
public class Java9Module implements JavaModule {

    private final Instrumentation instrumentation;
    private final Module module;

    public Java9Module(Instrumentation instrumentation, Module module) {
        if (instrumentation == null) {
            throw new NullPointerException("instrumentation");
        }
        this.instrumentation = instrumentation;
        this.module = module;
    }

    @Override
    public boolean isSupported() {
        return true;
    }

    @Override
    public boolean isNamed() {
        return this.module.isNamed();
    }

    @Override
    public String getName() {
        return this.module.getName();

    }

    @Override
    public List<Providers> getProviders() {
        List<Providers> result = new ArrayList<>();
        Set<ModuleDescriptor.Provides> providesSet = this.module.getDescriptor().provides();
        for (ModuleDescriptor.Provides provides : providesSet) {
            String service = provides.service();
            List<String> providers = provides.providers();
            Providers newProviders = new Providers(service, providers);
            result.add(newProviders);
        }
        return result;
    }

    @Override
    public void addReads(JavaModule targetJavaModule) {
        final Java9Module target = checkJavaModule(targetJavaModule);

        final Set<Module> readModules = Set.of(target.module);
        RedefineModuleUtils.addReads(instrumentation, module, readModules);
    }

    @Override
    public void addExports(String packageName, JavaModule targetJavaModule) {
        if (packageName == null) {
            throw new NullPointerException("packageName");
        }
        final Java9Module target = checkJavaModule(targetJavaModule);

        final Map<String, Set<Module>> extraModules = Map.of(packageName, Set.of(target.module));
        RedefineModuleUtils.addExports(instrumentation, module, extraModules);
    }

    private Java9Module checkJavaModule(JavaModule targetJavaModule) {
        if (targetJavaModule == null) {
            throw new NullPointerException("targetJavaModule");
        }
        if (targetJavaModule instanceof Java9Module) {
            return (Java9Module) targetJavaModule;
        }
        throw new ModuleException("invalid JavaModule: " + targetJavaModule.getClass());
    }

    @Override
    public void addOpens(String packageName, JavaModule javaModule) {
        if (packageName == null) {
            throw new NullPointerException("packageName");
        }
        final Java9Module target = checkJavaModule(javaModule);

        final Map<String, Set<Module>> extraOpens = Map.of(packageName, Set.of(target.module));
        RedefineModuleUtils.addOpens(instrumentation, module, extraOpens);
    }


    @Override
    public void addUses(Class<?> target) {
        if (target == null) {
            throw new NullPointerException("target");
        }
        final Set<Class<?>> extraUses = Set.of(target);
        RedefineModuleUtils.addUses(instrumentation, module, extraUses);
    }

    @Override
    public void addProvides(Class<?> service, List<Class<?>> providerList) {
        if (service == null) {
            throw new NullPointerException("target");
        }

        if (providerList == null) {
            throw new NullPointerException("list");
        }

        final Map<Class<?>, List<Class<?>>> extraProvides = Map.of(service, providerList);
        RedefineModuleUtils.addProvides(instrumentation, module, extraProvides);
    }

    @Override
    public boolean isExported(String packageName, JavaModule targetJavaModule) {
        if (packageName == null) {
            throw new NullPointerException("packageName");
        }
        final Java9Module target = checkJavaModule(targetJavaModule);
        return module.isExported(packageName, target.module);
    }

    @Override
    public boolean isOpen(String packageName, JavaModule targetJavaModule) {
        if (packageName == null) {
            throw new NullPointerException("packageName");
        }
        final Java9Module target = checkJavaModule(targetJavaModule);
        return module.isOpen(packageName, target.module);
    }

    @Override
    public boolean canRead(JavaModule targetJavaModule) {
        final Java9Module target = checkJavaModule(targetJavaModule);
        return this.module.canRead(target.module);
    }

    @Override
    public boolean canRead(Class<?> targetClazz) {
        return this.module.canUse(targetClazz);
    }


    @Override
    public ClassLoader getClassLoader() {
        return module.getClassLoader();
    }

    @Override
    public Set<String> getPackages() {
        return module.getPackages();
    }

    @Override
    public String toString() {
        return module.toString();
    }
}

