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

package io.shulie.tro.web.app.request.perfomanceanaly;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotNull;

/**
 * @Author: mubai
 * @Date: 2020-11-16 15:54
 * @Description:
 */

@Data
public class PressureMachineQueryRequest {


    /**
     * 压力机名称
     */
    @ApiModelProperty(value = "压力机名称")
    @NotNull
    private String name;

    /**
     * 压力机IP
     */
    @ApiModelProperty(value = "压力机IP")
    @NotNull
    private String ip;

    /**
     * 标签
     */
    @ApiModelProperty(value = "标签")
    private String flag;

    /**
     * 状态 0：空闲 ；1：压测中  -1:离线
     */
    @ApiModelProperty(value = "机器状态")
    @NotNull
    private Integer status;


}
