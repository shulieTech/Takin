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
package com.shulie.instrument.simulator.core.manager;

import com.shulie.instrument.simulator.api.listener.EventListener;
import com.shulie.instrument.simulator.api.listener.ext.BuildingForListeners;

import java.io.File;
import java.lang.instrument.ClassFileTransformer;
import java.util.List;
import java.util.Map;

/**
 * 仿真器类形变器
 */
public abstract class SimulatorClassFileTransformer implements ClassFileTransformer {

    /**
     * 获取观察ID
     *
     * @return 观察ID
     */
    public abstract int getWatchId();

    /**
     * 获取事件监听器
     *
     * @return 事件监听器
     */
    public abstract Map<Integer, EventListener> getEventListeners();

    /**
     * 获取所有的listener
     *
     * @return
     */
    public abstract List<BuildingForListeners> getAllListeners();

    /**
     * 获取本次匹配器
     *
     * @return 匹配器
     */
    public abstract Object getMatcher();

    /**
     * 获取本次增强的影响统计
     *
     * @return 本次增强的影响统计
     */
    public abstract AffectStatistic getAffectStatistic();

    public Map<String, File> getDumpResult() {
        return null;
    }
}
