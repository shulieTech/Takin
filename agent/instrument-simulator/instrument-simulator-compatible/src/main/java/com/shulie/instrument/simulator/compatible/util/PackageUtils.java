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
package com.shulie.instrument.simulator.compatible.util;

import org.apache.commons.lang.StringUtils;

/**
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/12/9 8:25 下午
 */
public final class PackageUtils {
    public static String getPackageName(String className, char packageSeparator, String defaultValue) {
        if (className == null) {
            throw new NullPointerException("fully-qualified class name");
        }
        final int lastPackageSeparatorIndex = className.lastIndexOf(packageSeparator);
        if (lastPackageSeparatorIndex == -1) {
            return defaultValue;
        }
        return className.substring(0, lastPackageSeparatorIndex);
    }

    public static String getPackageNameFromInternalName(String className) {
        if (className == null) {
            throw new NullPointerException("className");
        }
        final String jvmPackageName = ClassUtils.getPackageName(className, '/', null);
        if (jvmPackageName == null) {
            return null;
        }

        return jvmNameToJavaName(jvmPackageName);
    }

    /**
     * java/lang/String -> java.lang.String
     *
     * @param jvmName
     * @return
     */
    public static String jvmNameToJavaName(String jvmName) {
        if (jvmName == null) {
            throw new NullPointerException("jvmName");
        }
        return StringUtils.replaceChars(jvmName, '/', '.');
    }
}
