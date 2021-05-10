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
package com.pamirs.attach.plugin.okhttp.v3.interceptor;

import com.pamirs.attach.plugin.okhttp.OKHttpConstants;
import com.pamirs.pradar.MiddlewareType;
import com.pamirs.pradar.Pradar;
import com.pamirs.pradar.interceptor.SpanRecord;
import com.pamirs.pradar.interceptor.TraceInterceptorAdaptor;
import com.pamirs.pradar.pressurement.ClusterTestUtils;
import com.shulie.instrument.simulator.api.listener.ext.Advice;
import com.shulie.instrument.simulator.api.reflect.Reflect;
import okhttp3.HttpUrl;
import okhttp3.Request;

import java.util.Map;

/**
 * @Description
 * @Author xiaobin.zfb
 * @mail xiaobin@shulie.io
 * @Date 2020/6/29 8:40 下午
 */
public class RequestBuilderBuildMethodV3Interceptor extends TraceInterceptorAdaptor {
    @Override
    public String getPluginName() {
        return OKHttpConstants.PLUGIN_NAME;
    }

    @Override
    public int getPluginType() {
        return MiddlewareType.TYPE_RPC;
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
        Object target = advice.getTarget();
        Request.Builder builder = (Request.Builder) target;
        HttpUrl httpUrl = Reflect.on(builder).get(OKHttpConstants.DYNAMIC_FIELD_URL);
        String url = getService(httpUrl.scheme(), httpUrl.host(), httpUrl.port(), httpUrl.encodedPath());
        ClusterTestUtils.validateHttpClusterTest(url);
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
