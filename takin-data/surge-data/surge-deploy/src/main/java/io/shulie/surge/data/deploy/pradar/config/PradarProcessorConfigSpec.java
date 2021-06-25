/*
 * Copyright 2021 Shulie Technology, Co.Ltd
 * Email: shulie@shulie.io
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.shulie.surge.data.deploy.pradar.config;

import io.shulie.surge.data.runtime.processor.ProcessorConfigSpec;

/**
 * @author vincent
 */
public class PradarProcessorConfigSpec extends ProcessorConfigSpec<PradarProcessor> {

    /**
     * 返回工厂在注入时需要指定的名称
     *
     * @return
     */
    @Override
    public String factoryName() {
        return "pradarProcessor";
    }

    /**
     * @return 被创建的对象的 interface
     */
    @Override
    public Class<PradarProcessor> productClass() {
        return PradarProcessor.class;
    }
}
