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

import com.pamirs.pradar.pressurement.agent.event.IEvent;

/**
 * @author : wangjian
 * @since : 2020/9/8 14:38
 */
public class WhiteListSwitchOnEvent implements IEvent {

    private Object target;

    public WhiteListSwitchOnEvent(Object target) {
        this.target = target;
    }

    @Override
    public Object getTarget() {
        return target;
    }
}
