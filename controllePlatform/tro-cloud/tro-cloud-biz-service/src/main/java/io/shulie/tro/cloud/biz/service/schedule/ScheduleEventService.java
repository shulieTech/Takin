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

import com.pamirs.tro.entity.domain.vo.schedule.ScheduleRunRequest;
import com.pamirs.tro.entity.domain.vo.schedule.ScheduleStartRequest;
import com.pamirs.tro.entity.domain.vo.schedule.ScheduleStopRequest;
import io.shulie.tro.cloud.common.constants.ScheduleEventConstant;
import io.shulie.tro.eventcenter.Event;
import io.shulie.tro.eventcenter.EventCenterTemplate;
import io.shulie.tro.eventcenter.annotation.IntrestFor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author 莫问
 * @Date 2020-05-12
 */
@Component
public class ScheduleEventService {

    @Autowired
    private EventCenterTemplate eventCenterTemplate;

    @Autowired
    private ScheduleService scheduleService;

    /**
     * 调度初始化
     *
     * @param request
     */
    public void initSchedule(ScheduleRunRequest request) {
        Event event = new Event();
        event.setEventName(ScheduleEventConstant.INIT_SCHEDULE_EVENT);
        event.setExt(request);
        eventCenterTemplate.doEvents(event);
    }

    /**
     * 运行调度
     *
     * @param event
     */
    @IntrestFor(event = ScheduleEventConstant.RUN_SCHEDULE_EVENT)
    public void runSchedule(Event event) {
        scheduleService.runSchedule((ScheduleRunRequest)event.getExt());
    }

    /**
     * 启动调度
     *
     * @param event
     */
    @IntrestFor(event = ScheduleEventConstant.START_SCHEDULE_EVENT)
    public void startSchedule(Event event) {
        ScheduleStartRequest request = (ScheduleStartRequest)event.getExt();
        scheduleService.startSchedule(request);

    }

    /**
     * 停止调度
     *
     * @param event
     */
    @IntrestFor(event = ScheduleEventConstant.STOP_SCHEDULE_EVENT)
    public void stopSchedule(Event event) {
        ScheduleStopRequest request = (ScheduleStopRequest)event.getExt();
        scheduleService.stopSchedule(request);
    }

}
