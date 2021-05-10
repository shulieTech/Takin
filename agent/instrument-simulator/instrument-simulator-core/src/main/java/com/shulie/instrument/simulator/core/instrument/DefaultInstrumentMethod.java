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
package com.shulie.instrument.simulator.core.instrument;

import com.shulie.instrument.simulator.api.event.EventType;
import com.shulie.instrument.simulator.api.instrument.InstrumentMethod;
import com.shulie.instrument.simulator.api.listener.Listeners;
import com.shulie.instrument.simulator.api.listener.ext.IBehaviorMatchBuilder;

/**
 * 默认的增强方法实现，包括方法和构造器
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/10/11 6:10 下午
 */
public class DefaultInstrumentMethod implements InstrumentMethod {
    private IBehaviorMatchBuilder buildingForBehavior;

    public DefaultInstrumentMethod(IBehaviorMatchBuilder buildingForBehavior) {
        this.buildingForBehavior = buildingForBehavior;
    }

    @Override
    public InstrumentMethod withNot() {
        this.buildingForBehavior.withNot();
        return this;
    }

    @Override
    public void addInterceptor(Listeners listeners) {
        this.buildingForBehavior.onListener(listeners, EventType.BEFORE, EventType.RETURN, EventType.THROWS);
    }

    @Override
    public void addInterceptor(Listeners listeners, EventType... types) {
        this.buildingForBehavior.onListener(listeners, types);
    }

}
