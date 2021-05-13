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

package io.shulie.tro.cloud.common.constants;

/**
 * 常量
 *
 * @Author <a href="tangyuhan@shulie.io">yuhan.tang</a>
 * @package: io.shulie.tro.collector.util
 * @Date 2020-04-20 15:55
 */
public class CollectorConstants {

    /**
     * redis key前缀
     */
    public static final String REDIS_PRESSURE_TASK_KEY = "COLLECTOR:JOB:TASK:%s";

    /**
     * 窗口大小
     */
    public static int[] timeWindow = new int[]{0,5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55,60};

    /**
     * 指标key 超时时间
     */
    public static final long REDIS_KEY_TIMEOUT = 60;
    /**
     * 单位：秒
     */
    public static final int overdueSecond = 10;
    /**
     * 10秒过期策略，超时丢弃Metrics 数据，单位：毫秒
     */
    public static final long overdueTime = 2000 * overdueSecond;
    /**
     * Metrics 统计时间间隔
     */
    public static final int SEND_TIME = 5;
    public static final int SECOND_60 = 60;

}
