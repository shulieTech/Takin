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

package io.shulie.tro.cloud.data.result.scenemanage;

import java.io.Serializable;

import lombok.Data;

/**
 * 更新对应脚本时, 场景列表
 *
 * @author liuchuan
 */
@Data
public class SceneManageListFromUpdateScriptResult implements Serializable {

    private static final long serialVersionUID = -3967473117069389164L;

    /**
     * 场景id
     */
    private Long id;

    /**
     * 租户id
     */
    private Long customId;

    /**
     * 扩展字段
     */
    private String features;
}
