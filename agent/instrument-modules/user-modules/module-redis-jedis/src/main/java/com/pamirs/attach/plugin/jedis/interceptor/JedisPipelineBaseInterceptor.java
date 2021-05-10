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
package com.pamirs.attach.plugin.jedis.interceptor;

import com.pamirs.attach.plugin.common.datasource.redisserver.RedisClientMediator;
import com.pamirs.pradar.Pradar;
import com.pamirs.pradar.interceptor.ParametersWrapperInterceptorAdaptor;
import com.pamirs.pradar.pressurement.ClusterTestUtils;
import com.pamirs.pradar.pressurement.agent.shared.service.GlobalConfig;
import com.shulie.instrument.simulator.api.listener.ext.Advice;

import java.util.Collection;

/**
 * @Author: guohz
 * @ClassName: JedisPipelineBaseInterceptor
 * @Package: com.pamirs.attach.plugin.redis.interceptor
 * @Date: 2019/8/27 12:30 下午
 * @Description:
 */
public class JedisPipelineBaseInterceptor extends ParametersWrapperInterceptorAdaptor {
    @Override
    public Object[] getParameter0(Advice advice) {
        Object[] args = advice.getParameterArray();
        ClusterTestUtils.validateClusterTest();
        if (!Pradar.isClusterTest()) {
            return args;
        }

        if (RedisClientMediator.isShadowDb()) {
            return args;
        }

        Collection<String> whiteList = GlobalConfig.getInstance().getCacheKeyWhiteList();

        if (args[0] instanceof String) {
            String key = (String) args[0];

            //白名单 忽略
            if (whiteList != null && whiteList.contains(key)) {
                return args;
            }

            if (!Pradar.isClusterTestPrefix(key)) {
                key = Pradar.addClusterTestPrefix(key);
                args[0] = key;
                return args;
            }

        } else if (args[0] instanceof byte[]) {
            String key = new String((byte[]) args[0]);

            //白名单 忽略
            if (whiteList != null && whiteList.contains(key)) {
                return args;
            }

            if (!Pradar.isClusterTestPrefix(key)) {
                key = Pradar.addClusterTestPrefix(key);
                args[0] = key.getBytes();
                return args;
            }
        } else if (args[0] instanceof byte[][]) {
            byte[][] keyBytes = (byte[][]) args[0];
            String key = new String(keyBytes[0]);

            //白名单 忽略
            if (whiteList != null && whiteList.contains(key)) {
                return args;
            }

            if (!Pradar.isClusterTestPrefix(key)) {
                key = Pradar.addClusterTestPrefix(key);
                keyBytes[0] = key.getBytes();
                args[0] = keyBytes;
                return args;
            }
        }
        return args;
    }
}
