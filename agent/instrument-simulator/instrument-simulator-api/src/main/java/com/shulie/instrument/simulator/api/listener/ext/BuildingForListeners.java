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
package com.shulie.instrument.simulator.api.listener.ext;

import com.shulie.instrument.simulator.api.event.EventType;
import com.shulie.instrument.simulator.api.listener.Listeners;

import java.util.Arrays;

/**
 * 监听器构造器
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/10/24 1:29 下午
 */
public class BuildingForListeners {
    public final static BuildingForListeners NO_ACTION = new BuildingForListeners(null);

    private final Listeners listeners;
    private final EventType[] eventTypes;


    public BuildingForListeners(final Listeners listeners, final EventType... eventTypes) {
        this.listeners = listeners;
        this.eventTypes = eventTypes;
    }

    public Listeners getListeners() {
        return listeners;
    }

    public EventType[] getEventTypes() {
        return eventTypes;
    }

    public int getListenerId() {
        return System.identityHashCode(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BuildingForListeners that = (BuildingForListeners) o;

        if (listeners != null ? !listeners.equals(that.listeners) : that.listeners != null) {
            return false;
        }
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(eventTypes, that.eventTypes);
    }

    @Override
    public int hashCode() {
        int result = listeners != null ? listeners.hashCode() : 0;
        result = 31 * result + Arrays.hashCode(eventTypes);
        return result;
    }
}
