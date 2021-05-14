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
package com.pamirs.attach.plugin.mock.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/11/12 11:35 上午
 */
public class GroovyCache<T> {
    private static Logger LOGGER = LoggerFactory.getLogger(GroovyCache.class);

    private static Map<String, WeakReference<Object>> localMemoryCache =
            new ConcurrentHashMap<String, WeakReference<Object>>();
    private final static Object NULL = new Object();


    public static String GROOVY_SHELL_KEY_PREFIX = "GROOVY_SHELL#";

    public static <T> T getValue(String key, Callable<Object> load) {

        try {
            WeakReference<Object> value = localMemoryCache.get(key);
            if (value == null || value.get() == null) {
                Object object = load.call();
                if (object == null) {
                    localMemoryCache.put(key, new WeakReference<Object>(NULL));
                } else {
                    localMemoryCache.put(key, new WeakReference<Object>(object));
                }
                return (T) object;
            } else if (value.get() == NULL) {
                return null;
            }
            return (T) value.get();
        } catch (Throwable ex) {
            LOGGER.error("获取缓存异常,key:{} ", key, ex);
        }
        return null;
    }

}
