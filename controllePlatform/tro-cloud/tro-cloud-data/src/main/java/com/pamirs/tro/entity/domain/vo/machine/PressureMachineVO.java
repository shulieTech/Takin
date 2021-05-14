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

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.shulie.tro.cloud.common.enums.machine.EnumResult;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: fanxx
 * @Date: 2020/5/15 下午8:52
 * @Description:
 */
@Data
@ApiModel(description = "机器信息")
public class PressureMachineVO {
    @ApiModelProperty(value = "压力机ID")
    private Long id;

    @ApiModelProperty(value = "压力机IP")
    private String publicIp;

    @ApiModelProperty(value = "平台ID")
    private Long platformId;

    @ApiModelProperty(value = "机器平台")
    private String platformName;

    @ApiModelProperty(value = "开通账号")
    private String accountName;

    @ApiModelProperty(value = "机器规格")
    private String spec;

    @ApiModelProperty(value = "开通模式")
    private Integer openType;

    @ApiModelProperty(value = "到期时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date expireDate;

    @ApiModelProperty(value = "状态")
    private Integer status;

    @ApiModelProperty(value = "状态对象")
    private EnumResult statusObj;
}
