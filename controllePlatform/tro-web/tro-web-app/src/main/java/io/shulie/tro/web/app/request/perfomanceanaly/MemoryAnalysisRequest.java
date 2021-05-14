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

package io.shulie.tro.web.app.request.perfomanceanaly;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: mubai
 * @Date: 2020-11-11 18:12
 * @Description:
 */

@Data
@ApiModel("内存分析入参")
public class MemoryAnalysisRequest implements Serializable {
    private static final long serialVersionUID = -8097021911903550029L;

    @ApiModelProperty(value = "应用名称")
    private String appName;

    /**
     *  appIp | agentId
     */
    @ApiModelProperty(value = "进程名称")
    private String processName;

    /**
     * 根据报告id，确定起止时间
     */
    @ApiModelProperty(value = "报告ID")
    private Long reportId;

}
