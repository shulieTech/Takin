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

import com.pamirs.pradar.internal.config.ShadowJob;
import com.pamirs.pradar.pressurement.agent.event.IEvent;

/**
 * 影子JOB注册事件
 *
 * @author <a href="tangyuhan@shulie.io">yuhan.tang</a>
 * @since 2020-07-20 20:35
 */
public class ShadowJobRegisterEvent implements IEvent {

    private ShadowJob shadowJob;

    public ShadowJobRegisterEvent(ShadowJob shadowJob) {
        this.shadowJob = shadowJob;
    }

    @Override
    public Object getTarget() {
        return shadowJob;
    }
}
