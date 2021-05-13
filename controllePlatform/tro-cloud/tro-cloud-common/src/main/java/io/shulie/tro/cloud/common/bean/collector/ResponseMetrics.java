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

package io.shulie.tro.cloud.common.bean.collector;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import lombok.Data;

/**
 * 成功的响应
 *
 * @author shiyajian
 * create: 2020-10-10
 */
@Data
public class ResponseMetrics extends AbstractMetrics {
    private String transaction;
    private Integer count;
    private Integer failCount;
    private Long sentBytes;
    private Long receivedBytes;
    private Double rt;
    private Integer saCount;
    private Double maxRt;
    private Double minRt;
    private long timestamp;
    private Integer activeThreads;
    private Map<String, String> tags = new HashMap<>();
    private Set<ErrorInfo> errorInfos;

    public ResponseMetrics() {
        super(Constants.METRICS_TYPE_RESPONSE);
    }

    @Data
    class ErrorInfo {
        private String responseMessage;
        private String responseCode;
    }
}
