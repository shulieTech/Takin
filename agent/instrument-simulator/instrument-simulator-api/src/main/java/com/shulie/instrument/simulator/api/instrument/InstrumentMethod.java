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
package com.shulie.instrument.simulator.api.instrument;

import com.shulie.instrument.simulator.api.event.EventType;
import com.shulie.instrument.simulator.api.listener.Listeners;

/**
 * 增强方法
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/10/11 5:51 下午
 */
public interface InstrumentMethod {
    /**
     * 匹配规则取反
     */
    InstrumentMethod withNot();

    /**
     * 添加拦截器
     *
     * @param listeners 增强回调
     */
    void addInterceptor(Listeners listeners);


    /**
     * 添加拦截器
     *
     * @param listeners
     * @param types
     */
    void addInterceptor(Listeners listeners, EventType... types);
}
