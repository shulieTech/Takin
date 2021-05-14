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

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @ClassName PerformanceThreadDataResult
 * @Description
 * @Author qianshui
 * @Date 2020/11/10 下午3:56
 */
@Data
public class PerformanceThreadDataResult {

    @JsonProperty("base_id")
    private Long baseId;

    private String timestamp;

    @JsonProperty("thread_name")
    private String threadName;

    @JsonProperty("thread_status")
    private String threadStatus;

    @JsonProperty("thread_cpu_use_rate")
    private Double threadCpuUseRate;

    @JsonProperty("thread_stack_link")
    private Long threadStackLink;

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        PerformanceThreadDataResult result = (PerformanceThreadDataResult)o;
        return threadName.equals(result.threadName) &&
            threadStatus.equals(result.threadStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(threadName, threadStatus);
    }
}
