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

package io.shulie.tro.web.app.controller.openapi.response.application;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: mubai<chengjiacai @ shulie.io>
 * @Date: 2020-03-16 15:03
 * @Description:
 */

@Data
@ApiModel(value = "ApplicationListResponse", description = "应用出参")
public class ApplicationListResponse implements Serializable {
    private static final long serialVersionUID = 412834588202338300L;

    @ApiModelProperty(name = "applicationId", value = "应用id")
    private Long applicationId;

    @ApiModelProperty(name = "applicationName", value = "应用名称")
    private String applicationName;

    @ApiModelProperty(name = "accessStatus",value = "接入状态； 0：正常 ； 1；待配置 ；2：待检测 ; 3：异常")
    private Integer accessStatus;

    @ApiModelProperty(name = "nodeNum", value = "节点数量")
    private Integer nodeNum;

    @ApiModelProperty(name = "updateTime", value = "更新时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @ApiModelProperty(name = "ipList", value = "ip地址列表")
    private List<String> ipsList;



}
