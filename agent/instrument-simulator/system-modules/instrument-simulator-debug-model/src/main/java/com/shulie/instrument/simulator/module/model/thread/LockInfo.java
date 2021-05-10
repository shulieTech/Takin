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
package com.shulie.instrument.simulator.module.model.thread;

import java.io.Serializable;

/**
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/11/3 10:37 上午
 */
public class LockInfo implements Serializable {
    private final static long serialVersionUID = 1L;

    /**
     * 类名称
     */
    private String className;

    /**
     * 唯一的 hashCode
     */
    private int identityHashCode;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getIdentityHashCode() {
        return identityHashCode;
    }

    public void setIdentityHashCode(int identityHashCode) {
        this.identityHashCode = identityHashCode;
    }

    @Override
    public String toString() {
        return className + '@' + Integer.toHexString(identityHashCode);
    }
}
