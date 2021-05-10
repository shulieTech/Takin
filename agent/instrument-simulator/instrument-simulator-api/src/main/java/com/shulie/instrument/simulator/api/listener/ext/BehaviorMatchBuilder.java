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

import com.shulie.instrument.simulator.api.event.EventType;
import com.shulie.instrument.simulator.api.listener.Listeners;
import com.shulie.instrument.simulator.api.util.StringUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.shulie.instrument.simulator.api.event.EventType.*;
import static java.util.regex.Pattern.quote;

/**
 * 行为匹配构造器实现
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/10/23 10:45 下午
 */
class BehaviorMatchBuilder implements IBehaviorMatchBuilder {

    private final ClassMatchBuilder bfClass;
    private final PatternType patternType;
    private final String[] pattern;
    private int withAccess = 0;
    private boolean withNot;

    private final ArgumentTypeNameMatchGroupList withParameterTypes = new ArgumentTypeNameMatchGroupList();
    private final PatternGroupList hasExceptionTypes = new PatternGroupList();
    private final PatternGroupList hasAnnotationTypes = new PatternGroupList();
    private final List<BuildingForListeners> listeners = new ArrayList<BuildingForListeners>();
    private final Set<EventType> eventEventTypeSet = new HashSet<EventType>();

    BehaviorMatchBuilder(final ClassMatchBuilder bfClass,
                         final PatternType patternType,
                         final String... pattern) {
        this.bfClass = bfClass;
        this.patternType = patternType;
        this.pattern = pattern;
        this.eventEventTypeSet.add(BEFORE);
        this.eventEventTypeSet.add(RETURN);
        this.eventEventTypeSet.add(THROWS);
    }

    /**
     * 匹配任何参数
     *
     * @return
     */
    public BehaviorMatchBuilder withAnyParameters() {
        withParameterTypes.setExactlyMatched(false);
        return this;
    }

    public ClassMatchBuilder getBfClass() {
        return bfClass;
    }

    public String[] getPattern() {
        return pattern;
    }

    public int getWithAccess() {
        return withAccess;
    }

    public boolean isWithNot() {
        return withNot;
    }

    public ArgumentTypeNameMatchGroupList getWithParameterTypes() {
        return withParameterTypes;
    }

    public PatternGroupList getHasExceptionTypes() {
        return hasExceptionTypes;
    }

    public PatternGroupList getHasAnnotationTypes() {
        return hasAnnotationTypes;
    }

    public List<BuildingForListeners> getListeners() {
        return listeners;
    }

    @Override
    public IBehaviorMatchBuilder withAccess(final int access) {
        withAccess |= access;
        return this;
    }

    @Override
    public IBehaviorMatchBuilder withAnyParameterTypes() {
        withParameterTypes.setExactlyMatched(false);
        return this;
    }

    @Override
    public IBehaviorMatchBuilder withEmptyParameterTypes() {
        return this;
    }

    @Override
    public IBehaviorMatchBuilder withParameterTypes(final String... patterns) {
        if (patterns == null || patterns.length == 0) {
            return this;
        }
        withParameterTypes.addArgumentTypeNameMatch(patternType, patterns);
        return this;
    }

    @Override
    public IBehaviorMatchBuilder withParameterTypes(final Class<?>... classes) {
        if (classes == null || classes.length == 0) {
            return this;
        }
        switch (patternType) {
            case REGEX:
                return withParameterTypes(toRegexQuoteArray(StringUtil.getJavaClassNameArray(classes)));
            case WILDCARD:
            default:
                return withParameterTypes(StringUtil.getJavaClassNameArray(classes));
        }
    }

    @Override
    public IBehaviorMatchBuilder withParameterType(int index, String pattern) {
        if (StringUtil.isEmpty(pattern)) {
            return this;
        }
        withParameterTypes.addArgumentTypeNameMatch(patternType, index, pattern);
        return this;
    }

    @Override
    public IBehaviorMatchBuilder withParameterType(int index, Class<?> clazz) {
        if (clazz == null) {
            return this;
        }
        switch (patternType) {
            case REGEX:
                return withParameterType(index, quote(StringUtil.getJavaClassName(clazz)));
            case WILDCARD:
            default:
                return withParameterType(index, StringUtil.getJavaClassName(clazz));
        }
    }

