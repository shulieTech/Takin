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

import java.util.Collection;
import java.util.Collections;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.client.transport.TransportClient.HostFailureListener;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.plugins.Plugin;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

/**
 * @author jirenhe | jirenhe@shulie.io
 * @since 2021/04/09 6:26 下午
 */
public class PreBuiltTransportClientDefinition extends AbstractTransportClientDefinition {

    private static final PreBuiltTransportClientDefinition INSTANCE = new PreBuiltTransportClientDefinition();

    public static PreBuiltTransportClientDefinition getInstance() {return INSTANCE;}

    private PreBuiltTransportClientDefinition() {}

    @Override
    protected TransportClient doSolve(Settings settings, TransportClient target) {
        Collection<Class<? extends Plugin>> plugins = getPlugins(target);
        HostFailureListener hostFailureListener = getHostFailureListener(target);
        return new PreBuiltTransportClient(settings, plugins, hostFailureListener);
    }

    private HostFailureListener getHostFailureListener(TransportClient target) {
        return null; //目前拿不到
    }

    private Collection<Class<? extends Plugin>> getPlugins(TransportClient target) {
        return Collections.emptyList(); //目前拿不到
    }
}
