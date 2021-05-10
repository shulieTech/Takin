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

import org.elasticsearch.client.Request;
import org.elasticsearch.client.RestClient;

/**
 * @author jirenhe | jirenhe@shulie.io
 * @since 2021/04/12 11:41 上午
 */
public class RestClientPerformRequestInterceptor extends AbstractRestClientShadowServerInterceptor {

    @Override
    protected Object doCutoff(RestClient restClient, String methodName, Object[] args) throws IOException {
        return restClient.performRequest((Request)args[0]);
    }

    @Override
    protected boolean doCheck(Object target, String methodName, Object[] args) {
        return args.length == 1 && args[0] instanceof Request;
    }
}
