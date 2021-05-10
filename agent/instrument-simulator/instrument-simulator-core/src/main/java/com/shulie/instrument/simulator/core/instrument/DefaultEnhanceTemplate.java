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
package com.shulie.instrument.simulator.core.instrument;

import com.shulie.instrument.simulator.api.ExtensionModule;
import com.shulie.instrument.simulator.api.ModuleLifecycleAdapter;
import com.shulie.instrument.simulator.api.instrument.EnhanceCallback;
import com.shulie.instrument.simulator.api.instrument.EnhanceTemplate;
import com.shulie.instrument.simulator.api.instrument.InstrumentClass;
import com.shulie.instrument.simulator.api.listener.ext.EventWatchBuilder;
import com.shulie.instrument.simulator.api.listener.ext.IClassMatchBuilder;
import com.shulie.instrument.simulator.api.resource.InstrumentClassResource;
import com.shulie.instrument.simulator.api.resource.ModuleEventWatcher;

/**
 * 默认的增强模板实现
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/10/11 6:02 下午
 */
public class DefaultEnhanceTemplate implements EnhanceTemplate {
    private ModuleEventWatcher moduleEventWatcher;

    public DefaultEnhanceTemplate(ModuleEventWatcher moduleEventWatcher) {
        this.moduleEventWatcher = moduleEventWatcher;
    }

    @Override
    public InstrumentClass enhance(ExtensionModule module, String className, EnhanceCallback callback) {
        IClassMatchBuilder iClassMatchBuilder = new EventWatchBuilder(moduleEventWatcher)
                .onClass(className);
        final DefaultInstrumentClass instrumentClass = new DefaultInstrumentClass(iClassMatchBuilder);
        callback.doEnhance(instrumentClass);
        if (module instanceof ModuleLifecycleAdapter) {
            ((ModuleLifecycleAdapter) module).addReleaseResource(new InstrumentClassResource(instrumentClass));
        }
        instrumentClass.execute();
        return instrumentClass;
    }

    @Override
    public InstrumentClass enhance(ExtensionModule module, EnhanceCallback callback, String... classNames) {
        if (classNames == null || classNames.length == 0) {
            throw new IllegalArgumentException("classNames can't be empty.");
        }

        IClassMatchBuilder iClassMatchBuilder = new EventWatchBuilder(moduleEventWatcher)
                .onClass(classNames);
        final DefaultInstrumentClass instrumentClass = new DefaultInstrumentClass(iClassMatchBuilder);
        callback.doEnhance(instrumentClass);
        if (module instanceof ModuleLifecycleAdapter) {
            ((ModuleLifecycleAdapter) module).addReleaseResource(new InstrumentClassResource(instrumentClass));
        }
        instrumentClass.execute();
        return instrumentClass;
    }

    @Override
    public InstrumentClass enhanceWithSuperClass(ExtensionModule module, String superClassName, EnhanceCallback callback) {
        IClassMatchBuilder iClassMatchBuilder = new EventWatchBuilder(moduleEventWatcher)
                .onAnyClass().withSuperClass(superClassName);
        final DefaultInstrumentClass instrumentClass = new DefaultInstrumentClass(iClassMatchBuilder);
        callback.doEnhance(instrumentClass);
        if (module instanceof ModuleLifecycleAdapter) {
            ((ModuleLifecycleAdapter) module).addReleaseResource(new InstrumentClassResource(instrumentClass));
        }
        instrumentClass.execute();
        return instrumentClass;
    }

    @Override
    public InstrumentClass enhanceWithSuperClass(ExtensionModule module, EnhanceCallback callback, String... superClassNames) {
        IClassMatchBuilder iClassMatchBuilder = new EventWatchBuilder(moduleEventWatcher)
                .onAnyClass().withSuperClass(superClassNames);
        final DefaultInstrumentClass instrumentClass = new DefaultInstrumentClass(iClassMatchBuilder);
        callback.doEnhance(instrumentClass);
        if (module instanceof ModuleLifecycleAdapter) {
            ((ModuleLifecycleAdapter) module).addReleaseResource(new InstrumentClassResource(instrumentClass));
        }
        instrumentClass.execute();
        return instrumentClass;
    }

    @Override
    public InstrumentClass enhanceWithInterface(ExtensionModule module, String interfaceClassName, EnhanceCallback callback) {
        IClassMatchBuilder iClassMatchBuilder = new EventWatchBuilder(moduleEventWatcher)
                .onAnyClass().hasInterfaceTypes(interfaceClassName);
        final DefaultInstrumentClass instrumentClass = new DefaultInstrumentClass(iClassMatchBuilder);
        callback.doEnhance(instrumentClass);
        if (module instanceof ModuleLifecycleAdapter) {
            ((ModuleLifecycleAdapter) module).addReleaseResource(new InstrumentClassResource(instrumentClass));
        }
        instrumentClass.execute();
        return instrumentClass;
    }

    @Override
    public InstrumentClass enhanceWithInterface(ExtensionModule module, EnhanceCallback callback, String... interfaceClassNames) {
        IClassMatchBuilder iClassMatchBuilder = new EventWatchBuilder(moduleEventWatcher)
                .onAnyClass().hasInterfaceTypes(interfaceClassNames);
        final DefaultInstrumentClass instrumentClass = new DefaultInstrumentClass(iClassMatchBuilder);
        callback.doEnhance(instrumentClass);
        if (module instanceof ModuleLifecycleAdapter) {
            ((ModuleLifecycleAdapter) module).addReleaseResource(new InstrumentClassResource(instrumentClass));
        }
        instrumentClass.execute();
        return instrumentClass;
    }
}
