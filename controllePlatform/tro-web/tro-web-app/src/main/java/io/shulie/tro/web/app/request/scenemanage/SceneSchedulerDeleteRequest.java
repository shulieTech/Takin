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

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: mubai
 * @Date: 2020-12-03 16:55
 * @Description:
 */

@Data
@ApiModel(value = "取消场景定时启动模型")
public class SceneSchedulerDeleteRequest {
    @ApiModelProperty(name = "sceneId", value = "场景id")
    @NotNull(message = "场景id不能为空")
    private Long sceneId;
}
