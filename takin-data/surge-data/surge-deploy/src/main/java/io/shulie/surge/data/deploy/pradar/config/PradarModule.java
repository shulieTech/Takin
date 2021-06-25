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

import io.shulie.surge.data.runtime.common.TaskIdentifier;
import io.shulie.surge.data.runtime.common.impl.DefaultTaskIdentifier;
import io.shulie.surge.data.runtime.module.BaseConfigModule;

/**
 *
 */
public class PradarModule extends BaseConfigModule {
    private Integer workPort;

    public PradarModule() {
    }

    public PradarModule(Integer workPort) {
        this.workPort = workPort;
    }

    @Override
    protected void configure() {
        bindGeneric(PradarProcessor.class, PradarProcessorGenericFactory.class, PradarProcessorConfigSpec.class);

        DefaultTaskIdentifier identifier = new DefaultTaskIdentifier();
        identifier.setWorkerId(String.valueOf(workPort == null ? 0 : workPort));
        bind(TaskIdentifier.class).toInstance(identifier);
    }
}
