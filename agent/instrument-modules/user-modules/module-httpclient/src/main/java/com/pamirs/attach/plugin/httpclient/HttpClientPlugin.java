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
package com.pamirs.attach.plugin.httpclient;

import com.pamirs.attach.plugin.httpclient.interceptor.HttpClientv3MethodInterceptor;
import com.pamirs.attach.plugin.httpclient.interceptor.HttpClientv4MethodInterceptor;
import com.shulie.instrument.simulator.api.ExtensionModule;
import com.shulie.instrument.simulator.api.ModuleInfo;
import com.shulie.instrument.simulator.api.ModuleLifecycleAdapter;
import com.shulie.instrument.simulator.api.instrument.EnhanceCallback;
import com.shulie.instrument.simulator.api.instrument.InstrumentClass;
import com.shulie.instrument.simulator.api.instrument.InstrumentMethod;
import com.shulie.instrument.simulator.api.listener.Listeners;
import org.kohsuke.MetaInfServices;

/**
 * @author fabing.zhaofb
 */
@MetaInfServices(ExtensionModule.class)
@ModuleInfo(id = "httpclient", version = "1.0.0", author = "xiaobin@shulie.io",description = "apache http 客户端，支持3.x和4.x")
public class HttpClientPlugin extends ModuleLifecycleAdapter implements ExtensionModule {

    @Override
    public void onActive() throws Throwable {

        //httpclient v3
        enhanceTemplate.enhance(this, "org.apache.commons.httpclient.HttpClient", new EnhanceCallback() {
            @Override
            public void doEnhance(InstrumentClass target) {
                InstrumentMethod instrumentMethod = target.getDeclaredMethod("executeMethod", "org.apache.commons.httpclient.HostConfiguration",
                        "org.apache.commons.httpclient.HttpMethod",
                        "org.apache.commons.httpclient.HttpState");
                instrumentMethod.addInterceptor(Listeners.of(HttpClientv3MethodInterceptor.class));
            }
        });

        //httpclient v4
        enhanceTemplate.enhance(this, "org.apache.http.impl.client.AbstractHttpClient", new EnhanceCallback() {
            @Override
            public void doEnhance(InstrumentClass target) {

                //execute and doExecute, only one method exists!
                InstrumentMethod instrumentMethod = target.getDeclaredMethod("execute",
                        "org.apache.http.HttpHost",
                        "org.apache.http.HttpRequest",
                        "org.apache.http.protocol.HttpContext");
                instrumentMethod.addInterceptor(Listeners.of(HttpClientv4MethodInterceptor.class));


                InstrumentMethod instrumentMethodDoExecute = target.getDeclaredMethod("doExecute",
                        "org.apache.http.HttpHost",
                        "org.apache.http.HttpRequest",
                        "org.apache.http.protocol.HttpContext");
                instrumentMethodDoExecute.addInterceptor(Listeners.of(HttpClientv4MethodInterceptor.class));

            }
        });

        //调试ES RestClient发现 InternalHttpAsyncClient 未拦截
        //其他两个http 请求入口  经过debug发现他们不会经过 AbstractHttpClient
        enhanceTemplate.enhance(this, "org.apache.http.impl.client.InternalHttpClient", new EnhanceCallback() {
            @Override
            public void doEnhance(InstrumentClass target) {
                InstrumentMethod instrumentMethod = target.getDeclaredMethod("doExecute",
                        "org.apache.http.HttpHost",
                        "org.apache.http.HttpRequest",
                        "org.apache.http.protocol.HttpContext");
                instrumentMethod.addInterceptor(Listeners.of(HttpClientv4MethodInterceptor.class));

            }
        });


        enhanceTemplate.enhance(this, "org.apache.http.impl.client.MinimalHttpClient", new EnhanceCallback() {
            @Override
            public void doEnhance(InstrumentClass target) {
                InstrumentMethod instrumentMethod = target.getDeclaredMethod("doExecute",
                        "org.apache.http.HttpHost",
                        "org.apache.http.HttpRequest",
                        "org.apache.http.protocol.HttpContext");
                instrumentMethod.addInterceptor(Listeners.of(HttpClientv4MethodInterceptor.class));
            }
        });

    }

}
