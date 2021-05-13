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

package io.shulie.tro.cloud.biz.service.machine;

import com.alibaba.fastjson.JSON;

import com.pamirs.tro.entity.domain.entity.machine.MachineTask;
import com.pamirs.tro.entity.domain.vo.machine.MachineTaskVO;
import io.shulie.tro.eventcenter.Event;
import io.shulie.tro.eventcenter.EventCenterTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: fanxx
 * @Date: 2020/5/18 下午2:36
 * @Description:
 */
@Component
public class MachineEventPublishService {
    private static final Logger logger = LoggerFactory.getLogger(MachineEventPublishService.class);

    @Autowired
    private EventCenterTemplate eventCenterTemplate;

    public void open(MachineTask machineTask) {
        Event event = new Event();
        event.setEventName("machineOpen");
        event.setExt(machineTask);
        eventCenterTemplate.doEvents(event);
        logger.info("MachineEventPublishService event published: machineOpen,{}", JSON.toJSONString(event));
    }

    public void start(MachineTask machineTask) {
        Event event = new Event();
        event.setEventName("machineStart");
        event.setExt(machineTask);
        eventCenterTemplate.doEvents(event);
        logger.info("MachineEventPublishService event published: machineStart,{}", JSON.toJSONString(event));
    }

    public void init(MachineTask machineTask) {
        Event event = new Event();
        event.setEventName("machineInit");
        event.setExt(machineTask);
        eventCenterTemplate.doEvents(event);
        logger.info("MachineEventPublishService event published: machineInit,{}", JSON.toJSONString(event));
    }

    public void destroy(MachineTaskVO machineTaskVO) {
        Event event = new Event();
        event.setEventName("machineDestroy");
        event.setExt(machineTaskVO);
        eventCenterTemplate.doEvents(event);
        logger.info("MachineEventPublishService event published: machineDestroy,{}", JSON.toJSONString(event));
    }
}
