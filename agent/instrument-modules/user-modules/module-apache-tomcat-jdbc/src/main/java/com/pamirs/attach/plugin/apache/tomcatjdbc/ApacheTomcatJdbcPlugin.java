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
package com.pamirs.attach.plugin.apache.tomcatjdbc;

import com.pamirs.attach.plugin.apache.tomcatjdbc.interceptor.TomcatJdbcDataSourceProxyGetConnectionInterceptor;
import com.pamirs.attach.plugin.apache.tomcatjdbc.util.DataSourceWrapUtil;
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
 * @author angju
 * @date 2020/8/11 15:01
 */
@MetaInfServices(ExtensionModule.class)
@ModuleInfo(id = ApacheTomcatJdbcConstants.MODULE_NAME, version = "1.0.0", author = "xiaobin@shulie.io",description = "tomcat jdbc 数据源")
public class ApacheTomcatJdbcPlugin extends ModuleLifecycleAdapter implements ExtensionModule {
    private static Logger logger = LoggerFactory.getLogger(ApacheTomcatJdbcPlugin.class);

    @Override
    public void onActive() throws Throwable {
        //jdbc pt table mechanism
        enhanceTemplate.enhance(this, "org.apache.tomcat.jdbc.pool.DataSourceProxy", new EnhanceCallback() {
            @Override
            public void doEnhance(InstrumentClass target) {
                InstrumentMethod getCmethod_1 = target.getDeclaredMethod("getConnection",
                        "java.lang.String", "java.lang.String");
                getCmethod_1.addInterceptor(Listeners.of(TomcatJdbcDataSourceProxyGetConnectionInterceptor.class, "Tomcat_Jdbc_Get_Connection_Scope", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

                InstrumentMethod getCmethod_2 = target.getDeclaredMethod("getConnection");
                getCmethod_2.addInterceptor(Listeners.of(TomcatJdbcDataSourceProxyGetConnectionInterceptor.class, "Tomcat_Jdbc_Get_Connection_Scope", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

                InstrumentMethod getCmethod_3 = target.getDeclaredMethod("getConnectionAsync");
                getCmethod_3.addInterceptor(Listeners.of(TomcatJdbcDataSourceProxyGetConnectionInterceptor.class, "Tomcat_Jdbc_Get_Connection_Scope", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

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
