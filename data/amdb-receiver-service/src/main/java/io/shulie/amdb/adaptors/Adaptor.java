package io.shulie.amdb.adaptors;


import io.shulie.amdb.adaptors.common.Closeable;
import io.shulie.amdb.adaptors.connector.Processor;

import java.util.Map;

/**
 * 适配器
 *
 * @author vincent
 */
public interface Adaptor extends Processor, Closeable {

    /**
     * 添加拦截器支持
     */
    void addConnector();

    /**
     * 注册适配器
     *
     * @return
     */
    void registor();


    /**
     * 适配器操作
     *
     * @param adaptorTemplate
     */
    void setAdaptorTemplate(AdaptorTemplate adaptorTemplate);

    /**
     * @param config
     */
    void addConfig(Map<String, Object> config);
}
