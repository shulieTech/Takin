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

import com.pamirs.pradar.CutOffResult;
import com.pamirs.pradar.scope.ScopeFactory;
import com.shulie.instrument.simulator.api.listener.ext.Advice;
import com.shulie.instrument.simulator.api.scope.ExecutionPolicy;
import com.shulie.instrument.simulator.api.scope.InterceptorScope;
import com.shulie.instrument.simulator.api.scope.InterceptorScopeInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 挡板的拦截器实现, 这里需要注意的是不能使用其他的拦截器实现挡板拦截器，因为档板的返回事件触发是在
 * BEFORE事件中的，框架在实现返回事件时会中断后续的事件触发，如果作用域需要依赖 BEFORE、RETURN/THROWS
 * 事件组合才实现作用域，则 RETURN/THROWS 事件后续不会再被触发，则作用域会产生问题
 *
 * Created by xiaobin on 2017/1/19.
 */
public class ScopedCutoffInterceptor extends CutoffInterceptor {
    private final Logger logger = LoggerFactory.getLogger(ScopedCutoffInterceptor.class.getName());

    private final CutoffInterceptor interceptor;
    private final InterceptorScope scope;
    private final ExecutionPolicy policy;

    public ScopedCutoffInterceptor(CutoffInterceptor interceptor, ExecutionPolicy policy) {
        this(interceptor, null, policy);
    }

    public ScopedCutoffInterceptor(CutoffInterceptor interceptor, InterceptorScope scope, ExecutionPolicy policy) {
        if (interceptor == null) {
            throw new NullPointerException("interceptor must not be null");
        }
        if (policy == null) {
            throw new NullPointerException("policy must not be null");
        }
        this.interceptor = interceptor;
        this.scope = scope;
        super.setInterceptorScope(scope);
        this.policy = policy;
    }

    private InterceptorScope getScope(Advice advice) {
        if (scope != null) {
            return scope;
        }
        return ScopeFactory.getScope(interceptor.getClass().getName() + "#" +advice.getTargetClass().getName() + "_" + advice.getBehavior().getName());
    }

    @Override
    public CutOffResult cutoff(Advice advice) {
        final InterceptorScopeInvocation transaction = getScope(advice).getCurrentInvocation();

        CutOffResult result = CutOffResult.passed();
        try {
            final boolean success = transaction.tryEnter(policy);
            if (success) {
                try {
                    result = interceptor.cutoff(advice);
                } catch (Throwable t) {
                    InterceptorInvokerHelper.handleException(t);
                }
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug("tryBefore() returns false: interceptorScopeTransaction: {}, executionPoint: {}. Skip interceptor {}", transaction, policy, interceptor.getClass());
                }
            }
        } finally {
            if (transaction.canLeave(policy)) {
                transaction.leave(policy);
            }
        }
        return result;
    }

}

