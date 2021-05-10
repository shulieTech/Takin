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
package com.shulie.instrument.simulator.api.resource;

import java.lang.ref.WeakReference;

/**
 * 可释放资源
 *
 * @param <T> 资源类型
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/10/23 10:45 下午
 */
public abstract class ReleaseResource<T> {

    // 资源弱引用，允许被GC回收
    private final WeakReference<T> reference;

    /**
     * 构造释放资源
     *
     * @param resource 资源目标
     */
    public ReleaseResource(T resource) {
        this.reference = new WeakReference<T>(resource);
    }

    /**
     * 释放资源
     */
    public abstract void release();

    /**
     * 获取资源实体
     *
     * @return 资源实体
     */
    public T get() {
        return reference.get();
    }

}
