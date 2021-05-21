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

package io.shulie.amdb.adaptors.connector;

import com.google.common.collect.Maps;

import java.util.Map;
import java.util.ServiceLoader;

/**
 * 适配器工厂类
 *
 * @author vincent
 */
public class ConnnectorFactory {

    private Map<Connector.ConnectorType, Connector> connectorMap = Maps.newHashMap();

    private void init() {
        ServiceLoader<Connector> connectors = ServiceLoader.load(Connector.class);
        for (Connector connector : connectors) {
            connectorMap.put(connector.getType(), connector);
        }
    }

    private ConnnectorFactory() {
        init();
    }

    private static class ConnnectorFactoryHolder {
        public final static ConnnectorFactory INSTANCE = new ConnnectorFactory();
    }

    public static ConnnectorFactory getFactory() {
        return ConnnectorFactoryHolder.INSTANCE;
    }

    /**
     * 获取适配器列表
     *
     * @return
     */
    public Map<Connector.ConnectorType, Connector> getConnnectors() {
        return connectorMap;
    }

}
