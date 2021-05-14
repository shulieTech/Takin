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

package io.shulie.tro.web.app.response.perfomanceanaly;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName ProcessBaseDataResponse
 * @Description 进程基本数据
 * @Author qianshui
 * @Date 2020/11/4 上午11:18
 */
@Data
@ApiModel("性能基础数据返回值")
public class ProcessBaseDataResponse implements Serializable {
    private static final long serialVersionUID = 9204151589450625059L;

    @ApiModelProperty(value = "进程id")
    private Long processId;

    @ApiModelProperty(value = "进程名称")
    private String processName;

    @ApiModelProperty(value = "agentId")
    private String agentId;

    @ApiModelProperty(value = "机器ip")
    private String appIp;

    @ApiModelProperty(value = "应用名称")
    private String appName;
}
