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
package com.pamirs.attach.plugin.httpserver.interceptor;

import com.pamirs.attach.plugin.common.web.RequestTracer;
import com.pamirs.attach.plugin.httpserver.HttpServerConstants;
import com.pamirs.attach.plugin.httpserver.common.HttpExchangeRequestTracer;
import com.pamirs.pradar.Pradar;
import com.pamirs.pradar.interceptor.AroundInterceptor;
import com.pamirs.pradar.interceptor.TraceInterceptorAdaptor;
import com.shulie.instrument.simulator.api.listener.ext.Advice;
import com.sun.net.httpserver.HttpExchange;

/**
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/11/13 3:03 下午
 */
public class FilterChainDoFilterInterceptor extends AroundInterceptor {

    private final RequestTracer<HttpExchange, HttpExchange> requestTracer;

    public FilterChainDoFilterInterceptor() {
        this.requestTracer = new HttpExchangeRequestTracer();
    }

    @Override
    public void doBefore(Advice advice) throws Throwable {
        HttpExchange httpExchange = (HttpExchange) advice.getParameterArray()[0];
        try {
            requestTracer.startTrace(httpExchange, httpExchange, HttpServerConstants.PLUGIN_NAME);
        } finally {
            advice.mark(TraceInterceptorAdaptor.BEFORE_TRACE_SUCCESS);
        }
    }

    @Override
    public void doAfter(Advice advice) throws Throwable {
        try {
            HttpExchange httpExchange = (HttpExchange) advice.getParameterArray()[0];
            if (!advice.hasMark(TraceInterceptorAdaptor.BEFORE_TRACE_SUCCESS)) {
                return;
            }
            requestTracer.endTrace(httpExchange, httpExchange, null);
        } finally {
            advice.unMark(TraceInterceptorAdaptor.BEFORE_TRACE_SUCCESS);
            Pradar.clearInvokeContext();
        }
    }

    @Override
    public void doException(Advice advice) throws Throwable {
        try {
            HttpExchange httpExchange = (HttpExchange) advice.getParameterArray()[0];
            if (!advice.hasMark(TraceInterceptorAdaptor.BEFORE_TRACE_SUCCESS)) {
                return;
            }
            requestTracer.endTrace(httpExchange, httpExchange, advice.getThrowable());
        } finally {
            advice.unMark(TraceInterceptorAdaptor.BEFORE_TRACE_SUCCESS);
            Pradar.clearInvokeContext();
        }
    }
}
