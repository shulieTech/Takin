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
import com.pamirs.pradar.ResultCode;
import com.pamirs.pradar.common.HeaderMark;
import com.pamirs.pradar.interceptor.ContextTransfer;
import com.pamirs.pradar.interceptor.SpanRecord;
import com.pamirs.pradar.interceptor.TraceInterceptorAdaptor;
import com.pamirs.pradar.pressurement.ClusterTestUtils;
import com.shulie.instrument.simulator.api.listener.ext.Advice;
import org.apache.commons.lang.StringUtils;
import org.apache.http.*;
import org.apache.http.client.methods.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xiaobin on 2016/12/15.
 */
public class HttpClientv4MethodInterceptor extends TraceInterceptorAdaptor {
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
        HttpHost httpHost = (HttpHost) args[0];
        final HttpRequest request = (HttpRequest) args[1];
        if (httpHost == null) {
            return;
        }
        String host = httpHost.getHostName();
        int port = httpHost.getPort();
        String path = httpHost.getHostName();
        if (request instanceof HttpUriRequest) {
            path = ((HttpUriRequest) request).getURI().getPath();
        }

        //判断是否在白名单中
        String url = getService(httpHost.getSchemeName(), host, port, path);
        ClusterTestUtils.validateHttpClusterTest(url);
    }

    private Map toMap(String queryString) {
        Map map = new HashMap();
        if (StringUtils.isBlank(queryString)) {
            return map;
        }
        String[] array = StringUtils.split(queryString, '&');
        if (array == null || array.length == 0) {
            return map;
        }

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

    private String toString(InputStream in) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int length = 0;
        try {
            while ((length = in.read(data)) != -1) {
                os.write(data, 0, length);
            }
        } catch (IOException e) {
        }
        return new String(os.toByteArray());
    }

    private Map getParameters(HttpRequest httpRequest) {
        if (httpRequest instanceof HttpGet) {
            HttpGet httpGet = (HttpGet) httpRequest;
            return toMap(httpGet.getURI().getQuery());
        }
        if (httpRequest instanceof HttpPost) {
            HttpPost httpPost = (HttpPost) httpRequest;
            HttpEntity httpEntity = httpPost.getEntity();
            Map parameters = toMap(httpPost.getURI().getQuery());
            InputStream in = null;
            try {
                in = httpEntity.getContent();
                parameters.putAll(toMap(toString(in)));
            } catch (Throwable t) {
            } finally {
                if (in != null) {
                    try {
                        in.reset();
                    } catch (IOException e) {
                    }
                }
            }
            return parameters;
        }

        if (httpRequest instanceof HttpPut) {
            HttpPut httpPut = (HttpPut) httpRequest;
            HttpEntity httpEntity = httpPut.getEntity();
            Map parameters = toMap(httpPut.getURI().getQuery());
            InputStream in = null;
            try {
                in = httpEntity.getContent();
                parameters.putAll(toMap(toString(in)));
            } catch (Throwable t) {
            } finally {
                if (in != null) {
                    try {
                        in.reset();
                    } catch (IOException e) {
                    }
                }
            }
            return parameters;
        }

        if (httpRequest instanceof HttpDelete) {
            HttpDelete httpDelete = (HttpDelete) httpRequest;
            return toMap(httpDelete.getURI().getQuery());
        }

        if (httpRequest instanceof HttpHead) {
            HttpHead httpHead = (HttpHead) httpRequest;
            return toMap(httpHead.getURI().getQuery());
        }

        if (httpRequest instanceof HttpOptions) {
            HttpOptions httpOptions = (HttpOptions) httpRequest;
            return toMap(httpOptions.getURI().getQuery());
        }

        if (httpRequest instanceof HttpTrace) {
            HttpTrace httpTrace = (HttpTrace) httpRequest;
            return toMap(httpTrace.getURI().getQuery());
        }

        if (httpRequest instanceof HttpPatch) {
            HttpPatch httpPatch = (HttpPatch) httpRequest;
            HttpEntity httpEntity = httpPatch.getEntity();
            Map parameters = toMap(httpPatch.getURI().getQuery());
            InputStream in = null;
            try {
                in = httpEntity.getContent();
                parameters.putAll(toMap(toString(in)));
            } catch (Throwable t) {
            } finally {
                if (in != null) {
                    try {
                        in.reset();
                    } catch (IOException e) {
                    }
                }
            }
            return parameters;
        }
        return Collections.EMPTY_MAP;
    }

    @Override
    protected ContextTransfer getContextTransfer(Advice advice) {
        Object[] args = advice.getParameterArray();
        HttpHost httpHost = (HttpHost) args[0];
        final HttpRequest request = (HttpRequest) args[1];
        if (httpHost == null) {
            return null;
        }
        return new ContextTransfer() {
            @Override
            public void transfer(String key, String value) {
                if (request.getHeaders(HeaderMark.DONT_MODIFY_HEADER) == null ||
                        request.getHeaders(HeaderMark.DONT_MODIFY_HEADER).length == 0) {
                    request.setHeader(key, value);
                }
            }
        };
    }

    @Override
    public SpanRecord beforeTrace(Advice advice) {
        Object[] args = advice.getParameterArray();
        HttpHost httpHost = (HttpHost) args[0];
        final HttpRequest request = (HttpRequest) args[1];
        if (httpHost == null) {
            return null;
        }

        String host = httpHost.getHostName();
        int port = httpHost.getPort();
        String path = httpHost.getHostName();
        if (request instanceof HttpUriRequest) {
            path = ((HttpUriRequest) request).getURI().getPath();
        }
        SpanRecord record = new SpanRecord();
        record.setService(path);
        String reqStr = request.toString();
        String httpType = StringUtils.upperCase(reqStr.substring(0, reqStr.indexOf(" ")));
        record.setMethod(httpType);
        record.setRemoteIp(host);
        record.setPort(port);
        record.setMiddlewareName(HttpClientConstants.HTTP_CLIENT_NAME_4X);
        Header[] headers = request.getHeaders("content-length");
        if (headers != null && headers.length != 0) {
            try {
                Header header = headers[0];
                record.setRequestSize(Integer.valueOf(header.getValue()));
            } catch (NumberFormatException e) {
            }
        }
        record.setRemoteIp(httpHost.getHostName());
        return record;

    }

    @Override
    public SpanRecord afterTrace(Advice advice) {
        Object[] args = advice.getParameterArray();
        HttpRequest request = (HttpRequest) args[1];
        SpanRecord record = new SpanRecord();
        if (advice.getReturnObj() instanceof HttpResponse) {
            HttpResponse response = (HttpResponse) advice.getReturnObj();
            try {
                record.setResponseSize(response == null ? 0 : response.getEntity().getContentLength());
            } catch (Throwable e) {
                record.setResponseSize(0);
            }
            int code = response.getStatusLine().getStatusCode();
            record.setResultCode(code + "");
        }

        try {
            record.setRequest(getParameters(request));
        } catch (Throwable e) {
        }
        return record;

    }


    @Override
    public SpanRecord exceptionTrace(Advice advice) {
        Object[] args = advice.getParameterArray();
        HttpRequest request = (HttpRequest) args[1];
        SpanRecord record = new SpanRecord();
        record.setResultCode(ResultCode.INVOKE_RESULT_FAILED);
        try {
            record.setRequest(getParameters(request));
        } catch (Throwable e) {
        }
        record.setResponse(advice.getThrowable());
        return record;
    }
}
