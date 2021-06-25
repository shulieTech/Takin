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

package io.shulie.tro.cloud.data.result.engine;

import lombok.Data;

import java.io.Serializable;

/**
 * 引擎插件简介结果
 *
 * @author lipeng
 * @date 2021-01-20 5:00 下午
 */
@Data
public class EnginePluginSimpleInfoResult implements Serializable {

    private Long pluginId;

    private String pluginType;

    private String pluginName;

    private String gmtUpdate;

}
