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

package io.shulie.tro.eventcenter.publish;

import com.alibaba.fastjson.JSON;
import io.shulie.tro.eventcenter.Event;
import io.shulie.tro.eventcenter.EventCenterTemplate;
import io.shulie.tro.cloud.common.bean.task.TaskResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: fanxx
 * @Date: 2020/4/17 下午8:28
 * @Description:
 */
@Component
public class EventPublish {
    private static final Logger logger = LoggerFactory.getLogger(EventPublish.class);

    @Autowired
    private EventCenterTemplate eventCenterTemplate;

    public void finished(TaskResult taskResult) {
        Event event = new Event();
        event.setEventName("finished");
        event.setExt(taskResult);
        eventCenterTemplate.doEvents(event);
        logger.info("EventPublish event published: finished,{}", JSON.toJSONString(event));
    }

    public void started(TaskResult taskResult) {
        Event event = new Event();
        event.setEventName("started");
        event.setExt(taskResult);
        eventCenterTemplate.doEvents(event);
        logger.info("EventPublish event published: started,{}", JSON.toJSONString(event));
    }

    public void failed(TaskResult taskResult) {
        Event event = new Event();
        event.setEventName("failed");
        event.setExt(taskResult);
        eventCenterTemplate.doEvents(event);
        logger.info("EventPublish event published: failed,{}", JSON.toJSONString(event));
    }
}
