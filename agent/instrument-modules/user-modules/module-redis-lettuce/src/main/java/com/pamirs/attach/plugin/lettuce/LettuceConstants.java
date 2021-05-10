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
package com.pamirs.attach.plugin.lettuce;

import com.pamirs.pradar.MiddlewareType;

public interface LettuceConstants {

    int PLUGIN_TYPE = MiddlewareType.TYPE_CACHE;
    String PLUGIN_NAME = "redis-lettuce";

    String MODULE_NAME = "redis-lettuce";

    String MIDDLEWARE_NAME = "redis";

    String REFLECT_FIELD_REDIS_URI = "redisURI";
    String REFLECT_FIELD_INITIAL_URIS = "initialUris";
    String REFLECT_FIELD_CONNECTION = "connection";
    String REFLECT_FIELD_CHANNEL_WRITER = "channelWriter";
    String REFLECT_FIELD_CHANNEL = "channel";
    String REFLECT_FIELD_KEYS = "keys";

    String DYNAMIC_FIELD_REDIS_URIS = "redisURIs";
    String DYNAMIC_FIELD_LETTUCE_TARGET = "lettuceTarget";
    String DYNAMIC_FIELD_LETTUCE_METHOD = "lettuceMethod";
    String DYNAMIC_FIELD_LETTUCE_ARGS = "lettuceArgs";
    String DYNAMIC_FIELD_LETTUCE_RESULT = "lettuceResult";

    String CONNECT = "connect";
    String CONNECT_ASYNC = "connectAsync";
    String CONNECT_PUB_SUB = "connectPubSub";
    String CONNECT_PUB_SUB_ASYNC = "connectPubSubAsync";
    String CONNECT_TO_NODE = "connectToNode";
    String CONNECT_TO_NODE_ASYNC = "connectToNodeAsync";
    String CONNECT_PUB_SUB_TO_NODE_ASYNC = "connectPubSubToNodeAsync";
    String CONNECT_CLUSTER_ASYNC = "connectClusterAsync";
    String CONNECT_SENTINEL = "connectSentinel";
    String CONNECT_SENTINEL_ASYNC = "connectSentinelAsync";


    ThreadLocal<Boolean> masterSlave = new ThreadLocal<Boolean>() {
        @Override
        protected Boolean initialValue() {
            return true;
        }
    };

    String ADDRESS_UNKNOW = "unknow";
}
