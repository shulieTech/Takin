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
package com.pamirs.attach.plugin.guava;

import com.pamirs.attach.plugin.guava.interceptor.*;
import com.shulie.instrument.simulator.api.ExtensionModule;
import com.shulie.instrument.simulator.api.ModuleInfo;
import com.shulie.instrument.simulator.api.ModuleLifecycleAdapter;
import com.shulie.instrument.simulator.api.instrument.EnhanceCallback;
import com.shulie.instrument.simulator.api.instrument.InstrumentClass;
import com.shulie.instrument.simulator.api.instrument.InstrumentMethod;
import com.shulie.instrument.simulator.api.listener.Listeners;
import org.kohsuke.MetaInfServices;

/**
 * @author wangjian
 * @since 2021/2/4 17:42
 */
@MetaInfServices(ExtensionModule.class)
@ModuleInfo(id = GoogleGuavaConstants.MODULE_NAME, version = "1.0.0", author = "wangjian@shulie.io",description = "guava 本地缓存")
public class GoogleGuavaPlugin extends ModuleLifecycleAdapter implements ExtensionModule {
    @Override
    public void onActive() throws Throwable {
        addGuaveInterceptor();
    }

    private void addGuaveInterceptor() {
        enhanceTemplate.enhance(this, "com.google.common.collect.ImmutableMap", new EnhanceCallback() {
            @Override
            public void doEnhance(InstrumentClass target) {
                InstrumentMethod copyOf = target.getDeclaredMethods("copyOf");
                copyOf.addInterceptor(Listeners.of(CacheOperationGetAllResInterceptor.class));
            }
        });
        this.enhanceTemplate.enhance(this, "com.google.common.cache.LocalCache$LoadingValueReference", new EnhanceCallback() {
            @Override
            public void doEnhance(InstrumentClass target) {
                InstrumentMethod load = target.getDeclaredMethods("loadFuture");
                load.addInterceptor(Listeners.of(CacheLoaderKeyConvertInterceptor.class));
            }
        });

        final String[] methods = new String[]{"getAllPresent", "invalidateAll", "get",
                "invalidate", "putAll", "put",
                "getIfPresent"};
        // Cache operation

        enhanceTemplate.enhance(this, "com.google.common.cache.LocalCache$LocalManualCache", new EnhanceCallback() {
            @Override
            public void doEnhance(InstrumentClass target) {
                InstrumentMethod build = target.getDeclaredMethods(methods);
                build.addInterceptor(Listeners.of(CacheOperationInterceptor.class));
                InstrumentMethod declaredMethods = target.getDeclaredMethods("*");
                declaredMethods.addInterceptor(Listeners.of(CacheOperationTraceInterceptor.class));
                InstrumentMethod asMap = target.getDeclaredMethods("asMap");
                asMap.addInterceptor(Listeners.of(CacheAsMapInterceptor.class));
            }
        });



        enhanceTemplate.enhance(this, "com.google.common.cache.LocalCache$LocalLoadingCache", new EnhanceCallback() {
            @Override
            public void doEnhance(InstrumentClass target) {
                InstrumentMethod build = target.getDeclaredMethods(methods);
                build.addInterceptor(Listeners.of(CacheOperationInterceptor.class));
                InstrumentMethod declaredMethods = target.getDeclaredMethods("*");
                declaredMethods.addInterceptor(Listeners.of(CacheOperationTraceInterceptor.class));
                InstrumentMethod asMap = target.getDeclaredMethods("asMap");
                asMap.addInterceptor(Listeners.of(CacheAsMapInterceptor.class));
            }
        });
        enhanceTemplate.enhance(this, "com.google.common.cache.ForwardingCache", new EnhanceCallback() {
            @Override
            public void doEnhance(InstrumentClass target) {
                InstrumentMethod build = target.getDeclaredMethods(methods);
                build.addInterceptor(Listeners.of(CacheOperationInterceptor.class));
                InstrumentMethod declaredMethods = target.getDeclaredMethods("*");
                declaredMethods.addInterceptor(Listeners.of(CacheOperationTraceInterceptor.class));
                InstrumentMethod asMap = target.getDeclaredMethods("asMap");
                asMap.addInterceptor(Listeners.of(CacheAsMapInterceptor.class));
            }
        });
    }
}
