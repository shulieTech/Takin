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

package com.pamirs.tro.entity.domain.dto.linkmanage;

import com.pamirs.tro.entity.domain.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author: fanxx
 * @Date: 2020/8/17 下午9:42
 * @Description:
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "InterfaceVo", description = "应用接口模型")
public class InterfaceVo extends BaseEntity {

    @ApiModelProperty(name = "id", value = "接口ID")
    private String id;

    @ApiModelProperty(name = "interfaceType", value = "接口类型")
    private String interfaceType;

    @ApiModelProperty(name = "interfaceName", value = "接口名称")
    private String interfaceName;
}
