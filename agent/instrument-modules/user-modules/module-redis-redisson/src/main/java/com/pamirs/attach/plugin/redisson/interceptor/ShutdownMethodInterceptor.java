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
package com.pamirs.attach.plugin.redisson.interceptor;

import com.pamirs.attach.plugin.redisson.factory.RedissonFactory;
import com.pamirs.pradar.interceptor.AroundInterceptor;
import com.shulie.instrument.simulator.api.listener.ext.Advice;

/**
 * redisson 关闭的时候清除缓存
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2021/3/1 5:11 下午
 */
public class ShutdownMethodInterceptor extends AroundInterceptor {
    @Override
    public void doAfter(Advice advice) throws Throwable {
        Object target = advice.getTarget();
        RedissonFactory.RedissonHolder.getFactory().shutdown(target);
    }
}
