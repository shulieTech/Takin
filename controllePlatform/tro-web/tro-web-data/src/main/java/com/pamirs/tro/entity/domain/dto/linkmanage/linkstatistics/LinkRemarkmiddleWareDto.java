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
 * @Date: 2019/12/10 00:57
 * @Description:链路标记页面中间件下拉框出参数
 */
@Data
public class LinkRemarkmiddleWareDto {
    private Long middleWareId;
    @ApiModelProperty(name = "middleWareType", value = "中间件类型")
    private String middleWareType;
    @ApiModelProperty(name = "middleWareName", value = "中间件名字")
    private String middleWareName;
    @ApiModelProperty(name = "version", value = "中间件版本")
    private String version;
    @ApiModelProperty(name = "bussinessProcessCount", value = "设计业务流程")
    private String bussinessProcessCount;
    @ApiModelProperty(name = "systemProcessCount", value = "涉及系统流程")
    private String systemProcessCount;
}
