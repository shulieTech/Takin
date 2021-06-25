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

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author ZhangXT
 * @Description
 * @Date 2020/11/4 14:58
 */
@Data
@ApiModel(value = "UserUpdateRequest", description = "更新用户入参")
public class UserUpdateRequest {

    @NotNull
    @ApiModelProperty(name = "id", value = "用户id")
    private Long id;

    @ApiModelProperty(name = "name", value = "用户名称")
    @Size(max = 20)
    @NotBlank
    private String name;

    @ApiModelProperty(name = "password", value = "密码")
    @Size(min = 8, max = 20)
    @NotBlank
    private String password;

    @ApiModelProperty(name = "deptIdList", value = "所属部门")
    @NotEmpty
    private List<Long> deptIdList;
}
