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
import com.pamirs.pradar.interceptor.AroundInterceptor;
import com.shulie.instrument.simulator.api.ProcessController;
import com.shulie.instrument.simulator.api.listener.ext.Advice;
import net.sf.ehcache.Element;
import org.apache.commons.lang.ArrayUtils;

import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2021/3/1 7:20 下午
 */
public class CacheGetAllRemoveAllInterceptor extends AroundInterceptor {
    @Override
    public void doBefore(Advice advice) throws Throwable {
        if (!Pradar.isClusterTest()) {
            return;
        }
        Object[] args = advice.getParameterArray();
        if (ArrayUtils.isEmpty(args)) {
            return;
        }

        if (args[0] instanceof Set) {
            Set argsSet = (Set) args[0];
            Set set = buildSet(argsSet);

            boolean replace = false;
            for (Object key : argsSet) {
                if (key instanceof ClusterTestCacheWrapperKey) {
                    set.add(key);
                } else {
                    ClusterTestCacheWrapperKey clusterTestCacheWrapperKey = new ClusterTestCacheWrapperKey(key);
                    set.add(clusterTestCacheWrapperKey);
                    replace = true;
                }
            }
            if (replace) {
                advice.changeParameter(0, set);
            }
        } else if (args[0] instanceof Collection) {
            Collection coll = (Collection) args[0];
            List list = new ArrayList();
            boolean replace = false;
            for (Object key : coll) {
                if (key instanceof ClusterTestCacheWrapperKey) {
                    list.add(key);
                } else {
                    ClusterTestCacheWrapperKey clusterTestCacheWrapperKey = new ClusterTestCacheWrapperKey(key);
                    list.add(clusterTestCacheWrapperKey);
                    replace = true;
                }
            }
            if (replace) {
                advice.changeParameter(0, list);
            }
        }
    }

    private Set buildSet(Set set) {
        if (set instanceof TreeSet) {
            return new TreeSet();
        } else if (set instanceof CopyOnWriteArraySet) {
            return new CopyOnWriteArraySet();
        } else {
            return new HashSet();
        }
    }

    @Override
    public void doAfter(Advice advice) throws Throwable {
        Object result = advice.getReturnObj();
        if (!(result instanceof Map)) {
            return;
        }

        Map map = (Map) advice.getReturnObj();
        Map resultMap = new HashMap();
        Set<Map.Entry> set = map.entrySet();

        boolean replace = false;
        for (Map.Entry entry : set) {
            Object key = entry.getKey();
            if (key instanceof ClusterTestCacheWrapperKey) {
                key = ((ClusterTestCacheWrapperKey) key).getKey();
                replace = true;
            }

            Object value = entry.getValue();
            if (value instanceof Element) {
                Element element = (Element) value;
                if (element.getObjectKey() instanceof ClusterTestCacheWrapperKey) {
                    value = new Element(((ClusterTestCacheWrapperKey) element.getObjectKey()).getKey()
                            , element.getObjectValue()
                            , element.getVersion()
                            , element.getCreationTime()
                            , element.getLastAccessTime()
                            , element.getHitCount()
                            , element.usesCacheDefaultLifespan()
                            , element.getTimeToLive()
                            , element.getTimeToIdle()
                            , element.getLastUpdateTime());
                    replace = true;
                }
            }
            resultMap.put(key, value);
        }

        if (replace) {
            ProcessController.returnImmediately(resultMap);
        }
    }
}
