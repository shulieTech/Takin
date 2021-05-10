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

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSocketFactory;
import java.net.URI;

/**
 * @Author <a href="tangyuhan@shulie.io">yuhan.tang</a>
 * @package: com.pamirs.attach.plugin.jedis.interceptor
 * @Date 2020/12/1 2:32 下午
 */
public class JedisConstructorInterceptor extends AroundInterceptor {

    @Override
    public void doAfter(Advice advice) {
        Object[] args = advice.getParameterArray();
        Object target = advice.getTarget();
        JedisConstructorConfig config = new JedisConstructorConfig();
        if (args.length == 1) {
            config.setHost((String) args[0]).setConstructorType(1);
        } else if (args.length == 4 && args[0] instanceof String) {
            //"java.lang.String", "javax.net.ssl.SSLSocketFactory", "javax.net.ssl.SSLParameters", "javax.net.ssl.HostnameVerifier"
            config.setHost((String) args[0]).
            setSslSocketFactory((SSLSocketFactory) args[1]).
            setSslParameters((SSLParameters) args[2]).
            setHostnameVerifier((HostnameVerifier) args[3]).setConstructorType(2);
        } else if (args.length == 8) {
            //JedisPool(final GenericObjectPoolConfig poolConfig,
            //final String host, int port,final int connectionTimeout,
            //final int soTimeout, final String password,
            //final int database,final String clientName)
            config.setPoolConfig((GenericObjectPoolConfig) args[0]).
                    setHost((String) args[1]).
                    setPort((Integer) args[2]).
                    setConnectionTimeout((Integer) args[3]).
                    setSoTimeout((Integer) args[4]).
                    setPassword((String) args[5]).
                    setDatabase((Integer) args[6]).
                    setClientName((String) args[7]).setConstructorType(3);
        } else if (args.length == 12) {
            //JedisPool(final GenericObjectPoolConfig poolConfig, final String host,
            //int port,final int connectionTimeout, final int soTimeout,
            //final String password, final int database,final String clientName,
            //final boolean ssl, final SSLSocketFactory sslSocketFactory,
            //final SSLParameters sslParameters, final HostnameVerifier hostnameVerifier)
            config.setPoolConfig((GenericObjectPoolConfig) args[0]).
                    setHost((String) args[1]).
                    setPort((Integer) args[2]).
                    setConnectionTimeout((Integer) args[3]).
                    setSoTimeout((Integer) args[4]).
                    setPassword((String) args[5]).
                    setDatabase((Integer) args[6]).
                    setClientName((String) args[7]).
                    setSsl((Boolean) args[8]).
                    setSslSocketFactory((SSLSocketFactory) args[9]).
                    setSslParameters((SSLParameters) args[10]).
                    setHostnameVerifier((HostnameVerifier) args[11]).setConstructorType(4);
        } else if (args.length == 4) {
            //JedisPool(final GenericObjectPoolConfig poolConfig, final URI uri,
            //final int connectionTimeout, final int soTimeout)
            config.setPoolConfig((GenericObjectPoolConfig) args[0]).
                    setUri((URI) args[1]).
                    setConnectionTimeout((Integer) args[3]).
                    setSoTimeout((Integer) args[4]).setConstructorType(5);
        } else if (args.length == 7) {
            config.setPoolConfig((GenericObjectPoolConfig) args[0]).
                    setUri((URI) args[1]).
                    setConnectionTimeout((Integer) args[3]).
                    setSoTimeout((Integer) args[4]).
                    setSslSocketFactory((SSLSocketFactory) args[5]).
                    setSslParameters((SSLParameters) args[6]).
                    setHostnameVerifier((HostnameVerifier) args[7]).setConstructorType(6);
        }
        RedisConstants.jedisInstance.put(target, config);
    }
}
