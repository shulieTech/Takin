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

import com.shulie.instrument.simulator.api.resource.DynamicFieldManager;
import com.shulie.instrument.simulator.message.ConcurrentWeakHashMap;
import com.shulie.instrument.simulator.message.DestroyHook;

import java.io.Closeable;
import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 动态属性管理器的默认实现
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2021/4/24 1:52 下午
 */
public class DefaultDynamicFieldManager implements DynamicFieldManager {
    /**
     * 所有动态属性的集合,可能很多的模块根本不需要动态属性，所以这里使用延迟初始化
     * 防止浪费不必要的内存
     */
    private ConcurrentWeakHashMap<Object, DynamicField> dynamicFields;

    /**
     * 当前所属的模块 ID, 留一个当前模块的属性，方便以后此处出问题的排查
     */
    private String moduleId;

    public DefaultDynamicFieldManager(String moduleId) {
        this.moduleId = moduleId;
    }

    /**
     * 延迟初始化
     */
    private void lazyInit() {
        if (dynamicFields == null) {
            synchronized (this) {
                if (dynamicFields == null) {
                    dynamicFields = new ConcurrentWeakHashMap<Object, DynamicField>(new ConcurrentWeakHashMap.HashCodeGenerateFunction() {
                        @Override
                        public int hashcode(Object key) {
                            return System.identityHashCode(key);
                        }
                    }, new DestroyHook<Object, DynamicField>() {
                        @Override
                        public void destroy(Object key, DynamicField dynamicField) {
                            dynamicField.destroy();
                        }
                    });
                }
            }
        }
    }

    @Override
    public <T> T getDynamicField(Object target, String fieldName) {
        return getDynamicField(target, fieldName, null);
    }

    @Override
    public <T> T getDynamicField(Object target, String fieldName, T defaultValue) {
        if (dynamicFields == null) {
            return defaultValue;
        }
        DynamicField dynamicField = dynamicFields.get(target);
        if (dynamicField == null) {
            return defaultValue;
        }
        T value = dynamicField.getField(fieldName);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }

    @Override
    public void removeAll(Object target) {
        if (dynamicFields == null) {
            return;
        }
        DynamicField dynamicField = dynamicFields.remove(target);
        if (dynamicField != null) {
            dynamicField.destroy();
        }
    }

    @Override
    public void setDynamicField(Object target, String fieldName, Object value) {
        if (value == null || target == null || fieldName == null) {
            return;
        }
        lazyInit();
        DynamicField field = dynamicFields.get(target);
        if (field == null) {
            field = new DynamicField();
            DynamicField old = dynamicFields.putIfAbsent(target, field);
            if (old != null) {
                field = old;
            }
        }
        field.setField(fieldName, value);
    }

    @Override
    public <T> T removeField(Object target, String fieldName) {
        if (dynamicFields == null) {
            return null;
        }
        DynamicField field = dynamicFields.get(target);
        if (field == null) {
            return null;
        }

        return (T) field.removeField(fieldName);
    }

    @Override
    public void destroy() {
        if (dynamicFields == null) {
            return;
        }
        dynamicFields.clear();
    }

    private static class DynamicField implements Closeable, Serializable {
        /**
         * 缓存的对应只有当 gc 回收之后才会空，所以可以根据这个来判断该对象是否可以回收
         */
        private ConcurrentHashMap<String, Object> fields = new ConcurrentHashMap<String, Object>(4, 1.0f, 4);

        public synchronized void setField(String field, Object value) {
            this.fields.put(field, value);
        }

        public <T> T getField(String field) {
            return (T) this.fields.get(field);
        }

        /**
         * 判断是否具有该属性的时候做一下优化，如果值已经不存在了，则直接删除该值
         *
         * @param field
         * @return
         */
        public boolean hasField(String field) {
            return this.fields.containsKey(field);
        }

        public synchronized Object removeField(String field) {
            return this.fields.remove(field);
        }

        public boolean isEmpty() {
            return fields.isEmpty();
        }

        /**
         * 销毁
         */
        public void destroy() {
            fields.clear();
            fields = null;
        }

        @Override
        public void close() {
            destroy();
        }
    }
}
