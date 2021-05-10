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
package com.pamirs.attach.plugin.catalina;

import com.pamirs.attach.plugin.catalina.interceptor.RequestStartAsyncInterceptor;
import com.pamirs.attach.plugin.catalina.interceptor.StandardHostValveInvokeInterceptor;
import com.shulie.instrument.simulator.api.ExtensionModule;
import com.shulie.instrument.simulator.api.ModuleInfo;
import com.shulie.instrument.simulator.api.ModuleLifecycleAdapter;
import com.shulie.instrument.simulator.api.instrument.EnhanceCallback;
import com.shulie.instrument.simulator.api.instrument.InstrumentClass;
import com.shulie.instrument.simulator.api.instrument.InstrumentMethod;
import com.shulie.instrument.simulator.api.listener.Listeners;
import org.kohsuke.MetaInfServices;


@MetaInfServices(ExtensionModule.class)
@ModuleInfo(id = "catalina", version = "1.0.0", author = " xiaobin@shulie.io",description = "catalina 服务器,支持 tomcat 和 jboss")
public class CatalinaPlugin extends ModuleLifecycleAdapter implements ExtensionModule {

    @Override
    public void onActive() throws Throwable {
        enhanceTemplate.enhance(this, "org.apache.catalina.core.StandardHostValve", new EnhanceCallback() {
            @Override
            public void doEnhance(InstrumentClass target) {
                InstrumentMethod method = target.getDeclaredMethod("invoke", "org.apache.catalina.connector.Request", "org.apache.catalina.connector.Response");
                method.addInterceptor(Listeners.of(StandardHostValveInvokeInterceptor.class));
            }
        });

        enhanceTemplate.enhance(this, "org.apache.catalina.connector.Request", new EnhanceCallback() {
            @Override
            public void doEnhance(InstrumentClass target) {
                InstrumentMethod method = target.getDeclaredMethod("startAsync", "javax.servlet.ServletRequest", "javax.servlet.ServletResponse");
                method.addInterceptor(Listeners.of(RequestStartAsyncInterceptor.class));
            }
        });
    }
}
