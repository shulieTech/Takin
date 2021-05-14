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

package io.shulie.tro.web.app.controller.openapi.response.scenemanage;

import java.io.Serializable;
import java.math.BigDecimal;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhaoyong
 */
@ApiModel(description = "列表查询出参")
@Data
public class SceneManageListOpenApiResp implements Serializable {

    @ApiModelProperty(
            name = "id",
            value = "ID"
    )
    private Long id;
    @ApiModelProperty("客户ID")
    private Long customId;
    @ApiModelProperty("负责人ID")
    private Long userId;
    @ApiModelProperty("客户名称")
    private String customName;
    @ApiModelProperty("场景名称")
    private String sceneName;
    @ApiModelProperty("状态")
    private Integer status;
    @ApiModelProperty("最新压测时间")
    private String lastPtTime;
    @ApiModelProperty("是否有报告")
    private Boolean hasReport;
    @ApiModelProperty("预计消耗流量")
    private BigDecimal estimateFlow;
    @ApiModelProperty("最大并发")
    private Integer threadNum;
    @ApiModelProperty("拓展字段")
    private String features;
    @ApiModelProperty(
            name = "managerName",
            value = "负责人"
    )
    private String managerName;
    @ApiModelProperty(
            name = "managerId",
            value = "tro-web负责人id"
    )
    private Long managerId;
    @ApiModelProperty(
            name = "canEdit",
            value = "是否可编辑"
    )
    private Boolean canEdit = true;
    @ApiModelProperty(
            name = "canRemove",
            value = "是否可删除"
    )
    private Boolean canRemove = true;
    @ApiModelProperty(
            name = "canEnableDisable",
            value = "是否可启动停止"
    )
    private Boolean canStartStop = true;
}
