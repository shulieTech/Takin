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
package com.shulie.instrument.simulator.api.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 缓存加载
 *
 * @param <K> KEY
 * @param <V> VAL
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/10/23 10:45 下午
 */
public abstract class CacheGet<K, V> {

    private final Map<K, V> cache = new HashMap<K, V>();

    /**
     * 从缓存中加载
     *
     * @param key KEY
     * @return VALUE
     */
    public V getFromCache(K key) {
        if (!cache.containsKey(key)) {
            try {
                final V value;
                cache.put(key, value = load(key));
                return value;
            } catch (Throwable cause) {
                throw new CacheLoadUnCaughtException(cause);
            }
        } else {
            return cache.get(key);
        }
    }

    /**
     * 加载缓存
     *
     * @param key KEY
     * @return VALUE
     * @throws Throwable 加载失败
     */
    protected abstract V load(K key) throws Throwable;

    private final static class CacheLoadUnCaughtException extends RuntimeException {
        CacheLoadUnCaughtException(Throwable cause) {
            super(cause);
        }
    }

}
