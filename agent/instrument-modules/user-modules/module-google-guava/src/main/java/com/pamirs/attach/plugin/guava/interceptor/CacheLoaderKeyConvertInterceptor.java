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
package com.pamirs.attach.plugin.guava.interceptor;

import com.pamirs.pradar.cache.ClusterTestCacheWrapperKey;
import com.pamirs.pradar.interceptor.ParametersWrapperInterceptorAdaptor;
import com.shulie.instrument.simulator.api.listener.ext.Advice;

/**
 * @author wangjian
 * @since 2021/3/12 18:09
 */
public class CacheLoaderKeyConvertInterceptor extends ParametersWrapperInterceptorAdaptor {
    @Override
    protected Object[] getParameter0(Advice advice) {
        Object[] args = advice.getParameterArray();
        if (null != args && args.length > 0 && args[0] instanceof ClusterTestCacheWrapperKey) {
            args[0] = ((ClusterTestCacheWrapperKey) args[0]).getKey();
        }
        return args;
    }
}
