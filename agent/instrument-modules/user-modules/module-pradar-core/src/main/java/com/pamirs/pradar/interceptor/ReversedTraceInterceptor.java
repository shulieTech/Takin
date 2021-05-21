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
package com.pamirs.pradar.interceptor;

import com.shulie.instrument.simulator.api.listener.ext.Advice;

import static com.pamirs.pradar.interceptor.TraceInterceptorAdaptor.BEFORE_TRACE_SUCCESS;

/**
 * 方向方法埋点的环绕拦截器，与 ReversedTraceInterceptor 逻辑相反
 * <pre>
 *     实现的这些方法不支持重载,否则会抛出异常RuntimeException
 * </pre>
 */
abstract class ReversedTraceInterceptor extends TraceInterceptor {

    @Override
    public void doBefore(Advice advice) throws Throwable {
        advice.mark(BEFORE_TRACE_SUCCESS);
        super.doAfter(advice);
    }

    @Override
    public void doAfter(Advice advice) throws Throwable {
        super.doBefore(advice);
    }
}

