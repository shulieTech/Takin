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
package com.pamirs.attach.plugin.guava.interceptor;

import com.pamirs.attach.plugin.guava.GoogleGuavaConstants;
import com.pamirs.pradar.MiddlewareType;
import com.pamirs.pradar.ResultCode;
import com.pamirs.pradar.interceptor.SpanRecord;
import com.pamirs.pradar.interceptor.TraceInterceptorAdaptor;
import com.shulie.instrument.simulator.api.listener.ext.Advice;

/**
 * @author wangjian
 * @since 2021/3/8 14:58
 */
public class CacheOperationTraceInterceptor extends TraceInterceptorAdaptor {
    @Override
    public String getPluginName() {
        return GoogleGuavaConstants.PLUGIN_NAME;
    }

    @Override
    public int getPluginType() {
        return MiddlewareType.TYPE_CACHE;
    }

    @Override
    public SpanRecord beforeTrace(Advice advice) {
        Object[] args = advice.getParameterArray();
        String methodName = advice.getBehavior().getName();
        Object target = advice.getTarget();
        SpanRecord record = new SpanRecord();
        record.setService(target.getClass().getName());
        record.setMethod(methodName);
        record.setRequest(args);
        return record;
    }

    @Override
    public SpanRecord afterTrace(Advice advice) {
        Object[] args = advice.getParameterArray();
        String methodName = advice.getBehavior().getName();
        Object target = advice.getTarget();
        Object result = advice.getReturnObj();
        SpanRecord record = new SpanRecord();
        record.setService(target.getClass().getName());
        record.setMethod(methodName);
        record.setRequest(args);
        record.setResponse(result);
        record.setResultCode(ResultCode.INVOKE_RESULT_SUCCESS);
        return record;
    }

    @Override
    public SpanRecord exceptionTrace(Advice advice) {
        Object[] args = advice.getParameterArray();
        String methodName = advice.getBehavior().getName();
        Object target = advice.getTarget();
        SpanRecord record = new SpanRecord();
        record.setService(target.getClass().getName());
        record.setMethod(methodName);
        record.setRequest(args);
        record.setResponse(advice.getThrowable());
        record.setResultCode(ResultCode.INVOKE_RESULT_FAILED);
        return record;
    }
}
