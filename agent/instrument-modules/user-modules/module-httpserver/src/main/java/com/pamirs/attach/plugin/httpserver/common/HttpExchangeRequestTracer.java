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
package com.pamirs.attach.plugin.httpserver.common;

import com.pamirs.attach.plugin.common.web.RequestTracer;
import com.sun.net.httpserver.HttpExchange;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2021/3/30 3:05 下午
 */
public class HttpExchangeRequestTracer extends RequestTracer<HttpExchange, HttpExchange> {
    private static final String AND_DELIMITER = "&";
    private static final String EQUAL_DELIMITER = "=";
    private static final String CHARSET = "utf-8";

    @Override
    public String getHeader(HttpExchange request, String key) {
        return request.getRequestHeaders().getFirst(key);
    }

    @Override
    public String getRemoteAddr(HttpExchange request) {
        if (request.getRemoteAddress() == null || request.getRemoteAddress().getAddress() == null) {
            return request.getRequestURI() == null ? null : request.getRequestURI().getHost();
        }
        return request.getRemoteAddress().getAddress().getHostAddress();
    }

    @Override
    public String getRemotePort(HttpExchange request) {
        if (request.getRemoteAddress() == null) {
            return request.getRequestURI() == null ? null : String.valueOf(request.getRequestURI().getPort());
        }
        return String.valueOf(request.getRemoteAddress().getPort());
    }

    @Override
    public String getRequestURI(HttpExchange request) {
        return request.getRequestURI().toString();
    }

    @Override
    public String getMethod(HttpExchange request) {
        return request.getRequestMethod();
    }

    @Override
    public void setAttribute(HttpExchange request, String key, Object value) {
        request.setAttribute(key, value);
    }

    @Override
    public Object getAttribute(HttpExchange request, String key) {
        return request.getAttribute(key);
    }

    @Override
    public long getContentLength(HttpExchange request) {
        String value = request.getRequestHeaders().getFirst("Content-Length");
        if (NumberUtils.isDigits(value)) {
            return Long.valueOf(value);
        }
        return 0;
    }

    @Override
    public String getParams(HttpExchange request) {
        StringBuilder stringBuilder = new StringBuilder("{");
        String query = request.getRequestURI().getQuery();
        if (StringUtils.isNotBlank(query)) {
            String[] queryParams = query.split(AND_DELIMITER);
            if (ArrayUtils.isNotEmpty(queryParams)) {
                for (String qParam : queryParams) {
                    if (qParam.indexOf(EQUAL_DELIMITER) == -1) {
                        continue;
                    }
                    String[] param = qParam.split(EQUAL_DELIMITER);
                    try {
                        String key = URLDecoder.decode(param[0], CHARSET);
                        String value = param.length > 1 ? URLDecoder.decode(param[1], CHARSET) : "";
                        stringBuilder.append(key).append(':').append(value);
                    } catch (UnsupportedEncodingException e) {
                        String key = param[0];
                        String value = param.length > 1 ? param[1] : "";
                        stringBuilder.append(key).append(':').append(value);
                    }
                }
            }
        }
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    @Override
    public String getResponse(HttpExchange response) {
        return null;
    }

    @Override
    public String getStatusCode(HttpExchange response, Throwable throwable) {
        return String.valueOf(response.getResponseCode());
    }

    @Override
    public void setResponseHeader(HttpExchange httpExchange, String key, Object value) {
        httpExchange.getResponseHeaders().set(key, value == null ? "" : value.toString());
    }
}
