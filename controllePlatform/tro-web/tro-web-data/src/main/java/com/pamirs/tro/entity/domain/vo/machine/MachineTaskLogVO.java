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

package com.pamirs.tro.entity.domain.vo.machine;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: fanxx
 * @Date: 2020/5/13 上午11:23
 * @Description:
 */
@Data
@ApiModel(description = "机器任务日志")
public class MachineTaskLogVO implements Serializable {
    private static final long serialVersionUID = 8963893470487499218L;

    @ApiModelProperty(value = "机器IP")
    private String ip;

    @ApiModelProperty(value = "机器名称")
    private String hostname;

    @ApiModelProperty(value = "机器状态")
    private Byte status;

    @ApiModelProperty(value = "任务日志")
    private String log;
}
