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
package com.shulie.instrument.simulator.api.guard;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Simulator守卫者
 * <p>
 * 用来保护Simulator的操作所产生的事件不被响应
 * </p>
 */
public class SimulatorGuard {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private final ThreadLocal<AtomicInteger> guardThreadLocal = new ThreadLocal<AtomicInteger>() {
        @Override
        protected AtomicInteger initialValue() {
            return new AtomicInteger(0);
        }
    };

    /**
     * 进入守护区域
     *
     * @return 守护区域当前引用计数
     */
    public int enter() {
        final int referenceCount = guardThreadLocal.get().getAndIncrement();
        if (logger.isDebugEnabled()) {
            logger.debug("SIMULATOR: thread:{} enter protect:{}", Thread.currentThread(), referenceCount);
        }
        return referenceCount;
    }

    /**
     * 离开守护区域
     *
     * @return 守护区域当前引用计数
     */
    public int exit() {
        final int referenceCount = guardThreadLocal.get().decrementAndGet();
        assert referenceCount >= 0;
        if (referenceCount == 0) {
            guardThreadLocal.remove();
            if (logger.isDebugEnabled()) {
                logger.debug("SIMULATOR: thread:{} exit protect:{} with clean", Thread.currentThread(), referenceCount);
            }
        } else if (referenceCount > 0) {
            if (logger.isDebugEnabled()) {
                logger.debug("SIMULATOR: thread:{} exit protect:{}", Thread.currentThread(), referenceCount);
            }
        } else {
            logger.warn("SIMULATOR: thread:{} exit protect:{} with error!", Thread.currentThread(), referenceCount);
        }
        return referenceCount;
    }

    /**
     * 判断当前是否处于守护区域中
     *
     * @return TRUE:在守护区域中；FALSE：非守护区域中
     */
    public boolean isInGuard() {
        return guardThreadLocal.get().get() > 0;
    }

    /**
     * 守护接口定义的所有方法
     *
     * @param protectTargetInterface 保护目标接口类型
     * @param protectTarget          保护目标接口实现
     * @param <T>                    接口类型
     * @return 被保护的目标接口实现
     */
    public <T> T doGuard(final Class<T> protectTargetInterface,
                         final T protectTarget) {
        return (T) Proxy.newProxyInstance(protectTargetInterface.getClassLoader(), new Class<?>[]{protectTargetInterface}, new InvocationHandler() {

            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                final int enterReferenceCount = enter();
                try {
                    return method.invoke(protectTarget, args);
                } finally {
                    final int exitReferenceCount = exit();
                    assert enterReferenceCount == exitReferenceCount;
                    if (enterReferenceCount != exitReferenceCount) {
                        logger.warn("SIMULATOR: thread:{} exit protecting with error!, expect:{} actual:{}",
                                Thread.currentThread(),
                                enterReferenceCount,
                                exitReferenceCount
                        );
                    }
                }
            }

        });
    }


    /**
     * Simulator守护者单例
     */
    private static final SimulatorGuard INSTANCE = new SimulatorGuard();


    public static SimulatorGuard getInstance() {
        return INSTANCE;
    }
}
