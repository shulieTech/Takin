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

/**
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/12/9 8:25 下午
 */
public final class ClassUtils {

    private static final char PACKAGE_SEPARATOR = '.';

    private ClassUtils() {
    }

    public static boolean isLoaded(String name) {
        return isLoaded(name, ClassLoaderUtils.getDefaultClassLoader());
    }

    public static boolean isLoaded(String name, ClassLoader classLoader) {
        if (name == null) {
            throw new IllegalArgumentException("name");
        }
        ClassLoader classLoaderToUse = classLoader;
        if (classLoaderToUse == null) {
            classLoaderToUse = ClassLoaderUtils.getDefaultClassLoader();
        }
        try {
            classLoaderToUse.loadClass(name);
            return true;
        } catch (ClassNotFoundException ignore) {
            return false;
        }
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

    /**
     * convert "." based name to "/" based internal name.
     */
    public static String toInternalName(final String className) {
        if (className == null) {
            throw new IllegalArgumentException("class name must not be null");
        }
        return className.replace('.', '/');
    }

    public static String getPackageNameFromInternalName(String className) {
        if (className == null) {
            throw new NullPointerException("className");
        }
        final String jvmPackageName = PackageUtils.getPackageName(className, '/', null);
        if (jvmPackageName == null) {
            return null;
        }

        return jvmNameToJavaName(jvmPackageName);
    }

    public static String jvmNameToJavaName(String jvmName) {
        if (jvmName == null) {
            throw new NullPointerException("jvmName");
        }
        return jvmName.replace('/', '.');
    }
}
