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

package com.pamirs.tro.entity.domain.vo.settle;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;

/**
 * @Author 莫问
 * @Date 2020-05-14
 */
@Data
public class AccountTradeRequest implements Serializable {

    /**
     * 客户ID
     */
    private Long uid;

    /**
     * 场景ID
     */
    private Long sceneId;

    /**
     * 任务ID
     */
    private Long taskId;

    /**
     * 并发
     */
    private Integer expectThroughput;

    /**
     * 平均并发
     */
    private BigDecimal avgConcurrent;

    /**
     * 施压类型,0:并发,1:tps,2:自定义;不填默认为0
     */
    private Integer pressureType;

    /**
     * 压测模式
     */
    private Integer pressureMode;

    /**
     * 压测总时长
     */
    private Long pressureTotalTime;

    /**
     * 递增时长
     */
    private Long increasingTime;

    /**
     * 阶梯层级
     */
    private Integer step;

}
