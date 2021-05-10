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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2021/3/1 7:20 下午
 */
public class CachePutAllInterceptor extends ParametersWrapperInterceptorAdaptor {
    @Override
    protected Object[] getParameter0(Advice advice) throws Throwable {
        if (!Pradar.isClusterTest()) {
            return advice.getParameterArray();
        }
        Object[] args = advice.getParameterArray();
        if (ArrayUtils.isEmpty(args)) {
            return args;
        }
        if (!(args[0] instanceof Map)) {
            return args;
        }

        Map oldArg = (Map) args[0];
        Map map = buildMap(oldArg);

        boolean replace = false;
        Set<Map.Entry> entrySet = oldArg.entrySet();
        for (Map.Entry entry : entrySet) {
            if (entry.getKey() instanceof ClusterTestCacheWrapperKey) {
                map.put(entry.getKey(), entry.getValue());
            } else {
                ClusterTestCacheWrapperKey clusterTestCacheWrapperKey = new ClusterTestCacheWrapperKey(entry.getKey());
                map.put(clusterTestCacheWrapperKey, entry.getValue());
                replace = true;
            }
        }
        if (replace) {
            args[0] = map;
        }
        return args;
    }

    private Map buildMap(Map map) {
        if (map instanceof TreeMap) {
            return new TreeMap();
        } else if (map instanceof ConcurrentHashMap) {
            return new ConcurrentHashMap();
        } else {
            return new HashMap();
        }
    }
}
