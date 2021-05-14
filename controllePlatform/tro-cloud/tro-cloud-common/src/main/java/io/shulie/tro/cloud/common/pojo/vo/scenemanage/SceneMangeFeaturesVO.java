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

package io.shulie.tro.cloud.common.pojo.vo.scenemanage;

import lombok.Data;

/**
 * 场景扩展字段, json 转的类
 *
 * @author liuchuan
 * @date 2021/4/26 11:00 上午
 */
@Data
public class SceneMangeFeaturesVO {

    /**
     * 关联的脚本id
     * 对应的是 web 下的 脚本实例id
     */
    private Long scriptId;

    /**
     * 配置类型
     */
    private Integer configType;

}
