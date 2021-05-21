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

    /**
     * 匹配模式组中所有匹配模式都在目标中存在匹配通过的元素
     * 要求匹配组中每一个匹配项都在stringArray中存在匹配的字符串
     * <p>
     * 如果当前匹配的规则为两个，待匹配为三个，则三个中只要有任意两个匹配即可
     * 如果当前匹配的规则为两个，待匹配为一个，除非规则相同，否则肯定匹配不上
     *
     * @param stringArray 待匹配的数组
     * @return 匹配成功返回 true,否则返回 false
     */
    boolean matchingWith(final String[] stringArray) {
        if (ArrayUtils.isEmpty(patternArray)) {
            return true;
        }

        if (ArrayUtils.isEmpty(stringArray)) {
            return false;
        }

        for (int i = 0, len = patternArray.length; i < len; i++) {
            if (!anyMatching(stringArray, patternArray[i])) {
                return false;
            }
        }
        return true;
    }

}
