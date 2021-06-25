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

package io.shulie.tro.web.app.request.user;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: fanxx
 * @Date: 2021/3/8 10:56 上午
 * @Description:
 */
@Data
@ApiModel("部门修改对象")
public class DeptUpdateRequest {

    @ApiModelProperty(name = "id", value = "部门id")
    @NotNull
    private Long id;

    @ApiModelProperty(name = "name", value = "部门名称")
    @NotBlank
    private String name;

    @ApiModelProperty(name = "parentId", value = "父级部门id")
    private Long parentId;
}
