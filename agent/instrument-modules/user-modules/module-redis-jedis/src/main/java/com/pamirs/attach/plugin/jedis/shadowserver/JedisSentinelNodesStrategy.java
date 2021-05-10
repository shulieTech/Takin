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

import com.pamirs.attach.plugin.common.datasource.redisserver.RedisServerNodesStrategy;
import com.pamirs.attach.plugin.jedis.RedisConstants;
import com.pamirs.attach.plugin.jedis.util.JedisConstructorConfig;
import com.shulie.instrument.simulator.api.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author qianfan
 * @package: com.pamirs.attach.plugin.lettuce.factory
 * @Date 2020/11/26 11:23 上午
 */
public class JedisSentinelNodesStrategy implements RedisServerNodesStrategy {

    private final static Logger LOGGER = LoggerFactory.getLogger(JedisSentinelNodesStrategy.class);

    @Override
    public List<String> match(Object obj) {
        List<String> nodes = new ArrayList<String>();
        try {
            JedisConstructorConfig jedisConfig = RedisConstants.jedisInstance.get(obj);
            if (CollectionUtils.isNotEmpty(jedisConfig.getSentinels())) {
                nodes.addAll(jedisConfig.getSentinels());
            }
        } catch (Throwable e) {
            LOGGER.error("", e);
        }
        return nodes;
    }

    public String getKey(String host, int port) {
        return host + ":" + port;
    }
}
