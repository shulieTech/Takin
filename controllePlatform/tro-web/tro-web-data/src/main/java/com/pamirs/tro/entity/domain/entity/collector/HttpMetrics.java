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
 * 接收http Metrics
 *
 * @Author <a href="tangyuhan@shulie.io">yuhan.tang</a>
 * @package: com.pamirs.tro.entity.domain.entity.collector
 * @Date 2020-04-20 19:24
 */
@Data
public class HttpMetrics {

    long timestamp;
    private String transaction;
    private Integer count;
    private Integer failCount;
    private Double rt;
    private Integer saCount;
    private Double maxRt;
    private Double minRt;
}
