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
package com.pamirs.attach.plugin.es;

import com.pamirs.attach.plugin.es.interceptor.ElasticSearchExecuteInterceptor;
import com.pamirs.attach.plugin.es.interceptor.RestClientPerformAsyncLowVersionRequestInterceptor;
import com.pamirs.attach.plugin.es.interceptor.RestClientPerformAsyncRequestInterceptor;
import com.pamirs.attach.plugin.es.interceptor.RestClientPerformRequestInterceptor;
import com.pamirs.attach.plugin.es.interceptor.RestHighLevelClientInterceptor;
import com.pamirs.attach.plugin.es.interceptor.TransportClientExecuteInterceptor;
import com.pamirs.attach.plugin.es.interceptor.TransportClientTraceInterceptor;
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
 * @author vincent
 * @version v0.1 2017/2/14 11:20
 */
@MetaInfServices(ExtensionModule.class)
@ModuleInfo(id = ElasticsearchConstants.MODULE_NAME, version = "1.0.0", author = "xiaobin@shulie.io",
    description = "elasticsearch 搜索引擎，支持5.x、6.x、7.x，支持 transport、rest")
public class ElasticSearchPlugin extends ModuleLifecycleAdapter implements ExtensionModule {

    @Override
    public void onActive() throws Throwable {
        System.setProperty("es.set.netty.runtime.available.processors", "false");
        addTransportClientInterceptor();
        addRestClientInterceptor();
    }

    private void addTransportClientInterceptor() {
        //在6-下不可用 压测传递无效 属性更底层的api 适合用来校验兜底
        enhanceTemplate.enhance(this, "org.elasticsearch.client.support.AbstractClient", new EnhanceCallback() {
            @Override
            public void doEnhance(InstrumentClass target) {
                InstrumentMethod method = target.getDeclaredMethod("execute", "org.elasticsearch.action.Action",
                    "org.elasticsearch.action.ActionRequest", "org.elasticsearch.action.ActionListener");
                method.addInterceptor(Listeners.of(TransportClientExecuteInterceptor.class, "SEARCH_SCOPE", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));
                method.addInterceptor(Listeners.of(TransportClientTraceInterceptor.class));

                InstrumentMethod methodHighVersion = target.getDeclaredMethod("execute",
                    "org.elasticsearch.action.ActionType",
                    "org.elasticsearch.action.ActionRequest",
                    "org.elasticsearch.action.ActionListener");
                methodHighVersion.addInterceptor(Listeners.of(TransportClientExecuteInterceptor.class, "SEARCH_SCOPE", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));
                methodHighVersion.addInterceptor(Listeners.of(TransportClientTraceInterceptor.class));

                //org.elasticsearch.client.support.AbstractClient.prepareIndex(java.lang.String, java.lang.String,
                // java.lang.String)
                InstrumentMethod prepareIndexMethod = target.getDeclaredMethod("prepareIndex",
                    "java.lang.String"
                    , "java.lang.String"
                    , "java.lang.String");
                prepareIndexMethod.addInterceptor(Listeners.of(ElasticSearchExecuteInterceptor.class));

                //org.elasticsearch.client.support.AbstractClient.prepareDelete(java.lang.String, java.lang.String,
                // java.lang.String)
                InstrumentMethod prepareDelete = target.getDeclaredMethod("prepareDelete",
                    "java.lang.String",
                    "java.lang.String",
                    "java.lang.String");
                prepareDelete.addInterceptor(Listeners.of(ElasticSearchExecuteInterceptor.class));

                //org.elasticsearch.client.support.AbstractClient.prepareGet(java.lang.String, java.lang.String, java
                // .lang.String)
                InstrumentMethod prepareGet = target.getDeclaredMethod("prepareGet",
                    "java.lang.String",
                    "java.lang.String",
                    "java.lang.String");
                prepareGet.addInterceptor(Listeners.of(ElasticSearchExecuteInterceptor.class));

            }
        });

    }

    private void addRestClientInterceptor() {
        // spring boot elasticsearch 6.8.6测试版本
        // 修改影子索引
        enhanceTemplate.enhance(this, "org.elasticsearch.client.RestHighLevelClient", new EnhanceCallback() {
            @Override
            public void doEnhance(InstrumentClass target) {
                InstrumentMethod method = target.getDeclaredMethods("internalPerformRequest",
                    "internalPerformRequestAsync");
                method.addInterceptor(Listeners
                    .of(RestHighLevelClientInterceptor.class, "SEARCH_SCOPE", ExecutionPolicy.BOUNDARY,
                        Interceptors.SCOPE_CALLBACK));

            }
        });

        //链路监控需要增强该类所有方法
        enhanceTemplate.enhance(this, "org.elasticsearch.client.RestClient", new EnhanceCallback() {
            @Override
            public void doEnhance(InstrumentClass target) {
                InstrumentMethod performRequestMethod = target.getDeclaredMethod("performRequest",
                    "org.elasticsearch.client.Request");
                performRequestMethod.addInterceptor(Listeners
                    .of(RestClientPerformRequestInterceptor.class, "SHADOW_SEARCH_SCOPE", ExecutionPolicy.BOUNDARY,
                        Interceptors.SCOPE_CALLBACK));

                InstrumentMethod performRequestAsyncMethod = target.getDeclaredMethod("performRequestAsync", "org.elasticsearch.client.Request", "org.elasticsearch.client.ResponseListener");

                InstrumentMethod performRequestAsyncMethodLowVersion = target.getDeclaredMethod("performRequestAsync",
                    "java.lang.String", "java.lang.String", "java.util.Map", "org.apache.http.HttpEntity",
                    "org.elasticsearch.client.HttpAsyncResponseConsumerFactory", "org.elasticsearch.client.ResponseListener",
                    "org.apache.http.Header[]");

                if(performRequestAsyncMethod != null){
                    performRequestAsyncMethod.addInterceptor(Listeners.of(RestClientPerformAsyncRequestInterceptor.class, "SHADOW_SEARCH_SCOPE", ExecutionPolicy.BOUNDARY));
                }

                if(performRequestAsyncMethodLowVersion != null){
                    performRequestAsyncMethodLowVersion.addInterceptor(
                        Listeners.of(RestClientPerformAsyncLowVersionRequestInterceptor.class, "SHADOW_SEARCH_SCOPE", ExecutionPolicy.BOUNDARY));
                }
            }
        });

    }

}
