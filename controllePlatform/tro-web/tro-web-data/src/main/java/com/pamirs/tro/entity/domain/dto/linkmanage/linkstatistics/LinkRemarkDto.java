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

package com.pamirs.tro.entity.domain.dto.linkmanage.linkstatistics;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Auther: vernon
 * @Date: 2019/12/10 00:50
 * @Description:链路标记页面出参
 */
@Data
public class LinkRemarkDto {
    @ApiModelProperty(name = "businessProcessCount", value = "接入业务情况")
    private String businessProcessCount;
    @ApiModelProperty(name = "businessActiveCount", value = "覆盖业务情况")
    private String businessActiveCount;
    @ApiModelProperty(name = "systemProcessCount", value = "涵盖系统流程")
    private String systemProcessCount;
    @ApiModelProperty(name = "systemChangeCount", value = "系统流程变更")
    private String systemChangeCount;
    @ApiModelProperty(name = "onLineApplicationCount", value = "应用接入情况")
    private String onLineApplicationCount;
    @ApiModelProperty(name = "linkGuardCount", value = "挡板数量")
    private String linkGuardCount;

   /* @ApiModelProperty(name = "linkRemarkmiddleWareDtos", value = "链路标记页面中间件信息出参")
    List<LinkRemarkmiddleWareDto> linkRemarkmiddleWareDtos;*/
}
