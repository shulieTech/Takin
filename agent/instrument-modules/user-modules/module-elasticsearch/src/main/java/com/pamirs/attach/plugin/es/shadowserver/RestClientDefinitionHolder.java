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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.pamirs.attach.plugin.es.shadowserver.rest.definition.RestClientDefinition;

/**
 * @author jirenhe | jirenhe@shulie.io
 * @since 2021/04/09 4:09 下午
 */
public class RestClientDefinitionHolder {

    private static final Map<Object, RestClientDefinition> INSTANCE_REST_CLIENT_DEFINITION_MAP = new ConcurrentHashMap<Object, RestClientDefinition>();

    public static RestClientDefinition get(Object target) {
        return INSTANCE_REST_CLIENT_DEFINITION_MAP.get(target);
    }

    public static RestClientDefinition add(Object target, RestClientDefinition restClientDefinition) {
        return INSTANCE_REST_CLIENT_DEFINITION_MAP.put(target, restClientDefinition);
    }
}
