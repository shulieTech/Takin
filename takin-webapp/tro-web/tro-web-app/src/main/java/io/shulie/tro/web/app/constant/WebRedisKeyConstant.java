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

/**
 * @ClassName WebRedisKeyConstant
 * @Description redis key
 * @Author qianshui
 * @Date 2020/11/4 下午3:54
 */
public class WebRedisKeyConstant {
    //性能分析 线程、内存数据
    public static final String CACHE_PERFOMANCE_BASE_DATA_KEY = "WEB#PERFOMANCE_BASE_DATA";

    //报告告警set数据
    public final static String REPORT_WARN_PREFIX = "report:warn:";

    //压测中报告id
    public final static String PTING_REPORTID_KEY = "pting.reportid:set";

    /**
     * 压测 应用名列表
     */
    public final static String PTING_APPLICATION_KEY = "pting.application:hmset";
}
