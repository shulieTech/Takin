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
package com.pamirs.attach.plugin.grpc;

import com.pamirs.pradar.MiddlewareType;

/**
 * @Description
 * @Author xiaobin.zfb
 * @mail xiaobin@shulie.io
 * @Date 2020/8/5 8:15 下午
 */
public final class GrpcConstants {
    public final static String PLUGIN_NAME = "grpc";
    public final static int PLUGIN_TYPE = MiddlewareType.TYPE_RPC;

    public final static String MODULE_NAME = "grpc";

    public final static String DYNAMIC_FIELD_REMOTE_ADDRESS = "remoteAddress";
    public final static String DYNAMIC_FIELD_METHOD_NAME = "methodName";
    public final static String DYNAMIC_FIELD_IS_CLUSTER_TEST = "isClusterTest";
}
