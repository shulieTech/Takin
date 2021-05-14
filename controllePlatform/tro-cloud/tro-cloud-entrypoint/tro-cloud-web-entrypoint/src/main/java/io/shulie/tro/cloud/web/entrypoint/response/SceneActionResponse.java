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

package io.shulie.tro.cloud.web.entrypoint.response;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName SceneActionResponse
 * @Description
 * @Author qianshui
 * @Date 2020/11/13 上午11:03
 */
@Data
@ApiModel("状态检查返回值")
public class SceneActionResponse implements Serializable {

    private static final long serialVersionUID = -2592300523249555242L;

    @ApiModelProperty(value = "状态值")
    private Long data;

    @ApiModelProperty(value = "错误信息")
    private List<String> msg;
}