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

package com.pamirs.tro.entity.domain.vo.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: mubai
 * @Date: 2020-06-28 19:03
 * @Description:
 */

@Data
@ApiModel(value = "WorkBenchVo", description = "工作台展示")
public class WorkBenchVo {

    /**
     * 开关状态
     */
    @ApiModelProperty(name = "switchStatus", value = "开关状态")
    private String switchStatus;

    /**
     * 应用接入数量
     */
    @ApiModelProperty(name = "applicationNum", value = "应用数量")
    private Integer applicationNum;

    /**
     * 接入异常数量
     */
    @ApiModelProperty(name = "accessErrorNum", value = "应用接入异常数量")
    private Integer accessErrorNum;

    /**
     * 系统流程数量
     */
    @ApiModelProperty(name = "systemProcessNum", value = "系统流程数量")
    private Integer systemProcessNum;
    /**
     * 系统流程变更数量
     */
    @ApiModelProperty(name = "changedProcessNum", value = "系统流程变更数量")
    private Integer changedProcessNum;

}

