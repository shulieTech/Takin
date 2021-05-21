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

import java.util.ArrayList;
import java.util.List;

/**
 * 模式匹配组列表
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/10/23 10:47 下午
 */
class PatternGroupList {

    final List<Group> groups = new ArrayList<Group>();

    /*
     * 添加模式匹配组
     */
    void add(PatternType patternType, String... patternArray) {
        groups.add(new Group(patternType, patternArray));
    }

    void add(List<Group> groups) {
        groups.addAll(groups);
    }

    void add(PatternGroupList groupList) {
        groups.addAll(groupList.groups);
    }

    boolean isEmpty() {
        return groups.isEmpty();
    }

    boolean isNotEmpty() {
        return !isEmpty();
    }

    /**
     * 模式匹配With
     *
     * @param stringArray
     */
    boolean patternWith(final String[] stringArray) {

        // 如果模式匹配组为空，说明不参与本次匹配
        if (groups.isEmpty()) {
            return true;
        }

        for (final Group group : groups) {
            if (group.matchingWith(stringArray)) {
                return true;
            }
        }
        return false;
    }

}
