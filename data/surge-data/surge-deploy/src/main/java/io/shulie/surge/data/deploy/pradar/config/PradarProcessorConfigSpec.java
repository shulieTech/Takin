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
