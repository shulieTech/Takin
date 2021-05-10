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
import io.lettuce.core.RedisURI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.Arrays;

/**
 * @Description
 * @Author xiaobin.zfb
 * @mail xiaobin@shulie.io
 * @Date 2020/9/7 11:38 上午
 */
public class RedisClientConstructorInterceptor extends AroundInterceptor {
    private final static Logger logger = LoggerFactory.getLogger(RedisClientConstructorInterceptor.class.getName());

    @Resource
    protected DynamicFieldManager manager;

    @Override
    public void doBefore(Advice advice) {
        Object[] args = advice.getParameterArray();
        Object target = advice.getTarget();
        try {
            if (!validate(target, args)) {
                return;
            }

            final RedisURI redisURI = (RedisURI) args[1];
            manager.setDynamicField(target, LettuceConstants.DYNAMIC_FIELD_REDIS_URIS, Arrays.asList(redisURI));
        } catch (Throwable t) {
            if (logger.isWarnEnabled()) {
                logger.warn("Failed to BEFORE process. {}", t.getMessage(), t);
            }
        }
    }

    private boolean validate(final Object target, final Object[] args) {
        if (args == null || args.length < 2 || args[1] == null) {
            return false;
        }

        if (!(args[1] instanceof RedisURI)) {
            return false;
        }
        return true;
    }
}
