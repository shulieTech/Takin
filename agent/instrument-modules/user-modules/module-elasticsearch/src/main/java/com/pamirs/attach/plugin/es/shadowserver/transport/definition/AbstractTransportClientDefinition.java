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
package com.pamirs.attach.plugin.es.shadowserver.transport.definition;

import java.net.UnknownHostException;

import com.pamirs.attach.plugin.es.shadowserver.rest.definition.TransportClientDefinition;
import com.pamirs.attach.plugin.es.shadowserver.utils.HostAndPort;
import com.pamirs.pradar.exception.PressureMeasureError;
import com.pamirs.pradar.internal.config.ShadowEsServerConfig;
import com.shulie.instrument.simulator.api.reflect.Reflect;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jirenhe | jirenhe@shulie.io
 * @since 2021/04/26 10:16 上午
 */
public abstract class AbstractTransportClientDefinition implements TransportClientDefinition {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public TransportClient solve(TransportClient target, ShadowEsServerConfig shadowEsServerConfig) {
        Settings settings = getSettings(target);
        settings = withPtClusterNameSettings(settings, shadowEsServerConfig);
        TransportClient transportClient = doSolve(settings, target);
        addPtTransportAddress(transportClient, shadowEsServerConfig);
        return transportClient;
    }

    protected void addPtTransportAddress(TransportClient transportClient, ShadowEsServerConfig shadowEsServerConfig) {
        for (String performanceTestNode : shadowEsServerConfig.getPerformanceTestNodes()) {
            try {
                HostAndPort hostAndPort = new HostAndPort(performanceTestNode);
                transportClient.addTransportAddress(hostAndPort.toTransportAddress());
            } catch (UnknownHostException e) {
                throw new PressureMeasureError("影子sever nodes配置错误!", e);
            }
        }
    }

    protected Settings getSettings(TransportClient target) {
        try {
            return Reflect.on(target).get("settings");
        } catch (Throwable e) {
            return Settings.EMPTY;
        }
    }

    protected Settings withPtClusterNameSettings(Settings settings, ShadowEsServerConfig esServerConfig) {
        String clusterName = esServerConfig.getPtClusterName();
        if (StringUtils.isEmpty(clusterName)) {
            logger.error("pt cluster.name is empty, use biz cluster.name, biz url = {}"
                , esServerConfig.getBusinessNodes().toString());
            return settings;
        }
        logger.info("use pt cluster.name, pt culster.name = {}"
            , esServerConfig.getPtClusterName());
        return Settings.builder().put(settings).put("cluster.name", clusterName)
            .build();
    }

    protected abstract TransportClient doSolve(Settings settings,
        TransportClient target);
}
