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
package com.shulie.instrument.simulator.module.util;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 线程安全的 Set，底层基于 ConcurrentHashMap, 如果使用 CopyOnWriteArraySet 等读写混合场景时的性能太差，
 * 使用 ConcurrentHashSet 比 CopyOnWriteArraySet 性能最少高于3倍以上
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2021/1/29 12:04 下午
 */
public class ConcurrentHashSet<E> extends AbstractSet<E> implements Set<E> {
    private final Map<E, Boolean> _map = new ConcurrentHashMap<E, Boolean>();
    private transient Set<E> _keys = _map.keySet();

    public ConcurrentHashSet() {
    }

    @Override
    public boolean add(E e) {
        return _map.put(e, Boolean.TRUE) == null;
    }

    @Override
    public void clear() {
        _map.clear();
    }

    @Override
    public boolean contains(Object o) {
        return _map.containsKey(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return _keys.containsAll(c);
    }

    @Override
    public boolean equals(Object o) {
        return o == this || _keys.equals(o);
    }

    @Override
    public int hashCode() {
        return _keys.hashCode();
    }

    @Override
    public boolean isEmpty() {
        return _map.isEmpty();
    }

    @Override
    public Iterator<E> iterator() {
        return _keys.iterator();
    }

    @Override
    public boolean remove(Object o) {
        return _map.remove(o) != null;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return _keys.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return _keys.retainAll(c);
    }

    @Override
    public int size() {
        return _map.size();
    }

    @Override
    public Object[] toArray() {
        return _keys.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return _keys.toArray(a);
    }

    @Override
    public String toString() {
        return _keys.toString();
    }
}
