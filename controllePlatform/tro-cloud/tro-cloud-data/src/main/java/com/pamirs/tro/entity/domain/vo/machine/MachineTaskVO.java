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
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.shulie.tro.cloud.common.enums.machine.EnumResult;
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

    @ApiModelProperty(value = "序列号")
    private String serialNo;

    @ApiModelProperty(value = "平台ID")
    private Long platformId;

    @ApiModelProperty(value = "平台名称")
    private String platformName;

    @ApiModelProperty(value = "账号ID")
    private Long accountId;

    @ApiModelProperty(value = "账号名称")
    private String accountName;

    @ApiModelProperty(value = "规格ID")
    private Long specId;

    @ApiModelProperty(value = "规格描述")
    private String spec;

    @ApiModelProperty(value = "开通模式")
    private Integer openType;

    @ApiModelProperty(value = "开通时长")
    private Integer openTime;

    @ApiModelProperty(value = "机器数量")
    private Integer machineNum;

    @ApiModelProperty(value = "拓展配置")
    private String extendConfig;

    @ApiModelProperty(value = "机器任务状态")
    private Integer status;

    @ApiModelProperty(value = "机器任务状态对象")
    private EnumResult statusObj;

    @ApiModelProperty(value = "机器ID列表")
    private List machineIdList;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date gmtCreate;

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date gmtUpdate;
}
