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

import com.pamirs.pradar.scope.ScopeFactory;
import com.shulie.instrument.simulator.api.listener.AdviceListenerCallback;
import com.shulie.instrument.simulator.api.listener.ext.AdviceListener;
import com.shulie.instrument.simulator.api.scope.ExecutionPolicy;
import com.shulie.instrument.simulator.api.scope.InterceptorScope;

/**
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/10/14 6:22 下午
 */
public class Interceptors {

    public final static ScopeAdviceListenerCallback SCOPE_CALLBACK = new ScopeAdviceListenerCallback();

    public static class ScopeAdviceListenerCallback implements AdviceListenerCallback {

        @Override
        public AdviceListener onCall(AdviceListener listener, String scopeName, ExecutionPolicy policy) {
            if (scopeName == null) {
                return Interceptors.newScopeInterceptor(listener, policy);
            }
            return Interceptors.newScopeInterceptor(listener, scopeName, policy);
        }
    }

    /**
     * 构造带作用域的Interceptor
     *
     * @param adviceListener 监听器
     * @param scopeName      作用域名称
     * @return
     */
    public static AdviceListener newScopeInterceptor(AdviceListener adviceListener, String scopeName) {
        return newScopeInterceptor(adviceListener, scopeName, ExecutionPolicy.ALWAYS);
    }

    /**
     * 构造带作用域的Interceptor,作用域为动态的根据类和方法来进行确定
     *
     * @param adviceListener 监听器
     * @return
     */
    public static AdviceListener newScopeInterceptor(AdviceListener adviceListener) {
        return newScopeInterceptor(adviceListener, ExecutionPolicy.ALWAYS);
    }

    /**
     * 构造带作用域的Interceptor
     *
     * @param adviceListener 监听器
     * @param scope          作用域
     * @return
     */
    public static AdviceListener newScopeInterceptor(AdviceListener adviceListener, InterceptorScope scope) {
        return newScopeInterceptor(adviceListener, scope, ExecutionPolicy.ALWAYS);
    }

    /**
     * 构造带作用域的Interceptor
     *
     * @param adviceListener 监听器
     * @param scopeName      作用域名称
     * @param policy         执行策略
     * @return
     */
    public static AdviceListener newScopeInterceptor(AdviceListener adviceListener, String scopeName, ExecutionPolicy policy) {
        InterceptorScope scope = ScopeFactory.getScope(scopeName);
        return newScopeInterceptor(adviceListener, scope, policy);
    }

    /**
     * 构造带作用域的Interceptor
     *
     * @param adviceListener 监听器
     * @param scope          作用域名称
     * @param policy         执行策略
     * @return
     */
    public static AdviceListener newScopeInterceptor(AdviceListener adviceListener, InterceptorScope scope, ExecutionPolicy policy) {
        return wrapByScope(adviceListener, scope, policy);
    }

    /**
     * 构造带作用域的Interceptor
     *
     * @param adviceListener 监听器
     * @param policy         执行策略
     * @return
     */
    public static AdviceListener newScopeInterceptor(AdviceListener adviceListener, ExecutionPolicy policy) {
        return wrapByScope(adviceListener, policy);
    }

    private static AdviceListener wrapByScope(AdviceListener interceptor, ExecutionPolicy policy) {
        if (interceptor instanceof AroundInterceptor) {
            return new ScopedAroundInterceptor((AroundInterceptor) interceptor, policy);
        }

        if (interceptor instanceof TraceInterceptor) {
            return new ScopedTraceInterceptor((TraceInterceptor) interceptor, policy);
        }

        if (interceptor instanceof ResultInterceptor) {
            interceptor = new ScopedResultInterceptor((ResultInterceptor) interceptor, policy);
        }

        if (interceptor instanceof ParametersWrapperInterceptor) {
            interceptor = new ScopedParametersWrapperInterceptor((ParametersWrapperInterceptor) interceptor, policy);
        }

        if (interceptor instanceof CutoffInterceptor) {
            interceptor = new ScopedCutoffInterceptor((CutoffInterceptor) interceptor, policy);
        }

        if (interceptor instanceof ModificationInterceptor) {
            interceptor = new ScopedModificationInterceptor((ModificationInterceptor) interceptor, policy);
        }


        return interceptor;
    }

    private static AdviceListener wrapByScope(AdviceListener interceptor, InterceptorScope scope, ExecutionPolicy policy) {
        if (interceptor instanceof AroundInterceptor) {
            return new ScopedAroundInterceptor((AroundInterceptor) interceptor, scope, policy);
        }

        if (interceptor instanceof TraceInterceptor) {
            return new ScopedTraceInterceptor((TraceInterceptor) interceptor, scope, policy);
        }

        if (interceptor instanceof ResultInterceptor) {
            interceptor = new ScopedResultInterceptor((ResultInterceptor) interceptor, scope, policy);
        }

        if (interceptor instanceof ParametersWrapperInterceptor) {
            interceptor = new ScopedParametersWrapperInterceptor((ParametersWrapperInterceptor) interceptor, scope, policy);
        }

        if (interceptor instanceof CutoffInterceptor) {
            interceptor = new ScopedCutoffInterceptor((CutoffInterceptor) interceptor, scope, policy);
        }

        if (interceptor instanceof ModificationInterceptor) {
            interceptor = new ScopedModificationInterceptor((ModificationInterceptor) interceptor, scope, policy);
        }


        return interceptor;
    }

}
