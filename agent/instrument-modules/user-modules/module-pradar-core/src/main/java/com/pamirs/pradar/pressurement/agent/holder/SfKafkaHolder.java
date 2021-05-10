/**
 * Copyright 2021 Shulie Technology, Co.Ltd
 * Email: shulie@shulie.io
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.pamirs.pradar.pressurement.agent.holder;

import java.util.HashMap;
import java.util.Map;

/**
 * 保存缓存影子kafka对象
 */
public class SfKafkaHolder {

    /**
     * 顺丰kafka 传递出参数对象
     */
    public static Map<String, Object> sfKafkaPool = new HashMap<String, Object>(3);

    /**
     * 顺丰kafka Topic注入结果
     */
    public static Map<String, Boolean> sfKafkaInjectResult = new HashMap<String, Boolean>(3);

    /**
     * kafka配置参数保存对象
     */
    public static Map<String, Object> sfKafkaPluginConfig = new HashMap<String, Object>(3);
}
