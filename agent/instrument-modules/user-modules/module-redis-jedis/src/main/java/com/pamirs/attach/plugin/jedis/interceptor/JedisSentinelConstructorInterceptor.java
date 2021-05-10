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

import java.util.Set;

/**
 * @Author <a href="tangyuhan@shulie.io">yuhan.tang</a>
 * @package: com.pamirs.attach.plugin.jedis.interceptor
 * @Date 2020/12/2 4:43 下午
 */
public class JedisSentinelConstructorInterceptor extends AroundInterceptor {

    @Override
    public void doAfter(Advice advice) {
        Object[] args = advice.getParameterArray();
        Object target = advice.getTarget();
        JedisConstructorConfig config = new JedisConstructorConfig();
        if (null != args && args.length == 8) {
            config.setMaster((String) args[0]).
                    setSentinels((Set<String>) args[1]).
                    setPoolConfig((GenericObjectPoolConfig) args[2]).
                    setConnectionTimeout((Integer) args[3]).
                    setSoTimeout((Integer) args[4]).
                    setPassword((String) args[5]).
                    setDatabase((Integer) args[6]).
                    setClientName((String) args[7]).
                    setConstructorType(100);
        }
        RedisConstants.jedisInstance.put(target, config);
    }
}
