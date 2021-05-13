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

import lombok.Data;

import java.util.List;

/**
 * @Author: fanxx
 * @Date: 2020/4/17 下午3:05
 * @Description:
 */
@Data
public class TaskBean extends TaskConfig {
    /**
     * 进程号
     */
    private Long pid;
    /**
     * agent url
     */
    private List<String> znodes;
    /**
     * 重新拉起次数
     */
    private int retryTimes;
    /**
     * 开始压测时间
     */
    private long startTime;
    /**
     * 实际结束时间
     */
    private long currentTime;
    /**
     * 控制台地址
     */
    private String consoleUrl;
    /**
     * 任务启动配置
     */
    private RunConfig runConfig;
}
