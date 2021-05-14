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

package com.pamirs.tro.entity.domain.vo.report;

import java.io.Serializable;

import io.shulie.tro.web.common.domain.WebRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author 莫问
 * @Date 2020-04-17
 */
@Data
@ApiModel
public class ReportTrendQueryParam extends WebRequest implements Serializable {

    private static final long serialVersionUID = -8391664190402372494L;

    /**
     * 报表ID
     */
    @ApiModelProperty(value = "报表ID")
    private Long reportId;

    /**
     * 场景ID
     */
    @ApiModelProperty(value = "场景ID")
    private Long sceneId;

    /**
     * 活动ID
     */
    @ApiModelProperty(value = "活动ID，如果不指定活动ID则表示全局趋势")
    private Long businessActivityId;

    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间")
    private String startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    private String endTime;
}
