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
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: fanxx
 * @Date: 2020/5/13 上午11:02
 * @Description:
 */
@Data
@ApiModel(description = "机器任务信息")
public class MachineTaskVO implements Serializable {
    private static final long serialVersionUID = 5493406100253526438L;
    @ApiModelProperty(value = "任务ID")
    private Long id;

    @ApiModelProperty(value = "机器任务类型")
    private Integer taskType;

    @ApiModelProperty(value = "机器数量")
    private Integer machineNum;

    @ApiModelProperty(value = "机器任务状态")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    private Date gmtCreate;

    @ApiModelProperty(value = "更新时间")
    private Date gmtUpdate;
}
