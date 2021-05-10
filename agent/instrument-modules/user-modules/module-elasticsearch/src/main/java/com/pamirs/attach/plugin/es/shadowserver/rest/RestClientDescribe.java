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
package com.pamirs.attach.plugin.es.shadowserver.rest;

import java.lang.reflect.Constructor;

import org.elasticsearch.client.RestClient;

/**
 * @author jirenhe | jirenhe@shulie.io
 * @since 2021/04/26 10:54 上午
 */
public class RestClientDescribe {

    private final RestClient restClient;

    private final Constructor<?> constructor;

    public RestClientDescribe(RestClient restClient) {
        this.restClient = restClient;
        this.constructor = findBaseConstructor();
    }

    private Constructor<?> findBaseConstructor() {
        int argLength = 0;
        Constructor<?> constructor = null;
        for (Constructor<?> constructor1 : RestClient.class.getDeclaredConstructors()) {
            int l = constructor1.getParameterTypes().length;
            if (l > argLength) {
                constructor = constructor1;
                argLength = l;
            }
        }
        return constructor;
    }

    public Constructor<?> getBaseConstructor() {
        return this.constructor;
    }

    public Class<?>[] getBaseConstructorArgs() {
        return this.constructor.getParameterTypes();
    }

    public RestClient getRestClient() {
        return restClient;
    }
}
