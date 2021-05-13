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

package com.pamirs.tro.entity.domain.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: fanxx
 * @Date: 2020/9/24 10:42 上午
 * @Description:
 */
@Data
@ApiModel(value = "OperationLogVo", description = "操作日志返参")
public class OperationLogVo {

    @ApiModelProperty(name = "module", value = "操作模块-主模块")
    private String module;

    @ApiModelProperty(name = "subModule", value = "操作模块-子模块")
    private String subModule;

    @ApiModelProperty(name = "type", value = "操作类型")
    private String type;

    @ApiModelProperty(name = "content", value = "操作内容描述")
    private String content;

    @ApiModelProperty(name = "userName", value = "操作人")
    private String userName;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(name = "startTime", value = "操作时间(开始时间)")
    private Date startTime;
}
