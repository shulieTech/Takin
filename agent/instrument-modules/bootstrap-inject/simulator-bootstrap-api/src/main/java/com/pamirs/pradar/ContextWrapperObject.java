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

import java.io.Serializable;

/**
 * 包装调用上下文的目标对象
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2021/1/27 10:01 上午
 */
public class ContextWrapperObject<T> implements Serializable {
    private final static long serialVersionUID = 1L;

    /**
     * invoke 上下文
     */
    private Object context;

    /**
     * 目标对象
     */
    private T target;

    public ContextWrapperObject(Object context, T target) {
        this.context = context;
        this.target = target;
    }

    public Object getContext() {
        return context;
    }

    public T getTarget() {
        return target;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ContextWrapperObject<?> that = (ContextWrapperObject<?>) o;

        return target != null ? target.equals(that.target) : that.target == null;
    }

    @Override
    public int hashCode() {
        return target != null ? target.hashCode() : 0;
    }

    @Override
    public String toString() {
        return target == null ? null : target.toString();
    }
}
