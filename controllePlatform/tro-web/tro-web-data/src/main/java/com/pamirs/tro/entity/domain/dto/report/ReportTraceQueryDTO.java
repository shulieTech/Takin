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

package com.pamirs.tro.entity.domain.dto.report;

import com.pamirs.tro.entity.domain.PagingDevice;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName ReportCountDTO
 * @Description 压测报告；统计返回
 * @Author qianshui
 * @Date 2020/7/22 下午2:19
 */
@ApiModel
@Data
public class ReportTraceQueryDTO extends PagingDevice {

    private static final long serialVersionUID = 8928035842416997931L;

    @ApiModelProperty("场景id")
    private Long sceneId;

    @ApiModelProperty("报告id，如果是压测报告中有此参数")
    private Long reportId;

    @ApiModelProperty("开始压测的时间戳")
    private Long startTime;

    @ApiModelProperty("查询条件，null 为全部，1为成功，0为失败")
    private Integer type;

}
