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
package com.pamirs.attach.plugin.es.shadowserver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import com.pamirs.attach.plugin.es.shadowserver.rest.RestClientDefinitionStrategy;
import com.pamirs.attach.plugin.es.shadowserver.rest.definition.RestClientDefinition;
import com.pamirs.attach.plugin.es.shadowserver.rest.definition.TransportClientDefinition;
import com.pamirs.attach.plugin.es.shadowserver.transport.TransportClientDefinitionStrategy;
import com.pamirs.attach.plugin.es.shadowserver.utils.HostAndPort;
import com.pamirs.pradar.exception.PressureMeasureError;
import com.pamirs.pradar.internal.config.ShadowEsServerConfig;
import com.pamirs.pradar.pressurement.agent.event.IEvent;
import com.pamirs.pradar.pressurement.agent.event.impl.ShadowEsServerDisableEvent;
import com.pamirs.pradar.pressurement.agent.event.impl.ShadowEsServerRegisterEvent;
import com.pamirs.pradar.pressurement.agent.listener.EventResult;
import com.pamirs.pradar.pressurement.agent.listener.PradarEventListener;
import com.pamirs.pradar.pressurement.agent.shared.service.EventRouter;
import com.pamirs.pradar.pressurement.agent.shared.service.GlobalConfig;
import com.shulie.instrument.simulator.api.reflect.Reflect;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.client.Node;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.node.DiscoveryNode;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.transport.TransportAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jirenhe | jirenhe@shulie.io
 * @since 2021/04/09 2:42 下午
 */
@SuppressWarnings("unchecked")
public class ShadowEsClientHolder {

    protected final static Logger LOGGER = LoggerFactory.getLogger(ShadowEsClientHolder.class.getName());

    private final static Map<TransportClient, TransportClient> transportClientMapping =
        new ConcurrentHashMap<TransportClient, TransportClient>();

    private final static Map<RestClient, RestClient> restClientMapping =
        new ConcurrentHashMap<RestClient, RestClient>();

    private static final AtomicBoolean isAdded = new AtomicBoolean(false);

    private static boolean isLowVersionRestClient = false;

