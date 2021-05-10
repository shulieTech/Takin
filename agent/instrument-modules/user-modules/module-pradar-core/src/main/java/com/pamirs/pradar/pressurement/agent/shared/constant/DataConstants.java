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
package com.pamirs.pradar.pressurement.agent.shared.constant;

public class DataConstants {
    /**
     * http 请求时统一的压测后缀
     */
    public static final String HTTP_USER_AGENT_SUFFIX = "PerfomanceTest";

    /**
     * Http header for 压测
     */
    public static final String HTTP_HEADER = "User-Agent";

    /**
     * grpc header for 压测
     */
    public static final String GRPC_HEADER = "Grpc-PT";

    /**
     * dubbo hidden value for 压测
     */
    public static final String DUBBO_HEADER = "Dubbo-PT";


    /**
     * dubbo hidden value response for 压测
     */
    public static final String DUBBO_HEADER_RESPONSE = "Dubbo-PT-Response";
}
