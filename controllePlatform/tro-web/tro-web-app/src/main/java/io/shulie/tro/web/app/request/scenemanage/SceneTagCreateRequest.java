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

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: mubai
 * @Date: 2020-11-30 14:19
 * @Description:
 */
@Data
@ApiModel(value = "场景标签创建模型")
public class SceneTagCreateRequest implements Serializable {
    private static final long serialVersionUID = -7849149905765078978L;
    private Long id;

    /**
     * 标签名称
     */
    @ApiModelProperty("标签名称")
    private String tagName;
}
