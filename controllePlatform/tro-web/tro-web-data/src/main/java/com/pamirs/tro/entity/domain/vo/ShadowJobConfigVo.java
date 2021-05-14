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
import com.pamirs.tro.common.constant.JobEnum;
import com.pamirs.tro.entity.domain.entity.simplify.TShadowJobConfig;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author <a href="tangyuhan@shulie.io">yuhan.tang</a>
 * @package: com.pamirs.tro.entity.domain.vo
 * @Date 2020-03-17 16:02
 */
@ApiModel(value = "ShadowJobConfigQuery", description = "影子JOB配置视图")
@Data
public class ShadowJobConfigVo {

    @ApiModelProperty(name = "id", value = "ID")
    private String id;

    @ApiModelProperty(name = "applicationId", value = "应用ID")
    private String applicationId;

    @ApiModelProperty(name = "applicationName", value = "应用名称")
    private String applicationName;

    @ApiModelProperty(name = "name", value = "JOB任务名称")
    private String name;

    @ApiModelProperty(name = "typeName", value = "JOB类型名称")
    private String typeName;

    @ApiModelProperty(name = "type", value = "JOB类型 0-quartz、1-elastic-job、2-xxl-job")
    private Integer type;

    @ApiModelProperty(name = "status", value = "0-可用 1-不可用")
    private Integer status;

    @ApiModelProperty(name = "configCode", value = "JOB配置xml")
    private String configCode;

    @ApiModelProperty(name = "active", value = "检测是否可用 0-可用 1-不可用")
    private Integer active;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(name = "updateTime", value = "修改时间")
    private Date updateTime;

    @ApiModelProperty(name = "canEdit", value = "是否可编辑")
    private Boolean canEdit = true;

    @ApiModelProperty(name = "canRemove", value = "是否可删除")
    private Boolean canRemove = true;

    @ApiModelProperty(name = "canEnableDisable", value = "是否启用禁用")
    private Boolean canEnableDisable = true;

    @ApiModelProperty(name = "remark", value = "备注")
    private String remark;

    public ShadowJobConfigVo() {
    }

    public ShadowJobConfigVo(TShadowJobConfig config) {
        this.id = String.valueOf(config.getId());
        this.applicationId = String.valueOf(config.getApplicationId());
        this.name = config.getName();
        this.type = config.getType();
        this.status = config.getStatus();
        this.configCode = config.getConfigCode();
        this.typeName = JobEnum.getJobByIndex(type).getText();
        this.active = config.getActive();
        this.updateTime = config.getUpdateTime();
        this.remark = config.getRemark();
    }

    public ShadowJobConfigVo(TShadowJobConfig config, String applicationName) {
        this.id = String.valueOf(config.getId());
        this.applicationId = String.valueOf(config.getApplicationId());
        this.name = config.getName();
        this.type = config.getType();
        this.status = config.getStatus();
        this.configCode = config.getConfigCode();
        this.typeName = JobEnum.getJobByIndex(type).getText();
        this.updateTime = config.getUpdateTime();
        this.applicationName = applicationName;
        this.active = config.getActive();
        this.remark = config.getRemark();
    }
}
