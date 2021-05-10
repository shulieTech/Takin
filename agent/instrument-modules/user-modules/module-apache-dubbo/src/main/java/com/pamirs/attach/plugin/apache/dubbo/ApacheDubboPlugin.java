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
package com.pamirs.attach.plugin.apache.dubbo;

import com.pamirs.attach.plugin.apache.dubbo.interceptor.ConsumerContextFilterInterceptor;
import com.pamirs.attach.plugin.apache.dubbo.interceptor.DubboConsumerInterceptor;
import com.pamirs.attach.plugin.apache.dubbo.interceptor.DubboProviderInterceptor;
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
 * @package: com.pamirs.attach.plugin.apache.dubbo
 * @Date 2019-12-14 21:38
 */
@MetaInfServices(ExtensionModule.class)
@ModuleInfo(id = "apache-dubbo", version = "1.0.0", author = "xiaobin@shulie.io",description = "apache dubbo 远程调用框架")
public class ApacheDubboPlugin extends ModuleLifecycleAdapter implements ExtensionModule {

    @Override
    public void onActive() throws Throwable {
        this.enhanceTemplate.enhance(this, "org.apache.dubbo.rpc.protocol.AbstractInvoker", new EnhanceCallback() {
            @Override
            public void doEnhance(InstrumentClass target) {
                final InstrumentMethod invokeMethod = target.getDeclaredMethod("invoke", "org.apache.dubbo.rpc.Invocation");
                if (invokeMethod != null) {
                    invokeMethod
                            .addInterceptor(Listeners.of(DubboConsumerInterceptor.class));
                }
            }
        });

        this.enhanceTemplate.enhance(this, "org.apache.dubbo.rpc.filter.ConsumerContextFilter", new EnhanceCallback() {
            @Override
            public void doEnhance(InstrumentClass target) {
                final InstrumentMethod invokeMethod = target.getDeclaredMethod("invoke", "org.apache.dubbo.rpc.Invoker", "org.apache.dubbo.rpc.Invocation");
                if (invokeMethod != null) {
                    invokeMethod
                            .addInterceptor(Listeners.of(ConsumerContextFilterInterceptor.class));
                }
            }
        });

        this.enhanceTemplate.enhance(this, "org.apache.dubbo.rpc.proxy.AbstractProxyInvoker", new EnhanceCallback() {
            @Override
            public void doEnhance(InstrumentClass target) {
                final InstrumentMethod invokeMethod = target.getDeclaredMethod("invoke", "org.apache.dubbo.rpc.Invocation");
                if (invokeMethod != null) {
                    invokeMethod
                            .addInterceptor(Listeners.of(DubboProviderInterceptor.class));
                }
            }
        });

    }
}
