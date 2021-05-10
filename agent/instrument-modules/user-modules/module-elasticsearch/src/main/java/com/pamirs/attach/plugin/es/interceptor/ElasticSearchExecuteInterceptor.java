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
package com.pamirs.attach.plugin.es.interceptor;

import com.pamirs.pradar.Pradar;
import com.pamirs.pradar.exception.PressureMeasureError;
import com.pamirs.pradar.interceptor.ParametersWrapperInterceptorAdaptor;
import com.pamirs.pradar.pressurement.ClusterTestUtils;
import com.pamirs.pradar.pressurement.agent.shared.service.GlobalConfig;
import com.shulie.instrument.simulator.api.listener.ext.Advice;

/**
 * @Auther: vernon
 * @Date: 2020/8/13 11:40
 * @Description:
 */
public class ElasticSearchExecuteInterceptor extends ParametersWrapperInterceptorAdaptor {
    @Override
    public Object[] getParameter0(Advice advice) {
        Object[] args = advice.getParameterArray();
        ClusterTestUtils.validateClusterTest();
        if (!Pradar.isClusterTest()) {
            return args;
        }
        if (GlobalConfig.getInstance().isShadowEsServer()) {
            return args;
        }
        if (args != null && args.length == 3) {
            String index = (String) args[0];
            /**
             * 当索引在白名单中并且是只读接口,则走业务索引
             */
            if (GlobalConfig.getInstance().getSearchWhiteList().contains(index)) {
                if ("prepareGet".equals(advice.getBehavior().getName())) {
                    return args;
                } else {
                    throw new PressureMeasureError("Cluster Test request can't write business index !");
                }
            } else {
                if (!Pradar.isClusterTestPrefix(index)) {
                    index = Pradar.addClusterTestPrefixLower(index);
                    args[0] = index;
                }
            }
        }
        return args;
    }
}
