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

package io.shulie.tro.web.data.result.perfomanceanaly;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @ClassName PerformanceBaseDataResult
 * @Description
 * @Author qianshui
 * @Date 2020/11/4 下午2:35
 */
@Data
public class PerformanceBaseDataResult {


    @JsonProperty("agent_id")
    private String agentId;

    @JsonProperty("app_name")
    private String appName;

    @JsonProperty("app_ip")
    private String appIp;

    @JsonProperty("process_id")
    private Long processId;

    @JsonProperty("process_name")
    private String processName;

    /**
     * 原始时间戳数据
     */
    private Long timestamp;
    /**
     * 存influxDB生成的数据
     */
    private String time;

    @JsonProperty("total_memory")
    private Double totalMemory;

    @JsonProperty("perm_memory")
    private Double permMemory;

    @JsonProperty("young_memory")
    private Double youngMemory;

    @JsonProperty("old_memory")
    private Double oldMemory;

    @JsonProperty("young_gc_count")
    private Integer youngGcCount;

    @JsonProperty("full_gc_count")
    private Integer fullGcCount;

    @JsonProperty("young_gc_cost")
    private Long youngGcCost;

    @JsonProperty("full_gc_cost")
    private Long fullGcCost;

    @JsonProperty("base_id")
    private String baseId;

    @JsonProperty("cpu_use_rate")
    private Double cpuUseRate;

    @JsonProperty("thread_count")
    private Integer threadCount;

    /**
     * 非堆内存大小
     */
    @JsonProperty("total_no_heap_memory")
    private Double totalNonHeapMemory;

    /**
     * buffer pool 总内存大小
     */
    @JsonProperty("total_buffer_pool_memory")
    private Double totalBufferPoolMemory;


}
