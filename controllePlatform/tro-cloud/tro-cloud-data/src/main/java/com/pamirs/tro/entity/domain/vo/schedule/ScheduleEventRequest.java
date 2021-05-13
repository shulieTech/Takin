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
public class ScheduleEventRequest implements Serializable {

    /**
     * 场景ID
     */
    private Long sceneId;

    /**
     * 场景任务ID
     */
    private Long taskId;

    /**
     * 客户Id
     */
    private Long customerId;

    /**
     * 扩展参数
     */
    private Map<String, Object> extend;
}
