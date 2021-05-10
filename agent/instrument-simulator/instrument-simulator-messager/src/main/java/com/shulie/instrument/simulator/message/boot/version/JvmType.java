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
package com.shulie.instrument.simulator.message.boot.version;

import java.util.EnumSet;

/**
 * JVM 类型
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/12/9 4:22 下午
 */
public enum JvmType {
    UNKNOWN(null),
    JROCKIT("JRockit"),
    IBM("J9"),
    OPENJDK("OpenJDK"),
    ORACLE("HotSpot");

    private final String inclusiveString;

    private static final EnumSet<JvmType> JVM_TYPE = EnumSet.allOf(JvmType.class);

    JvmType(String inclusiveString) {
        this.inclusiveString = inclusiveString;
    }

    public static JvmType fromVendor(String vendorName) {
        if (vendorName == null) {
            return UNKNOWN;
        }
        final String vendorNameTrimmed = vendorName.trim();
        for (JvmType jvmType : JVM_TYPE) {
            if (jvmType.toString().equalsIgnoreCase(vendorNameTrimmed)) {
                return jvmType;
            }
        }
        return UNKNOWN;
    }

    public static JvmType fromVmName(String vmName) {
        if (vmName == null) {
            return UNKNOWN;
        }
        for (JvmType jvmType : JVM_TYPE) {
            if (jvmType.inclusiveString == null) {
                continue;
            }
            if (vmName.contains(jvmType.inclusiveString)) {
                return jvmType;
            }
        }
        return UNKNOWN;
    }
}
