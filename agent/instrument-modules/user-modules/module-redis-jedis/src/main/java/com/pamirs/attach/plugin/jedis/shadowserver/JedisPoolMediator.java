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
package com.pamirs.attach.plugin.jedis.shadowserver;

import com.pamirs.attach.plugin.common.datasource.redisserver.RedisClientMediator;
import redis.clients.jedis.JedisPool;

/**
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/12/21 9:34 下午
 */
public class JedisPoolMediator extends RedisClientMediator<JedisPool> {
    public JedisPoolMediator(JedisPool businessRedisClient, JedisPool performanceRedisClient) {
        super(businessRedisClient, performanceRedisClient);
    }

    public JedisPoolMediator(JedisPool performanceRedisClient, JedisPool businessRedisClient, boolean useKey) {
        super(performanceRedisClient, businessRedisClient, useKey);
    }

    @Override
    public void destroy() {
        if (performanceRedisClient != null) {
            try {
                performanceRedisClient.destroy();
            } catch (Throwable e) {
                logger.error("SIMULATOR: jedis pool close err! ", e);
            }
            performanceRedisClient = null;
        }
    }
}
