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
package com.pamirs.attach.plugin.lettuce.interceptor;

import com.pamirs.attach.plugin.lettuce.LettuceConstants;
import com.pamirs.pradar.interceptor.AroundInterceptor;
import com.shulie.instrument.simulator.api.listener.ext.Advice;
import com.shulie.instrument.simulator.api.reflect.Reflect;
import com.shulie.instrument.simulator.api.reflect.ReflectException;
import com.shulie.instrument.simulator.api.resource.DynamicFieldManager;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.cluster.RedisClusterClient;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * @Description
 * @Author xiaobin.zfb
 * @mail xiaobin@shulie.io
 * @Date 2020/9/7 11:44 上午
 */
public class RedisClientAttachRedisURIsInterceptor extends AroundInterceptor {
    @Resource
    protected DynamicFieldManager manager;

    @Override
    public void doAfter(Advice advice) {
        Object[] args = advice.getParameterArray();
        String methodName = advice.getBehavior().getName();
        Object target = advice.getTarget();
        Object result = advice.getReturnObj();
        if (target instanceof RedisClusterClient) {
            try {
                Iterable<RedisURI> initialUris = Reflect.on(target).get(LettuceConstants.REFLECT_FIELD_INITIAL_URIS);
                List<RedisURI> list = new ArrayList<RedisURI>();
                if (initialUris != null) {
                    Iterator<RedisURI> it = initialUris.iterator();
                    while (it.hasNext()) {
                        RedisURI redisURI = it.next();
                        list.add(redisURI);
                    }
                }
                manager.setDynamicField(result, LettuceConstants.DYNAMIC_FIELD_REDIS_URIS, list);
            } catch (ReflectException e) {
            }
        } else if (target instanceof RedisClient) {
            try {
                RedisURI redisURI = Reflect.on(target).get(LettuceConstants.REFLECT_FIELD_REDIS_URI);
                manager.setDynamicField(result, LettuceConstants.DYNAMIC_FIELD_REDIS_URIS, Arrays.asList(redisURI));
            } catch (ReflectException e) {
            }
        } else {
            final List<RedisURI> list = manager.getDynamicField(target, LettuceConstants.DYNAMIC_FIELD_REDIS_URIS);
            manager.setDynamicField(result, LettuceConstants.DYNAMIC_FIELD_REDIS_URIS, list);
            manager.setDynamicField(result, LettuceConstants.DYNAMIC_FIELD_LETTUCE_TARGET, target);
            manager.setDynamicField(result, LettuceConstants.DYNAMIC_FIELD_LETTUCE_METHOD, methodName);
            manager.setDynamicField(result, LettuceConstants.DYNAMIC_FIELD_LETTUCE_ARGS, args);
            manager.setDynamicField(result, LettuceConstants.DYNAMIC_FIELD_LETTUCE_RESULT, result);
        }
    }
}
