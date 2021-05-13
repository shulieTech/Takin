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

package com.pamirs.tro.entity.domain.dto.schedule;

import java.io.Serializable;
import java.math.BigDecimal;

import io.shulie.tro.cloud.common.enums.machine.EnumResult;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName ScheduleRecordDTO
 * @Description
 * @Author qianshui
 * @Date 2020/5/9 下午2:06
 */
@Data
@ApiModel(description = "列表查询出参")
public class ScheduleRecordDTO implements Serializable {
    private static final long serialVersionUID = -9035230941808348639L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "pod数")
    private Integer podNum;

    @ApiModelProperty(value = "pod种类")
    private String podClass;

    private transient Integer statusInt;

    @ApiModelProperty(value = "状态")
    private EnumResult status;

    @ApiModelProperty(value = "cpu核数")
    private BigDecimal cpuCoreNum;

    @ApiModelProperty(value = "内存大小")
    private BigDecimal memorySize;

    @ApiModelProperty(value = "调度时间")
    private String createTime;
}
