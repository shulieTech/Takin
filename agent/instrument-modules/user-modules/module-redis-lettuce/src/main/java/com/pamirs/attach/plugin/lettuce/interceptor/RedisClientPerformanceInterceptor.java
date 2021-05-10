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
import com.pamirs.attach.plugin.lettuce.shadowserver.LettuceFactory;
import com.pamirs.pradar.exception.PressureMeasureError;
import com.pamirs.pradar.interceptor.ResultInterceptorAdaptor;
import com.shulie.instrument.simulator.api.listener.ext.Advice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author qianfan
 * @package: com.pamirs.attach.plugin.lettuce.interceptor
 * @Date 2020/11/26 3:47 下午
 */
public class RedisClientPerformanceInterceptor extends ResultInterceptorAdaptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisClientPerformanceInterceptor.class.getName());

    @Override
    protected Object getResult0(Advice advice) {
        Object[] args = advice.getParameterArray();
        String methodName = advice.getBehavior().getName();
        Object target = advice.getTarget();
        Object result = advice.getReturnObj();
        if (!LettuceConstants.masterSlave.get()) {
            return result;
        }
        try {
            // TODO 这里无论如何都必须初始化成功。
            // 1、判断影子库还是影子表模式
            // 2、影子库模式无论是业务流量还是压测流量都需要创建协调者RedisClient
            return LettuceFactory.getFactory().getClient(target, methodName, args, result);
        } catch (PressureMeasureError e) {
            throw e;
        } catch (Throwable e) {
            LOGGER.error(e.getMessage(), e);
        }
        return result;
    }
}
