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

import com.pamirs.attach.plugin.httpclient.interceptor.*;
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
 * @author fabing.zhaofb
 */
@MetaInfServices(ExtensionModule.class)
@ModuleInfo(id = "httpclient", version = "1.0.0", author = "xiaobin@shulie.io", description = "apache http 客户端，支持3.x和4.x")
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
        enhanceTemplate.enhance(this, "org.apache.http.impl.client.CloseableHttpClient", new EnhanceCallback() {
            @Override
            public void doEnhance(InstrumentClass target) {

                //execute and doExecute, only one method exists!
                InstrumentMethod executeMethod0 = target.getDeclaredMethod("execute",
                        "org.apache.http.HttpHost", "org.apache.http.HttpRequest");
                executeMethod0.addInterceptor(Listeners.of(HttpClientv4MethodInterceptor.class, "HTTPCLIENT_SYNC_EXECUTE_SCOPE", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));


                InstrumentMethod executeMethod1 = target.getDeclaredMethod("execute",
                        "org.apache.http.HttpHost", "org.apache.http.HttpRequest", "org.apache.http.protocol.HttpContext");
                executeMethod1.addInterceptor(Listeners.of(HttpClientv4MethodInterceptor.class, "HTTPCLIENT_SYNC_EXECUTE_SCOPE", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

                InstrumentMethod executeMethod2 = target.getDeclaredMethod("execute",
                        "org.apache.http.HttpHost", "org.apache.http.HttpRequest", "org.apache.http.client.ResponseHandler");
                executeMethod2.addInterceptor(Listeners.of(HttpClientv4MethodInterceptor.class, "HTTPCLIENT_SYNC_EXECUTE_SCOPE", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

                InstrumentMethod executeMethod3 = target.getDeclaredMethod("execute",
                        "org.apache.http.HttpHost", "org.apache.http.HttpRequest", "org.apache.http.client.ResponseHandler", "org.apache.http.protocol.HttpContext");
                executeMethod3.addInterceptor(Listeners.of(HttpClientv4MethodInterceptor.class, "HTTPCLIENT_SYNC_EXECUTE_SCOPE", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

                InstrumentMethod executeMethod4 = target.getDeclaredMethod("execute",
                        "org.apache.http.client.methods.HttpUriRequest");
                executeMethod4.addInterceptor(Listeners.of(HttpClientv4MethodInterceptor1.class, "HTTPCLIENT_SYNC_EXECUTE_SCOPE", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

                InstrumentMethod executeMethod5 = target.getDeclaredMethod("execute",
                        "org.apache.http.client.methods.HttpUriRequest", "org.apache.http.protocol.HttpContext");
                executeMethod5.addInterceptor(Listeners.of(HttpClientv4MethodInterceptor1.class, "HTTPCLIENT_SYNC_EXECUTE_SCOPE", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

                InstrumentMethod executeMethod6 = target.getDeclaredMethod("execute",
                        "org.apache.http.client.methods.HttpUriRequest", "org.apache.http.client.ResponseHandler");
                executeMethod6.addInterceptor(Listeners.of(HttpClientv4MethodInterceptor1.class, "HTTPCLIENT_SYNC_EXECUTE_SCOPE", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

                InstrumentMethod executeMethod7 = target.getDeclaredMethod("execute",
                        "org.apache.http.client.methods.HttpUriRequest", "org.apache.http.client.ResponseHandler", "org.apache.http.protocol.HttpContext");
                executeMethod7.addInterceptor(Listeners.of(HttpClientv4MethodInterceptor1.class, "HTTPCLIENT_SYNC_EXECUTE_SCOPE", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            }
        });

        enhanceTemplate.enhance(this, "org.apache.http.impl.nio.client.CloseableHttpAsyncClient", new EnhanceCallback() {
            @Override
            public void doEnhance(InstrumentClass target) {
                InstrumentMethod executeMethod0 = target.getDeclaredMethod("execute",
                        "org.apache.http.HttpHost", "org.apache.http.HttpRequest", "org.apache.http.protocol.HttpContext", "org.apache.http.concurrent.FutureCallback");
                executeMethod0.addInterceptor(Listeners.of(AsyncHttpClientv4MethodInterceptor.class, "HTTPCLIENT_ASYNC_EXECUTE_SCOPE", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

                InstrumentMethod executeMethod1 = target.getDeclaredMethod("execute",
                        "org.apache.http.HttpHost", "org.apache.http.HttpRequest", "org.apache.http.concurrent.FutureCallback");
                executeMethod1.addInterceptor(Listeners.of(AsyncHttpClientv4MethodInterceptor.class, "HTTPCLIENT_ASYNC_EXECUTE_SCOPE", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

                InstrumentMethod executeMethod2 = target.getDeclaredMethod("execute",
                        "org.apache.http.nio.protocol.HttpAsyncRequestProducer", "org.apache.http.nio.protocol.HttpAsyncResponseConsumer", "org.apache.http.concurrent.FutureCallback");
                executeMethod2.addInterceptor(Listeners.of(AsyncHttpClientv4MethodInterceptor2.class, "HTTPCLIENT_ASYNC_EXECUTE_SCOPE", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

                InstrumentMethod executeMethod3 = target.getDeclaredMethod("execute",
                        "org.apache.http.client.methods.HttpUriRequest", "org.apache.http.concurrent.FutureCallback");
                executeMethod3.addInterceptor(Listeners.of(AsyncHttpClientv4MethodInterceptor1.class, "HTTPCLIENT_ASYNC_EXECUTE_SCOPE", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

                InstrumentMethod executeMethod4 = target.getDeclaredMethod("execute",
                        "org.apache.http.client.methods.HttpUriRequest", "org.apache.http.protocol.HttpContext", "org.apache.http.concurrent.FutureCallback");
                executeMethod4.addInterceptor(Listeners.of(AsyncHttpClientv4MethodInterceptor1.class, "HTTPCLIENT_ASYNC_EXECUTE_SCOPE", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            }
        });

    }

}
