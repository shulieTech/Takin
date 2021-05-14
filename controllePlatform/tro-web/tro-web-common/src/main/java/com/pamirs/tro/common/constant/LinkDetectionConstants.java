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

package com.pamirs.tro.common.constant;

/**
 * 说明: 压测检测常量类
 *
 * @author shulie
 * @version v1.0
 * @2018年5月17日
 */
public class LinkDetectionConstants {

    // @Field APPLICATION_ID : 应用id
    public static final String APPLICATION_ID = "applicationId";

    // @Field APPLICATION_NAME : 应用名称
    public static final String APPLICATION_NAME = "applicationName";

    // @Field DDL_BUILD_STATUS : 影子库脚本构建状态
    public static final String DDL_BUILD_STATUS = "ddlBuildStatus";

    // @Field READY_BUILD_STATUS : 基础数据构建状态
    public static final String READY_BUILD_STATUS = "readyBuildStatus";

    // @Field BASIC_BUILD_STATUS : 铺底数据构建状态
    public static final String BASIC_BUILD_STATUS = "basicBuildStatus";

    // @Field INTERFACE_NAME : 接口名称
    public static final String INTERFACE_NAME = "interfaceName";

    // @Field TYPE : 白名单类型
    public static final String TYPE = "type";

    // @Field CACHE_BUILD_STATUS : 缓存脚本构建状态
    public static final String CACHE_BUILD_STATUS = "cacheBuildStatus";

    // @Field CACHE_LAST_SUCCESS_TIME : 缓存脚本上一次执行成功时间
    public static final String CACHE_LAST_SUCCESS_TIME = "cacheLastSuccessTime";

    //@Field CACHE_EXP_TIME : 缓存过期时间
    public static final String CACHE_EXP_TIME = "cacheExpTime";
}
