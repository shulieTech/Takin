/*
 * Copyright 2021 Shulie Technology, Co.Ltd
 * Email: shulie@shulie.io
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.shulie.tro.web.app.constant;

public class BusinessActivityRedisKeyConstant {

    /**
     * 业务活动流量验证key
     */
    public static final String ACTIVITY_VERIFY_KEY = "activity_verify:";

    /**
     * 业务活动流量验证缓存超时时长
     */
    public static final Integer ACTIVITY_VERIFY_KEY_EXPIRE = 24 * 60 * 60;

    public static final String ACTIVITY_VERIFY_SUFFIX = "-FlowVerify";

    public static final Integer ACTIVITY_VERIFY_DEFAULT_CONFIG_TYPE = 1;

    public static final Integer ACTIVITY_VERIFY_DEFAULT_IP_NUM = 1;

    public static final Integer ACTIVITY_VERIFY_DEFAULT_TARGET_RT = 1;

    public static final Integer ACTIVITY_VERIFY_DEFAULT_TARGET_TPS = 1;

    public static final Integer ACTIVITY_VERIFY_UNVERIFIED = 0;

    public static final Integer ACTIVITY_VERIFY_VERIFYING = 1;

    public static final Integer ACTIVITY_VERIFY_VERIFIED = 2;



}
