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
package com.pamirs.attach.plugin.dbcp2;

import com.pamirs.attach.plugin.dbcp2.interceptor.DataSourceGetConnectionCutoffArgsInterceptor;
import com.pamirs.attach.plugin.dbcp2.interceptor.DataSourceGetConnectionCutoffInterceptor;
import com.pamirs.attach.plugin.dbcp2.utils.DataSourceWrapUtil;
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
 * @author xiaobin.zfb
 * @since 2020/8/17 10:13 上午
 */
@MetaInfServices(ExtensionModule.class)
@ModuleInfo(id = "dbcp2", version = "1.0.0", author = "xiaobin@shulie.io",description = "dbcp2数据源")
public class Dbcp2Plugin extends ModuleLifecycleAdapter implements ExtensionModule {
    private static Logger logger = LoggerFactory.getLogger(Dbcp2Plugin.class);

    @Override
    public void onActive() throws Throwable {
        enhanceTemplate.enhance(this, "org.apache.commons.dbcp2.BasicDataSource", new EnhanceCallback() {
            @Override
            public void doEnhance(InstrumentClass target) {
                InstrumentMethod getConnection = target.getDeclaredMethod("getConnection");
                getConnection.addInterceptor(Listeners.of(DataSourceGetConnectionCutoffInterceptor.class, "Dbcp2_Get_Connection_Scope", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

                InstrumentMethod getConnection0 = target.getDeclaredMethod("getConnection", "java.lang.String", "java.lang.String");
                getConnection0.addInterceptor(Listeners.of(DataSourceGetConnectionCutoffArgsInterceptor.class, "Dbcp2_Get_Connection_Scope", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

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
