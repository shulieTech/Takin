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
package com.pamirs.attach.plugin.google.httpclient;

import com.pamirs.attach.plugin.google.httpclient.interceptor.HttpRequestExecuteMethodInterceptor;
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
@ModuleInfo(id = "google-httpclient", version = "1.0.0", author = "xiaobin@shulie.io",description = "google开源的 http 客户端")
public class GoogleHttpClientPlugin extends ModuleLifecycleAdapter implements ExtensionModule {

    @Override
    public void onActive() throws Throwable {
        addHttpRequestClass();
    }

    private void addHttpRequestClass() {
        enhanceTemplate.enhance(this, "com.google.api.client.http.HttpRequest", new EnhanceCallback() {

            @Override
            public void doEnhance(InstrumentClass target) {

                InstrumentMethod execute = target.getDeclaredMethod("execute");
                execute.addInterceptor(Listeners.of(HttpRequestExecuteMethodInterceptor.class));
            }
        });
    }
}
