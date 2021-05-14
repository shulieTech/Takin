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

package com.pamirs.tro.entity.domain.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author <a href="tangyuhan@shulie.io">yuhan.tang</a>
 * @package: com.pamirs.tro.entity.domain.query
 * @Date 2020-03-17 15:47
 */
@ApiModel(value = "ShadowJobConfigQuery", description = "影子JOB配置查询实体类")
@Data
public class ShadowJobConfigQuery extends QueryPage {

    @ApiModelProperty(name = "id", value = "id")
    private Long id;

    @ApiModelProperty(name = "applicationId", value = "应用ID")
    private Long applicationId;

    @ApiModelProperty(name = "name", value = "JOB任务名称")
    private String name;

    @ApiModelProperty(name = "type", value = "JOB类型 0-quartz、1-elastic-job、2-xxl-job")
    private Integer type;

    @ApiModelProperty(name = "status", value = "0-可用 1-不可用")
    private Integer status;

    @ApiModelProperty(name = "configCode", value = "JOB配置xml")
    private String configCode;

    @ApiModelProperty(name = "orderBy", value = "排序字段")
    private String orderBy;

    @ApiModelProperty(name = "active", value = "检测影子JOB是否生效")
    private Integer active;

    @ApiModelProperty(name = "remark", value = "备注")
    private String remark;

}
