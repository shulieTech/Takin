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

import com.pamirs.pradar.internal.config.ShadowEsServerConfig;
import org.elasticsearch.client.transport.TransportClient;

/**
 * @author jirenhe | jirenhe@shulie.io
 * @since 2021/04/09 4:12 下午
 */
public interface TransportClientDefinition {

    TransportClient solve(TransportClient target, ShadowEsServerConfig shadowEsServerConfig);
}
