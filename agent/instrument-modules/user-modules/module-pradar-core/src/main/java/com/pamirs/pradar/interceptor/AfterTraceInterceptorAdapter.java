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
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2021/5/20 10:13 下午
 */
public abstract class AfterTraceInterceptorAdapter extends AfterTraceInterceptor {
    @Override
    public void afterFirst(Advice advice) {

    }

    @Override
    public void afterLast(Advice advice) {

    }


    @Override
    public void exceptionFirst(Advice advice) {

    }

    @Override
    public void exceptionLast(Advice advice) {

    }

}
