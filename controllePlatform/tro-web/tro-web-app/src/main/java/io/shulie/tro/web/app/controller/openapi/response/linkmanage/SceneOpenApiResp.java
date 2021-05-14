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

package io.shulie.tro.web.app.controller.openapi.response.linkmanage;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhaoyong
 */
@Data
@ApiModel(value = "SceneOpenApiResp", description = "场景出参")
public class SceneOpenApiResp implements Serializable {

    @ApiModelProperty(name = "id", value = "场景id")
    private Long id;

    @ApiModelProperty(name = "sceneName", value = "场景名字")
    private String sceneName;

    @ApiModelProperty(name = "businessLinkCount", value = "业务活动条数")
    private int businessLinkCount;

    @ApiModelProperty(name = "techLinkCount", value = "系统流程条数")
    private int techLinkCount;

    @ApiModelProperty(name = "ischange", value = "是否变更")
    private String ischange;

    @ApiModelProperty(name = "createTime", value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty(name = "managerName", value = "负责人")
    private String managerName;

    @ApiModelProperty(name = "managerId", value = "负责人id")
    private Long managerId;

    @ApiModelProperty(name = "canEdit", value = "是否可编辑")
    private Boolean canEdit = true;

    @ApiModelProperty(name = "canEdit", value = "是否可删除")
    private Boolean canRemove = true;
}
