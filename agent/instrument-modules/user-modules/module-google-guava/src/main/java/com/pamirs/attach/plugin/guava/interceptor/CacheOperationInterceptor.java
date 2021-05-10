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

import com.pamirs.pradar.AppNameUtils;
import com.pamirs.pradar.Pradar;
import com.pamirs.pradar.PradarSwitcher;
import com.pamirs.pradar.cache.ClusterTestCacheWrapperKey;
import com.pamirs.pradar.exception.PressureMeasureError;
import com.pamirs.pradar.interceptor.ParametersWrapperInterceptorAdaptor;
import com.shulie.instrument.simulator.api.listener.ext.Advice;

import java.util.*;

/**
 * @author wangjian
 * @since 2021/2/22 16:54
 */
public class CacheOperationInterceptor extends ParametersWrapperInterceptorAdaptor {

    @Override
    protected Object[] getParameter0(Advice advice) throws Throwable {
        Object[] parameterArray = advice.getParameterArray();
        /**
         * 压测状态为关闭,如果当前为压测流量则直接报错
         */
        if (!PradarSwitcher.isClusterTestEnabled()) {
            if (Pradar.isClusterTest()) {
                throw new PressureMeasureError(PradarSwitcher.PRADAR_SWITCHER_OFF + ":" + AppNameUtils.appName());
            }
            return parameterArray;
        }
        if (!Pradar.isClusterTest()) {
            // 非压测流量
            return parameterArray;
        }
        if (parameterArray.length == 0) {
            return parameterArray;
        }
        if (parameterArray[0] instanceof Map) {
            Set<Map.Entry> set = ((Map) parameterArray[0]).entrySet();
            HashMap<Object, Object> objectObjectHashMap = new HashMap<Object, Object>();
            for (Map.Entry o : set) {
                if (o.getKey() instanceof ClusterTestCacheWrapperKey) {
                    return parameterArray;
                }
                objectObjectHashMap.put(new ClusterTestCacheWrapperKey(o.getKey()), o.getValue());
            }
            parameterArray[0] = objectObjectHashMap;
        } else if (parameterArray[0] instanceof Iterable) {
            Iterable<?> objects = (Iterable<?>) parameterArray[0];
            Iterator<?> iterator = objects.iterator();
            ArrayList<Object> objects1 = new ArrayList<Object>();
            while (iterator.hasNext()) {
                Object next = iterator.next();
                if (next instanceof ClusterTestCacheWrapperKey) {
                    return parameterArray;
                }
                objects1.add(new ClusterTestCacheWrapperKey(next));
            }
            parameterArray[0] = objects1;
        } else {
            if (parameterArray[0] instanceof ClusterTestCacheWrapperKey) {
                return parameterArray;
            }
            parameterArray[0] = new ClusterTestCacheWrapperKey(parameterArray[0]);
        }
        return parameterArray;
    }
}
