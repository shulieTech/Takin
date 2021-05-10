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
package com.pamirs.pradar.scope;


import com.shulie.instrument.simulator.api.scope.InterceptorScope;
import com.shulie.instrument.simulator.api.scope.InterceptorScopeInvocation;

/**
 * Created by xiaobin on 2017/1/19.
 */
public class DefaultInterceptorScope implements InterceptorScope {
    private final String name;
    private final ThreadLocal<InterceptorScopeInvocation> threadLocal;

    public DefaultInterceptorScope(final String name) {
        this.name = name;
        this.threadLocal = new ThreadLocal<InterceptorScopeInvocation>() {

            @Override
            protected InterceptorScopeInvocation initialValue() {
                return new DefaultInterceptorScopeInvocation(name);
            }

        };
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public InterceptorScopeInvocation getCurrentInvocation() {
        return threadLocal.get();
    }
}
