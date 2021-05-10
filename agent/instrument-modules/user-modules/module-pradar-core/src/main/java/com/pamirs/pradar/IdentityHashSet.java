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
package com.pamirs.pradar;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

final class IdentityHashSet<KType> implements Iterable<KType> {
    public final static float DEFAULT_LOAD_FACTOR = 0.75f;

    public final static int MIN_CAPACITY = 4;

    public Object[] keys;

    public int assigned;

    public final float loadFactor;

    private int resizeThreshold;

    public IdentityHashSet() {
        this(16, DEFAULT_LOAD_FACTOR);
    }

    public IdentityHashSet(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    public IdentityHashSet(int initialCapacity, float loadFactor) {
        initialCapacity = Math.max(MIN_CAPACITY, initialCapacity);

        assert initialCapacity > 0 : "Initial capacity must be between (0, "
                + Integer.MAX_VALUE + "].";
        assert loadFactor > 0 && loadFactor < 1 : "Load factor must be between (0, 1).";
        this.loadFactor = loadFactor;
        allocateBuffers(roundCapacity(initialCapacity));
    }

    public boolean add(KType e) {
        assert e != null : "Null keys not allowed.";

        if (assigned >= resizeThreshold) expandAndRehash();

        final int mask = keys.length - 1;
        int slot = rehash(e) & mask;
        Object existing;
        while ((existing = keys[slot]) != null) {
            if (e == existing) {
                return false; // already found.
            }
            slot = (slot + 1) & mask;
        }
        assigned++;
        keys[slot] = e;
        return true;
    }

    public boolean contains(KType e) {
        final int mask = keys.length - 1;
        int slot = rehash(e) & mask;
        Object existing;
        while ((existing = keys[slot]) != null) {
            if (e == existing) {
                return true;
            }
            slot = (slot + 1) & mask;
        }
        return false;
    }

    private static int rehash(Object o) {
        return MurmurHash3.hash(System.identityHashCode(o));
    }

    private void expandAndRehash() {
        final Object[] oldKeys = this.keys;

        assert assigned >= resizeThreshold;
        allocateBuffers(nextCapacity(keys.length));

        final int mask = keys.length - 1;
        for (int i = 0; i < oldKeys.length; i++) {
            final Object key = oldKeys[i];
            if (key != null) {
                int slot = rehash(key) & mask;
                while (keys[slot] != null) {
                    slot = (slot + 1) & mask;
                }
                keys[slot] = key;
            }
        }
        Arrays.fill(oldKeys, null);
    }

    private void allocateBuffers(int capacity) {
        this.keys = new Object[capacity];
        this.resizeThreshold = (int) (capacity * DEFAULT_LOAD_FACTOR);
    }

    protected int nextCapacity(int current) {
        assert current > 0 && Long.bitCount(current) == 1 : "Capacity must be a power of two.";
        assert ((current << 1) > 0) : "Maximum capacity exceeded ("
                + (0x80000000 >>> 1) + ").";

        if (current < MIN_CAPACITY / 2) current = MIN_CAPACITY / 2;
        return current << 1;
    }

    protected int roundCapacity(int requestedCapacity) {
        if (requestedCapacity > (0x80000000 >>> 1)) return (0x80000000 >>> 1);

        int capacity = MIN_CAPACITY;
        while (capacity < requestedCapacity) {
            capacity <<= 1;
        }

        return capacity;
    }

    public void clear() {
        assigned = 0;
        Arrays.fill(keys, null);
    }

    public int size() {
        return assigned;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public Iterator<KType> iterator() {
        return new Iterator<KType>() {
            int pos = -1;
            Object nextElement = fetchNext();

            @Override
            public boolean hasNext() {
                return nextElement != null;
            }

            @SuppressWarnings("unchecked")
            @Override
            public KType next() {
                Object r = this.nextElement;
                if (r == null) {
                    throw new NoSuchElementException();
                }
                this.nextElement = fetchNext();
                return (KType) r;
            }

            private Object fetchNext() {
                pos++;
                while (pos < keys.length && keys[pos] == null) {
                    pos++;
                }

                return (pos >= keys.length ? null : keys[pos]);
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
