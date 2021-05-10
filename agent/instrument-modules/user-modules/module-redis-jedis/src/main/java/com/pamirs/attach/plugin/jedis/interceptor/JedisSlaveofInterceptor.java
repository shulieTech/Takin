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
import com.pamirs.pradar.Pradar;
import com.pamirs.pradar.exception.PressureMeasureError;
import com.pamirs.pradar.interceptor.AroundInterceptor;
import com.shulie.instrument.simulator.api.listener.ext.Advice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Client;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;

/**
 * @Author <a href="tangyuhan@shulie.io">yuhan.tang</a>
 * @package: com.pamirs.attach.plugin.jedis.interceptor
 * @Date 2020/12/8 7:07 下午
 */
public class JedisSlaveofInterceptor extends AroundInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(JedisPoolGetResourceInterceptor.class.getName());

    @Override
    public void doAfter(Advice advice) {
        Object target = advice.getTarget();
        if (!Pradar.isClusterTest()) {
            return;
        }
        try {
            if (target instanceof Jedis) {
                Jedis jedis = (Jedis) target;
                Client client = jedis.getClient();
                HostAndPort hostAndPort = JedisFactory.getFactory().getMaster(client.getHost() + ":" + client.getPort());
                if (null != hostAndPort) {
                    jedis.slaveof(hostAndPort.getHost(), hostAndPort.getPort());
                }
            }
        } catch (PressureMeasureError e) {
            throw e;
        } catch (Throwable e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}
