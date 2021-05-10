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
package com.shulie.instrument.simulator.api.utils;

import java.util.Set;

/**
 * 解析工具类
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/9/24 6:02 下午
 */
public class ParseUtils {
    private final static String DEFAULT_PACKAGE = ".";

    /**
     * Get package name from a specified class name.
     *
     * @param className 类名
     * @return 返回包名
     */
    private static String getPackageName(String className) {
        int index = className.lastIndexOf('.');
        if (index > 0) {
            return className.substring(0, index);
        }
        return DEFAULT_PACKAGE;
    }

    /**
     * Parse package node startsWith * and endsWith *
     *
     * @param candidates candidate packages
     * @param prefixs    endswith * packages
     * @param suffixs    startsWith * packages
     * @param exactly    exactly matches packages
     */
    public static void parsePackagePrefixAndSuffix(Set<String> candidates, Set<String> prefixs,
                                                   Set<String> suffixs, Set<String> exactly) {
        for (String pkgPattern : candidates) {
            if (pkgPattern.endsWith("*")) {
                prefixs.add(getPackageName(pkgPattern));
            } else if (pkgPattern.startsWith("*")) {
                int index = pkgPattern.lastIndexOf('.');
                if (index > 0) {
                    pkgPattern = pkgPattern.substring(index + 1);
                }
                suffixs.add(pkgPattern);
            } else {
                exactly.add(pkgPattern);
            }
        }
    }

    /**
     * Parse resource exactly match and stem(with *)
     *
     * @param candidates  candidate resources
     * @param prefixStems with xxx/* resources match by prefix
     * @param suffixStems with *.xxx  *\/xxx resources match by suffix
     * @param resources   exactly match resources
     */
    public static void parseResourceAndStem(Set<String> candidates, Set<String> prefixStems,
                                            Set<String> suffixStems, Set<String> resources) {
        for (String candidate : candidates) {
            // do not support export *
            if (candidate.equals("*")) {
                continue;
            }
            if (candidate.endsWith("*")) {
                prefixStems.add(candidate.substring(0, candidate.length()
                        - 1));
            } else if (candidate.startsWith("*")) {
                suffixStems.add(candidate.substring(1));
            } else {
                resources.add(candidate);
            }
        }
    }
}
