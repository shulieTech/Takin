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
package com.pamirs.attach.plugin.c3p0;

import com.pamirs.attach.plugin.c3p0.interceptor.DataSourceGetConnectionArgsCutoffInterceptor;
import com.pamirs.attach.plugin.c3p0.interceptor.DataSourceGetConnectionCutoffInterceptor;
import com.pamirs.attach.plugin.c3p0.utils.DataSourceWrapUtil;
import com.pamirs.pradar.interceptor.Interceptors;
import com.shulie.instrument.simulator.api.ExtensionModule;
import com.shulie.instrument.simulator.api.ModuleInfo;
import com.shulie.instrument.simulator.api.ModuleLifecycleAdapter;
import com.shulie.instrument.simulator.api.instrument.EnhanceCallback;
import com.shulie.instrument.simulator.api.instrument.InstrumentClass;
import com.shulie.instrument.simulator.api.instrument.InstrumentMethod;
import com.shulie.instrument.simulator.api.listener.Listeners;
import com.shulie.instrument.simulator.api.resource.ReleaseResource;
import com.shulie.instrument.simulator.api.scope.ExecutionPolicy;
import org.kohsuke.MetaInfServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description
 * @Author xiaobin.zfb
 * @mail xiaobin@shulie.io
 * @Date 2020/8/17 10:13 上午
 */
@MetaInfServices(ExtensionModule.class)
@ModuleInfo(id = "c3p0", version = "1.0.0", author = "xiaobin@shulie.io",description = "c3p0数据源")
public class C3p0Plugin extends ModuleLifecycleAdapter implements ExtensionModule {
    private static Logger logger = LoggerFactory.getLogger(C3p0Plugin.class);

    @Override
    public void onActive() throws Throwable {
        enhanceTemplate.enhance(this, "com.mchange.v2.c3p0.impl.AbstractPoolBackedDataSource", new EnhanceCallback() {
            @Override
            public void doEnhance(InstrumentClass target) {
                InstrumentMethod getConnection = target.getDeclaredMethod("getConnection");
                getConnection.addInterceptor(Listeners.of(DataSourceGetConnectionCutoffInterceptor.class, "C3p0_Get_Connection_Scope", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

                InstrumentMethod getConnection0 = target.getDeclaredMethod("getConnection", "java.lang.String", "java.lang.String");
                getConnection0.addInterceptor(Listeners.of(DataSourceGetConnectionArgsCutoffInterceptor.class, "C3p0_Get_Connection_Scope", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            }
        });

        addReleaseResource(new ReleaseResource(null) {
            @Override
            public void release() {
                DataSourceWrapUtil.destroy();
            }
        });
    }
}
