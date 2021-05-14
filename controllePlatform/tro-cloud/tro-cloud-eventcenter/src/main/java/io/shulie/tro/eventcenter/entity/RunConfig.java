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

/**
 * @Author: fanxx
 * @Date: 2020/4/20 上午10:20
 * @Description:
 */
@Data
public class RunConfig {
    /**
     * 场景ID
     */
    private Long sceneId;

    /**
     * zookeeper地址
     */
    private String zkServer;

    /**
     * 压测任务启动所需内存
     */
    private String memory;

    /**
     * 引擎类型
     */
    private EngineTypeEnum engineType;

    /**
     * 压测任务运行所在机器IP
     */
    private String ip;

    /**
     * 任务运行agent url
     */
    private String znode;
}
