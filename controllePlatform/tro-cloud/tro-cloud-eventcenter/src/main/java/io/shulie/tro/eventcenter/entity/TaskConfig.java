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

package io.shulie.tro.eventcenter.entity;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import lombok.Data;

/**
 * @Author: fanxx
 * @Date: 2020/4/20 下午4:14
 * @Description:
 */
@Data
public class TaskConfig {
    /**
     * 场景ID
     */
    private Long sceneId;
    /**
     * 场景名称
     */
    private String sceneName;
    /**
     * 任务ID
     */
    private Long taskId;
    /**
     * 客户 id
     */
    private Long customerId;

    /**
     * 引擎类型
     */
    private EngineTypeEnum engineType;
    /**
     * ip数
     */
    private int agentNum;
    /**
     * 资源路径
     */
    private List<String> resPath;
    /**
     * 压测并发数
     */
    private int expectThroughput;
    /**
     * 压测时常
     */
    private long continuedTime;
    /**
     * 预热时长
     */
    private long preheatTime;
    /**
     * 时间单位
     */
    private TimeUnit timeUnit;
    /**
     * 业务活动
     */
    private Map<String, String> businessMap;
    /**
     * 是否主动停止
     */
    private Boolean forceStop = false;
    /**
     * 是否主动启动
     */
    private Boolean forceStart = true;
    /**
     * 拓展配置
     */
    private Map<String, Object> extendMap;
}
