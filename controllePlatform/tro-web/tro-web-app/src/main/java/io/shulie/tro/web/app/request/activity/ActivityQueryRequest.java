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

package io.shulie.tro.web.app.request.activity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.shulie.tro.common.beans.page.PagingDevice;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author shiyajian
 * create: 2020-12-29
 */
@Data
@ApiModel("业务活动查询对象")
public class ActivityQueryRequest extends PagingDevice {

    @ApiModelProperty("业务活动名称")
    private String activityName;

    @ApiModelProperty("业务域")
    private String domain;

    @ApiModelProperty("是否变更，0正常，1变更")
    @JsonProperty("ischange")
    private Integer isChange;
}
