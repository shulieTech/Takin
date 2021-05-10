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

/**
 * 实例方法的结果修改拦截
 * Created by xiaobin on 2017/2/6.
 */
abstract class ResultInterceptor extends BaseInterceptor {

    @Override
    public final void doAfter(Advice advice) throws Throwable {
        Object result = getResult(advice);
        ProcessController.returnImmediately(result);
    }

    /**
     * 对结果进行拦截修改
     *
     * @param advice 切点对象
     * @return
     */
    public abstract Object getResult(Advice advice) throws Throwable;
}
