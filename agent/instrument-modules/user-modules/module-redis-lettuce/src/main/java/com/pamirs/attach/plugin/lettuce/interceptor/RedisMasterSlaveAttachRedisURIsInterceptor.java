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
import com.shulie.instrument.simulator.api.resource.DynamicFieldManager;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * @Author <a href="tangyuhan@shulie.io">yuhan.tang</a>
 * @package: com.pamirs.attach.plugin.lettuce.interceptor
 * @Date 2020/12/7 9:50 下午
 */
public class RedisMasterSlaveAttachRedisURIsInterceptor extends AroundInterceptor {
    @Resource
    protected DynamicFieldManager manager;

    @Override
    public void doBefore(Advice advice) {
        Object[] args = advice.getParameterArray();
        if (!(args[0] instanceof RedisClient)) {
            return;
        }
        LettuceConstants.masterSlave.set(false);
        if (args[2] instanceof RedisURI) {
            manager.setDynamicField(args[0], LettuceConstants.DYNAMIC_FIELD_REDIS_URIS, Collections.singletonList((RedisURI) args[2]));
        } else {
            manager.setDynamicField(args[0], LettuceConstants.DYNAMIC_FIELD_REDIS_URIS, (List<RedisURI>) args[2]);
        }
    }

    @Override
    public void doAfter(Advice advice) {
        Object[] args = advice.getParameterArray();
        String methodName = advice.getBehavior().getName();
        Object target = advice.getTarget();
        Object result = advice.getReturnObj();
        manager.setDynamicField(result, LettuceConstants.DYNAMIC_FIELD_LETTUCE_RESULT, result);
        if (args[2] instanceof RedisURI) {
            manager.setDynamicField(result, LettuceConstants.DYNAMIC_FIELD_REDIS_URIS, Collections.singletonList((RedisURI) args[2]));
        } else {
            manager.setDynamicField(result, LettuceConstants.DYNAMIC_FIELD_REDIS_URIS, (List<RedisURI>) args[2]);
        }
        manager.setDynamicField(result, LettuceConstants.DYNAMIC_FIELD_LETTUCE_TARGET, target);
        manager.setDynamicField(result, LettuceConstants.DYNAMIC_FIELD_LETTUCE_METHOD, methodName);
        manager.setDynamicField(result, LettuceConstants.DYNAMIC_FIELD_LETTUCE_ARGS, args);
    }
}
