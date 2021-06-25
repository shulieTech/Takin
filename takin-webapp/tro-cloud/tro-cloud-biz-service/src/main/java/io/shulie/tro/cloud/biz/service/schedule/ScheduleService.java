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

package io.shulie.tro.cloud.biz.service.schedule;

import com.pamirs.tro.entity.domain.vo.schedule.ScheduleInitParam;
import com.pamirs.tro.entity.domain.vo.schedule.ScheduleRunRequest;
import com.pamirs.tro.entity.domain.vo.schedule.ScheduleStartRequest;
import com.pamirs.tro.entity.domain.vo.schedule.ScheduleStopRequest;

/**
 * @Author 莫问
 * @Date 2020-05-12
 */
public interface ScheduleService {

    /**
     * 启动调度
     *
     * @param request
     */
    void startSchedule(ScheduleStartRequest request);

    /**
     * 停止调度
     *
     * @param request
     */
    void stopSchedule(ScheduleStopRequest request);

    /**
     * 调度运行
     *
     * @param request
     */
    void runSchedule(ScheduleRunRequest request);

    /**
     * 初始化回调
     *
     * @param param
     */
    void initScheduleCallback(ScheduleInitParam param);
}
