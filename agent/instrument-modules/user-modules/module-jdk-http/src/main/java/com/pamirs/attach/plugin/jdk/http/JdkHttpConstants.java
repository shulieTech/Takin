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
package com.pamirs.attach.plugin.jdk.http;

import com.pamirs.pradar.MiddlewareType;

/**
 * Created by xiaobin on 2017/2/17.
 */
public final class JdkHttpConstants {
    public final static String PLUGIN_NAME = "jdk-http";
    public final static int PLUGIN_TYPE = MiddlewareType.TYPE_RPC;

    public static final Object TRACE_BLOCK_BEGIN_MARKER = new Object();

    public static final String DYNAMIC_FIELD_CONNECTED = "connected";
    public static final String DYNAMIC_FIELD_CONNECTING = "connecting";
    public static final String DYNAMIC_FIELD_REQUESTS = "requests";
}
