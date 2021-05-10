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
package com.pamirs.attach.plugin.jedis.interceptor;

import com.pamirs.attach.plugin.jedis.shadowserver.JedisFactory;
import com.pamirs.pradar.CutOffResult;
import com.pamirs.pradar.exception.PressureMeasureError;
import com.pamirs.pradar.interceptor.CutoffInterceptorAdaptor;
import com.shulie.instrument.simulator.api.listener.ext.Advice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPool;

/**
 * @Author qianfan
 * @package: com.pamirs.attach.plugin.jedis.interceptor
 * @Date 2020/11/27 10:28 下午
 */
public class JedisPoolGetResourceInterceptor extends CutoffInterceptorAdaptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(JedisPoolGetResourceInterceptor.class.getName());

    @Override
    public CutOffResult cutoff0(Advice advice) {
        Object target = advice.getTarget();
        try {
            JedisPool client = (JedisPool) JedisFactory.getFactory().getClient(target);
            return CutOffResult.cutoff(client.getResource());
        } catch (PressureMeasureError e) {
            throw e;
        } catch (Throwable e) {
            LOGGER.error(e.getMessage(), e);
        }
        return CutOffResult.passed();
    }
}
