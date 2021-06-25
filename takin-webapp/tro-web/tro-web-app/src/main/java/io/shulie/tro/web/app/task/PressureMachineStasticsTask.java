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

package io.shulie.tro.web.app.task;

import com.pamirs.tro.common.util.DateUtils;
import io.lettuce.core.RedisClient;
import io.shulie.tro.common.beans.page.PagingList;
import io.shulie.tro.web.app.constant.ScheduleTaskDistributedLockKey;
import io.shulie.tro.web.app.request.perfomanceanaly.PressureMachineQueryRequest;
import io.shulie.tro.web.app.request.perfomanceanaly.PressureMachineUpdateRequest;
import io.shulie.tro.web.app.response.perfomanceanaly.PressureMachineResponse;
import io.shulie.tro.web.app.service.perfomanceanaly.PressureMachineLogService;
import io.shulie.tro.web.app.service.perfomanceanaly.PressureMachineService;
import io.shulie.tro.web.app.service.perfomanceanaly.PressureMachineStatisticsService;
import io.shulie.tro.web.data.param.machine.PressureMachineQueryParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author: mubai
 * @Date: 2020-11-13 11:26
 * @Description:
 */

@Component
@Slf4j
public class PressureMachineStasticsTask {

    @Autowired
    private PressureMachineStatisticsService pressureMachineStatisticsService;

    @Autowired
    private PressureMachineService pressureMachineService;

    @Value("${pressure.machine.upload.interval.time:180000}")
    private Long machineUploadIntervalTime;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private PressureMachineLogService machineLogService;

    /**
     * 30s 统计一次
     * 为了保证同一时间只有一个任务在执行，需要添加分布式锁（解决在集群部署时出现的问题）
     */
    @Scheduled(cron = "0 */1 * * * ?")
    public void statisticsPressureMachine() {
        // 获取分布式锁，获取成功则执行
        Boolean aBoolean = redisTemplate.opsForValue().setIfAbsent(ScheduleTaskDistributedLockKey.PRESSURE_MACHINE_STATISTICS_SCHEDULE_TASK_KEY, "1", 50, TimeUnit.SECONDS);
        if (aBoolean instanceof Boolean && aBoolean == true) {
            pressureMachineStatisticsService.statistics();
        }
    }

    /**
     * 一分钟统计一次，压力机没更新时间距离当前超过3分钟，记为离线状态
     */
    @Scheduled(cron = "0 */1 * * * ?")
    public void judgePressureMachineStatus() {
        Boolean aBoolean = redisTemplate.opsForValue().setIfAbsent(ScheduleTaskDistributedLockKey.PRESSURE_MACHINE_OFFLINE_STATUS_SCHEDULE_CALCULATE_KEY, "1", 50, TimeUnit.SECONDS);
        if (aBoolean instanceof Boolean && aBoolean == true) {
            doJudgePressureMachineStatus();
        }
    }

    /**
     * 每天执行一次，清理90天之前的统计数据
     */
    @Scheduled(cron = "0 0 */3 * * ?")
    public void clearPressureMachineStatisticsData() {
        pressureMachineStatisticsService.clearRubbishData();
    }

    /**
     * 定时清理压力机日志数据，清理20天之前的数据
     */
    @Scheduled(cron = "0 0 */2 * * ?")
    public void clearPressureMachineLogRubbishData() {
        machineLogService.clearRubbishData();
    }

    private void doJudgePressureMachineStatus() {
        PressureMachineQueryParam param = new PressureMachineQueryParam();
        param.setCurrent(0);
        param.setPageSize(Integer.MAX_VALUE);
        PagingList<PressureMachineResponse> pressureMachineResponsePagingList = pressureMachineService.queryByExample(param);
        List<PressureMachineResponse> list = pressureMachineResponsePagingList.getList();
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        for (PressureMachineResponse machine : list) {
            if (machine.getStatus() == -1) {
                continue;
            }
            Date updateTime = DateUtils.strToDate(machine.getGmtUpdate(), null);
            if (updateTime != null && System.currentTimeMillis() - updateTime.getTime() > machineUploadIntervalTime) {
                //认为机器处于离线状态
                pressureMachineService.updatePressureMachineStatus(machine.getId(), -1);
            }
        }

    }
}
