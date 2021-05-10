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
package com.shulie.instrument.simulator.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 全局开关，可以注册很多的开关，如果模块依赖对应的开关，则该开关未开启状态，则模块不会加载
 * 如果回调执行错误会直接忽略
 * 每一个开关都会对应一个回调队列，当开关状态发生变更时则回调队列会全部执行，执行完一个则移除一个，每一个回调只会被执行一次
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/11/25 7:29 下午
 */
public class GlobalSwitch {
    private final static Logger logger = LoggerFactory.getLogger(GlobalSwitch.class);

    private static ConcurrentHashMap<String, AtomicBoolean> switches = new ConcurrentHashMap<String, AtomicBoolean>();
    private static ConcurrentHashMap<Set<String>, Queue<Runnable>> aggrSwitchCallbacks = new ConcurrentHashMap<Set<String>, Queue<Runnable>>();

    private static ModuleLoader moduleLoader;

    public static void setModuleLoader(ModuleLoader loader) {
        moduleLoader = loader;
    }

    /**
     * 开关是否打开
     *
     * @return
     */
    public static boolean isSwitchOn(String switcherName) {
        AtomicBoolean switcher = switches.get(switcherName);
        return switcher == null ? false : switcher.get();
    }

    /**
     * 判断所有开关是否都是开启状态
     *
     * @param switcherNames
     * @return
     */
    public static boolean isAllSwitchOn(Set<String> switcherNames) {
        if (switcherNames == null || switcherNames.isEmpty()) {
            return true;
        }
        for (String switcherName : switcherNames) {
            if (!isSwitchOn(switcherName)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 打开开关
     *
     * @param switcherName 开关名称
     */
    public static void switchOn(final String switcherName) {
        moduleLoader.load(new Runnable() {
            @Override
            public void run() {
                AtomicBoolean switcher = switches.get(switcherName);
                if (switcher == null) {
                    switcher = new AtomicBoolean(false);
                    AtomicBoolean oldSwitcher = switches.putIfAbsent(switcherName, switcher);
                    if (oldSwitcher != null) {
                        switcher = oldSwitcher;
                    }
                }

                if (switcher.compareAndSet(false, true)) {
                    List<Set<String>> switchers = new ArrayList<Set<String>>();
                    for (Map.Entry<Set<String>, Queue<Runnable>> entry : aggrSwitchCallbacks.entrySet()) {
                        if (!entry.getKey().contains(switcherName)) {
                            continue;
                        }
                        if (isAllSwitchOn(entry.getKey())) {
                            switchers.add(entry.getKey());
                        }
                    }
                    for (Set<String> switcherNames : switchers) {
                        Queue<Runnable> queue = aggrSwitchCallbacks.remove(switcherNames);
                        if (queue != null) {
                            while (true) {
                                Runnable runnable = queue.poll();
                                if (runnable == null) {
                                    break;
                                }
                                try {
                                    runnable.run();
                                } catch (Throwable e) {
                                    logger.error("SIMULATOR: execute global switcher[{}] on err! {}", switcherName, runnable, e);
                                }
                            }
                        }
                    }
                }
            }
        });

    }

    /**
     * 关闭开关
     *
     * @param switcherName 开关名称
     */
    public static void switchOff(final String switcherName) {
        //TODO 未实现
    }

    /**
     * 注册开关开启时的回调，当开关状态变化时会回调对应的回调函数
     * 回调时需要拿开关状态时需要调用{@link #isSwitchOn(String)}
     *
     * @param callback
     * @see #isSwitchOn(String)
     */
    public static void registerMultiSwitchOnCallback(String switcherName, Runnable callback) {
        Set<String> sets = new HashSet<String>();
        sets.add(switcherName);
        registerMultiSwitchOnCallback(sets, callback);
    }

    /**
     * 注册多个开关同时开启时的回调,当所有的开关状态都变成 true 时，则回调对应的回调函数
     *
     * @param switcherName
     * @param callback
     */
    public static void registerMultiSwitchOnCallback(Set<String> switcherName, Runnable callback) {
        Queue<Runnable> callbacks = aggrSwitchCallbacks.get(switcherName);
        if (callbacks == null) {
            callbacks = new ConcurrentLinkedQueue<Runnable>();
            Queue<Runnable> oldCallbacks = aggrSwitchCallbacks.putIfAbsent(switcherName, callbacks);
            if (oldCallbacks != null) {
                callbacks = oldCallbacks;
            }
        }

        callbacks.add(callback);
    }

    /**
     * 模块加载器，负责模块的加载和卸载任务执行
     * 设计主要是防止多个线程同时加载模块时可能会产生死锁
     */
    public interface ModuleLoader {
        /**
         * 执行加载
         *
         * @param runnable
         */
        void load(Runnable runnable);

        /**
         * 执行卸载
         * @param runnable
         */
        void unload(Runnable runnable);
    }
}
