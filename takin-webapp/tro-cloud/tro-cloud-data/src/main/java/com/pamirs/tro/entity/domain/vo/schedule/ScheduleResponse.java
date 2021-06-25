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

package com.pamirs.tro.entity.domain.vo.schedule;

import java.io.Serializable;
import java.util.Map;

import lombok.Data;

/**
 * @Author 莫问
 * @Date 2020-05-14
 */
@Data
public class ScheduleResponse implements Serializable {

    /**
     * 场景ID
     */
    private Long sceneId;

    /**
     * 任务ID
     */
    private Long taskId;

    /**
     * 状态描述
     */
    private String errorMgs;

    /**
     * 拓展配置
     */
    private Map<String, Object> extendMap;

}
