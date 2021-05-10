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
package com.pamirs.attach.plugin.es.interceptor;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;

import com.pamirs.pradar.exception.PressureMeasureError;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.elasticsearch.client.HttpAsyncResponseConsumerFactory;
import org.elasticsearch.client.ResponseListener;
import org.elasticsearch.client.RestClient;

/**
 * @author jirenhe | jirenhe@shulie.io
 * @since 2021/04/12 11:41 上午
 */
public class RestClientPerformAsyncLowVersionRequestInterceptor extends AbstractRestClientShadowServerInterceptor {

    private static volatile Method performRequestAsyncMethod;

    @Override
    protected Object doCutoff(RestClient restClient, String methodName, Object[] args) throws IOException {
        if (performRequestAsyncMethod == null) {
            synchronized (RestClientPerformAsyncLowVersionRequestInterceptor.class) {
                if (performRequestAsyncMethod == null) {
                    try {
                        performRequestAsyncMethod = restClient.getClass().getMethod("performRequestAsync",
                            String.class, String.class, Map.class, HttpEntity.class,
                            HttpAsyncResponseConsumerFactory.class, ResponseListener.class, Header[].class);
                    } catch (NoSuchMethodException e) {
                        throw new PressureMeasureError("performRequestAsyncMethod not find!", e);
                    }
                }
            }
        }
        try {
            performRequestAsyncMethod.invoke(restClient, args);
        } catch (Exception e) {
            throw new PressureMeasureError("shadow es restClient performRequestAsyncMethod invoke fail!", e);
        }
        return null;
    }

    @Override
    protected boolean doCheck(Object target, String methodName, Object[] args) {
        return args.length == 7
            && args[0] instanceof String
            && args[1] instanceof String
            && args[2] instanceof Map
            && (args[3] == null || args[3] instanceof HttpEntity)
            && args[4] instanceof HttpAsyncResponseConsumerFactory
            && args[5] instanceof ResponseListener
            && args[6].getClass().isArray();
    }
}
