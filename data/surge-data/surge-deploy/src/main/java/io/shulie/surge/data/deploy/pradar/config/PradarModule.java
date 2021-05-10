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
