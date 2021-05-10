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
import com.pamirs.pradar.interceptor.SpanRecord;
import com.pamirs.pradar.interceptor.TraceInterceptorAdaptor;
import com.shulie.instrument.simulator.api.listener.ext.Advice;
import okhttp3.Call;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

/**
 * @Description
 * @Author xiaobin.zfb
 * @mail xiaobin@shulie.io
 * @Date 2020/6/30 10:20 上午
 */
public class RealCallEnqueueV3Interceptor extends TraceInterceptorAdaptor {
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
        Call call = (Call) target;

        SpanRecord record = new SpanRecord();
        record.setRemoteIp(call.request().url().host());
        record.setService(call.request().url().encodedPath());
        record.setMethod(StringUtils.upperCase(call.request().method()));
        record.setRemoteIp(call.request().url().host());
        record.setPort(call.request().url().port());
        record.setRequest(call.request().url().encodedQuery());

        String header = call.request().header("content-length");
        if (StringUtils.isNotBlank(header) && NumberUtils.isDigits(header)) {
            try {
                record.setRequestSize(Integer.valueOf(header));
            } catch (NumberFormatException e) {
            }
        }
        return record;
    }
}
