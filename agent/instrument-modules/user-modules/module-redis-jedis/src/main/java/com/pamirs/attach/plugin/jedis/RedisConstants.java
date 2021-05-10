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
package com.pamirs.attach.plugin.jedis;

import com.pamirs.attach.plugin.jedis.util.JedisConstructorConfig;
import com.pamirs.pradar.MiddlewareType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public interface RedisConstants {

    int PLUGIN_TYPE = MiddlewareType.TYPE_CACHE;

    String PLUGIN_NAME = "redis-jedis";

    String MIDDLEWARE_NAME = "redis";

    Map<Object, JedisConstructorConfig> jedisInstance = new ConcurrentHashMap<Object, JedisConstructorConfig>();

    List<String> registerShadowNodes = new ArrayList<String>();
}
