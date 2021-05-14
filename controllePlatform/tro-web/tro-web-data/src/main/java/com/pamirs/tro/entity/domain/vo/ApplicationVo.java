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

import java.io.Serializable;
import java.util.Date;

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
@ApiModel(value = "ApplicationVo", description = "应用配置入参")
public class ApplicationVo implements Serializable {
    private static final long serialVersionUID = 412834588202338300L;

    @ApiModelProperty(name = "id", value = "项目id")
    private String id;
    @ApiModelProperty(name = "applicationName", value = "项目名称")
    private String applicationName;

    @ApiModelProperty(name = "accessStatus", value = "接入状态； 0：正常 ； 1；待配置 ；2：待检测 ; 3：异常 ")
    private Integer accessStatus;

    @ApiModelProperty(name = "nodeNum", value = "节点数量")
    private Integer nodeNum;

    @ApiModelProperty(name = "exceptionInfo", value = "异常信息")
    private String exceptionInfo;

    @ApiModelProperty(name = "switchStatus", value = "开关状态")
    private String switchStutus;

    @ApiModelProperty(name = "pressureEnable", value = "压测开关，true：开")
    private Boolean pressureEnable = true;

    @ApiModelProperty(name = "updateTime", value = "更新时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @ApiModelProperty(name = "applicationDesc", value = "项目信息")
    private String applicationDesc;

    @ApiModelProperty(name = "ddlScriptPath", value = "影子库表结构脚本路径")
    private String ddlScriptPath;

    @ApiModelProperty(name = "cleanScriptPath", value = "数据清理脚本路径")
    private String cleanScriptPath;

    @ApiModelProperty(name = "readyScriptPath", value = "基础数据准备脚本路径")
    private String readyScriptPath;

    @ApiModelProperty(name = "basicScriptPath", value = "铺底数据脚本路径")
    private String basicScriptPath;

    @ApiModelProperty(name = "cacheScriptPath", value = "缓存预热脚本地址")
    private String cacheScriptPath;

    @ApiModelProperty(name = "managerName", value = "负责人")
    private String managerName;

    @ApiModelProperty(name = "managerId", value = "负责人id")
    private Long managerId;

    @ApiModelProperty(name = "canEdit", value = "是否可编辑")
    private Boolean canEdit = true;

    @ApiModelProperty(name = "canRemove", value = "是否可删除")
    private Boolean canRemove = true;
}
