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
import com.shulie.instrument.simulator.api.util.ArrayUtils;
import com.shulie.instrument.simulator.core.util.matcher.structure.BehaviorStructure;
import com.shulie.instrument.simulator.core.util.matcher.structure.ClassStructure;

import java.util.*;

public abstract class GroupMatcher implements Matcher {

    final Matcher[] matcherArray;

    private GroupMatcher(final Matcher... matcherArray) {
        this.matcherArray = matcherArray;
    }

    public static final class Or extends GroupMatcher {

        public Or(Matcher... matcherArray) {
            super(matcherArray);
        }

        @Override
        public List<BuildingForListeners> getAllListeners() {
            List<BuildingForListeners> listeners = new ArrayList<BuildingForListeners>();
            if (ArrayUtils.isEmpty(matcherArray)) {
                return listeners;
            }
            for (Matcher matcher : matcherArray) {
                listeners.addAll(matcher.getAllListeners());
            }
            return listeners;
        }

        @Override
        public boolean preMatching(String javaClassName) {
            if (ArrayUtils.isEmpty(matcherArray)) {
                return false;
            }
            for (Matcher matcher : matcherArray) {
                if (matcher.preMatching(javaClassName)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public MatchingResult matching(final ClassStructure classStructure) {
            if (ArrayUtils.isEmpty(matcherArray)) {
                return MatchingResult.UN_MATCHED;
            }

            final MatchingResult result = new MatchingResult(true);
            final Map<BehaviorStructure, Set<BuildingForListeners>> found = new HashMap<BehaviorStructure, Set<BuildingForListeners>>();
            for (final Matcher subMatcher : matcherArray) {
                final MatchingResult subResult = subMatcher.matching(classStructure);

                // 只要有一次匹配失败，剩下的是取交集运算，所以肯定也没戏，就不用花这个计算了
                if (!subResult.isMatched()) {
                    return MatchingResult.UN_MATCHED;
                }

                for (Map.Entry<BehaviorStructure, Set<BuildingForListeners>> entry : subResult.getBehaviorStructureListMap().entrySet()) {
                    Set<BuildingForListeners> listeners = found.get(entry.getKey());
                    if (listeners == null) {
                        found.put(entry.getKey(), entry.getValue());
                    } else {
                        listeners.addAll(entry.getValue());
                    }
                }

                result.getBehaviorStructureListMap().putAll(found);
            }
            return result;
        }

    }

    public static final class And extends GroupMatcher {

        public And(Matcher... matcherArray) {
            super(matcherArray);
        }

        @Override
        public List<BuildingForListeners> getAllListeners() {
            List<BuildingForListeners> listeners = new ArrayList<BuildingForListeners>();
            if (ArrayUtils.isEmpty(matcherArray)) {
                return listeners;
            }
            for (Matcher matcher : matcherArray) {
                listeners.addAll(matcher.getAllListeners());
            }
            return listeners;
        }

        @Override
        public boolean preMatching(String javaClassName) {
            if (ArrayUtils.isEmpty(matcherArray)) {
                return false;
            }
            for (Matcher matcher : matcherArray) {
                if (!matcher.preMatching(javaClassName)) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public MatchingResult matching(ClassStructure classStructure) {
            boolean isFirst = true;
            final Map<BehaviorStructure, Set<BuildingForListeners>> found = new HashMap<BehaviorStructure, Set<BuildingForListeners>>();
            if (ArrayUtils.isEmpty(matcherArray)) {
                return MatchingResult.UN_MATCHED;
            }
            for (final Matcher subMatcher : matcherArray) {
                final MatchingResult subResult = subMatcher.matching(classStructure);

                // 只要有一次匹配失败，剩下的是取交集运算，所以肯定也没戏，就不用花这个计算了
                if (!subResult.isMatched()) {
                    return MatchingResult.UN_MATCHED;
                }

                if (isFirst) {
                    for (Map.Entry<BehaviorStructure, Set<BuildingForListeners>> entry : subResult.getBehaviorStructureListMap().entrySet()) {
                        Set<BuildingForListeners> listeners = found.get(entry.getKey());
                        if (listeners == null) {
                            found.put(entry.getKey(), entry.getValue());
                        } else {
                            listeners.addAll(entry.getValue());
                        }
                    }
                    isFirst = false;
                } else {
                    Set<BehaviorStructure> foundBehaviors = found.keySet();
                    Set<BehaviorStructure> subResultBehaviors = subResult.getBehaviorStructureListMap().keySet();
                    foundBehaviors.retainAll(subResultBehaviors);
                    final Iterator<Map.Entry<BehaviorStructure, Set<BuildingForListeners>>> iterator = found.entrySet().iterator();
                    while (iterator.hasNext()) {
                        Map.Entry<BehaviorStructure, Set<BuildingForListeners>> entry = iterator.next();
                        if (!foundBehaviors.contains(entry.getKey())) {
                            iterator.remove();
                        }
                    }
                    for (Map.Entry<BehaviorStructure, Set<BuildingForListeners>> entry : found.entrySet()) {
                        if (subResult.getBehaviorStructureListMap().containsKey(entry.getKey())) {
                            entry.getValue().addAll(subResult.getBehaviorStructureListMap().get(entry.getKey()));
                        }
                    }
                }
            }
            MatchingResult result = new MatchingResult(true);
            if (!found.isEmpty()) {
                result.getBehaviorStructureListMap().putAll(found);
            }
            return result;
        }

    }

}
