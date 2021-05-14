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

package com.pamirs.tro.entity.domain.entity.collector;

import lombok.Data;

/**
 * Influxdb 对象
 *
 * @Author <a href="tangyuhan@shulie.io">yuhan.tang</a>
 * @package: io.shulie.tro.log.entity
 * @Date 2020-04-20 15:20
 */
@Data
public class Metrics {

    /**
     * tag
     */
    private String transaction;

    /**
     * field
     */
    private Integer count;
    private Integer failCount;
    private Double avgTps;
    private Double avgRt;
    private Double successRate;
    private Double sa;
    private Double maxRt;
    private Double minRt;
    private long timestamp;

}
