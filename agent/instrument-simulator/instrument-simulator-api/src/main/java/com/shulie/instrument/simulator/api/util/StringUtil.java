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
package com.shulie.instrument.simulator.api.util;

import java.util.*;

/**
 * 字符串操作工具类
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/10/23 10:45 下午
 */
public class StringUtil {

    public static boolean startsWith(String str1, String str2) {
        if (str1 == null || str2 == null) {
            return false;
        }
        return str1.startsWith(str2);
    }

    public static boolean endsWith(String str1, String str2) {
        if (str1 == null || str2 == null) {
            return false;
        }
        return str1.endsWith(str2);
    }

    public static boolean equals(String str1, String str2) {
        if (str1 == str2) {
            return true;
        }
        if (str1 == null || str2 == null) {
            return false;
        }
        return str1.equals(str2);
    }

    /**
     * 提取Class的类名
     * <p>
     * 默认情况下提取{@link Class#getCanonicalName()}，但在遇到不可见类时提取{@link Class#getName()}
     * </p>
     *
     * @param clazz Class类
     * @return Class类名
     */
    public static String getJavaClassName(final Class<?> clazz) {
        return clazz.isArray()
                ? clazz.getCanonicalName()
                : clazz.getName();
    }

    /**
     * 提取Class数组的类名数组
     *
     * @param classArray Class数组
     * @return 类名数组
     */
    public static String[] getJavaClassNameArray(final Class<?>[] classArray) {
        if (ArrayUtils.isEmpty(classArray)) {
            return null;
        }
        final String[] javaClassNameArray = new String[classArray.length];
        for (int index = 0; index < classArray.length; index++) {
            javaClassNameArray[index] = getJavaClassName(classArray[index]);
        }
        return javaClassNameArray;
    }

    /**
     * 字符串是否为空
     * <p>
     * null和""均为空
     * </p>
     *
     * @param string 目标字符串
     * @return TRUE:空字符串;FALSE:非空字符串
     */
    public static boolean isEmpty(String string) {
        return null == string
                || string.isEmpty();
    }

    /**
     * 通配符表达式匹配
     * <p>
     * 通配符是一种特殊语法，主要有星号(*)和问号(?)组成，在Simulator中主要用来模糊匹配类名和方法名。
     * 比如：java.lang.String，可以被"*String"所匹配
     * </p>
     * <ul>
     * <li>(null) matching (null) == false</li>
     * <li>    ANY matching ("*") == true</li>
     * </ul>
     *
     * @param string   目标字符串
     * @param wildcard 通配符匹配模版
     * @return true:目标字符串符合匹配模版;false:目标字符串不符合匹配模版
     */
    public static boolean matching(final String string, final String wildcard) {
        if ("*".equals(wildcard)) {
            return true;
        }
        if (wildcard == null || string == null) {
            return false;
        }
        /**
         * 如果没有通配符则全匹配
         */
        if (wildcard.indexOf("*") == -1) {
            return wildcard.equals(string);
        }
        return null != wildcard
                && null != string
                && matching(string, wildcard, 0, 0);
    }

    /**
     * Internal matching recursive function.
     */
    private static boolean matching(String string, String wildcard, int stringStartNdx, int patternStartNdx) {
        int pNdx = patternStartNdx;
        int sNdx = stringStartNdx;
        int pLen = wildcard.length();
        if (pLen == 1) {
            if (wildcard.charAt(0) == '*') {     // speed-up
                return true;
            }
        }
        int sLen = string.length();
        boolean nextIsNotWildcard = false;

        while (true) {

            // check if end of string and/or pattern occurred
            if ((sNdx >= sLen)) {   // end of string still may have pending '*' callback pattern
                while ((pNdx < pLen) && (wildcard.charAt(pNdx) == '*')) {
                    pNdx++;
                }
                return pNdx >= pLen;
            }
            if (pNdx >= pLen) {         // end of pattern, but not end of the string
                return false;
            }
            char p = wildcard.charAt(pNdx);    // pattern char

            // perform logic
            if (!nextIsNotWildcard) {

                if (p == '\\') {
                    pNdx++;
                    nextIsNotWildcard = true;
                    continue;
                }
                if (p == '?') {
                    sNdx++;
                    pNdx++;
                    continue;
                }
                if (p == '*') {
                    char pnext = 0;           // next pattern char
                    if (pNdx + 1 < pLen) {
                        pnext = wildcard.charAt(pNdx + 1);
                    }
                    if (pnext == '*') {         // double '*' have the same effect as one '*'
                        pNdx++;
                        continue;
                    }
                    int i;
                    pNdx++;

                    // find recursively if there is any substring from the end of the
                    // line that matches the rest of the pattern !!!
                    for (i = string.length(); i >= sNdx; i--) {
                        if (matching(string, wildcard, i, pNdx)) {
                            return true;
                        }
                    }
                    return false;
                }
            } else {
                nextIsNotWildcard = false;
            }

            // check if pattern char and string char are equals
            if (p != string.charAt(sNdx)) {
                return false;
            }

            // everything matches for now, continue
            sNdx++;
            pNdx++;
        }
    }

    public static Set<String> strToSet(String str, String delimiter) {
        return new LinkedHashSet<String>(strToList(str, delimiter));
    }

    public static List<String> strToList(String str, String delimiter) {
        if (str == null || str.isEmpty()) {
            return Collections.emptyList();
        }
        List<String> stringList = new ArrayList<String>();
        for (String s : str.split(delimiter)) {
            stringList.add(s.trim());
        }
        return stringList;
    }
}
