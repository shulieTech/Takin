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
package com.pamirs.attach.plugin.lettuce.shadowserver;

import com.pamirs.attach.plugin.common.datasource.redisserver.RedisServerNodesStrategy;
import com.pamirs.attach.plugin.lettuce.LettuceConstants;
import com.shulie.instrument.simulator.api.resource.DynamicFieldManager;
import io.lettuce.core.RedisURI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @Author qianfan
 * @package: com.pamirs.attach.plugin.lettuce.factory
 * @Date 2020/11/26 11:23 上午
 */
public class LettuceMasterStrategy implements RedisServerNodesStrategy {

    private final static Logger LOGGER = LoggerFactory.getLogger(LettuceMasterStrategy.class);
    private DynamicFieldManager manager;

    public LettuceMasterStrategy(DynamicFieldManager manager) {
        this.manager = manager;
    }


    @Override
    public List<String> match(Object obj) {
        List<String> nodes = new ArrayList<String>();
        try {
            List<RedisURI> redisURIS = manager.removeField(obj, LettuceConstants.DYNAMIC_FIELD_REDIS_URIS);
            iterator(redisURIS.iterator(), nodes);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return nodes;
    }

    private void iterator(Iterator<RedisURI> iterator, List<String> nodes) {
        while (iterator.hasNext()) {
            RedisURI redisURI = iterator.next();
            if (null == redisURI.getHost()) {
                List<RedisURI> sentinels = redisURI.getSentinels();
                for (RedisURI sentinel : sentinels) {
                    nodes.add(getKey(sentinel));
                }
            } else {
                nodes.add(getKey(redisURI));
            }
        }
    }

    public String getKey(RedisURI redisURI) {
        if (redisURI == null) {
            return null;
        }
        String host = redisURI.getHost();
        //int database = redisURI.getDatabase();
        int port = redisURI.getPort();
        return host + ":" + port;
    }
}
