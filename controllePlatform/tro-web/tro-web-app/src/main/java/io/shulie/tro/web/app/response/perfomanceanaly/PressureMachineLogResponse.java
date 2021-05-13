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

package io.shulie.tro.web.app.response.perfomanceanaly;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: mubai
 * @Date: 2020-11-16 13:53
 * @Description:
 */

@Data
@ApiModel(value = "压力机详情趋势返回模型")
public class PressureMachineLogResponse implements Serializable {


    private static final long serialVersionUID = -7026185022939992174L;

    @ApiModelProperty(value = "cpu利用率")
    private List<TypeValueDateVo> cpuUsageList;

    @ApiModelProperty(value = "cpu load 列表")
    private List<TypeValueDateVo> cpuLoadList;

    @ApiModelProperty(value = "内存利用率")
    private List<TypeValueDateVo> memoryUsageList;

    @ApiModelProperty(value = "io等待率")
    private List<TypeValueDateVo> ioWaitPerList;

    @ApiModelProperty(value = "网络带宽利用率")
    private List<TypeValueDateVo> transmittedUsageList;
}
