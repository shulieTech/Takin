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
import io.lettuce.core.ConnectionFuture;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import io.lettuce.core.sentinel.api.StatefulRedisSentinelConnection;

/**
 * @Author qianfan
 * @package: com.pamirs.attach.plugin.lettuce.factory
 * @Date 2020/11/26 11:52 上午
 */
public class RedisClientPressure extends RedisClient {

    RedisClient redisClient;

    public RedisClientPressure(RedisClient clusterClient) {
        this.redisClient = clusterClient;
    }

    public void check(){
        if (!Pradar.isClusterTest()) {
            throw new PressureMeasureError("lettuce RedisClient pressure connection get a business request.");
        }
    }

    @Override
    public StatefulRedisConnection<String, String> connect() {
        check();
        return redisClient.connect();
    }

    @Override
    public <K, V> StatefulRedisConnection<K, V> connect(RedisCodec<K, V> codec) {
        check();
        return redisClient.connect(codec);
    }

    @Override
    public StatefulRedisConnection<String, String> connect(RedisURI redisURI) {
        check();
        return redisClient.connect(redisURI);
    }

    @Override
    public <K, V> StatefulRedisConnection<K, V> connect(RedisCodec<K, V> codec, RedisURI redisURI) {
        check();
        return redisClient.connect(codec, redisURI);
    }

    @Override
    public <K, V> ConnectionFuture<StatefulRedisConnection<K, V>> connectAsync(RedisCodec<K, V> codec, RedisURI redisURI) {
        check();
        return redisClient.connectAsync(codec, redisURI);
    }

    @Override
    public StatefulRedisPubSubConnection<String, String> connectPubSub() {
        check();
        return redisClient.connectPubSub();
    }

    @Override
    public StatefulRedisPubSubConnection<String, String> connectPubSub(RedisURI redisURI) {
        check();
        return redisClient.connectPubSub(redisURI);
    }

    @Override
    public <K, V> StatefulRedisPubSubConnection<K, V> connectPubSub(RedisCodec<K, V> codec) {
        check();
        return redisClient.connectPubSub(codec);
    }

    @Override
    public <K, V> StatefulRedisPubSubConnection<K, V> connectPubSub(RedisCodec<K, V> codec, RedisURI redisURI) {
        check();
        return redisClient.connectPubSub(codec, redisURI);
    }

    @Override
    public <K, V> ConnectionFuture<StatefulRedisPubSubConnection<K, V>> connectPubSubAsync(RedisCodec<K, V> codec, RedisURI redisURI) {
        check();
        return redisClient.connectPubSubAsync(codec, redisURI);
    }

    @Override
    public StatefulRedisSentinelConnection<String, String> connectSentinel() {
        check();
        return redisClient.connectSentinel();
    }

    @Override
    public <K, V> StatefulRedisSentinelConnection<K, V> connectSentinel(RedisCodec<K, V> codec) {
        check();
        return redisClient.connectSentinel(codec);
    }

    @Override
    public StatefulRedisSentinelConnection<String, String> connectSentinel(RedisURI redisURI) {
        check();
        return redisClient.connectSentinel(redisURI);
    }

    @Override
    public <K, V> StatefulRedisSentinelConnection<K, V> connectSentinel(RedisCodec<K, V> codec, RedisURI redisURI) {
        check();
        return redisClient.connectSentinel(codec, redisURI);
    }
}
