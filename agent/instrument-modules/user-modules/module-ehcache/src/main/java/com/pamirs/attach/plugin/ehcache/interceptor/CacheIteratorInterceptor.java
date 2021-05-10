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

import com.pamirs.attach.plugin.ehcache.Iter;
import com.pamirs.pradar.Pradar;
import com.pamirs.pradar.interceptor.ResultInterceptorAdaptor;
import com.shulie.instrument.simulator.api.listener.ext.Advice;
import org.ehcache.Cache;

import java.util.Iterator;

/**
 * @Description
 * @Author xiaobin.zfb
 * @mail xiaobin@shulie.io
 * @Date 2020/8/19 7:50 下午
 */
public class CacheIteratorInterceptor extends ResultInterceptorAdaptor {
    @Override
    protected Object getResult0(Advice advice) {
        Object result = advice.getReturnObj();
        if (!Pradar.isClusterTest()) {
            return result;
        }
        if (result instanceof Iter || !(result instanceof Iterator)) {
            return result;
        }

        Iterator<Cache.Entry> it = (Iterator<Cache.Entry>) result;
        Iter iter = new Iter(it);
        return iter;
    }
}
