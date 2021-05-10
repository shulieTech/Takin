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
import com.shulie.instrument.simulator.api.ProcessController;
import com.shulie.instrument.simulator.api.listener.ext.Advice;

/**
 * 挡板的拦截器实现, 这里需要注意的是不能使用其他的拦截器实现挡板拦截器，因为档板的返回事件触发是在
 * BEFORE事件中的，框架在实现返回事件时会中断后续的事件触发，如果作用域需要依赖 BEFORE、RETURN/THROWS
 * 事件组合才实现作用域，则 RETURN/THROWS 事件后续不会再被触发，则作用域会产生问题
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/10/10 8:59 下午
 */
abstract class CutoffInterceptor extends BaseInterceptor {

    @Override
    public final void before(Advice advice) throws Throwable {
        CutOffResult cutOffResult = cutoff(advice);
        if (cutOffResult.isCutoff()) {
            ProcessController.returnImmediately(cutOffResult.getResult());
        }
    }

    public abstract CutOffResult cutoff(Advice advice) throws Throwable;
}
