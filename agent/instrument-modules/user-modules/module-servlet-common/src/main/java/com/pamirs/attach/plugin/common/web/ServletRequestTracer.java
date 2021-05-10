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
package com.pamirs.attach.plugin.common.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.Map;


/**
 * http servlet 的 RequestTrace 实现
 *
 * @author xiaobin@shulie.io
 * @since 2021-01-12
 */
public class ServletRequestTracer extends RequestTracer<HttpServletRequest, HttpServletResponse> {

    @Override
    public String getHeader(HttpServletRequest request, String key) {
        return request.getHeader(key);
    }

    @Override
    public String getRemoteAddr(HttpServletRequest request) {
        return request.getRemoteAddr();
    }

    @Override
    public String getRemotePort(HttpServletRequest request) {
        return String.valueOf(request.getRemotePort());
    }

    @Override
    public String getRequestURI(HttpServletRequest request) {
        return request.getRequestURI();
    }

    @Override
    public String getMethod(HttpServletRequest request) {
        return request.getMethod();
    }

    @Override
    public void setAttribute(HttpServletRequest request, String key, Object value) {
        request.setAttribute(key, value);
    }

    @Override
    public Object getAttribute(HttpServletRequest request, String key) {
        return request.getAttribute(key);
    }

    @Override
    public long getContentLength(HttpServletRequest request) {
        return request.getContentLength();
    }

    @Override
    public String getParams(HttpServletRequest request) {
        Map parameterMap = request.getParameterMap();
        StringBuilder stringBuilder = new StringBuilder("{");
        if (null != parameterMap) {
            int index = 0;
            for (Object key : parameterMap.keySet()) {
                String[] values = (String[]) parameterMap.get(key);
                stringBuilder.append(key);
                stringBuilder.append(":");
                for (int i = 0; i < values.length; i++) {
                    stringBuilder.append(values[i]);
                    if (i < (values.length - 1)) {
                        stringBuilder.append("-");
                    }
                }
                if (index++ < (parameterMap.keySet().size() - 1)) {
                    stringBuilder.append(",");
                }
            }
        } else {
            Enumeration parameterNames = request.getParameterNames();
            while (parameterNames.hasMoreElements()) {
                Object key = parameterNames.nextElement();
                String value = request.getParameter(String.valueOf(key));
                stringBuilder.append(key);
                stringBuilder.append(":");
                stringBuilder.append(value);
            }
        }
        // POST 方法将body留下来
        if (request instanceof IBufferedServletRequestWrapper) {
            final byte[] body = ((IBufferedServletRequestWrapper) request).getBody();
            if (body != null) {
                String bodyParam = new String(body);
                bodyParam = bodyParam.replace("\n", "");
                stringBuilder.append(bodyParam);
            }
        }
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    @Override
    public String getResponse(HttpServletResponse response) {
        return null;
    }

    @Override
    public String getStatusCode(HttpServletResponse response, Throwable throwable) {
        if (throwable != null) {
            return "500";
        }
        if (response == null) {
            return "200";
        }
        try {
            return String.valueOf(response.getStatus());
        } catch (NoSuchMethodError e) {
            return "200";
        } catch (Throwable t) {
            return "200";
        }
    }

    @Override
    public void setResponseHeader(HttpServletResponse httpServletResponse, String key, Object value) {
        if (httpServletResponse != null) {
            httpServletResponse.setHeader(key, String.valueOf(value));
        }
    }

}
