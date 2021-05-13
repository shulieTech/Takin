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

package io.shulie.tro.web.common.vo.fastdebug;

import java.util.List;
import java.util.Objects;

import lombok.Data;

/**
 * @author 无涯
 * @Package io.shulie.tro.web.common.vo.fastdebug
 * @date 2021/1/5 3:16 下午
 */
@Data
public class FastDebugExceptionVO {
    private Long id;

    /**
     * 结果id
     */
    private Long resultId;

    /**
     * traceId
     */
    private String traceId;

    /**
     * rpc_id
     */
    private String rpcId;

    /**
     * 应用名
     */
    private String applicationName;

    /**
     * agentId
     */
    private List<String> agentId;

    /**
     * 异常类型
     */
    private String type;

    /**
     * 异常编码
     */
    private String code;

    /**
     * 异常描述
     */
    private String description;

    /**
     * 处理建议
     */
    private String suggestion;

    /**
     * 异常时间
     */
    private String time;

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        FastDebugExceptionVO that = (FastDebugExceptionVO)o;
        return Objects.equals(applicationName, that.applicationName) &&
            Objects.equals(agentId, that.agentId) &&
            Objects.equals(type, that.type) &&
            Objects.equals(code, that.code) &&
            Objects.equals(description, that.description) &&
            Objects.equals(suggestion, that.suggestion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(applicationName, agentId, type, code, description, suggestion);
    }
}
