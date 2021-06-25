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

package io.shulie.tro.web.app.request.scriptmanage;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author zhaoyong
 */
@Data
public class ScriptTagCreateRefRequest implements Serializable {
    private static final long serialVersionUID = -1835666947708446002L;

    /**
     * 脚本发布实例id
     */

    @JsonProperty("scriptId")
    @NotNull(message = "脚本id不能为空")
    private Long scriptDeployId;

    /**
     * 标签id列表
     */
    private List<String> tagNames;
}
