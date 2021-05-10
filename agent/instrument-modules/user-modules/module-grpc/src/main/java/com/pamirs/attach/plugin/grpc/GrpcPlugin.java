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
package com.pamirs.attach.plugin.grpc;

import com.pamirs.attach.plugin.grpc.interceptor.*;
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
 * @package: com.pamirs.attach.plugin.grpc
 * @Date 2020-03-13 15:53
 */
@MetaInfServices(ExtensionModule.class)
@ModuleInfo(id = GrpcConstants.MODULE_NAME, version = "1.0.0", author = "xiaobin@shulie.io",description = "grpc 远程调用框架")
public class GrpcPlugin extends ModuleLifecycleAdapter implements ExtensionModule {
    @Override
    public void onActive() throws Throwable {

        /* *****GRPC client**** */
        enhanceNewCall("io.grpc.internal.ManagedChannelImpl$RealChannel");
        enhanceNewCall("io.grpc.internal.OobChannel");
        enhanceClientCallStart("io.grpc.internal.ClientCallImpl");
        /* *****GRPC client**** */

        /* *****GRPC server**** */
        enhanceServerStreamCreated("io.grpc.internal.ServerImpl$ServerTransportListenerImpl");
        //压测标传递
        enhanceServerPressurePass("io.grpc.stub.ServerCalls$UnaryServerCallHandler");
        //压测标重置
        enhanceServerPressureReset("io.grpc.stub.ServerCalls$UnaryServerCallHandler$UnaryServerCallListener");
        /* *****GRPC server**** */
    }

    private void enhanceNewCall(String className) {
        enhanceTemplate.enhance(this, className, new EnhanceCallback() {
            @Override
            public void doEnhance(InstrumentClass target) {
                InstrumentMethod newCallMethod = target.getDeclaredMethod("newCall", "io.grpc.MethodDescriptor", "io.grpc.CallOptions");
                newCallMethod.addInterceptor(Listeners.of(ChannelNewCallInterceptor.class));
            }
        });
    }

    private void enhanceClientCallStart(String className) {
        enhanceTemplate.enhance(this, className, new EnhanceCallback() {
            @Override
            public void doEnhance(InstrumentClass target) {
                InstrumentMethod startMethod = target.getDeclaredMethod("start", "io.grpc.ClientCall$Listener", "io.grpc.Metadata");
                startMethod.addInterceptor(Listeners.of(ClientCallStartInterceptor.class));
            }
        });
    }

    private void enhanceServerStreamCreated(String className) {
        enhanceTemplate.enhance(this, className, new EnhanceCallback() {
            @Override
            public void doEnhance(InstrumentClass target) {
                InstrumentMethod streamCreatedMethod = target.getDeclaredMethod("streamCreated",
                        "io.grpc.internal.ServerStream", "java.lang.String", "io.grpc.Metadata");
                streamCreatedMethod.addInterceptor(Listeners.of(ServerStreamCreatedInterceptor.class));
            }
        });
    }

    private void enhanceServerPressurePass(String className) {
        enhanceTemplate.enhance(this, className, new EnhanceCallback() {
            @Override
            public void doEnhance(InstrumentClass target) {
                InstrumentMethod streamCreatedMethod = target.getDeclaredMethod("startCall",
                        "io.grpc.ServerCall", "io.grpc.Metadata");
                streamCreatedMethod.addInterceptor(Listeners.of(ServerPressurePassInterceptor.class));
            }
        });
    }

    private void enhanceServerPressureReset(String className) {
        enhanceTemplate.enhance(this, className, new EnhanceCallback() {
            @Override
            public void doEnhance(InstrumentClass target) {
                InstrumentMethod constructor = target.getConstructors();
                constructor.addInterceptor(Listeners.of(ServerPreesureConstructorInterceptor.class));

                InstrumentMethod streamCreatedMethod = target.getDeclaredMethod("onHalfClose");
                streamCreatedMethod.addInterceptor(Listeners.of(ServerPreesureResetInterceptor.class));
            }
        });
    }

}
