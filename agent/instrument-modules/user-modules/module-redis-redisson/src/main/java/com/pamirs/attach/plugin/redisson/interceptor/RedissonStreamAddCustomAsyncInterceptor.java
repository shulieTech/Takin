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
package com.pamirs.attach.plugin.redisson.interceptor;

import com.pamirs.pradar.Pradar;
import com.pamirs.pradar.pressurement.ClusterTestUtils;
import com.pamirs.pradar.pressurement.agent.shared.service.GlobalConfig;
import com.shulie.instrument.simulator.api.listener.ext.Advice;

/**
 * @Description
 * @Author xiaobin.zfb
 * @mail xiaobin@shulie.io
 * @Date 2020/9/8 3:49 下午
 */
public class RedissonStreamAddCustomAsyncInterceptor extends ClusterTestArgsWrapperInterceptor {
    @Override
    protected Object[] getParameter0(Advice advice) {
        Object[] args = advice.getParameterArray();
        ClusterTestUtils.validateClusterTest();

        if (!Pradar.isClusterTest()) {
            return args;
        }
        if (GlobalConfig.getInstance().isShadowDbRedisServer()) {
            return args;
        }
        if (args.length < 2) {
            return args;
        }
        Object key = args[0];
        args[0] = toClusterTestKey(key);

        return args;
    }

}
