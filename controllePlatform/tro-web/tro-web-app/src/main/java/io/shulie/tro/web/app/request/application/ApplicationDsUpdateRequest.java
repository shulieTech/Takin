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

package io.shulie.tro.web.app.request.application;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @Author: fanxx
 * @Date: 2020/11/27 5:23 下午
 * @Description:
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Valid
public class ApplicationDsUpdateRequest extends ApplicationDsCreateRequest {

    /**
     * 配置id
     */
    @NotNull
    private Long id;
}
