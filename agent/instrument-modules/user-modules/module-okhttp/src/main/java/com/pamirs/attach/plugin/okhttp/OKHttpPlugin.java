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
package com.pamirs.attach.plugin.okhttp;

import com.pamirs.attach.plugin.okhttp.v2.interceptor.AsyncCallConstructorV2Interceptor;
import com.pamirs.attach.plugin.okhttp.v2.interceptor.RealCallEnqueueV2Interceptor;
import com.pamirs.attach.plugin.okhttp.v2.interceptor.RealCallExecuteV2Interceptor;
import com.pamirs.attach.plugin.okhttp.v2.interceptor.RequestBuilderBuildMethodV2Interceptor;
import com.pamirs.attach.plugin.okhttp.v3.interceptor.AsyncCallConstructorV3Interceptor;
import com.pamirs.attach.plugin.okhttp.v3.interceptor.RealCallEnqueueV3Interceptor;
import com.pamirs.attach.plugin.okhttp.v3.interceptor.RealCallExecuteV3Interceptor;
import com.pamirs.attach.plugin.okhttp.v3.interceptor.RequestBuilderBuildMethodV3Interceptor;
import com.shulie.instrument.simulator.api.ExtensionModule;
import com.shulie.instrument.simulator.api.ModuleInfo;
import com.shulie.instrument.simulator.api.ModuleLifecycleAdapter;
import com.shulie.instrument.simulator.api.instrument.EnhanceCallback;
import com.shulie.instrument.simulator.api.instrument.InstrumentClass;
import com.shulie.instrument.simulator.api.instrument.InstrumentMethod;
import com.shulie.instrument.simulator.api.listener.Listeners;
import org.kohsuke.MetaInfServices;

/**
 * Created by xiaobin on 2016/12/15.
 */
@MetaInfServices(ExtensionModule.class)
@ModuleInfo(id = "okhttp", version = "1.0.0", author = "xiaobin@shulie.io",description = "okhttp http 客户端,支持2.x-3.x 版本")
public class OKHttpPlugin extends ModuleLifecycleAdapter implements ExtensionModule {

    @Override
    public void onActive() throws Throwable {
        // Support for OkHttp3
        enhanceTemplate.enhance(this, "okhttp3.Request$Builder", new EnhanceCallback() {
            @Override
            public void doEnhance(InstrumentClass target) {
                final InstrumentMethod buildMethod = target.getDeclaredMethod("build");
                buildMethod.addInterceptor(Listeners.of(RequestBuilderBuildMethodV3Interceptor.class));
            }
        });

        enhanceTemplate.enhance(this, "okhttp3.RealCall", new EnhanceCallback() {
            @Override
            public void doEnhance(InstrumentClass target) {
                final InstrumentMethod executeMethod = target.getDeclaredMethod("execute");
                executeMethod.addInterceptor(Listeners.of(RealCallExecuteV3Interceptor.class));

                final InstrumentMethod enqueueMethod = target.getDeclaredMethod("enqueue", "okhttp3.Callback");
                enqueueMethod.addInterceptor(Listeners.of(RealCallEnqueueV3Interceptor.class));
            }
        });

        enhanceTemplate.enhance(this, "okhttp3.RealCall$AsyncCall", new EnhanceCallback() {
            @Override
            public void doEnhance(InstrumentClass target) {
                InstrumentMethod constructor = target.getConstructor("okhttp3.RealCall", "okhttp3.Callback");
                constructor.addInterceptor(Listeners.of(AsyncCallConstructorV3Interceptor.class));
            }
        });

        // Support for OkHttp2
        enhanceTemplate.enhance(this, "com.squareup.okhttp.Request$Builder", new EnhanceCallback() {
            @Override
            public void doEnhance(InstrumentClass target) {
                final InstrumentMethod buildMethod = target.getDeclaredMethod("build");
                buildMethod.addInterceptor(Listeners.of(RequestBuilderBuildMethodV2Interceptor.class));
            }
        });

        enhanceTemplate.enhance(this, "com.squareup.okhttp.Call", new EnhanceCallback() {
            @Override
            public void doEnhance(InstrumentClass target) {
                final InstrumentMethod executeMethod = target.getDeclaredMethod("execute");
                executeMethod.addInterceptor(Listeners.of(RealCallExecuteV2Interceptor.class));
                final InstrumentMethod enqueueMethod = target.getDeclaredMethod("enqueue", "com.squareup.okhttp.Callback");
                enqueueMethod.addInterceptor(Listeners.of(RealCallEnqueueV2Interceptor.class));
            }
        });

        enhanceTemplate.enhance(this, "com.squareup.okhttp.Call$AsyncCall", new EnhanceCallback() {
            @Override
            public void doEnhance(InstrumentClass target) {
                InstrumentMethod constructor = target.getConstructor("com.squareup.okhttp.Call", "com.squareup.okhttp.Callback", "boolean");
                constructor.addInterceptor(Listeners.of(AsyncCallConstructorV2Interceptor.class));
            }
        });

    }
}
