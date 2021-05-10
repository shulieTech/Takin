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
package com.pamirs.attach.plugin.httpclient.interceptor;


import com.pamirs.attach.plugin.httpclient.HttpClientConstants;
import com.pamirs.pradar.Pradar;
import com.pamirs.pradar.ResultCode;
import com.pamirs.pradar.exception.PressureMeasureError;
import com.pamirs.pradar.interceptor.ContextTransfer;
import com.pamirs.pradar.interceptor.SpanRecord;
import com.pamirs.pradar.interceptor.TraceInterceptorAdaptor;
import com.pamirs.pradar.pressurement.ClusterTestUtils;
import com.shulie.instrument.simulator.api.listener.ext.Advice;
import com.shulie.instrument.simulator.api.reflect.Reflect;
import com.shulie.instrument.simulator.api.reflect.ReflectException;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.params.DefaultHttpParams;
import org.apache.commons.httpclient.params.HttpParams;
import org.apache.commons.lang.StringUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xiaobin on 2016/12/15.
 */
public class HttpClientv3MethodInterceptor extends TraceInterceptorAdaptor {
    @Override
    public String getPluginName() {
        return HttpClientConstants.PLUGIN_NAME;
    }

    @Override
    public int getPluginType() {
        return HttpClientConstants.PLUGIN_TYPE;
    }

    private static String getService(String schema, String host, int port, String path) {
        String url = schema + "://" + host;
        if (port != -1 && port != 80) {
            url = url + ':' + port;
        }
        return url + path;
    }

    @Override
    public void beforeLast(Advice advice) {
        Object[] args = advice.getParameterArray();
        try {
            final HttpMethod method = (HttpMethod) args[1];
            if (method == null) {
                return;
            }
            int port = method.getURI().getPort();
            String path = method.getURI().getPath();
            String url = getService(method.getURI().getScheme(), method.getURI().getHost(), port, path);
            ClusterTestUtils.validateHttpClusterTest(url);
        } catch (URIException e) {
            LOGGER.error("", e);
            if (Pradar.isClusterTest()) {
                throw new PressureMeasureError(e);
            }
        } catch (Throwable t) {
            LOGGER.error("", t);
            if (Pradar.isClusterTest()) {
                throw new PressureMeasureError(t);
            }
        }
    }

    private Map toMap(String queryString) {
        if (StringUtils.isBlank(queryString)) {
            return Collections.EMPTY_MAP;
        }
        String[] array = StringUtils.split(queryString, '&');
        if (array == null || array.length == 0) {
            return Collections.EMPTY_MAP;
        }
        Map map = new HashMap();
        for (String str : array) {
            String[] kv = StringUtils.split(str, '=');
            if (kv == null || kv.length != 2) {
                continue;
            }
            if (StringUtils.isBlank(kv[0])) {
                continue;
            }
            map.put(StringUtils.trim(kv[0]), StringUtils.trim(kv[1]));
        }
        return map;
    }

    private void buildParameters(HttpParams httpParams, Map map) {
        if (httpParams == null) {
            return;
        }
        if (!(httpParams instanceof DefaultHttpParams)) {
            return;
        }

        HttpParams defaults = httpParams.getDefaults();
        if (defaults != null) {
            buildParameters(defaults, map);
        }

        HashMap hashMap = null;
        try {
            hashMap = Reflect.on(httpParams).get(HttpClientConstants.DYNAMIC_FIELD_PARAMETERS);
        } catch (ReflectException e) {
            LOGGER.warn("{} has not field {}", httpParams.getClass().getName(), HttpClientConstants.DYNAMIC_FIELD_PARAMETERS);
        }

        if (hashMap != null) {
            map.putAll(hashMap);
        }
    }

    @Override
    protected ContextTransfer getContextTransfer(Advice advice) {
        Object[] args = advice.getParameterArray();
        final HttpMethod method = (HttpMethod) args[1];
        if (method == null) {
            return null;
        }
        return new ContextTransfer() {
            @Override
            public void transfer(String key, String value) {
                method.setRequestHeader(key, value);
            }
        };
    }

    @Override
    public SpanRecord beforeTrace(Advice advice) {
        Object[] args = advice.getParameterArray();
        final HttpMethod method = (HttpMethod) args[1];
        if (method == null) {
            return null;
        }

        try {
            SpanRecord record = new SpanRecord();
            int port = method.getURI().getPort();
            record.setService(method.getURI().getPath());
            record.setMethod(StringUtils.upperCase(method.getName()));
            Header header = method.getRequestHeader("content-length");
            if (header != null && StringUtils.isNotBlank(header.getValue())) {
                try {
                    record.setRequestSize(Integer.valueOf(header.getValue()));
                } catch (NumberFormatException e) {
                }
            }
            if ("get".equalsIgnoreCase(record.getMethod()) || "head".equalsIgnoreCase(record.getMethod())) {
                record.setRequest(toMap(method.getQueryString()));
            } else {
                Map parameters = new HashMap();
                buildParameters(method.getParams(), parameters);
                parameters.putAll(toMap(method.getQueryString()));
                record.setRequest(parameters);
            }
            record.setMiddlewareName(HttpClientConstants.HTTP_CLIENT_NAME_3X);
            record.setRemoteIp(method.getURI().getHost());
            record.setPort(port);
            return record;
        } catch (URIException e) {
            return null;
        }
    }


    @Override
    public SpanRecord afterTrace(Advice advice) {
        Object[] args = advice.getParameterArray();
        HttpMethod method = (HttpMethod) args[1];
        if (method == null) {
            return null;
        }
        try {
            SpanRecord record = new SpanRecord();
            Integer code = (Integer) advice.getReturnObj();
            String msg = method.getURI().toString() + "->" + code;
            record.setResultCode(code + "");
            record.setResponse(msg);
            record.setResponseSize(method.getResponseBody() == null ? 0 : method.getResponseBody().length);
            return record;
        } catch (Throwable e) {
            Pradar.responseSize(0);
        }

        return null;
    }


    @Override
    public SpanRecord exceptionTrace(Advice advice) {
        Object[] args = advice.getParameterArray();
        HttpMethod method = (HttpMethod) args[1];
        if (method == null) {
            return null;
        }
        try {
            SpanRecord record = new SpanRecord();
            record.setResultCode(ResultCode.INVOKE_RESULT_FAILED);
            record.setRequest(method.getParams());
            record.setResponse(advice.getThrowable());
            record.setResponseSize(method.getResponseBody() == null ? 0 : method.getResponseBody().length);
            return record;
        } catch (Throwable e) {
            Pradar.responseSize(0);
        }

        return null;
    }
}
