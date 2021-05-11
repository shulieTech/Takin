package io.shulie.amdb.adaptors;


import io.shulie.amdb.adaptors.common.Closeable;
import io.shulie.amdb.adaptors.common.Startable;
import io.shulie.amdb.adaptors.connector.Connector;

/**
 * 执行模板
 *
 * @author vincent
 */
public interface AdaptorTemplate extends Startable, Closeable {


    /**
     * 添加拦截器
     * @param connectorType
     */
    void addConnector(Connector.ConnectorType connectorType);

    /**
     * 添加处理的path
     *
     * @param connectorType
     * @param path
     * @param paramsClazz
     * @param adaptor
     * @param <T>
     */
    <T> void addPath(Connector.ConnectorType connectorType, String path, Class<T> paramsClazz, Adaptor adaptor) throws Exception;


    /**
     * @throws Exception
     */
    void init() throws Exception;


    /**
     * 获取适配器实例
     *
     * @param clazz
     * @return
     */
    Adaptor getAdapter(Class<? extends Adaptor> clazz);


}
