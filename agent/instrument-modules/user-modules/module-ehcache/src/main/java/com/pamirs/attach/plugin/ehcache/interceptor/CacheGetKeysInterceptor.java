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

import com.pamirs.pradar.cache.ClusterTestCacheWrapperKey;
import com.pamirs.pradar.interceptor.ResultInterceptorAdaptor;
import com.shulie.instrument.simulator.api.listener.ext.Advice;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2021/3/2 8:08 下午
 */
public class CacheGetKeysInterceptor extends ResultInterceptorAdaptor {
    @Override
    protected Object getResult0(Advice advice) {
        Object result = advice.getReturnObj();
        if (!(result instanceof List)) {
            return result;
        }

        List keys = (List) result;
        List list = new ArrayList();
        boolean replace = false;
        for (Object key : keys) {
            if (key instanceof ClusterTestCacheWrapperKey) {
                list.add(((ClusterTestCacheWrapperKey) key).getKey());
                replace = true;
            } else {
                list.add(key);
            }
        }

        if (replace) {
            return list;
        }
        return result;
    }
}
