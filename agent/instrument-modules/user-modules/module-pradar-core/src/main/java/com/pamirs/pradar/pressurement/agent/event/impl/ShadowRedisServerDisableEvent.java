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
package com.pamirs.pradar.pressurement.agent.event.impl;

import com.pamirs.pradar.internal.config.ShadowRedisConfig;
import com.pamirs.pradar.pressurement.agent.event.IEvent;

import java.util.List;

/**
 * @Auther: vernon
 * @Date: 2020/12/5 00:16
 * @Description:
 */
public class ShadowRedisServerDisableEvent implements IEvent {
    private List<ShadowRedisConfig> config;

    public ShadowRedisServerDisableEvent(List<ShadowRedisConfig> config) {
        this.config = config;
    }

    @Override
    public Object getTarget() {
        return config;
    }
}
