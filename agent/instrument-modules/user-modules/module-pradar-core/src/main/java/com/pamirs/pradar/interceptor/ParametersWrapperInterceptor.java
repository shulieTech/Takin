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
 * 对实例方法参数包装拦截修改
 */
abstract class ParametersWrapperInterceptor extends BaseInterceptor {

    @Override
    public final void doBefore(Advice advice) throws Throwable {
        Object[] args = getParameter(advice);
        if (args != null) {
            for (int i = 0; i < Math.min(args.length, advice.getParameterArray().length); i++) {
                advice.changeParameter(i, args[i]);
            }
        }
    }

    /**
     * 拦截修改参数
     */
    public abstract Object[] getParameter(Advice advice) throws Throwable;
}
