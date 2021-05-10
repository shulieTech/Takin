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

import org.elasticsearch.client.NodeSelector;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.WarningsHandler;

/**
 * @author jirenhe | jirenhe@shulie.io
 * @since 2021/04/12 2:48 下午
 */
public class RestClient70Definition extends AbstractRestClientDefinition {

    private static final RestClient70Definition INSTANCE = new RestClient70Definition();

    public static RestClient70Definition getInstance() {return INSTANCE;}

    private RestClient70Definition() {}

    @Override
    protected void doAssembleBuilder(RestClient target, RestClientBuilder builder) {
        NodeSelector nodeSelector = reflectSilence(target, "nodeSelector");
        if (nodeSelector != null) {
            builder.setNodeSelector(nodeSelector);
        }
        WarningsHandler warningsHandler = reflectSilence(target, "warningsHandler");
        if (warningsHandler != null) {
            builder.setStrictDeprecationMode(warningsHandler == WarningsHandler.STRICT);
        }
    }
}
