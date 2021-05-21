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
import com.shulie.instrument.simulator.api.filter.*;
import com.shulie.instrument.simulator.api.listener.Listeners;
import com.shulie.instrument.simulator.api.resource.ModuleEventWatcher;
import com.shulie.instrument.simulator.api.util.StringUtil;

import java.util.*;

import static com.shulie.instrument.simulator.api.event.EventType.*;

/**
 * 正在观察匹配构造器的实现
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/10/23 10:42 下午
 */
class WatchingMatchBuilder implements IWatchingMatchBuilder {
    private final ClassMatchBuilder bfClass;
    private final Set<EventType> eventEventTypeSet = new HashSet<EventType>();
    private final List<Progress> progresses = new ArrayList<Progress>();

    private final ModuleEventWatcher moduleEventWatcher;
    private final PatternType patternType;
    private final WatchCallback watchCallback;

    public WatchingMatchBuilder(ClassMatchBuilder bfClass, ModuleEventWatcher moduleEventWatcher, WatchCallback watchCallback, final PatternType patternType) {
        this.bfClass = bfClass;
        this.moduleEventWatcher = moduleEventWatcher;
        this.watchCallback = watchCallback;
        this.patternType = patternType;
    }

    @Override
    public IWatchingMatchBuilder withProgress(Progress progress) {
        if (null != progress) {
            progresses.add(progress);
        }
        return this;
    }

    @Override
    public IWatchingMatchBuilder withCall() {
        eventEventTypeSet.add(CALL_BEFORE);
        eventEventTypeSet.add(CALL_RETURN);
        eventEventTypeSet.add(CALL_THROWS);
        return this;
    }

    @Override
    public IWatchingMatchBuilder withLine() {
        eventEventTypeSet.add(LINE);
        return this;
    }

    @Override
    public void onWatching(Listeners listeners) throws Throwable {
        eventEventTypeSet.add(BEFORE);
        eventEventTypeSet.add(RETURN);
        eventEventTypeSet.add(THROWS);
        eventEventTypeSet.add(IMMEDIATELY_RETURN);
        eventEventTypeSet.add(IMMEDIATELY_THROWS);
        watching(
                toProgressGroup(progresses)
        );
    }

    @Override
    public void onWatching(Listeners listeners, EventType... eventEventTypeArray) throws Throwable {
        watching(toProgressGroup(progresses));
    }

    private void watching(final Progress progress) throws Throwable {

        moduleEventWatcher.watching(
                toFilter(),
                progress,
                watchCallback,
                progress
        );
    }

    private Filter toFilter() {
        return new ExtFilter() {

            @Override
            public boolean isIncludeSubClasses() {
                return bfClass.isIncludeSubClasses();
            }

            @Override
            public boolean isIncludeBootstrap() {
                return bfClass.isIncludeBootstrap();
            }

            @Override
            public boolean doClassNameFilter(String javaClassName) {
                /**
                 * 类的类型为 Class 并且类名匹配或者是非 Class 类型
                 */
                return patternMatching(javaClassName, bfClass.getPattern(), patternType);
            }

            @Override
            public boolean doClassFilter(ClassDescriptor classDescriptor) {
                if ((classDescriptor.getAccess() & bfClass.getWithAccess()) != bfClass.getWithAccess()) {
                    return false;
                }
                if (!patternMatching(classDescriptor.getClassName(), bfClass.getPattern(), patternType)) {
                    return false;
                }

                if (!patternMatching(classDescriptor.getSuperClassTypeJavaClassName(), bfClass.getSuperPatterns(), patternType)) {
                    return false;
                }

                if (bfClass.getHasInterfaceTypes().isNotEmpty()) {
                    if (!bfClass.getHasInterfaceTypes().patternWith(classDescriptor.getInterfaceTypeJavaClassNameArray())) {
                        return false;
                    }
                }
                if (bfClass.getHasAnnotationTypes().isNotEmpty()) {
                    if (!bfClass.getHasAnnotationTypes().patternWith(classDescriptor.getAnnotationTypeJavaClassNameArray())) {
                        return false;
                    }
                }
                return true;
            }

            @Override
            public List<BuildingForListeners> doMethodFilter(MethodDescriptor methodDescriptor) {
                // nothing to matching
                if (bfClass.getBfBehaviors().isEmpty()) {
                    return Collections.EMPTY_LIST;
                }

                List<BuildingForListeners> list = new ArrayList<BuildingForListeners>();
                // matching any behavior
                for (final BehaviorMatchBuilder bfBehavior : bfClass.getBfBehaviors()) {
                    if ((bfBehavior.isWithNot() && matches(methodDescriptor, bfBehavior)) || (!bfBehavior.isWithNot() && !matches(methodDescriptor, bfBehavior))) {
                        continue;
                    }
                    list.addAll(bfBehavior.getListeners());
                }//for

                // non matched
                return list;
            }

            private boolean matches(MethodDescriptor methodDescriptor, BehaviorMatchBuilder bfBehavior) {
                if ((methodDescriptor.getAccess() & bfBehavior.getWithAccess()) != bfBehavior.getWithAccess()) {
                    return true;
                }
                if (!patternMatching(methodDescriptor.getMethodName(), bfBehavior.getPattern(), patternType)) {
                    return true;
                }
                if (!bfBehavior.getWithParameterTypes().matching(methodDescriptor.getParameterTypeJavaClassNameArray())) {
                    return true;
                }
                if (bfBehavior.getHasExceptionTypes().isNotEmpty()) {
                    if (!bfBehavior.getHasExceptionTypes().patternWith(methodDescriptor.getThrowsTypeJavaClassNameArray())) {
                        return true;
                    }
                }
                if (bfBehavior.getHasAnnotationTypes().isNotEmpty()) {
                    if (!bfBehavior.getHasAnnotationTypes().patternWith(methodDescriptor.getAnnotationTypeJavaClassNameArray())) {
                        return true;
                    }
                }
                return false;
            }

            @Override
            public List<BuildingForListeners> getAllListeners() {
                List<BuildingForListeners> listeners = new ArrayList<BuildingForListeners>();
                for (BehaviorMatchBuilder buildingForBehavior : bfClass.getBfBehaviors()) {
                    listeners.addAll(buildingForBehavior.getListeners());
                }
                return listeners;
            }
        };//filter

    }

    private Filter makeExtFilter(final Filter filter,
                                 final ClassMatchBuilder bfClass) {
        return ExtFilterFactory.make(
                filter,
                bfClass.isIncludeSubClasses(),
                bfClass.isIncludeBootstrap()
        );
    }

    private ProgressGroup toProgressGroup(final List<Progress> progresses) {
        if (progresses.isEmpty()) {
            return null;
        }
        return new ProgressGroup(progresses);
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
     * 模式匹配
     *
     * @param string      目标字符串
     * @param patterns    模式字符串
     * @param patternType 匹配模式
     * @return TRUE:匹配成功 / FALSE:匹配失败
     */
    private static boolean patternMatching(final String string,
                                           final String[] patterns,
                                           final PatternType patternType) {
        switch (patternType) {
            case WILDCARD:
                if (patterns == null || patterns.length == 0) {
                    return false;
                }
                for (String p : patterns) {
                    boolean matches = StringUtil.matching(string, p);
                    if (matches) {
                        return true;
                    }
                }
                return false;
            case REGEX:
                if (patterns == null || patterns.length == 0) {
                    return false;
                }
                for (String p : patterns) {
                    boolean matches = string.matches(p);
                    if (matches) {
                        return true;
                    }
                }
                return false;
            default:
                return false;
        }
    }

}
