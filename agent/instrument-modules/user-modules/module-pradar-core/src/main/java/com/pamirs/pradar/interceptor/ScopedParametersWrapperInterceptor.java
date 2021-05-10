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
import com.shulie.instrument.simulator.api.listener.ext.Advice;
import com.shulie.instrument.simulator.api.scope.ExecutionPolicy;
import com.shulie.instrument.simulator.api.scope.InterceptorScope;
import com.shulie.instrument.simulator.api.scope.InterceptorScopeInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author vincent
 * @version v0.1 2017/3/2 23:00
 */
public class ScopedParametersWrapperInterceptor extends ParametersWrapperInterceptor {
    private final Logger logger = LoggerFactory.getLogger(ScopedParametersWrapperInterceptor.class.getName());

    private final ParametersWrapperInterceptor interceptor;
    private final InterceptorScope scope;
    private final ExecutionPolicy policy;

    public ScopedParametersWrapperInterceptor(ParametersWrapperInterceptor interceptor, ExecutionPolicy policy) {
        this(interceptor, null, policy);
    }

    public ScopedParametersWrapperInterceptor(ParametersWrapperInterceptor interceptor, InterceptorScope scope, ExecutionPolicy policy) {
        if (interceptor == null) {
            throw new NullPointerException("interceptor must not be null");
        }
        if (scope == null) {
            throw new NullPointerException("scope must not be null");
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
    public Object[] getParameter(Advice advice) {
        final InterceptorScopeInvocation transaction = getScope(advice).getCurrentInvocation();
        Object[] result = advice.getParameterArray();

        try {
            final boolean success = transaction.tryEnter(policy);
            if (success) {
                try {
                    result = interceptor.getParameter(advice);
                } catch (Throwable t) {
                    InterceptorInvokerHelper.handleException(t);
                }
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug("tryAfter() returns false: interceptorScopeTransaction: {}, executionPoint: {}. Skip interceptor {}", transaction, policy, interceptor.getClass());
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
