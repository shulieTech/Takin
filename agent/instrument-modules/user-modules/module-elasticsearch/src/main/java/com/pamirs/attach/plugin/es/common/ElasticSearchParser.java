/**
 * Copyright 2021 Shulie Technology, Co.Ltd
 * Email: shulie@shulie.io
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.pamirs.attach.plugin.es.common;

import com.pamirs.attach.plugin.es.ElasticsearchConstants;
import com.shulie.instrument.simulator.api.util.CollectionUtils;
import org.elasticsearch.client.support.AbstractClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.node.DiscoveryNode;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author vincent
 * @version v0.1 2017/2/15 14:35
 */
public class ElasticSearchParser {
    private static Method getAddressMethod;
    private static Method getPortMethod;

    public static String parseAddressAndPort(AbstractClient abstractClient) {
        return doParseAddressAndPort(abstractClient);
    }

    private static void initMethod(TransportClient client) {
        if (client == null) {
            return;
        }
        if (getAddressMethod == null || getPortMethod == null) {
            List<DiscoveryNode> nodes = client.listedNodes();
            if (CollectionUtils.isNotEmpty(nodes)) {
                Object tmpObject = client.listedNodes().iterator().next().getAddress();//TransportAddress
                Class<?> clazz = tmpObject.getClass();
                try {
                    getAddressMethod = clazz.getMethod("getAddress");
                    getPortMethod = clazz.getMethod("getPort");
                } catch (Throwable e) {
                }
            }
        }
    }

    public static String doParseAddressAndPort(AbstractClient abstractClient) {
        if (!(abstractClient instanceof TransportClient)) {
            return ElasticsearchConstants.ELASTICSEARCH_ADDRESS_UNKNOW;
        }
        TransportClient client = (TransportClient) abstractClient;
        StringBuilder addressAndPorts = new StringBuilder();

        List<DiscoveryNode> nodes = client.listedNodes();
        initMethod(client);
        if (nodes.size() > 0) {
            for (DiscoveryNode discoveryNode : nodes) {
                Object object = discoveryNode.getAddress(); //this is a TransportAddress instance
                try {
                    addressAndPorts.append(getAddressMethod.invoke(object)).append(":").append(
                            getPortMethod.invoke(object)).append(";");
                } catch (Throwable e) {
                    return ElasticsearchConstants.ELASTICSEARCH_ADDRESS_UNKNOW;
                }
            }
        }
        return addressAndPorts.length() > 0 ?
                addressAndPorts.substring(0, addressAndPorts.length() - 1) : addressAndPorts.toString();
    }
}
