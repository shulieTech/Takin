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
package com.pamirs.attach.plugin.ehcache.interceptor;

import com.pamirs.pradar.Pradar;
import com.pamirs.pradar.cache.ClusterTestCacheWrapperKey;
import com.pamirs.pradar.interceptor.ParametersWrapperInterceptorAdaptor;
import com.shulie.instrument.simulator.api.listener.ext.Advice;
import org.apache.commons.lang.ArrayUtils;

/**
 * @Description
 * @Author xiaobin.zfb
 * @mail xiaobin@shulie.io
 * @Date 2020/8/19 7:50 下午
 */
public class CacheKeyInterceptor extends ParametersWrapperInterceptorAdaptor {
    @Override
    protected Object[] getParameter0(Advice advice) {
        if (!Pradar.isClusterTest()) {
            return advice.getParameterArray();
        }
        Object[] args = advice.getParameterArray();
        if (ArrayUtils.isEmpty(args)) {
            return args;
        }

        Object key = advice.getParameterArray()[0];
        if (key instanceof ClusterTestCacheWrapperKey) {
            return args;
        }

        args[0] = new ClusterTestCacheWrapperKey(args[0]);
        return args;
    }
}
