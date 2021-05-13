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

package io.shulie.tro.cloud.common.bean.task;

import java.util.Map;

import io.shulie.tro.cloud.common.enums.scenemanage.TaskStatusEnum;
import lombok.Data;

/**
 * @Author: fanxx
 * @Date: 2020/4/20 下午2:41
 * @Description:
 */
@Data
public class TaskResult {
    /**
     * 场景ID
     */
    private Long sceneId;
    /**
     * 任务ID
     */
    private Long taskId;
    /**
     * 客户 ID 新增
     */
    private Long customerId;
    /**
     * pod 调度 任务状态
     */
    private TaskStatusEnum status;
    /**
     * 状态描述
     */
    private String msg;
    /**
     * 是否主动停止
     */
    private Boolean forceStop;
    /**
     * 是否主动启动
     */
    private Boolean forceStart;
    /**
     * 拓展配置
     */
    private Map<String, Object> extendMap;

    public TaskResult(Long sceneId, Long taskId, Long customerId) {
        this.sceneId = sceneId;
        this.taskId = taskId;
        this.customerId = customerId;
    }

    public TaskResult() {

    }
}
