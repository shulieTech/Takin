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

import com.shulie.instrument.simulator.api.ProcessController;
import com.shulie.instrument.simulator.api.listener.ext.Advice;
import com.shulie.instrument.simulator.api.listener.ext.AdviceListener;
import com.shulie.instrument.simulator.api.scope.InterceptorScope;

/**
 * 修改拦截器，主要进行参数、返回值修改
 *
 * @author xiaobin.zfb
 * @since 2020/7/1 2:04 下午
 */
abstract class ModificationInterceptor extends AdviceListener {
    protected InterceptorScope interceptorScope;

    public void setInterceptorScope(InterceptorScope interceptorScope) {
        this.interceptorScope = interceptorScope;
    }

    @Override
    public final void before(Advice advice) throws Throwable {
        Object[] args = getParameter(advice);
        if (args != null) {
            for (int i = 0; i < Math.min(args.length, advice.getParameterArray().length); i++) {
                advice.changeParameter(i, args[i]);
            }
        }
    }

    @Override
    public final void afterReturning(Advice advice) throws Throwable {
        Object result = getResult(advice);
        if (result != null) {
            ProcessController.returnImmediately(result);
        }
    }

    @Override
    public final void afterThrowing(Advice advice) throws Throwable {
        Object result = getExceptionResult(advice);
        if (result != null) {
            ProcessController.returnImmediately(result);
        }

    }

    /**
     * 参数修改拦截
     */
    public abstract Object[] getParameter(Advice advice) throws Throwable;


    /**
     * 返回值修改拦截
     *
     * @param advice 切点对象
     * @return
     */
    public abstract Object getResult(Advice advice) throws Throwable;

    /**
     * 异常处理
     *
     * @param advice 切点对象
     */
    public abstract Object getExceptionResult(Advice advice) throws Throwable;
}
