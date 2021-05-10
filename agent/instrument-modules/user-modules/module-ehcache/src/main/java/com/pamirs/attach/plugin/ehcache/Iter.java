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
package com.pamirs.attach.plugin.ehcache;

import com.pamirs.pradar.cache.ClusterTestCacheWrapperKey;
import org.ehcache.Cache;

import java.util.Iterator;
import java.util.function.Consumer;

/**
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2021/3/1 7:08 下午
 */
public class Iter implements Iterator<Cache.Entry> {
    private Iterator<Cache.Entry> it;

    public Iter(Iterator<Cache.Entry> it) {
        this.it = it;
    }

    @Override
    public boolean hasNext() {
        return it.hasNext();
    }

    @Override
    public Cache.Entry next() {
        final Cache.Entry value = it.next();
        if (value == null) {
            return null;
        }
        if (value.getKey() instanceof ClusterTestCacheWrapperKey) {
            return new Cache.Entry() {
                @Override
                public Object getKey() {
                    return ((ClusterTestCacheWrapperKey) value.getKey()).getKey();
                }

                @Override
                public Object getValue() {
                    return value.getValue();
                }
            };
        }
        return value;
    }

    @Override
    public void remove() {
        it.remove();
    }

    @Override
    public void forEachRemaining(final Consumer action) {
        it.forEachRemaining(new Consumer<Cache.Entry>() {
            @Override
            public void accept(final Cache.Entry entry) {
                if (entry == null) {
                    action.accept(entry);
                    return;
                }
                if (entry.getKey() instanceof ClusterTestCacheWrapperKey) {
                    Cache.Entry ent = new Cache.Entry() {
                        @Override
                        public Object getKey() {
                            return ((ClusterTestCacheWrapperKey) entry.getKey()).getKey();
                        }

                        @Override
                        public Object getValue() {
                            return entry.getValue();
                        }
                    };
                    action.accept(ent);
                    return;
                }
                action.accept(entry);
            }
        });
    }
}
