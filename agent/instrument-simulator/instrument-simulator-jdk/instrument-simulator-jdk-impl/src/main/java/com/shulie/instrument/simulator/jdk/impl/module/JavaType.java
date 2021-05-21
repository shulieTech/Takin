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
package com.shulie.instrument.simulator.jdk.impl.module;

/**
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2021/5/14 11:50 上午
 */
public enum JavaType {
    MODULE("java.lang.Module");

    /**
     * 类名称
     */
    private String typeName;

    JavaType(String typeName) {
        this.typeName = typeName;
    }

    /**
     * 返回是否可用
     *
     * @return
     */
    public boolean isAvailable() {
        try {
            load();
            return true;
        } catch (Throwable e) {
            return false;
        }
    }

    /**
     * 加载指定的类
     *
     * @return
     * @throws ClassNotFoundException
     */
    public Class<?> load() throws ClassNotFoundException {
        return Class.forName(typeName, false, null);
    }

    /**
     * 判断是否是指定的类型
     *
     * @param instance 该类型的实例
     * @return
     */
    public boolean isInstance(Object instance) {
        if (!isAvailable()) {
            return false;
        }
        try {
            return load().isInstance(instance);
        } catch (ClassNotFoundException ignored) {
            return false;
        }
    }
}
