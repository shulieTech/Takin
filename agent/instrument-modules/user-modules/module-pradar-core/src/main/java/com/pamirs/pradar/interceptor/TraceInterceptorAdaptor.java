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

/**
 * 实例方法埋点的环绕拦截器抽象实现,可实现追踪埋点与压测增强的混合逻辑
 * <pre>
 *     实现的这些方法不支持重载,否则会抛出异常RuntimeException
 * </pre>
 * Created by xiaobin on 2017/2/6.
 */
public abstract class TraceInterceptorAdaptor extends TraceInterceptor {
    public final static String BEFORE_TRACE_SUCCESS = "before-trace-success";

    @Override
    public void beforeFirst(Advice advice) {

    }

    @Override
    public void beforeLast(Advice advice) {

    }

    @Override
    public SpanRecord beforeTrace(Advice advice) {
        return null;
    }

    @Override
    public void afterFirst(Advice advice) {

    }

    @Override
    public void afterLast(Advice advice) {

    }

    @Override
    public SpanRecord afterTrace(Advice advice) {
        return null;
    }

    @Override
    public void exceptionFirst(Advice advice) {

    }

    @Override
    public void exceptionLast(Advice advice) {

    }

    @Override
    public SpanRecord exceptionTrace(Advice advice) {
        return null;
    }
}
