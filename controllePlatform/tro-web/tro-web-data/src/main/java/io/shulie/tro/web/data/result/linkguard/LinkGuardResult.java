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

package io.shulie.tro.web.data.result.linkguard;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author shiyajian
 * create: 2020-12-17
 */
@Data
public class LinkGuardResult {

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
}
