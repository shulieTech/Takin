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
package com.shulie.instrument.simulator.core.manager.impl;

import com.shulie.instrument.simulator.api.ModuleException;
import com.shulie.instrument.simulator.api.event.Event;
import com.shulie.instrument.simulator.api.listener.EventListener;
import com.shulie.instrument.simulator.api.listener.InitializingBean;
import com.shulie.instrument.simulator.api.listener.Interruptable;
import com.shulie.instrument.simulator.api.listener.Listeners;
import com.shulie.instrument.simulator.api.listener.ext.AdviceAdapterListener;
import com.shulie.instrument.simulator.api.listener.ext.AdviceListener;
import com.shulie.instrument.simulator.api.reflect.Reflect;
import com.shulie.instrument.simulator.api.util.ObjectIdUtils;
import com.shulie.instrument.simulator.core.CoreModule;
import com.shulie.instrument.simulator.core.classloader.BizClassLoaderHolder;
import com.shulie.instrument.simulator.core.util.ReflectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 延迟加载的事件监听器代理
 * 每个延迟加载的对象持有多个EventListener实例，因为要考虑到多类加载器问题
 * 增强的目标类可能会多个
 *
 * @author xiaobin.zfb
 * @since 2020/9/18 12:48 上午
 */
public class LazyEventListenerProxy implements EventListener, Interruptable {
    private final static Logger logger = LoggerFactory.getLogger(LazyEventListenerProxy.class.getName());
    private final CoreModule coreModule;
    private final Listeners listeners;
    /**
     * 所有的EventListener,一个业务类加载器对应一个EventListener实例
     * key -> 业务classloader id : EventListener
     * <p>
     * 测试过 jctools 的 NonBlockingHashMap,性能比 ConcurrentHashMap 大概低30-40%左右
     */
    private final ConcurrentHashMap<Integer, EventListener> eventListeners;
    private final AtomicBoolean isRunning = new AtomicBoolean(true);

    private final AtomicBoolean isInitInterrupt = new AtomicBoolean(false);
    /**
     * 是否是可中断的
     */
    private boolean isInterrupt;

    /**
     * 监听器耗时
     */
    private boolean listenerCostEnabled;

    public LazyEventListenerProxy(final CoreModule coreModule, final Listeners listeners) {
        this.coreModule = coreModule;
        this.listeners = listeners;
        this.eventListeners = new ConcurrentHashMap<Integer, EventListener>();
        this.listenerCostEnabled = Boolean.valueOf(System.getProperty("simulator.listener.cost.enabled", "false"));
    }

    @Override
    public void onEvent(Event event) throws Throwable {
        if (listeners == Listeners.NONE) {
            return;
        }
        if (!isRunning.get()) {
            return;
        }

        EventListener listener = getEventListener();
        if (listener == null) {
            logger.error("SIMULATOR: event listener onEvent failed, cause by event listener init failed.");
        } else {
            if (!listenerCostEnabled) {
                listener.onEvent(event);
            } else {
                long start = System.nanoTime();
                try {
                    listener.onEvent(event);
                } finally {
                    long end = System.nanoTime();
                    logger.info("{} execute {} cost {} ns", listeners.getClassName(), event.getType().name(), (end - start));
                }
            }
        }
    }

    @Override
    public void clean() {
        if (!isRunning.compareAndSet(true, false)) {
            return;
        }
        /**
         * 执行所有的资源的清理
         */
        Collection<EventListener> eventListenerSet = eventListeners.values();
        eventListeners.clear();
        for (EventListener eventListener : eventListenerSet) {
            eventListener.clean();
        }
    }

    /**
     * 如果是非中间件模块，则不会根据业务类加载器生成多实例，这是为了支持
     * 基础的全局模块被多个模块同时依赖时需要保证类一致，对象一致
     *
     * @return
     */
    private int getBizClassLoaderId() {
        if (!coreModule.isMiddlewareModule()) {
            return 0;
        }
        return ObjectIdUtils.instance.identity(BizClassLoaderHolder.getBizClassLoader());
    }

