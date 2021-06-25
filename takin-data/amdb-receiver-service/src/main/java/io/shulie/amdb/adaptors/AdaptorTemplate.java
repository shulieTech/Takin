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
