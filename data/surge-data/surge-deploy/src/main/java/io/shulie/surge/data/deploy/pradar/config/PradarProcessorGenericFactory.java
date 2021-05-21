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

import io.shulie.surge.data.common.factory.GenericFactory;
import io.shulie.surge.data.runtime.processor.ProcessorConfigSpec;

/**
 * @author vincent
 */
public class PradarProcessorGenericFactory implements GenericFactory<PradarProcessor, ProcessorConfigSpec<PradarProcessor>> {
    /**
     * 按照参数配置来创建 T
     *
     * @param spec
     * @return
     * @throws Exception 参数不正确，或创建失败时抛出异常
     */
    @Override
    public PradarProcessor create(ProcessorConfigSpec spec) throws Exception {
        PradarProcessor processor = new PradarProcessor();
        processor.setProcessorConfig(spec);
        return processor;
    }
}
