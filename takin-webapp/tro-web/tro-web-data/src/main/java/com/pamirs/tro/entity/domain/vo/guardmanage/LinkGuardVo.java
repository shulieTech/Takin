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

package com.pamirs.tro.entity.domain.vo.guardmanage;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: 慕白
 * @Date: 2020-03-05 09:26
 * @Description:
 */

@Data
@ApiModel(value = "LinkGuardVo", description = "挡板配置入参")
public class LinkGuardVo implements Serializable {
    private static final long serialVersionUID = -6566738117528313331L;

    @ApiModelProperty(name = "id", value = "挡板id")
    private Long id;
    @ApiModelProperty(name = "applicationName", value = "项目名称")
    private String applicationName;
    @ApiModelProperty(name = "applicationId", value = "项目id")
    private String applicationId;

    @ApiModelProperty(name = "methodInfo", value = "方法信息")
    private String methodInfo;
    @ApiModelProperty(name = "groovy", value = "流程消息体")
    private String groovy;

    @ApiModelProperty(name = "isEnable", value = "是否开启")
    private Boolean isEnable;

    @ApiModelProperty(name = "createTime", value = "创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty(name = "updateTime", value = "更新时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @ApiModelProperty(name = "canEdit", value = "是否可编辑")
    private Boolean canEdit = true;

    @ApiModelProperty(name = "canRemove", value = "是否可删除")
    private Boolean canRemove = true;

    @ApiModelProperty(name = "canEnableDisable", value = "是否启用禁用")
    private Boolean canEnableDisable = true;

    @ApiModelProperty(name = "remark", value = "备注")
    private String remark ;
}
