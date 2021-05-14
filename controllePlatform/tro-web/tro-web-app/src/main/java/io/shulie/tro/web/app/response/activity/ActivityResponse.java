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

package io.shulie.tro.web.app.response.activity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.shulie.tro.web.amdb.bean.common.EntranceTypeEnum;
import io.shulie.tro.web.app.response.application.ApplicationEntranceTopologyResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author shiyajian
 * create: 2021-01-11
 */
@Data
@ApiModel
public class ActivityResponse {

    private Long activityId;

    private String activityName;

    private String applicationName;

    private String entranceName;

    private EntranceTypeEnum type;

    private String changeBefore;

    private String changeAfter;

    private Boolean isChange;

    private Integer verifyStatus;

    private Boolean verifiedFlag;

    /**
     * 业务链路是否否核心链路 0:不是;1:是
     */
    @ApiModelProperty(name = "isCore", value = "业务活动链路是否核心链路 0:不是;1:是")
    private String isCore;

    @ApiModelProperty(name = "link_level", value = "业务活动等级")
    @JsonProperty("link_level")
    private String activityLevel;

    /**
     * 业务域: 0:订单域", "1:运单域", "2:结算域
     */
    @ApiModelProperty(name = "businessDomain", value = "业务域")
    private String businessDomain;

    private Long userId;

    @ApiModelProperty(name = "manager", value = "负责人姓名")
    private String manager;

    private String extend;

    private String method;

    private String rpcType;

    private String serviceName;

    private String linkId;

    @ApiModelProperty(name = "topology", value = "拓扑图")
    private ApplicationEntranceTopologyResponse topology;

    @ApiModelProperty(name = "enableLinkFlowCheck", value = "是否开启流量验证")
    private boolean enableLinkFlowCheck;

    @ApiModelProperty(name = "enableLinkFlowCheck", value = "是否开启流量验证")
    private boolean flowCheckStatus;
}
