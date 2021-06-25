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

package io.shulie.tro.web.app.request.scenemanage;

import java.util.Date;

import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * @Author: mubai
 * @Date: 2020-12-01 10:32
 * @Description:
 */
@Data
public class SceneSchedulerTaskCreateRequest {

    @NotNull(message = "场景id不能为空")
    private Long sceneId ;

    private Long userId ;

    private String content ;

    @NotNull(message = "执行时间不能为空")
    private Date executeTime ;

    private Integer isExecuted ;

}
