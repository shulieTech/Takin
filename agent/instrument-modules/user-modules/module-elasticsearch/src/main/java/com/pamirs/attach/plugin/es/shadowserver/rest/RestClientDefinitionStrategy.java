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

import java.util.List;

import com.pamirs.attach.plugin.es.shadowserver.rest.definition.RestClient61Definition;
import com.pamirs.attach.plugin.es.shadowserver.rest.definition.RestClient68Definition;
import com.pamirs.attach.plugin.es.shadowserver.rest.definition.RestClient70Definition;
import com.pamirs.attach.plugin.es.shadowserver.rest.definition.RestClientDefinition;
import com.pamirs.pradar.exception.PressureMeasureError;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClient.FailureListener;

/**
 * @author jirenhe | jirenhe@shulie.io
 * @since 2021/04/12 4:27 下午
 */
public enum RestClientDefinitionStrategy {

    LOW_VERSION {
        @Override
        public RestClientDefinition definition() {
            return RestClient61Definition.getInstance();
        }

        @Override
        public boolean support(RestClientDescribe describe) {
            Class<?>[] args = describe.getBaseConstructorArgs();
            return args.length == 6
                && args[0] == CloseableHttpAsyncClient.class
                && args[1] == long.class
                && args[2].isArray()
                && args[3].isArray()
                && args[4] == String.class
                && args[5] == FailureListener.class;
        }
    },

    VERSION6_8 {
        @Override
        public RestClientDefinition definition() {
            return RestClient68Definition.getInstance();
        }

        @Override
        public boolean support(RestClientDescribe describe) {
            Class<?>[] args = describe.getBaseConstructorArgs();
            return args.length == 8
                && args[0] == CloseableHttpAsyncClient.class
                && args[1] == long.class
                && args[2].isArray()
                && args[3] == List.class
                && args[4] == String.class
                && args[5] == FailureListener.class
                && args[6].getName().equals("org.elasticsearch.client.NodeSelector")
                && args[7] == boolean.class;
        }
    },

    VERSION7X {
        @Override
        public RestClientDefinition definition() {
            return RestClient70Definition.getInstance();
        }

        @Override
        public boolean support(RestClientDescribe describe) {
            Class<?>[] args = describe.getBaseConstructorArgs();
            return args.length == 7
                && args[0] == CloseableHttpAsyncClient.class
                && args[1].isArray()
                && args[2] == List.class
                && args[3] == String.class
                && args[4] == FailureListener.class
                && args[5].getName().equals("org.elasticsearch.client.NodeSelector")
                && args[6] == boolean.class;
        }
    };

    public static RestClientDefinition match(RestClient restClient) {
        RestClientDescribe restClientDescribe = new RestClientDescribe(restClient);
        for (RestClientDefinitionStrategy value : RestClientDefinitionStrategy.values()) {
            if (value.support(restClientDescribe)) {
                return value.definition();
            }
        }
        throw new PressureMeasureError("未支持的RestClient版本！:" + restClient.getClass().getName());
    }

    public abstract boolean support(RestClientDescribe describe);

    public abstract RestClientDefinition definition();

}
