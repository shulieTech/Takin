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
 * @Date: 2021/3/8 2:49 下午
 * @Description:
 */
@Data
@ApiModel(value = "UserPasswordUpdateRequest", description = "更新密码入参")
public class UserPasswordUpdateRequest {

    @ApiModelProperty(name = "id", value = "用户id")
    @NotNull
    private Long id;

    @ApiModelProperty(name = "oldPassword", value = "旧密码")
    @NotBlank
    private String oldPassword;

    @ApiModelProperty(name = "newPassword", value = "新密码")
    @NotBlank
    private String newPassword;
}
