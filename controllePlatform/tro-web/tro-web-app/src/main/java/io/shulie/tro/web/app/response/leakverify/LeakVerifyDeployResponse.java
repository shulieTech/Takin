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

package io.shulie.tro.web.app.response.leakverify;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhaoyong
 */
@Data
@ApiModel(value = "LeakVerifyDeployResponse", description = "漏数实例")
@Deprecated
public class LeakVerifyDeployResponse implements Serializable {
    private static final long serialVersionUID = 834522014546469149L;

    @ApiModelProperty(value = "漏数实例id")
    private Long id;

    /**
     * 漏数记录id
     */
    @ApiModelProperty(value = "漏数记录id")
    private Long leakVerifyId;

    /**
     * 应用名
     */
    @ApiModelProperty(value = "应用名")
    private String applicationName;

    /**
     * 链路入口名称
     */
    @ApiModelProperty(value = "链路入口名称")
    private String entryName;

    /**
     * 压测请求数量
     */
    @ApiModelProperty(value = "压测请求数量")
    private Long pressureRequestCount;

    /**
     * 压测漏数数量
     */
    @ApiModelProperty(value = "压测漏数数量")
    private Long pressureLeakCount;

    /**
     * 业务请求数量
     */
    @ApiModelProperty(value = "业务请求数量")
    private Long bizRequestCount;

    /**
     * 业务漏数数量
     */
    @ApiModelProperty(value = "业务漏数数量")
    private Long bizLeakCount;
}
