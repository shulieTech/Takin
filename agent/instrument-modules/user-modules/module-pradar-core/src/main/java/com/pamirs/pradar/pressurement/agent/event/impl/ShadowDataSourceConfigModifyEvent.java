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

import com.pamirs.pradar.internal.config.ShadowDatabaseConfig;
import com.pamirs.pradar.pressurement.agent.event.IEvent;

import java.util.Set;

/**
 * @ClassName: ShadowDataSourceConfigModifyEvent
 * @author: wangjian
 * @Date: 2020/12/22 10:20
 * @Description:
 */
public class ShadowDataSourceConfigModifyEvent implements IEvent {

    private Set<ShadowDatabaseConfig> closedDatasources;

    public ShadowDataSourceConfigModifyEvent(Set<ShadowDatabaseConfig> closedDatasources) {
        this.closedDatasources = closedDatasources;
    }

    @Override
    public Set<ShadowDatabaseConfig> getTarget() {
        return this.closedDatasources;
    }
}