    public static void addListener() {
        if (!isAdded.compareAndSet(false, true)) {
            return;
        }
        EventRouter.router().addListener(new PradarEventListener() {

            @Override
            public EventResult onEvent(IEvent event) {
                if (event instanceof ShadowEsServerDisableEvent
                    || event instanceof ShadowEsServerRegisterEvent) {
                    List<ShadowEsServerConfig> shadowEsServerConfigs = (List<ShadowEsServerConfig>)event.getTarget();
                    synchronized (ShadowEsClientHolder.class) {
                        for (ShadowEsServerConfig config : shadowEsServerConfigs) {
                            for (Entry<TransportClient, TransportClient> entry : transportClientMapping.entrySet()) {
                                List<String> nodesAddressAsString = getNodesAddressAsString(entry.getKey());
                                if (config.matchBusinessNodes(nodesAddressAsString)) {
                                    TransportClient transportClient = transportClientMapping.remove(entry.getKey());
                                    if (transportClient != null) {
                                        transportClient.close();
                                    }
                                }
                            }
                            for (Entry<RestClient, RestClient> entry : restClientMapping.entrySet()) {
                                List<String> nodesAddressAsString = getNodesAddressAsString(entry.getKey());
                                if (config.matchBusinessNodes(nodesAddressAsString)) {
                                    RestClient restClient = restClientMapping.remove(entry.getKey());
                                    if (restClient != null) {
                                        try {
                                            restClient.close();
                                        } catch (IOException e) {
                                            LOGGER.error("shadow restClient close fail!", e);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                return EventResult.success(event.getTarget());
            }

            @Override
            public int order() {
                return Integer.MAX_VALUE;
            }
        });
    }

    public static TransportClient getShadowTransportClient(TransportClient target) {
        ShadowEsClientHolder.addListener();
        TransportClient transportClient = transportClientMapping.get(target);
        if (transportClient == null) {
            synchronized (ShadowEsClientHolder.class) {
                transportClient = transportClientMapping.get(target);
                if (transportClient == null) {
                    transportClient = createShadowTransportClient(target);
                    transportClientMapping.put(target, transportClient);
                }
            }
        }
        return transportClient;
    }

    public static RestClient getShadowRestClient(RestClient target) {
        ShadowEsClientHolder.addListener();
        RestClient restClient = restClientMapping.get(target);
        if (restClient == null) {
            synchronized (ShadowEsClientHolder.class) {
                restClient = restClientMapping.get(target);
                if (restClient == null) {
                    restClient = createShadowRestClient(target);
                    restClientMapping.put(target, restClient);
                }
            }
        }
        return restClient;
    }

    private static RestClient createShadowRestClient(RestClient target) {
        List<String> nodesAddressAsString = getNodesAddressAsString(target);
        ShadowEsServerConfig shadowEsServerConfig = findMatchShadowEsServerConfig(nodesAddressAsString);
        if (shadowEsServerConfig == null) {
            throw new PressureMeasureError(String.format(
                "影子集群未配置，业务节点：%s", StringUtils.join(nodesAddressAsString, ",")
            ));
        }
        RestClientDefinition restClientDefinition = RestClientDefinitionStrategy.match(target);
        return restClientDefinition.solve(target, shadowEsServerConfig);
    }

    private static TransportClient createShadowTransportClient(TransportClient target) {
        List<String> nodesAddressAsString = getNodesAddressAsString(target);
        ShadowEsServerConfig shadowEsServerConfig = findMatchShadowEsServerConfig(nodesAddressAsString);
        if (shadowEsServerConfig == null) {
            throw new PressureMeasureError(String.format(
                "影子集群未配置，业务节点：%s", StringUtils.join(nodesAddressAsString, ",")
            ));
        }
        TransportClientDefinition transportClientDefinition = TransportClientDefinitionStrategy.match(target);
        return transportClientDefinition.solve(target, shadowEsServerConfig);
    }

    private static ShadowEsServerConfig findMatchShadowEsServerConfig(List<String> nodesAsString) {
        for (Entry<String, ShadowEsServerConfig> entry : GlobalConfig.getInstance().getShadowEsServerConfigs()
            .entrySet()) {
            ShadowEsServerConfig shadowEsServerConfig = entry.getValue();
            if (shadowEsServerConfig.matchBusinessNodes(nodesAsString)) {
                return shadowEsServerConfig;
            }
        }
        return null;
    }

    private static List<String> getNodesAddressAsString(TransportClient target) {
        List<String> nodesAddressAsString = new ArrayList<String>();
        List<DiscoveryNode> nodes = target.listedNodes();
        for (DiscoveryNode node : nodes) {
            Object object = node.getAddress();
            if (HostAndPort.usingInetSocket) {
                InetSocketTransportAddress address = (InetSocketTransportAddress)object;
                nodesAddressAsString.add(address.getAddress() + ":" + address.getPort());
            } else {
                TransportAddress address = (TransportAddress)object;
                nodesAddressAsString.add(address.getAddress() + ":" + address.getPort());
            }
        }
        return nodesAddressAsString;
    }

    private static List<String> getNodesAddressAsString(RestClient target) {
        List<Node> nodes = getNodes(target);
        List<String> nodesAddressAsString = new ArrayList<String>(nodes.size());
        for (Node node : nodes) {
            nodesAddressAsString.add(node.getHost().getHostName() + ":" + node.getHost().getPort());
        }
        return nodesAddressAsString;
    }

    private static List<Node> getNodes(RestClient target) {
        List<Node> nodes;
        if (!isLowVersionRestClient) {
            try {
                nodes = target.getNodes();
            } catch (Exception e) {
                isLowVersionRestClient = true;
                nodes = Reflect.on(Reflect.on(target).get("hostTuple")).get("nodes");
            }
        } else {
            nodes = Reflect.on(Reflect.on(target).get("hostTuple")).get("nodes");
        }
        return nodes;
    }

}
