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

import java.util.concurrent.TimeUnit;
import io.shulie.tro.web.app.constant.ScheduleTaskDistributedLockKey;
import io.shulie.tro.web.app.service.scenemanage.SceneSchedulerTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @Author: mubai
 * @Date: 2020-12-02 19:01
 * @Description:
 */

@Component
@Slf4j
public class SceneSchedulerTask {

    @Autowired
    private SceneSchedulerTaskService sceneSchedulerTaskService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 压测场景定时执行，一分钟检查一次，待压测场景执行
     */
    @Scheduled(cron = "0 */1 * * * ?")
    public void startSceneTask() {
        // 获取分布式锁，获取成功则执行
        Boolean aBoolean = redisTemplate.opsForValue().setIfAbsent(
            ScheduleTaskDistributedLockKey.SCENE_SCHEDULER_PRESSURE_TASK_KEY, "1", 50, TimeUnit.SECONDS);
        if (aBoolean instanceof Boolean && aBoolean == true) {
            sceneSchedulerTaskService.executeSchedulerPressureTask();
        }
    }
}
