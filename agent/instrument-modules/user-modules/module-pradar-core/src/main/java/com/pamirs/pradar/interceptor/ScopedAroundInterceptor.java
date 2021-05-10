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


import com.pamirs.pradar.Pradar;
import com.pamirs.pradar.scope.ScopeFactory;
import com.shulie.instrument.simulator.api.listener.ext.Advice;
import com.shulie.instrument.simulator.api.scope.ExecutionPolicy;
import com.shulie.instrument.simulator.api.scope.InterceptorScope;
import com.shulie.instrument.simulator.api.scope.InterceptorScopeInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by xiaobin on 2017/1/19.
 */
public class ScopedAroundInterceptor extends AroundInterceptor {
    private final Logger logger = LoggerFactory.getLogger(ScopedAroundInterceptor.class.getName());

    private final AroundInterceptor interceptor;
    private final InterceptorScope scope;
    private final ExecutionPolicy policy;

    public ScopedAroundInterceptor(AroundInterceptor interceptor, ExecutionPolicy policy) {
        this(interceptor, null, policy);
    }

    public ScopedAroundInterceptor(AroundInterceptor interceptor, InterceptorScope scope, ExecutionPolicy policy) {
        if (interceptor == null) {
            throw new NullPointerException("interceptor must not be null");
        }
        if (policy == null) {
            throw new NullPointerException("policy must not be null");
        }
        this.interceptor = interceptor;
        this.scope = scope;
        this.policy = policy;
        super.setInterceptorScope(scope);
    }

    private InterceptorScope getScope(Advice advice) {
        if (scope != null) {
            return scope;
        }
        return ScopeFactory.getScope(interceptor.getClass().getName() + "#" + advice.getTargetClass().getName() + "_" + advice.getBehavior().getName());
    }

    @Override
    public void doBefore(Advice advice) throws Exception {
        final InterceptorScopeInvocation transaction = getScope(advice).getCurrentInvocation();
        final boolean success = transaction.tryEnter(policy);
        if (success) {
            try {
                interceptor.doBefore(advice);
            } catch (Throwable t) {
                if (Pradar.isClusterTest() || InterceptorInvokerHelper.isPropagateException()) {
                    if (transaction.canLeave(policy)) {
                        transaction.leave(policy);
                    }
                }
                InterceptorInvokerHelper.handleException(t);
            }
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("tryBefore() returns false: interceptorScopeTransaction: {}, executionPoint: {}. Skip interceptor {}", transaction, policy, interceptor.getClass());
            }
        }
    }

    @Override
    public void doAfter(Advice advice) {
        final InterceptorScopeInvocation transaction = getScope(advice).getCurrentInvocation();
        boolean success = transaction.canLeave(policy);
        try {
            if (success) {
                try {
                    interceptor.doAfter(advice);
                } catch (Throwable t) {
                    InterceptorInvokerHelper.handleException(t);
                }
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug("tryAfter() returns false: interceptorScopeTransaction: {}, executionPoint: {}. Skip interceptor {}", transaction, policy, interceptor.getClass());
                }
            }
        } finally {
            if (success) {
                transaction.leave(policy);
            }
        }
    }

    @Override
    public void doException(Advice advice) {
        final InterceptorScopeInvocation transaction = getScope(advice).getCurrentInvocation();

        boolean success = transaction.canLeave(policy);
        try {
            if (success) {
                try {
                    interceptor.doException(advice);
                } catch (Throwable t) {
                    InterceptorInvokerHelper.handleException(t);
                }
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug("doException() returns false: interceptorScopeTransaction: {}, executionPoint: {}. Skip interceptor {}", transaction, policy, interceptor.getClass());
                }
            }
        } finally {
            if (success) {
                transaction.leave(policy);
            }
        }
    }
}

