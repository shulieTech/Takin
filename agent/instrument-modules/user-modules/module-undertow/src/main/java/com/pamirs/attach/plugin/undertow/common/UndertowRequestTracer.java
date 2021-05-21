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
package com.pamirs.attach.plugin.undertow.common;

import com.pamirs.attach.plugin.common.web.RequestTracer;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;

import java.net.InetAddress;
import java.net.InetSocketAddress;

/**
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2021/5/14 7:42 下午
 */
public class UndertowRequestTracer extends RequestTracer<HttpServerExchange, HttpServerExchange> {
    @Override
    public String getHeader(HttpServerExchange request, String key) {
        return request.getRequestHeaders().getFirst(key);
    }

    @Override
    public String getRemoteAddr(HttpServerExchange request) {
        InetSocketAddress sourceAddress = request.getSourceAddress();
        if (sourceAddress == null) {
            return "";
        }
        InetAddress address = sourceAddress.getAddress();
        if (address == null) {
            //this is unresolved, so we just return the host name
            //not exactly spec, but if the name should be resolved then a PeerNameResolvingHandler should be used
            //and this is probably better than just returning null
            try {
                return sourceAddress.getHostString();
            } catch (Throwable e) {
            }
            InetAddress inetAddress = sourceAddress.getAddress();
            if (inetAddress != null) {
                return inetAddress.getHostAddress();
            }
            return sourceAddress.toString();
        }
        return address.getHostAddress();
    }

    @Override
    public String getRemotePort(HttpServerExchange request) {
        return String.valueOf(request.getSourceAddress().getPort());
    }

    @Override
    public String getRequestURI(HttpServerExchange request) {
        if (request.isHostIncludedInRequestURI()) {
            //we need to strip out the host part
            String uri = request.getRequestURI();
            int slashes = 0;
            for (int i = 0; i < uri.length(); ++i) {
                if (uri.charAt(i) == '/') {
                    if (++slashes == 3) {
                        return uri.substring(i);
                    }
                }
            }
            return "/";
        } else {
            return request.getRequestURI();
        }
    }

    @Override
    public String getMethod(HttpServerExchange request) {
        return request.getRequestMethod().toString();
    }

    @Override
    public void setAttribute(HttpServerExchange request, String key, Object value) {

    }

    @Override
    public Object getAttribute(HttpServerExchange request, String key) {
        return null;
    }

    @Override
    public long getContentLength(HttpServerExchange request) {
        long length = getContentLengthLong(request);
        if (length > Integer.MAX_VALUE) {
            return -1;
        }
        return (int) length;
    }

    public long getContentLengthLong(HttpServerExchange request) {
        final String contentLength = getHeader(request, "Content-Length");
        if (contentLength == null || contentLength.isEmpty()) {
            return -1;
        }
        return Long.parseLong(contentLength);
    }

    @Override
    public String getParams(HttpServerExchange request) {
        return null;
    }

    @Override
    public String getResponse(HttpServerExchange response) {
        return null;
    }

    @Override
    public String getStatusCode(HttpServerExchange response, Throwable throwable) {
        int code = response.getResponseCode();
        if (throwable != null && code % 500 >= 100) {
            code = 500;
        }
        return String.valueOf(code);
    }

    @Override
    public void setResponseHeader(HttpServerExchange httpServerExchange, String key, Object value) {
        httpServerExchange.getResponseHeaders().put(new HttpString(key), value == null ? "" : value.toString());
    }
}
