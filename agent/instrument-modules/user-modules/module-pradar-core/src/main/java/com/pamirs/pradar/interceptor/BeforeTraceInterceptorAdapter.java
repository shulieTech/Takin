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
 * 方法的 trace 埋点的开始埋点，适用于一次 trace 需要两个方法完成的场景
 * 适用于方法开始进行 trace 追踪，方法结果进行 trace 提交的场景
 * <p>
 * 例如:
 * 下面为某一 rpc 框架一次完整的生命周期调用
 * void send(String value) {
 * System.out.println("xxxxx");
 * }
 * void response(Object response) {
 * xxxx
 * }
 * <p>
 * void failure(Throwable t) {
 * xxxx
 * }
 * 则需要在 send 中开始 trace，并且在 response 和 failure 中结束 trace
 * 则在 send 中的拦截器使用{@link BeforeTraceInterceptorAdapter}
 * 在 response 和 failure 中则使用 {@link AfterTraceInterceptor}
 *
 * <p>
 * 需要注意的是如果在 send 中捕获到异常，则表明这次调用已经完成
 *
 * <p>
 * 需要追踪 test 方法的执行耗时、入参、出参等,其他场景并不适用
 *
 * <pre>
 *     实现的这些方法不支持重载,否则会抛出异常RuntimeException
 * </pre>
 */
public abstract class BeforeTraceInterceptorAdapter extends BeforeTraceInterceptor {

    @Override
    public void beforeFirst(Advice advice) {

    }

    @Override
    public void beforeLast(Advice advice) {

    }

    @Override
    public void exceptionFirst(Advice advice) {

    }

    @Override
    public void exceptionLast(Advice advice) {

    }
}

