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
package com.pamirs.attach.plugin.okhttp;

import com.pamirs.pradar.MiddlewareType;

/**
 * @Description
 * @Author xiaobin.zfb
 * @mail xiaobin@shulie.io
 * @Date 2020/6/30 11:17 上午
 */
public final class OKHttpConstants {
    public static final String PLUGIN_NAME = "okhttp";
    public static final int PLUGIN_TYPE = MiddlewareType.TYPE_RPC;

    public static final String DYNAMIC_FIELD_URL = "url";
    public static final String DYNAMIC_FIELD_URL_STRING = "urlString";
    public static final String DYNAMIC_FIELD_REQUEST = "request";
    public static final String DYNAMIC_FIELD_ORIGINAL_REQUEST = "originalRequest";
}
