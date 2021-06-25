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

package com.pamirs.tro.common.redis;

/**
 * 放白名单 黑名单的 redis key
 *
 * @author Administrator
 */
public class WhiteBlackListRedisKey {

    //失效时间七天
    public final static long TIMEOUT = 60 * 60 * 24 * 7;

    /**
     * 白名单key
     */
    public final static String TRO_WHITE_LIST_KEY = "tro_white_list_";

    /**
     * 白名单metric key
     */
    public final static String TRO_WHITE_LIST_KEY_METRIC = "tro_white_list_metric_";

    /**
     * 黑名单key
     */
    public final static String TRO_BLACK_LIST_KEY = "tro_black_list";

}
