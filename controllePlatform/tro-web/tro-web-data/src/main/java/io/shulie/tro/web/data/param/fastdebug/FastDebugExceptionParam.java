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

package io.shulie.tro.web.data.param.fastdebug;

import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * @author 无涯
 * @Package io.shulie.tro.web.data.param.fastdebug
 * @date 2020/12/29 7:21 下午
 */
@Data
public class FastDebugExceptionParam {

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

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 更新时间
     */
    private Date gmtModified;

    /**
     * 租户id
     */
    private Long customerId;

    public FastDebugExceptionParam() {
        gmtCreate = new Date();
        gmtModified = new Date();
    }
}
