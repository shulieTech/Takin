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

package io.shulie.tro.eventcenter.entity;

import lombok.Data;

import java.util.Map;

/**
 * @ClassName SlaBean
 * @Description
 * @Author qianshui
 * @Date 2020/4/22 下午1:53
 */
@Data
public class SlaBean {
    /**
     * 场景ID
     */
    private Long sceneId;

    /**
     * 触发数据
     * type -> TPS RT 成功率 SA
     * unit -> 空 ms % %
     * compare -> >= > = <= <
     * real -> 实际值
     */
    private Map<String, Object> achieveMap;
}
