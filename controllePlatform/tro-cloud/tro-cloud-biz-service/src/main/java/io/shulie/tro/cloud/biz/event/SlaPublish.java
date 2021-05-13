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

package io.shulie.tro.cloud.biz.event;

import com.pamirs.tro.entity.domain.vo.schedule.ScheduleStopRequest;
import io.shulie.tro.cloud.common.constants.ScheduleEventConstant;
import io.shulie.tro.eventcenter.Event;
import io.shulie.tro.eventcenter.EventCenterTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: qianshui
 * @Date: 2020/4/17 下午8:28
 * @Description:
 */
@Component
@Slf4j
public class SlaPublish {

    @Autowired
    private EventCenterTemplate eventCenterTemplate;

    public void stop(ScheduleStopRequest scheduleStopRequest) {
        try {
            Event event = new Event();
            event.setEventName(ScheduleEventConstant.STOP_SCHEDULE_EVENT);
            event.setExt(scheduleStopRequest);
            eventCenterTemplate.doEvents(event);
        } catch (Exception e) {
            log.error("【SLA】发起stop动作异常={}", e.getMessage(), e);
        }
    }
}
