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
package com.shulie.instrument.simulator.thread;

import com.shulie.instrument.simulator.api.ExtensionModule;
import com.shulie.instrument.simulator.api.ModuleInfo;
import com.shulie.instrument.simulator.api.ModuleLifecycleAdapter;
import com.shulie.instrument.simulator.api.instrument.EnhanceCallback;
import com.shulie.instrument.simulator.api.instrument.InstrumentClass;
import com.shulie.instrument.simulator.api.instrument.InstrumentMethod;
import com.shulie.instrument.simulator.api.listener.Listeners;
import com.shulie.instrument.simulator.thread.interceptor.*;
import org.kohsuke.MetaInfServices;

/**
 * 对所有的 runnable 和 callable 进行增强，解决对 ttl 依赖的问题
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2021/1/22 4:47 下午
 */
@MetaInfServices(ExtensionModule.class)
@ModuleInfo(id = ThreadConstants.MODULE_NAME, version = "1.0.0", author = "xiaobin@shulie.io", description="多线程支持，用于解决跨线程的上下文传递,用于替换阿里开源的 transmittable-thread-local")
public class ThreadPlugin extends ModuleLifecycleAdapter implements ExtensionModule {

    @Override
    public void onActive() throws Throwable {
        this.enhanceTemplate.enhanceWithInterface(this, "java.lang.Runnable", new EnhanceCallback() {
            @Override
            public void doEnhance(InstrumentClass target) {
                target.isIncludeBootstrap(false);
                InstrumentMethod constructors = target.getConstructors();
                constructors.addInterceptor(Listeners.of(RunnableConstructorInterceptor.class));

                InstrumentMethod runMethod = target.getDeclaredMethod("run");
                runMethod.addInterceptor(Listeners.of(RunnableRunInterceptor.class));
            }
        });

        this.enhanceTemplate.enhanceWithInterface(this, "java.util.concurrent.Callable", new EnhanceCallback() {
            @Override
            public void doEnhance(InstrumentClass target) {
                target.isIncludeBootstrap(false);
                InstrumentMethod constructors = target.getConstructors();
                constructors.addInterceptor(Listeners.of(CallableConstructorInterceptor.class));

                InstrumentMethod runMethod = target.getDeclaredMethod("call");
                runMethod.addInterceptor(Listeners.of(CallableCallInterceptor.class));
            }
        });

    }
}
