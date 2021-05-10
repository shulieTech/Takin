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

import com.pamirs.pradar.Pradar;
import com.pamirs.pradar.exception.PressureMeasureError;
import io.lettuce.core.cluster.RedisClusterClient;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.cluster.pubsub.StatefulRedisClusterPubSubConnection;
import io.lettuce.core.codec.RedisCodec;

/**
 * @Author qianfan
 * @package: com.pamirs.attach.plugin.lettuce.factory
 * @Date 2020/11/26 11:52 上午
 */
public class RedisClusterClientPressure extends RedisClusterClient {

    RedisClusterClient clusterClient;

    public RedisClusterClientPressure(RedisClusterClient clusterClient) {
        this.clusterClient = clusterClient;
    }

    public void check() {
        if (!Pradar.isClusterTest()) {
            throw new PressureMeasureError("lettuce RedisClusterClient pressure connection get a business request.");
        }
    }

    @Override
    public StatefulRedisClusterConnection<String, String> connect() {
        check();
        return clusterClient.connect();
    }

    @Override
    public <K, V> StatefulRedisClusterConnection<K, V> connect(RedisCodec<K, V> codec) {
        check();
        return clusterClient.connect(codec);
    }

    @Override
    public StatefulRedisClusterPubSubConnection<String, String> connectPubSub() {
        check();
        return clusterClient.connectPubSub();
    }

    @Override
    public <K, V> StatefulRedisClusterPubSubConnection<K, V> connectPubSub(RedisCodec<K, V> codec) {
        check();
        return clusterClient.connectPubSub(codec);
    }

}
