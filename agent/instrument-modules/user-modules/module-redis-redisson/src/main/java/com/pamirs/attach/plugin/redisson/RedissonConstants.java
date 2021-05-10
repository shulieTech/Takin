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
package com.pamirs.attach.plugin.redisson;

import com.pamirs.pradar.MiddlewareType;

public final class RedissonConstants {

    public static final int PLUGIN_TYPE = MiddlewareType.TYPE_CACHE;

    public static final String PLUGIN_NAME = "redis-redisson";

    public static final String MIDDLEWARE_NAME = "redis";

    public static final String MODULE_NAME = "redis-redisson";

    public static final String REDISSON_SCOPE = "Redisson-Scope";
    public static final String REDISSON_TRACE_SCOPE = "Redisson-Trace-Scope";

    public static final String DYNAMIC_FIELD_CONFIG = "config";
    public static final String DYNAMIC_FIELD_SENTINEL_SERVERS_CONFIG = "sentinelServersConfig";
    public static final String DYNAMIC_FIELD_MASTER_SLAVE_SERVERS_CONFIG = "masterSlaveServersConfig";
    public static final String DYNAMIC_FIELD_SINGLE_SERVER_CONFIG = "singleServerConfig";
    public static final String DYNAMIC_FIELD_CLUSTER_SERVERS_CONFIG = "clusterServersConfig";
    public static final String DYNAMIC_FIELD_REPLICATED_SERVERS_CONFIG = "replicatedServersConfig";
}
