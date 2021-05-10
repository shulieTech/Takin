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
import com.pamirs.pradar.interceptor.ResultInterceptorAdaptor;
import com.pamirs.pradar.pressurement.ClusterTestUtils;
import com.pamirs.pradar.pressurement.agent.shared.service.GlobalConfig;
import com.shulie.instrument.simulator.api.listener.ext.Advice;

import java.util.Collection;

/**
 * @Author: guohz
 * @ClassName: SpringStartRedisListOperatorInterceptor
 * @Package: com.pamirs.attach.plugin.redis.interceptor
 * @Date: 2019/8/27 10:38 上午
 * @Description: Spring Redis Template getKey enhance
 */
public class SpringStartRedisListOperatorInterceptor extends ResultInterceptorAdaptor {

    @Override
    public Object getResult0(Advice advice) {
        Object[] args = advice.getParameterArray();
        Object result = advice.getReturnObj();
        ClusterTestUtils.validateClusterTest();
        if (!Pradar.isClusterTest()) {
            return result;
        }
        if (RedisClientMediator.isShadowDb()) {
            return args;
        }

        String key = (String) result;

        Collection<String> whiteList = GlobalConfig.getInstance().getCacheKeyWhiteList();

        //白名单 忽略
        if (whiteList != null && whiteList.contains(key)) {
            return key;
        }

        if (!Pradar.isClusterTestPrefix(key)) {
            key = Pradar.addClusterTestPrefix(key);
            return key;
        }
        return result;
    }
}
