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
import com.pamirs.pradar.ResultCode;
import com.pamirs.pradar.interceptor.SpanRecord;
import com.pamirs.pradar.interceptor.TraceInterceptorAdaptor;
import com.shulie.instrument.simulator.api.listener.ext.Advice;
import com.shulie.instrument.simulator.api.reflect.Reflect;
import com.shulie.instrument.simulator.api.reflect.ReflectException;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import java.io.IOException;

/**
 * @Description
 * @Author xiaobin.zfb
 * @mail xiaobin@shulie.io
 * @Date 2020/6/30 10:20 上午
 */
public class RealCallExecuteV2Interceptor extends TraceInterceptorAdaptor {
    @Override
    public String getPluginName() {
        return OKHttpConstants.PLUGIN_NAME;
    }

    @Override
    public int getPluginType() {
        return OKHttpConstants.PLUGIN_TYPE;
    }

    @Override
    public SpanRecord beforeTrace(Advice advice) {
        Object target = advice.getTarget();
        Request request = null;
        try {
            request = Reflect.on(target).get(OKHttpConstants.DYNAMIC_FIELD_REQUEST);
        } catch (ReflectException e) {
            request = Reflect.on(target).get(OKHttpConstants.DYNAMIC_FIELD_ORIGINAL_REQUEST);
        }
        SpanRecord record = new SpanRecord();
        record.setRemoteIp(request.url().getHost());
        record.setService(request.url().getPath());
        record.setMethod(StringUtils.upperCase(request.method()));
        record.setRemoteIp(request.url().getHost());
        record.setPort(request.url().getPort());
        record.setRequest(request.url().getQuery());

        String header = request.header("content-length");
        if (StringUtils.isNotBlank(header) && NumberUtils.isDigits(header)) {
            try {
                record.setRequestSize(Integer.valueOf(header));
            } catch (NumberFormatException e) {
            }
        }
        return record;
    }

    @Override
    public SpanRecord exceptionTrace(Advice advice) {
        SpanRecord record = new SpanRecord();
        record.setResultCode(ResultCode.INVOKE_RESULT_FAILED);
        record.setResponse(advice.getThrowable());
        record.setResponseSize(0);
        return record;
    }

    @Override
    public SpanRecord afterTrace(Advice advice) {
        SpanRecord record = new SpanRecord();
        Response response = (Response) advice.getReturnObj();
        record.setResultCode(String.valueOf(response.code()));
        long length = 0;
        try {
            length = response.body().contentLength();
        } catch (IOException e) {
            LOGGER.warn("error {}", e);
        }
        record.setResponseSize(length < 0 ? 0 : length);
        return record;
    }
}
