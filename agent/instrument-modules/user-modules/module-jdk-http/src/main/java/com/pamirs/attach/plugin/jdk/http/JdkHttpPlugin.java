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
package com.pamirs.attach.plugin.jdk.http;

import com.pamirs.attach.plugin.jdk.http.interceptor.HttpClientInterceptor;
import com.pamirs.attach.plugin.jdk.http.interceptor.HttpURLConnectionInterceptor;
import com.pamirs.pradar.interceptor.Interceptors;
import com.shulie.instrument.simulator.api.ExtensionModule;
import com.shulie.instrument.simulator.api.ModuleInfo;
import com.shulie.instrument.simulator.api.ModuleLifecycleAdapter;
import com.shulie.instrument.simulator.api.instrument.EnhanceCallback;
import com.shulie.instrument.simulator.api.instrument.InstrumentClass;
import com.shulie.instrument.simulator.api.instrument.InstrumentMethod;
import com.shulie.instrument.simulator.api.listener.Listeners;
import com.shulie.instrument.simulator.api.scope.ExecutionPolicy;
import org.kohsuke.MetaInfServices;

/**
 * Created by xiaobin on 2016/12/15.
 */
@MetaInfServices(ExtensionModule.class)
@ModuleInfo(id = "jdk-http", version = "1.0.0", author = "xiaobin@shulie.io",description = "jdk 内置的 http 客户端支持")
public class JdkHttpPlugin extends ModuleLifecycleAdapter implements ExtensionModule {

    @Override
    public void onActive() throws Throwable {
        enhanceTemplate.enhance(this, "sun.net.www.protocol.http.HttpURLConnection", new EnhanceCallback() {
            @Override
            public void doEnhance(InstrumentClass target) {
                target.includeBootstrap();
                final InstrumentMethod connectMethod = target.getDeclaredMethod("connect");
                connectMethod.addInterceptor(Listeners.of(HttpURLConnectionInterceptor.class, "JDK_HTTP_HTTPURLCONNECTION_SCOPE", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

//
                final InstrumentMethod getInputStreamMethod = target.getDeclaredMethod("getInputStream");
                getInputStreamMethod.addInterceptor(Listeners.of(HttpURLConnectionInterceptor.class, "JDK_HTTP_HTTPURLCONNECTION_SCOPE", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

                final InstrumentMethod getOutputStreamMethod = target.getDeclaredMethod("getOutputStream");
                getOutputStreamMethod.addInterceptor(Listeners.of(HttpURLConnectionInterceptor.class, "JDK_HTTP_HTTPURLCONNECTION_SCOPE", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));
            }
        });


        enhanceTemplate.enhance(this, "sun.net.www.http.HttpClient", new EnhanceCallback() {
            @Override
            public void doEnhance(InstrumentClass target) {
                target.includeBootstrap();
                InstrumentMethod method = target.getDeclaredMethod("writeRequests",
                        "sun.net.www.MessageHeader",
                        "sun.net.www.http.PosterOutputStream",
                        "boolean"
                );
                method.addInterceptor(Listeners.of(HttpClientInterceptor.class));
            }
        });

    }
}
