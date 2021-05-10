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
package com.pamirs.attach.plugin.okhttp.v2.interceptor;

import com.pamirs.attach.plugin.okhttp.OKHttpConstants;
import com.pamirs.pradar.Pradar;
import com.pamirs.pradar.interceptor.SpanRecord;
import com.pamirs.pradar.interceptor.TraceInterceptorAdaptor;
import com.pamirs.pradar.pressurement.ClusterTestUtils;
import com.shulie.instrument.simulator.api.listener.ext.Advice;
import com.shulie.instrument.simulator.api.reflect.Reflect;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.Request;

import java.net.URL;
import java.util.Map;

/**
 * @Description
 * @Author xiaobin.zfb
 * @mail xiaobin@shulie.io
 * @Date 2020/6/29 8:40 下午
 */
public class RequestBuilderBuildMethodV2Interceptor extends TraceInterceptorAdaptor {
    @Override
    public String getPluginName() {
        return OKHttpConstants.PLUGIN_NAME;
    }

    @Override
    public int getPluginType() {
        return OKHttpConstants.PLUGIN_TYPE;
    }

    private static String getService(String schema, String host, int port, String path) {
        String url = schema + "://" + host;
        if (port != -1 && port != 80) {
            url = url + ':' + port;
        }
        return url + path;
    }

    @Override
    public void beforeFirst(Advice advice) {
        ClusterTestUtils.validateClusterTest();
        if (Pradar.isClusterTest()){
            Object target = advice.getTarget();
            Request.Builder builder = (Request.Builder) target;
            Object httpUrl = Reflect.on(builder).get(OKHttpConstants.DYNAMIC_FIELD_URL);

            //2的低版本
            if (httpUrl instanceof URL){
                String url = null;
                if (httpUrl == null) {
                    url = Reflect.on(builder).get(OKHttpConstants.DYNAMIC_FIELD_URL_STRING);
                } else {
                    url = getService(((URL)httpUrl).getProtocol(), ((URL)httpUrl).getHost(), ((URL)httpUrl).getPort(), ((URL)httpUrl).getPath());
                }
                ClusterTestUtils.validateHttpClusterTest(url);
            } else {//2的高版本
                String url = null;
                if (httpUrl == null) {
                    url = Reflect.on(builder).get(OKHttpConstants.DYNAMIC_FIELD_URL_STRING);
                } else {
                    url = getService(((HttpUrl)httpUrl).scheme(), ((HttpUrl)httpUrl).host(), ((HttpUrl)httpUrl).port(), ((HttpUrl)httpUrl).encodedPath());
                }
                ClusterTestUtils.validateHttpClusterTest(url);
            }
        }
    }

    @Override
    public SpanRecord beforeTrace(Advice advice) {
        Object target = advice.getTarget();
        Request.Builder builder = (Request.Builder) target;
        Map<String, String> contextMap = Pradar.getInvokeContextTransformMap();
        for (Map.Entry<String, String> entry : contextMap.entrySet()) {
            builder.removeHeader(entry.getKey());
            builder.addHeader(entry.getKey(), entry.getValue());
        }
        return null;
    }
}
