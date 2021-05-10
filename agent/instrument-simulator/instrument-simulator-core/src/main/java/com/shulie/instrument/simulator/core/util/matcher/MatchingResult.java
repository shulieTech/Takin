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
package com.shulie.instrument.simulator.core.util.matcher;

import com.shulie.instrument.simulator.api.listener.ext.BuildingForListeners;
import com.shulie.instrument.simulator.core.util.matcher.structure.BehaviorStructure;

import java.util.*;

/**
 * 匹配结果
 */
public class MatchingResult {
    public final static MatchingResult UN_MATCHED = new MatchingResult(false);
    private boolean isMatched;

    public MatchingResult(boolean isMatched) {
        this.isMatched = isMatched;
    }

    private final Map<BehaviorStructure, Set<BuildingForListeners>> behaviorStructureListMap = new HashMap<BehaviorStructure, Set<BuildingForListeners>>();

    /**
     * 是否匹配成功
     *
     * @return TRUE:匹配成功;FALSE:匹配失败;
     */
    public boolean isMatched() {
        return isMatched;
    }

    public Map<BehaviorStructure, Set<BuildingForListeners>> getBehaviorStructureListMap() {
        return behaviorStructureListMap;
    }

    public MatchingResult addMatchingResult(BehaviorStructure behaviorStructure, Collection<BuildingForListeners> buildingForListeners) {
        Set<BuildingForListeners> set = behaviorStructureListMap.get(behaviorStructure);
        if (set == null) {
            set = new HashSet<BuildingForListeners>();
            behaviorStructureListMap.put(behaviorStructure, set);
        }
        set.addAll(buildingForListeners);
        return this;
    }

    public Map<String, Set<BuildingForListeners>> getBehaviorSignCodeMap() {
        if (behaviorStructureListMap.isEmpty()) {
            return Collections.EMPTY_MAP;
        }
        final Map<String, Set<BuildingForListeners>> behaviorSignCodes = new HashMap<String, Set<BuildingForListeners>>();
        for (Map.Entry<BehaviorStructure, Set<BuildingForListeners>> entry : behaviorStructureListMap.entrySet()) {
            behaviorSignCodes.put(entry.getKey().getSignCode(), entry.getValue());
        }
        return behaviorSignCodes;
    }
}
