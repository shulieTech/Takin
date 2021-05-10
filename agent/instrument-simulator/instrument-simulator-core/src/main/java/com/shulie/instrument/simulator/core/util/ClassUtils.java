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
package com.shulie.instrument.simulator.core.util;

import org.apache.commons.lang.StringUtils;

/**
 * 类工具
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/9/24 5:47 下午
 */
public class ClassUtils {
    public final static String DEFAULT_PACKAGE = "";
    private static final char PACKAGE_SEPARATOR = '.';

    /**
     * Get package name from a specified package name.
     *
     * @param packageName 包名
     * @return 返回下一个包名
     */
    public static String getNextPackageName(String packageName) {
        AssertUtils.isFalse(StringUtils.isEmpty(packageName), "ClassName should not be empty!");
        int index = packageName.indexOf('.');
        if (index > 0) {
            return packageName.substring(index + 1);
        }
        return DEFAULT_PACKAGE;
    }

    public static String getPackageName(String fqcn, char packageSeparator, String defaultValue) {
        if (fqcn == null) {
            throw new NullPointerException("fully-qualified class name");
        }
        final int lastPackageSeparatorIndex = fqcn.lastIndexOf(packageSeparator);
        if (lastPackageSeparatorIndex == -1) {
            return defaultValue;
        }
        return fqcn.substring(0, lastPackageSeparatorIndex);
    }

    public static String getPackageName(String fqcn) {
        return getPackageName(fqcn, PACKAGE_SEPARATOR, "");
    }

}
