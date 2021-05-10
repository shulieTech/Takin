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
package com.shulie.instrument.simulator.core.classloader;

/**
 * 模块类加载器工厂
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/11/13 11:33 下午
 */
public interface ClassLoaderFactory {
    /**
     * 获取类加载器
     *
     * @return 模块类加载器
     */
    ClassLoader getClassLoader(ClassLoader businessClassLoader);

    /**
     * 获取默认类加载器
     * @return
     */
    ClassLoader getDefaultClassLoader();

    /**
     * 获取ChecksumCRC32
     *
     * @return ChecksumCRC32
     */
    long getChecksumCRC32();

    /**
     * 释放资源
     */
    void release();
}
