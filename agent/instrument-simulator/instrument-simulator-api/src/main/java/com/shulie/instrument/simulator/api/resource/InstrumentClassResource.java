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
package com.shulie.instrument.simulator.api.resource;

import com.shulie.instrument.simulator.api.instrument.InstrumentClass;

/**
 * 增强类资源
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/10/11 7:34 下午
 */
public class InstrumentClassResource extends ReleaseResource<InstrumentClass> {
    /**
     * 构造释放资源
     *
     * @param resource 资源目标
     */
    public InstrumentClassResource(InstrumentClass resource) {
        super(resource);
    }

    @Override
    public void release() {
        if (get() != null) {
            get().reset();
        }
    }
}
