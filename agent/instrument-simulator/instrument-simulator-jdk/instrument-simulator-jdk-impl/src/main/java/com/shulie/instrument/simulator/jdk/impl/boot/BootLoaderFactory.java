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
package com.shulie.instrument.simulator.jdk.impl.boot;


import com.shulie.instrument.simulator.jdk.api.boot.BootLoader;
import com.shulie.instrument.simulator.jdk.jdk8.impl.boot.J9BootLoader;
import com.shulie.instrument.simulator.jdk.jdk8.impl.boot.LauncherBootLoader;
import com.shulie.instrument.simulator.jdk.jdk9.boot.Java9BootLoader;
import com.shulie.instrument.simulator.message.boot.util.JvmUtils;
import com.shulie.instrument.simulator.message.boot.version.JvmType;
import com.shulie.instrument.simulator.message.boot.version.JvmVersion;

/**
 * boot loader 工厂，主要解决多 JVM 版本和多 JVM 类型时对加载 jdk 类采用不同的方式加载
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/12/9 4:37 下午
 */
public final class BootLoaderFactory {

    private static final boolean isJ9Vm = isJ9VM();
    private static final JvmVersion jvmVersion = JvmUtils.getVersion();
    private static BootLoader bootLoader;

    private BootLoaderFactory() {
    }

    /**
     * 初始化 BootLoader
     * 如果是 java9及以后版本，则使用 Java9的 BootLoader
     * 如果是 ibm j9类型的 jvm，则使用 J9的 BootLoader
     * 否则使用 LauncherBootLoader
     * LauncherBootLoader兼容 sun hotspot 和 openjdk
     *
     * @return BootLoader
     */
    public static BootLoader newBootLoader() {
        if (bootLoader == null) {
            synchronized (BootLoaderFactory.class) {
                if (bootLoader == null) {
                    if (jvmVersion.onOrAfter(JvmVersion.JAVA_9)) {
                        bootLoader = new Java9BootLoader();
                    } else if (isJ9Vm) {
                        try {
                            bootLoader = new J9BootLoader();
                        } catch (Error linkageError) {
                            throw new IllegalStateException("J9BootLoader create fail Caused by:" + linkageError.getMessage(), linkageError);
                        }
                    } else {
                        bootLoader = new LauncherBootLoader();
                    }
                }
            }
        }
        return bootLoader;
    }

    /**
     * 判断是否是 ibm j9的 jvm
     *
     * @return true|false
     */
    private static boolean isJ9VM() {
        JvmType jvmType = JvmUtils.getType();
        return jvmType == JvmType.IBM;
    }

}
