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
package com.pamirs.attach.plugin.es.shadowserver.rest.definition;

import java.util.List;

import com.pamirs.attach.plugin.es.shadowserver.utils.HostAndPort;
import com.pamirs.pradar.internal.config.ShadowEsServerConfig;
import com.shulie.instrument.simulator.api.reflect.Reflect;
import com.shulie.instrument.simulator.api.reflect.ReflectException;
import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClient.FailureListener;
import org.elasticsearch.client.RestClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jirenhe | jirenhe@shulie.io
 * @since 2021/04/25 9:04 下午
 */
public abstract class AbstractRestClientDefinition implements RestClientDefinition {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public RestClient solve(RestClient target, ShadowEsServerConfig shadowEsServerConfig) {
        HttpHost[] httpHosts = new HttpHost[shadowEsServerConfig.getPerformanceTestNodes().size()];
        for (int i = 0; i < shadowEsServerConfig.getPerformanceTestNodes().size(); i++) {
            httpHosts[i] = new HostAndPort(shadowEsServerConfig.getPerformanceTestNodes().get(i)).toHttpHost();
        }
        RestClientBuilder builder = RestClient.builder(httpHosts);
        assembleBuilder(target, builder);
        return builder.build();
    }

    protected void assembleBuilder(RestClient target, RestClientBuilder builder) {
        List<Header> headers = reflectSilence(target, "defaultHeaders");
        if (headers != null) {
            Header[] defaultHeaders = (Header[])headers.toArray();
            builder.setDefaultHeaders(defaultHeaders);
        }
        String pathPrefix = reflectSilence(target, "pathPrefix");
        if (StringUtils.isNotBlank(pathPrefix)) {
            builder.setPathPrefix(pathPrefix);
        }
        FailureListener failureListener = reflectSilence(target, "pathPrefix");
        if (failureListener != null) {
            builder.setFailureListener(failureListener);
        }
        doAssembleBuilder(target, builder);
    }

    protected abstract void doAssembleBuilder(RestClient target, RestClientBuilder builder);

    protected <T> T reflectSilence(RestClient target, String name) {
        try {
            return Reflect.on(target).get(name);
        } catch (ReflectException e) {
            logger.warn("can not find field '{}' from : '{}'", name, target.getClass().getName());
            return null;
        }
    }

}
