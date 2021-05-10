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
package com.pamirs.attach.plugin.oscache;


import com.pamirs.attach.plugin.oscache.interceptor.CacheKeyInterceptor;
import com.shulie.instrument.simulator.api.ExtensionModule;
import com.shulie.instrument.simulator.api.ModuleInfo;
import com.shulie.instrument.simulator.api.ModuleLifecycleAdapter;
import com.shulie.instrument.simulator.api.instrument.EnhanceCallback;
import com.shulie.instrument.simulator.api.instrument.InstrumentClass;
import com.shulie.instrument.simulator.api.instrument.InstrumentMethod;
import com.shulie.instrument.simulator.api.listener.Listeners;
import org.kohsuke.MetaInfServices;

/**
 * @author xiaobin.zfb | xiaobin@shulie.io
 * @since 2020/8/19 10:26 上午
 */
@MetaInfServices(ExtensionModule.class)
@ModuleInfo(id = OscacheConstants.MODULE_NAME, version = "1.0.0", author = "xiaobin@shulie.io",description = "oscache 本地缓存")
public class OscachePlugin extends ModuleLifecycleAdapter implements ExtensionModule {
    @Override
    public void onActive() throws Throwable {

        enhanceTemplate.enhance(this, "com.opensymphony.oscache.base.Cache", new EnhanceCallback() {
            @Override
            public void doEnhance(InstrumentClass target) {
                InstrumentMethod getMethod = target.getDeclaredMethod("getFromCache", "java.lang.String", "int", "java.lang.String");
                getMethod.addInterceptor(Listeners.of(CacheKeyInterceptor.class));

                InstrumentMethod cancelUpdateMethod = target.getDeclaredMethod("cancelUpdate", "java.lang.String");
                cancelUpdateMethod.addInterceptor(Listeners.of(CacheKeyInterceptor.class));

                InstrumentMethod getCacheEntryMethod = target.getDeclaredMethod("getCacheEntry", "java.lang.String", "com.opensymphony.oscache.base.EntryRefreshPolicy", "java.lang.String");
                getCacheEntryMethod.addInterceptor(Listeners.of(CacheKeyInterceptor.class));

                InstrumentMethod putInCacheMethod = target.getDeclaredMethod("putInCache", "java.lang.String", "java.lang.Object", "java.lang.String[]", "com.opensymphony.oscache.base.EntryRefreshPolicy", "java.lang.String");
                putInCacheMethod.addInterceptor(Listeners.of(CacheKeyInterceptor.class));

                InstrumentMethod removeEntryMethod = target.getDeclaredMethod("removeEntry", "java.lang.String", "java.lang.String");
                removeEntryMethod.addInterceptor(Listeners.of(CacheKeyInterceptor.class));
            }
        });


    }
}
