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
package com.pamirs.attach.plugin.undertow;

import com.pamirs.attach.plugin.undertow.interceptor.ConnectorsExecuteRootHandlerInterceptor;
import com.pamirs.attach.plugin.undertow.interceptor.HttpServletRequestImplStartAsyncInterceptor;
import com.shulie.instrument.simulator.api.ExtensionModule;
import com.shulie.instrument.simulator.api.ModuleInfo;
import com.shulie.instrument.simulator.api.ModuleLifecycleAdapter;
import com.shulie.instrument.simulator.api.instrument.EnhanceCallback;
import com.shulie.instrument.simulator.api.instrument.InstrumentClass;
import com.shulie.instrument.simulator.api.instrument.InstrumentMethod;
import com.shulie.instrument.simulator.api.listener.Listeners;
import org.kohsuke.MetaInfServices;

/**
 * @Author <a href="tangyuhan@shulie.io">yuhan.tang</a>
 * @Date 2020-05-25 14:20
 */
@MetaInfServices(ExtensionModule.class)
@ModuleInfo(id = UndertowConstants.MODULE_NAME, version = "1.0.0", author = "xiaobin@shulie.io",description = "undertow 容器支持,jboss wildfly 版本底层也是依赖这个")
public class UndertowPlugin extends ModuleLifecycleAdapter implements ExtensionModule {

    @Override
    public void onActive() throws Throwable {

        enhanceTemplate.enhance(this, "io.undertow.server.Connectors", new EnhanceCallback() {
            @Override
            public void doEnhance(InstrumentClass target) {
                // Entry Point
                final InstrumentMethod connectorsExecuteRootHandlerMethod = target.getDeclaredMethod("executeRootHandler", "io.undertow.server.HttpHandler", "io.undertow.server.HttpServerExchange");
                connectorsExecuteRootHandlerMethod.addInterceptor(Listeners.of(ConnectorsExecuteRootHandlerInterceptor.class));
            }
        });

        enhanceTemplate.enhance(this, "io.undertow.servlet.spec.HttpServletRequestImpl", new EnhanceCallback() {
            @Override
            public void doEnhance(InstrumentClass target) {
                final InstrumentMethod startAsyncMethod = target.getDeclaredMethod("startAsync", "javax.servlet.ServletRequest", "javax.servlet.ServletResponse");
                startAsyncMethod.addInterceptor(Listeners.of(HttpServletRequestImplStartAsyncInterceptor.class));
            }
        });
    }
}
