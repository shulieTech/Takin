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

package io.shulie.tro.cloud.biz.service.async.impl;

import com.pamirs.tro.entity.domain.vo.scenemanage.SceneManageStartRecordVO;
import com.pamirs.tro.entity.domain.vo.schedule.ScheduleStartRequest;
import io.shulie.tro.cloud.biz.service.async.AsyncService;
import io.shulie.tro.cloud.biz.service.scene.SceneManageService;
import io.shulie.tro.cloud.common.bean.task.TaskResult;
import io.shulie.tro.cloud.common.constants.ScheduleConstants;
import io.shulie.tro.cloud.common.redis.RedisClientUtils;
import io.shulie.tro.eventcenter.Event;
import io.shulie.tro.eventcenter.EventCenterTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @ClassName AsyncServiceImpl
 * @Description
 * @Author qianshui
 * @Date 2020/10/30 下午7:13
 */
@Service
@Slf4j
public class AsyncServiceImpl implements AsyncService {

    @Autowired
    private RedisClientUtils redisClientUtils;

    @Autowired
    private EventCenterTemplate eventCenterTemplate;
    
    @Autowired
    private SceneManageService sceneManageService;
    
    /**
     * pod 启动时间超时
     */
    @Value("${k8s.pod.start.expireTime: 30}")
    private Integer podStartExpireTime;

    /**
     * 线程定时检查休眠时间
     */
    private final static Integer CHECK_INTERVAL_TIME = 3;

    @Async("checkStartedPodPool")
    @Override
    public void checkStartedPod(ScheduleStartRequest startRequest) {
        log.info("启动后台检查pod状态线程.....");
        Integer currentTime = 0;
        boolean checkPass = false;
        while (currentTime <= podStartExpireTime) {
            String podTotalName = ScheduleConstants.getPodTotal(startRequest.getSceneId(), startRequest.getTaskId(),
                startRequest.getCustomerId());
            String podTotal = redisClientUtils.getString(podTotalName);
            String podName = ScheduleConstants.getPodName(startRequest.getSceneId(), startRequest.getTaskId(),
                startRequest.getCustomerId());
            String podNum = redisClientUtils.getString(podName);
            log.info("任务id={}, 计划启动【{}】个pod，当前启动【{}】个pod.....", startRequest.getTaskId(), podTotal, podNum);
            if(podTotal != null && podNum != null) {
                try {
                    if (Integer.parseInt(podTotal) == Integer.parseInt(podNum)) {
                        checkPass = true;
                        log.info("后台检查到pod全部启动成功.....");
                        break;
                    }
                } catch (Exception e) {
                    log.error("从Redis里获取pod数量数据格式异常:{}", e.getMessage());
                }
            }
            try {
                Thread.sleep(CHECK_INTERVAL_TIME * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            currentTime += CHECK_INTERVAL_TIME;
        }
        //pod 没有在设定时间内启动完毕，停止压测
        if(!checkPass) {
            callStop(startRequest);
        }
    }

    private void callStop(ScheduleStartRequest startRequest) {

        // 汇报失败
        sceneManageService.reportRecord(SceneManageStartRecordVO.build(startRequest.getSceneId(),
            startRequest.getTaskId(),
            startRequest.getCustomerId()).success(false).errorMsg("").build());
        // 清除 SLA配置 清除PushWindowDataScheduled 删除pod job configMap  生成报告拦截 状态拦截
        Event event = new Event();
        event.setEventName("finished");
        event.setExt(new TaskResult(startRequest.getSceneId(), startRequest.getTaskId(), startRequest.getCustomerId()));
        eventCenterTemplate.doEvents(event);
    }
}
