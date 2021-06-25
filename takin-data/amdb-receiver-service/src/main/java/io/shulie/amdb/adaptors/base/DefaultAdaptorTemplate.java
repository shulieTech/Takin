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

package io.shulie.amdb.adaptors.base;

import com.google.common.collect.Maps;
import io.shulie.amdb.adaptors.Adaptor;
import io.shulie.amdb.adaptors.AdaptorTemplate;
import io.shulie.amdb.adaptors.connector.Connector;
import io.shulie.amdb.adaptors.connector.ConnnectorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;

/**
 * 适配器模板类
 *
 * @author vincent
 */
public class DefaultAdaptorTemplate implements AdaptorTemplate {

    private Map<Connector.ConnectorType, Connector> connectors = Maps.newHashMap();

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultAdaptorTemplate.class);

    @Override
    public void addConnector(Connector.ConnectorType connectorType) {
        connectors.put(connectorType, ConnnectorFactory.getFactory().getConnnectors().get(connectorType));
    }

    @Override
    /**
     * 增加监听path
     */
    public <T> void addPath(Connector.ConnectorType connectorType, String path, Class<T> paramsClazz, Adaptor adaptor) throws Exception {
        Connector connector = connectors.get(connectorType);
        if (!connectors.containsKey(connectorType)) {
            LOGGER.error("Connnector is not register");
        }
        connector.addPath(path, paramsClazz, adaptor);
    }

    @Override
    public void init() throws Exception {
        if (connectors == null || connectors.isEmpty()) {
            return;
        }
        for (Connector connector : connectors.values()) {
            connector.init();
        }
    }

    //@Override
    public Adaptor getAdapter(Class<? extends Adaptor> clazz) {
        if (clazz == null) {
            return null;
        }
        Set<Adaptor> adaptors = AdaptorFactory.getFactory().getAdaptors();
        for (Adaptor adaptor : adaptors) {
            if (adaptor.getClass().equals(clazz)) {
                return adaptor;
            }
        }
        return null;
    }

    @Override
    public boolean close() throws Exception {
        if (connectors == null || connectors.isEmpty()) {
            return false;
        }
        for (Connector connector : connectors.values()) {
            connector.close();
        }
        return true;
    }

    @Override
    public void start() throws Exception {
        if (connectors == null || connectors.isEmpty()) {
            return;
        }
        for (Connector connector : connectors.values()) {
            connector.init();
            connector.start();
        }
    }
}
