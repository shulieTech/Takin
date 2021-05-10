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
package com.shulie.instrument.simulator.message.boot.util;


import com.shulie.instrument.simulator.message.boot.version.JvmType;
import com.shulie.instrument.simulator.message.boot.version.JvmVersion;

/**
 * jvm 工具类
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/12/9 4:24 下午
 */
public final class JvmUtils {

    private static final JvmVersion JVM_VERSION = getVersion0();
    private static final JvmType JVM_TYPE = getType0();

    private JvmUtils() {
    }

    public static JvmVersion getVersion() {
        return JVM_VERSION;
    }

    public static JvmType getType() {
        return JVM_TYPE;
    }

    public static boolean supportsVersion(JvmVersion other) {
        return JVM_VERSION.onOrAfter(other);
    }

    public static String getSystemProperty(SystemPropertyKey systemPropertyKey) {
        return System.getProperty(systemPropertyKey.getKey(), "");
    }

    private static JvmVersion getVersion0() {
        String javaVersion = getSystemProperty(SystemPropertyKey.JAVA_SPECIFICATION_VERSION);
        return JvmVersion.getFromVersion(javaVersion);
    }

    private static JvmType getType0() {
        String javaVmName = getSystemProperty(SystemPropertyKey.JAVA_VM_NAME);
        return JvmType.fromVmName(javaVmName);
    }
}
