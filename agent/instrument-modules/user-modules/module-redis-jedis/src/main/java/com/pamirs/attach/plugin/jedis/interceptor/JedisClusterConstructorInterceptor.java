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

import com.pamirs.attach.plugin.jedis.RedisConstants;
import com.pamirs.attach.plugin.jedis.util.JedisConstructorConfig;
import com.pamirs.pradar.interceptor.AroundInterceptor;
import com.shulie.instrument.simulator.api.listener.ext.Advice;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisClusterHostAndPortMap;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSocketFactory;
import java.util.Set;

/**
 * @Author <a href="tangyuhan@shulie.io">yuhan.tang</a>
 * @package: com.pamirs.attach.plugin.jedis.interceptor
 * @Date 2020/12/2 4:43 下午
 */
public class JedisClusterConstructorInterceptor extends AroundInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(JedisClusterConstructorInterceptor.class.getName());

    @Override
    public void doAfter(Advice advice) {
        Object[] args = advice.getParameterArray();
        Object target = advice.getTarget();
        JedisConstructorConfig config = new JedisConstructorConfig();
        if (null != args && args.length == 11) {
            config.setSetNodes((Set<HostAndPort>) args[0]).
                    setPoolConfig((GenericObjectPoolConfig) args[1]).
                    setConnectionTimeout((Integer) args[2]).
                    setSoTimeout((Integer) args[3]).
                    setPassword((String) args[4]).
                    setClientName((String) args[5]).
                    setSsl((Boolean) args[6]).
                    setSslSocketFactory((SSLSocketFactory) args[7]).
                    setSslParameters((SSLParameters) args[8]).
                    setHostnameVerifier((HostnameVerifier) args[9]).
                    setJedisMap((JedisClusterHostAndPortMap) args[10]).
                    setConstructorType(200);
        } else if (args.length == 5) {
            config.setSetNodes((Set<HostAndPort>) args[0]).
                    setPoolConfig((GenericObjectPoolConfig) args[1]).
                    setConnectionTimeout((Integer) args[2]).
                    setSoTimeout((Integer) args[3]).
                    setPassword((String) args[4]).
                    setConstructorType(201);
        }
        RedisConstants.jedisInstance.put(target, config);
    }

}
