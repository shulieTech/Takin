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

import java.util.HashMap;
import java.util.Map;

/**
 * 参数类型名称匹配组列表，一个方法的增强参数匹配会对应一个此对象
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2021/1/15 11:28 上午
 */
public class ArgumentTypeNameMatchGroupList {
    /**
     * 是否是精确匹配，如果精确匹配则参数个数与类型须与指定规则个数以及规则匹配上
     */
    private boolean exactlyMatched = true;

    /**
     * 匹配内容
     * Integer (参数索引号), ArgumentTypeNameMatch (参数类型名称规则匹配)
     */
    private Map<Integer, ArgumentTypeNameMatch> argumentTypeNameMatches = new HashMap<Integer, ArgumentTypeNameMatch>();

    public ArgumentTypeNameMatchGroupList() {
    }

    public void setExactlyMatched(boolean exactlyMatched) {
        this.exactlyMatched = exactlyMatched;
    }

    /**
     * 添加参数类型名称规则匹配
     * 使用此方法代表参数非精确匹配
     *
     * @param patternType 匹配类型
     * @param index       参数索引号
     * @param pattern     规则
     */
    public void addArgumentTypeNameMatch(PatternType patternType, int index, String pattern) {
        ArgumentTypeNameMatch match = new ArgumentTypeNameMatch(patternType, pattern);
        argumentTypeNameMatches.put(index, match);
        this.exactlyMatched = false;
    }

    /**
     * 添加参数类型名称规则匹配，使用此方法代表参数匹配为精确匹配，即参数个数与规则都必须得匹配上
     *
     * @param patternType
     * @param patterns
     */
    public void addArgumentTypeNameMatch(PatternType patternType, String... patterns) {
        if (ArrayUtils.isEmpty(patterns)) {
            return;
        }
        for (int i = 0, len = patterns.length; i < len; i++) {
            ArgumentTypeNameMatch match = new ArgumentTypeNameMatch(patternType, patterns[i]);
            argumentTypeNameMatches.put(i, match);
        }
    }

    /**
     * 与指定的参数类型列表进行规则匹配，如果匹配上则返回 true,如果匹配不上则返回 false
     *
     * @param parameterTypes
     * @return
     */
    public boolean matching(String[] parameterTypes) {
        /**
         * 如果是精确匹配，则需要匹配所有的索引下标参数
         * 如果非精确匹配则所有的输入规则都匹配上即可
         */
        if (exactlyMatched) {
            if (ArrayUtils.isEmpty(parameterTypes)) {
                if (!argumentTypeNameMatches.isEmpty()) {
                    return false;
                }
                return true;
            } else {
                if (argumentTypeNameMatches.isEmpty()) {
                    return false;
                }
                if (parameterTypes.length != argumentTypeNameMatches.size()) {
                    return false;
                }
                for (int i = 0, len = parameterTypes.length; i < len; i++) {
                    final ArgumentTypeNameMatch argumentTypeNameMatch = argumentTypeNameMatches.get(i);
                    if (argumentTypeNameMatch == null) {
                        return false;
                    }
                    if (!argumentTypeNameMatch.matching(parameterTypes[i])) {
                        return false;
                    }
                }
                return true;
            }
        } else {
            /**
             * 如果是非精确匹配时，只要配置了参数，则必须要匹配上
             */
            if (ArrayUtils.isEmpty(parameterTypes)) {
                if (!argumentTypeNameMatches.isEmpty()) {
                    return false;
                }
                return true;
            } else {
                /**
                 * 如果有参数而没有指定任何参数的匹配规则，则认为是匹配上的
                 */
                if (argumentTypeNameMatches.isEmpty()) {
                    return true;
                }
                for (Map.Entry<Integer, ArgumentTypeNameMatch> entry : argumentTypeNameMatches.entrySet()) {
                    int index = entry.getKey();
                    /**
                     * 如果指定的参数下标不存在，则匹配不上
                     */
                    if (parameterTypes.length <= index) {
                        return false;
                    }
                    String value = parameterTypes[index];
                    ArgumentTypeNameMatch argumentTypeNameMatch = entry.getValue();
                    if (argumentTypeNameMatch == null) {
                        continue;
                    }
                    if (!argumentTypeNameMatch.matching(value)) {
                        return false;
                    }
                }
                return true;
            }
        }
    }
}
