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

import io.shulie.tro.cloud.common.bean.collector.SendMetricsEvent;
import io.shulie.tro.cloud.common.bean.task.TaskResult;
import io.shulie.tro.eventcenter.Event;
import io.shulie.tro.eventcenter.annotation.IntrestFor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import io.shulie.tro.cloud.biz.service.sla.SlaService;

/**
 * @ClassName SlaListener
 * @Description
 * @Author qianshui
 * @Date 2020/4/20 下午8:19
 */
@Component
@Slf4j
public class SlaListener {

    @Autowired
    private SlaService slaService;

    /**
     * 任务启动，缓存值
     *
     * @param event
     */
    @IntrestFor(event = "started")
    public void doStartSLAEvent(Event event) {
        log.info("SLA配置，从调度中心收到压测任务启动成功事件");
        Object object = event.getExt();
        TaskResult taskBean = (TaskResult)object;
        slaService.cacheData(taskBean.getSceneId());
    }

    /**
     * 任务停止，清除缓存
     *
     * @param event
     */
    @IntrestFor(event = "finished")
    public void doStopSLAEvent(Event event) {
        log.info("通知SLA配置模块，从调度中心收到压测任务结束事件");
        try {
            Object object = event.getExt();
            TaskResult taskResult = (TaskResult)object;
            slaService.removeMap(taskResult.getSceneId());
        } catch (Exception e) {
            log.error("【SLA】处理finished事件异常={}", e.getMessage(), e);
        }
    }

    @IntrestFor(event = "metricsData")
    public void doMetricsData(Event event) {
        try {
            Object object = event.getExt();
            SendMetricsEvent metricsEvnet = (SendMetricsEvent)object;
            log.info("收到数据采集发来Metrics数据，timestamp = {}", metricsEvnet.getTimestamp());
            slaService.buildWarn(metricsEvnet);
        } catch (Exception e) {
            log.error("【SLA】metricsData事件异常={}", e.getMessage(), e);
        }
    }
}

