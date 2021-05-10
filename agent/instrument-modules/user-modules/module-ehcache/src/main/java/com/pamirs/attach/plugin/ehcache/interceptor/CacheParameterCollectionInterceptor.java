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
import net.sf.ehcache.Element;
import org.apache.commons.lang.ArrayUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2021/3/3 11:15 上午
 */
public class CacheParameterCollectionInterceptor extends ParametersWrapperInterceptorAdaptor {
    @Override
    protected Object[] getParameter0(Advice advice) throws Throwable {
        Object[] args = advice.getParameterArray();
        if (!Pradar.isClusterTest()) {
            return args;
        }
        if (ArrayUtils.isEmpty(args)) {
            return args;
        }

        Object key = advice.getParameterArray()[0];
        if (!(key instanceof Collection)) {
            return args;
        }
        Collection coll = (Collection) key;
        List list = new ArrayList();
        boolean replace = false;
        for (Object obj : coll) {
            if (obj instanceof Element) {
                Element element = (Element) obj;
                if (!(element.getObjectKey() instanceof ClusterTestCacheWrapperKey)) {
                    Element ele = new Element(new ClusterTestCacheWrapperKey(element.getObjectKey())
                            , element.getObjectValue()
                            , element.getVersion()
                            , element.getCreationTime()
                            , element.getLastAccessTime()
                            , element.getHitCount()
                            , element.usesCacheDefaultLifespan()
                            , element.getTimeToLive()
                            , element.getTimeToIdle()
                            , element.getLastUpdateTime());
                    list.add(ele);
                    replace = true;
                } else {
                    list.add(element);
                }
            } else if (!(key instanceof ClusterTestCacheWrapperKey)) {
                list.add(new ClusterTestCacheWrapperKey(obj));
                replace = true;
            } else {
                list.add(obj);
            }
        }
        if (replace) {
            args[0] = list;
        }
        return args;
    }
}