    /**
     * 获取事件监听器，会涉及到初始化的动作
     * <p>
     * 非中间件模块使用单实例，不会根据业务类加载器生成多实例
     *
     * @return 返回事件监听器
     */
    private EventListener getEventListener() {
        int id = getBizClassLoaderId();
        EventListener eventListener = this.eventListeners.get(id);
        if (eventListener != null) {
            return eventListener;
        }
        synchronized (this) {
            eventListener = this.eventListeners.get(id);
            if (eventListener != null) {
                return eventListener;
            }
            ClassLoader classLoader = coreModule.getClassLoader(BizClassLoaderHolder.getBizClassLoader());
            try {
                Object listener = null;
                if (listeners.getArgs() == null || listeners.getArgs().length == 0) {
                    listener = Reflect.on(listeners.getClassName(), classLoader).create().get();
                } else {
                    listener = Reflect.on(listeners.getClassName(), classLoader).create(listeners.getArgs()).get();
                }
                /**
                 * 设置是否可中断
                 */
                if (isInitInterrupt.compareAndSet(false, true)) {
                    isInterrupt = ReflectUtils.isInterruptEventHandler(eventListener);
                }

                if (listener != null) {
                    try {
                        coreModule.injectResource(listener);
                    } catch (ModuleException e) {
                        logger.warn("SIMULATOR: can't inject resource into event listener. by module={} listener:{}", coreModule.getUniqueId(), listeners, e);
                    }
                    if (listener instanceof InitializingBean) {
                        ((InitializingBean) listener).init();
                    }
                    if (listener instanceof EventListener) {
                        eventListener = (EventListener) listener;

                        if (listeners.getScopeName() != null && listeners.getEventListenerCallback() != null) {
                            eventListener = listeners.getEventListenerCallback().onCall(eventListener, listeners.getScopeName(), listeners.getExecutionPolicy());
                        }

                    } else if (listener instanceof AdviceListener) {
                        AdviceListener adviceListener = (AdviceListener) listener;
                        if (listeners.getScopeName() != null && listeners.getAdviceListenerCallback() != null) {
                            adviceListener = listeners.getAdviceListenerCallback().onCall(adviceListener, listeners.getScopeName(), listeners.getExecutionPolicy());
                        }
                        eventListener = new AdviceAdapterListener(adviceListener);
                    }
                }


                if (eventListener != null) {
                    EventListener old = this.eventListeners.putIfAbsent(id, eventListener);
                    if (old != null) {
                        eventListener = old;
                    }
                }
            } catch (Throwable e) {
                logger.error("SIMULATOR: event listener onEvent failed, cause by event listener init failed:{}.", listeners.getClassName(), e);
                return null;
            }
        }
        return eventListener;
    }

    @Override
    public boolean isInterrupted() {
        if (isInitInterrupt.get()) {
            return this.isInterrupt;
        }
        try {
            if (!eventListeners.isEmpty()) {
                EventListener eventListener = eventListeners.values().iterator().next();
                boolean isInterrupt = ReflectUtils.isInterruptEventHandler(eventListener);
                if (isInitInterrupt.compareAndSet(false, true)) {
                    this.isInterrupt = isInterrupt;
                    return this.isInterrupt;
                }
            }
            Class clazz = coreModule.getClassLoaderFactory().getDefaultClassLoader().loadClass(listeners.getClassName());
            boolean isInterrupt = ReflectUtils.isInterruptEventHandler(clazz);
            if (isInitInterrupt.compareAndSet(false, true)) {
                this.isInterrupt = isInterrupt;
            }
            return this.isInterrupt;
        } catch (ClassNotFoundException e) {
            logger.error("SIMULATOR: can't found class {} by ModuleClassLoader:{}.", listeners.getClassName(), coreModule.getClassLoaderFactory().getDefaultClassLoader(), e);
            return false;
        } catch (Throwable e) {
            logger.error("SIMULATOR: can't found class {} by ModuleClassLoader:{}.", listeners.getClassName(), coreModule.getClassLoaderFactory().getDefaultClassLoader(), e);
            return false;
        }
    }
}
