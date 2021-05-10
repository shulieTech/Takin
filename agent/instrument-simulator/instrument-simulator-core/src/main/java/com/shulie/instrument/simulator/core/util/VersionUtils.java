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
import org.apache.commons.lang.math.NumberUtils;

/**
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2021/2/2 3:17 下午
 */
public class VersionUtils {
    /**
     * 是否是合法的 version
     *
     * @param version
     * @return
     */
    public static boolean isValidVersion(String version) {
        String[] arr = StringUtils.split(version, '.');
        if (arr.length != 3) {
            return false;
        }
        for (String str : arr) {
            if (!NumberUtils.isDigits(StringUtils.trim(str))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断一个版本号是否小于等于另一个版本号
     *
     * @param version1 版本号
     * @param version2 版本号
     * @return
     */
    public static boolean isLeVersion(String version1, String version2) {
        if (version1 == version2) {
            return true;
        }
        if (version1 == null) {
            return false;
        }
        if (version2 == null) {
            return true;
        }
        String[] arr1 = StringUtils.split(version1, '.');
        String[] arr2 = StringUtils.split(version2, '.');
        for (int i = 0, len = Math.min(arr1.length, arr2.length); i < len; i++) {
            int v1 = Integer.valueOf(arr1[0]);
            int v2 = Integer.valueOf(arr2[0]);
            if (v1 > v2) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断一个版本号是否大于等于另一个版本号
     *
     * @param version1 版本号
     * @param version2 版本号
     * @return
     */
    public static boolean isGeVersion(String version1, String version2) {
        if (version1 == version2) {
            return true;
        }
        if (version1 == null) {
            return true;
        }
        if (version2 == null) {
            return false;
        }
        String[] arr1 = StringUtils.split(version1, '.');
        String[] arr2 = StringUtils.split(version2, '.');
        for (int i = 0, len = Math.min(arr1.length, arr2.length); i < len; i++) {
            int v1 = Integer.valueOf(arr1[0]);
            int v2 = Integer.valueOf(arr2[0]);
            if (v1 < v2) {
                return false;
            }
        }
        return true;
    }
}
