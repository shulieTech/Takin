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
 * 拦截器
 * <pre>
 *     注意:实现的这些方法不支持重载,否则会抛出异常RuntimeException
 * </pre>
 *
 * @author fabing.zhaofb
 */
public abstract class AroundInterceptor extends BaseInterceptor {

    @Override
    public final void before(Advice advice) throws Throwable {
        super.before(advice);
    }

    @Override
    public final void afterReturning(Advice advice) throws Throwable {
        super.afterReturning(advice);
    }

    @Override
    public final void afterThrowing(Advice advice) throws Throwable {
        super.afterThrowing(advice);
    }
}

