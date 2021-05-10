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
package com.pamirs.pradar.scope;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by xiaobin on 2017/1/19.
 */
public class ConcurrentPool<K, V> implements Pool<K, V> {

    private final ConcurrentMap<K, V> pool = new ConcurrentHashMap<K, V>(500, 0.9f);

    private final PoolObjectFactory<K, V> objectFactory;

    public ConcurrentPool(PoolObjectFactory<K, V> objectFactory) {
        if (objectFactory == null) {
            throw new NullPointerException("objectFactory must not be null");
        }
        this.objectFactory = objectFactory;
    }

    @Override
    public V get(K key) {
        if (key == null) {
            throw new IllegalArgumentException("key must not be null");
        }

        final V alreadyExist = this.pool.get(key);
        if (alreadyExist != null) {
            return alreadyExist;
        }

        final V newValue = this.objectFactory.create(key);
        final V oldValue = this.pool.putIfAbsent(key, newValue);
        if (oldValue != null) {
            return oldValue;
        }
        return newValue;
    }


}

