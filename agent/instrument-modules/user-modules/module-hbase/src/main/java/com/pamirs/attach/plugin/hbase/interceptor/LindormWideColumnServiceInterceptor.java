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
package com.pamirs.attach.plugin.hbase.interceptor;

import com.pamirs.pradar.Pradar;
import com.pamirs.pradar.interceptor.ParametersWrapperInterceptorAdaptor;
import com.pamirs.pradar.pressurement.ClusterTestUtils;
import com.pamirs.pradar.pressurement.agent.shared.service.GlobalConfig;
import com.shulie.instrument.simulator.api.listener.ext.Advice;

/**
 * @author jirenhe | jirenhe@shulie.io
 * @since 2021/04/02 2:16 下午
 */
public class LindormWideColumnServiceInterceptor extends ParametersWrapperInterceptorAdaptor {


    @Override
    public Object[] getParameter0(Advice advice) throws Throwable {
        ClusterTestUtils.validateClusterTest();
        Object[] args = advice.getParameterArray();
        Object arg0 = args[0];
        if (!(arg0 instanceof String)) {
            return args;
        }
        if (GlobalConfig.getInstance().isShadowHbaseServer()) {
            return args;
        }
        if (!Pradar.isClusterTest()) {
            return args;
        }
        String tableName = (String)arg0;
        if (tableName.startsWith("hbase") || tableName.startsWith(Pradar.CLUSTER_TEST_PREFIX)) {
            return args;
        }
        tableName = Pradar.CLUSTER_TEST_PREFIX + tableName;
        args[0] = tableName;
        return args;
    }
}