    @Override
    public IBehaviorMatchBuilder hasExceptionTypes(final String... patterns) {
        if (patterns == null || patterns.length == 0) {
            return this;
        }
        hasExceptionTypes.add(patternType, patterns);
        return this;
    }

    @Override
    public IBehaviorMatchBuilder hasExceptionTypes(final Class<?>... classes) {
        if (classes == null || classes.length == 0) {
            return this;
        }
        switch (patternType) {
            case REGEX:
                return hasExceptionTypes(toRegexQuoteArray(StringUtil.getJavaClassNameArray(classes)));
            case WILDCARD:
            default:
                return hasExceptionTypes(StringUtil.getJavaClassNameArray(classes));
        }
    }

    @Override
    public IBehaviorMatchBuilder hasAnnotationTypes(final String... patterns) {
        if (patterns == null || patterns.length == 0) {
            return this;
        }
        hasAnnotationTypes.add(patternType, patterns);
        return this;
    }

    @Override
    public IBehaviorMatchBuilder hasAnnotationTypes(final Class<?>... classes) {
        if (classes == null || classes.length == 0) {
            return this;
        }
        switch (patternType) {
            case REGEX:
                return hasAnnotationTypes(toRegexQuoteArray(StringUtil.getJavaClassNameArray(classes)));
            case WILDCARD:
            default:
                return hasAnnotationTypes(StringUtil.getJavaClassNameArray(classes));
        }
    }

    @Override
    public IBehaviorMatchBuilder onBehavior(final String pattern) {
        return bfClass.onBehavior(pattern);
    }

    @Override
    public IBehaviorMatchBuilder withNot() {
        withNot = true;
        return this;
    }

    @Override
    public IBehaviorMatchBuilder withInvoke() {
        eventEventTypeSet.add(BEFORE);
        eventEventTypeSet.add(RETURN);
        eventEventTypeSet.add(THROWS);
        return this;
    }

    @Override
    public IBehaviorMatchBuilder withCall() {
        eventEventTypeSet.add(CALL_BEFORE);
        eventEventTypeSet.add(CALL_RETURN);
        eventEventTypeSet.add(CALL_THROWS);
        return this;
    }

    @Override
    public IBehaviorMatchBuilder withLine() {
        eventEventTypeSet.add(LINE);
        return this;
    }

    @Override
    public IBehaviorMatchBuilder onListener(Listeners listener) {
        BuildingForListeners buildingForListeners = new BuildingForListeners(listener, eventEventTypeSet.toArray(EMPTY));
        this.listeners.add(buildingForListeners);
        return this;
    }

    @Override
    public IClassMatchBuilder onClass() {
        return bfClass;
    }

    @Override
    public IBehaviorMatchBuilder onListener(Listeners listener, EventType... eventEventTypeArray) {

        Set<EventType> eventTypes = new HashSet<EventType>();
        if (eventEventTypeArray != null) {
            for (EventType eventType : eventEventTypeArray) {
                eventTypes.add(eventType);
            }
        }
        eventTypes.addAll(this.eventEventTypeSet);
        BuildingForListeners buildingForListeners = new BuildingForListeners(listener, eventTypes.toArray(EMPTY));
        this.listeners.add(buildingForListeners);
        return this;
    }

    @Override
    public void onWatching(Listeners listeners, WatchCallback watchCallback, EventType... eventEventTypeArray) throws Throwable {
        IWatchingMatchBuilder buildingForWatching = new WatchingMatchBuilder(bfClass, bfClass.getModuleEventWatcher(), watchCallback, patternType);
        buildingForWatching.onWatching(listeners, eventEventTypeArray);
    }

    /**
     * 将字符串数组转换为正则表达式字符串数组
     *
     * @param stringArray 目标字符串数组
     * @return 正则表达式字符串数组
     */
    private static String[] toRegexQuoteArray(final String[] stringArray) {
        if (null == stringArray) {
            return null;
        }
        final String[] regexQuoteArray = new String[stringArray.length];
        for (int index = 0; index < stringArray.length; index++) {
            regexQuoteArray[index] = quote(stringArray[index]);
        }
        return regexQuoteArray;
    }

}
