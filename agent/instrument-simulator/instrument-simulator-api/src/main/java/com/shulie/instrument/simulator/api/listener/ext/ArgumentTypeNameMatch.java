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

import com.shulie.instrument.simulator.api.util.StringUtil;

/**
 * 参数类型名称匹配规则
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2021/1/15 11:28 上午
 */
public class ArgumentTypeNameMatch {

    /**
     * 匹配类型
     *
     * @see PatternType
     */
    private PatternType patternType;

    /**
     * 匹配模式，支持两种规则，一种是通配符，一种是正则表达式
     */
    private String pattern;

    public ArgumentTypeNameMatch(PatternType patternType, String pattern) {
        this.patternType = patternType;
        this.pattern = pattern;
    }

    /**
     * 匹配规则
     *
     * @param value
     * @return
     */
    public boolean matching(final String value) {
        return patternMatching(value, pattern, patternType);
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
}
