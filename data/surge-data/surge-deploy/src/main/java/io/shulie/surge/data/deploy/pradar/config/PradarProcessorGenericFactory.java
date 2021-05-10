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
