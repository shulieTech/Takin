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
 * @Author: mubai
 * @Date: 2020-11-25 10:03
 * @Description:
 */
public class ScheduleTaskDistributedLockKey {
    //压力机趋势统计定时任务，分布式锁
    public static final String PRESSURE_MACHINE_STATISTICS_SCHEDULE_TASK_KEY = "pressure:machine:statistics:schedule:task:key";
    //压力机离线状态定时判断，分布式锁
    public static final String PRESSURE_MACHINE_OFFLINE_STATUS_SCHEDULE_CALCULATE_KEY = "pressure:mcahine:offline:status:calculate:key";

    public static final String SCENE_SCHEDULER_PRESSURE_TASK_KEY = "scene:scheduler:pressure:task:key";

}
