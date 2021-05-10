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
package com.shulie.instrument.simulator.module.stack;

import com.shulie.instrument.simulator.api.listener.ext.Advice;
import com.shulie.instrument.simulator.api.listener.ext.AdviceListener;
import com.shulie.instrument.simulator.module.model.stack.StackElement;
import com.shulie.instrument.simulator.module.util.ThreadUtil;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2021/1/29 2:00 下午
 */
public class StackListener extends AdviceListener {
    private CountDownLatch latch;
    private List stacks;

    public StackListener(CountDownLatch latch, List stacks) {
        this.latch = latch;
        this.stacks = stacks;
    }

    @Override
    public void afterReturning(Advice advice) throws Throwable {
        List<StackElement> elements = ThreadUtil.getThreadStack(Thread.currentThread());
        stacks.addAll(elements);
        latch.countDown();
    }

    @Override
    public void afterThrowing(Advice advice) throws Throwable {
        List<StackElement> elements = ThreadUtil.getThreadStack(Thread.currentThread());
        stacks.addAll(elements);
        latch.countDown();
    }
}
