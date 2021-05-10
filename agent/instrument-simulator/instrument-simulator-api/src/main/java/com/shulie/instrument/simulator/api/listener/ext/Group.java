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
package com.shulie.instrument.simulator.api.listener.ext;


import com.shulie.instrument.simulator.api.util.ArrayUtils;
import com.shulie.instrument.simulator.api.util.StringUtil;

/**
 * 模式匹配组
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/10/23 10:45 下午
 */
class Group {

    final String[] patternArray;
    final PatternType patternType;

    Group(PatternType patternType, String[] patternArray) {
        this.patternType = patternType;
        this.patternArray = ArrayUtils.isEmpty(patternArray)
                ? new String[0]
                : patternArray;
    }

    /*
     * stringArray中任意字符串能匹配上匹配模式
     */
    boolean anyMatching(final String[] stringArray,
                        final String pattern) {
        if (ArrayUtils.isEmpty(stringArray)) {
            return false;
        }
        for (final String string : stringArray) {
            if (patternMatching(string, pattern, patternType)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 模式匹配
     *
     * @param string      目标字符串
     * @param pattern     模式字符串
     * @param patternType 匹配模式
     * @return TRUE:匹配成功 / FALSE:匹配失败
     */
    private static boolean patternMatching(final String string,
                                           final String pattern,
                                           final PatternType patternType) {
        switch (patternType) {
            case WILDCARD:
                return StringUtil.matching(string, pattern);
            case REGEX:
                return string.matches(pattern);
            default:
                return false;
        }
    }

    /*
     * 匹配模式组中所有匹配模式都在目标中存在匹配通过的元素
     * 要求匹配组中每一个匹配项都在stringArray中存在匹配的字符串
     */
    boolean matchingHas(final String[] stringArray) {
        if (ArrayUtils.isEmpty(patternArray) && ArrayUtils.isEmpty(stringArray)) {
            return true;
        }

        int length = stringArray == null ? 0 : stringArray.length;
        int length0 = patternArray == null ? 0 : patternArray.length;
        if (length != length0) {
            return false;
        }

        for (int i = 0, len = patternArray.length; i < len; i++) {
            if (!patternMatching(stringArray[i], patternArray[i], patternType)) {
                return false;
            }
        }
        return true;
    }

    /*
     * 匹配模式组中所有匹配模式都在目标中对应数组位置存在匹配通过元素
     * 要求字符串数组每一个位对应模式匹配组的每一个模式匹配表达式
     * stringArray[0] matching wildcardArray[0]
     * stringArray[1] matching wildcardArray[1]
     * stringArray[2] matching wildcardArray[2]
     *     ...
     * stringArray[n] matching wildcardArray[n]
     */
    boolean matchingWith(final String[] stringArray) {

        // 长度不一样就不用不配了
        int length;
        if ((length = ArrayUtils.getLength(stringArray)) != ArrayUtils.getLength(patternArray)) {
            return false;
        }
        // 长度相同则逐个位置比较，只要有一个位置不符，则判定不通过
        for (int index = 0; index < length; index++) {
            if (!patternMatching(stringArray[index], patternArray[index], patternType)) {
                return false;
            }
        }
        // 所有位置匹配通过，判定匹配成功
        return true;
    }

}
