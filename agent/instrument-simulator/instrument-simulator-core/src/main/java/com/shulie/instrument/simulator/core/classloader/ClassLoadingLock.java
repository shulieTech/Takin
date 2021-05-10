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
package com.shulie.instrument.simulator.core.classloader;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 类加载锁
 */
class ClassLoadingLock {

    private final ConcurrentHashMap<String, Object> classLoadingLockMap = new ConcurrentHashMap<String, Object>();

    /**
     * 类加载
     */
    interface ClassLoading {

        /**
         * 加载类
         *
         * @param javaClassName 类名称
         * @return 类
         * @throws ClassNotFoundException 类找不到
         */
        Class<?> loadClass(String javaClassName) throws ClassNotFoundException;

    }

    /**
     * <p>解决 #218</p>
     * 参考JDK1.7；因为Simulator需要维持在1.6上运行，
     * 未来平台统一升级到1.8+之后就用系统自带的
     *
     * @param javaClassName JavaClassName
     * @return ClassLoading Lock
     */
    private Object getClassLoadingLock(String javaClassName) {
        final Object newLock = new Object();
        final Object lock = classLoadingLockMap.putIfAbsent(javaClassName, newLock);
        return null == lock
                ? newLock
                : lock;
    }

    /**
     * 在锁中加载类
     *
     * @param javaClassName JavaClassName
     * @param loading       类加载回调(在锁中)
     * @return 加载的类
     * @throws ClassNotFoundException 类找不到
     */
    public Class<?> loadingInLock(String javaClassName, ClassLoading loading) throws ClassNotFoundException {
        synchronized (getClassLoadingLock(javaClassName)) {
            try {
                return loading.loadClass(javaClassName);
            } finally {
                classLoadingLockMap.remove(javaClassName);
            }
        }
    }

}
