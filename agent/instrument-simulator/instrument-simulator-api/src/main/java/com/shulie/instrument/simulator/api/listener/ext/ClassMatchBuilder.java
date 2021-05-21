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


import com.shulie.instrument.simulator.api.filter.ClassDescriptor;
import com.shulie.instrument.simulator.api.filter.ExtFilter;
import com.shulie.instrument.simulator.api.filter.Filter;
import com.shulie.instrument.simulator.api.filter.MethodDescriptor;
import com.shulie.instrument.simulator.api.resource.ModuleEventWatcher;
import com.shulie.instrument.simulator.api.util.ArrayUtils;
import com.shulie.instrument.simulator.api.util.CollectionUtils;
import com.shulie.instrument.simulator.api.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.regex.Pattern.quote;

/**
 * 类匹配构造器实现
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/10/23 10:45 下午
 */
class ClassMatchBuilder implements IClassMatchBuilder {
    private final static String[] PATTERN_REGEX = new String[]{".*"};
    private final static String[] PATTERN_WILDCARD = new String[]{"*"};
    private final ModuleEventWatcher moduleEventWatcher;
    private final String[] pattern;
    private String[] superPatterns;
    private final PatternType patternType;
    private int withAccess = 0;
    private boolean isIncludeSubClasses = false;
    private boolean isIncludeBootstrap = true;
    private final PatternGroupList hasInterfaceTypes = new PatternGroupList();
    private final PatternGroupList hasAnnotationTypes = new PatternGroupList();
    private final List<BehaviorMatchBuilder> bfBehaviors = new ArrayList<BehaviorMatchBuilder>();
    private final List<Progress> progresses = new ArrayList<Progress>();

    /**
     * 构造类构建器
     *
     * @param pattern 类名匹配模版
     */
    ClassMatchBuilder(final ModuleEventWatcher moduleEventWatcher, final PatternType patternType, final String... pattern) {
        this.moduleEventWatcher = moduleEventWatcher;
        this.patternType = patternType;
        if (ArrayUtils.isEmpty(pattern)) {
            this.pattern = this.patternType == PatternType.WILDCARD ? PATTERN_WILDCARD : PATTERN_REGEX;
        } else {
            this.pattern = pattern;
        }
        this.superPatterns = this.patternType == PatternType.WILDCARD ? PATTERN_WILDCARD : PATTERN_REGEX;
    }

    @Override
    public IClassMatchBuilder includeBootstrap() {
        this.isIncludeBootstrap = true;
        return this;
    }

    @Override
    public IClassMatchBuilder isIncludeBootstrap(boolean isIncludeBootstrap) {
        this.isIncludeBootstrap = false;
        return this;
    }

    public ModuleEventWatcher getModuleEventWatcher() {
        return moduleEventWatcher;
    }

    public String[] getPattern() {
        return pattern;
    }

    public PatternType getPatternType() {
        return patternType;
    }

    public int getWithAccess() {
        return withAccess;
    }

    public boolean isIncludeSubClasses() {
        return isIncludeSubClasses;
    }

    public boolean isIncludeBootstrap() {
        return isIncludeBootstrap;
    }

    public PatternGroupList getHasInterfaceTypes() {
        return hasInterfaceTypes;
    }

    public PatternGroupList getHasAnnotationTypes() {
        return hasAnnotationTypes;
    }

    public List<BehaviorMatchBuilder> getBfBehaviors() {
        return bfBehaviors;
    }

    @Override
    public IClassMatchBuilder includeSubClasses() {
        this.isIncludeSubClasses = true;
        return this;
    }

    public String[] getSuperPatterns() {
        return superPatterns;
    }

    @Override
    public IClassMatchBuilder isIncludeSubClasses(boolean isIncludeSubClasses) {
        this.isIncludeSubClasses = isIncludeSubClasses;
        return this;
    }

    @Override
    public IClassMatchBuilder withAccess(final int access) {
        withAccess |= access;
        return this;
    }

    @Override
    public IClassMatchBuilder hasInterfaceTypes(final String... patterns) {
        if (patterns == null || patterns.length == 0) {
            return this;
        }
        hasInterfaceTypes.add(patternType, patterns);
        return this;
    }

    @Override
    public IClassMatchBuilder hasAnnotationTypes(final String... patterns) {
        if (patterns == null || patterns.length == 0) {
            return this;
        }
        hasAnnotationTypes.add(patternType, patterns);
        return this;
    }

    @Override
    public IClassMatchBuilder hasInterfaceTypes(final Class<?>... classes) {
        if (classes == null || classes.length == 0) {
            return this;
        }
        switch (patternType) {
            case REGEX:
                return hasInterfaceTypes(toRegexQuoteArray(StringUtil.getJavaClassNameArray(classes)));
            case WILDCARD:
            default:
                return hasInterfaceTypes(StringUtil.getJavaClassNameArray(classes));
        }
    }

    @Override
    public IClassMatchBuilder withSuperClass(String... patterns) {
        if (patterns == null || patterns.length == 0) {
            return this;
        }
        this.superPatterns = patterns;
        return this;
    }

    @Override
    public IClassMatchBuilder withSuperClass(Class... classes) {
        if (classes == null || classes.length == 0) {
            return this;
        }
        switch (patternType) {
            case REGEX:
                this.superPatterns = toRegexQuoteArray(StringUtil.getJavaClassNameArray(classes));
            case WILDCARD:
            default:
                this.superPatterns = StringUtil.getJavaClassNameArray(classes);
        }
        return this;
    }

    @Override
    public IClassMatchBuilder hasAnnotationTypes(final Class<?>... classes) {
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
        return CollectionUtils.add(bfBehaviors, new BehaviorMatchBuilder(this, patternType, pattern));
    }

    @Override
    public IBehaviorMatchBuilder onBehavior(String... patterns) {
        if (patterns == null || patterns.length == 0) {
            return onAnyBehavior();
        }
        return CollectionUtils.add(bfBehaviors, new BehaviorMatchBuilder(this, patternType, patterns));
    }

    @Override
    public IBehaviorMatchBuilder onAnyBehavior(String pattern) {
        return CollectionUtils.add(bfBehaviors, new BehaviorMatchBuilder(this, patternType, pattern).withAnyParameters());
    }

    @Override
    public IBehaviorMatchBuilder onAnyBehavior(String... patterns) {
        if (patterns == null || patterns.length == 0) {
            return onAnyBehavior();
        }
        return CollectionUtils.add(bfBehaviors, new BehaviorMatchBuilder(this, patternType, patterns).withAnyParameters());
    }

    @Override
    public EventWatcher onWatch() {
        return build(toProgressGroup(this.progresses));
    }

    @Override
    public IClassMatchBuilder withProgress(Progress progress) {
        if (null != progress) {
            progresses.add(progress);
        }
        return this;
    }

    private EventWatcher build(final Progress progress) {

        final int watchId = moduleEventWatcher.watch(
                toEventWatchCondition(),
                progress
        );

        return new EventWatcher() {

            final List<Progress> progresses = new ArrayList<Progress>();

            @Override
            public int getWatchId() {
                return watchId;
            }

            @Override
            public IUnWatchingMatchBuilder withProgress(Progress progress) {
                if (null != progress) {
                    progresses.add(progress);
                }
                return this;
            }

            @Override
            public void onUnWatched() {
                moduleEventWatcher.delete(watchId, toProgressGroup(progresses));
            }

        };
    }

    private EventWatchCondition toEventWatchCondition() {
        final List<Filter> filters = new ArrayList<Filter>();

        final Filter filter = new ExtFilter() {

            @Override
            public boolean isIncludeSubClasses() {
                return isIncludeSubClasses;
            }

            @Override
            public boolean isIncludeBootstrap() {
                return isIncludeBootstrap;
            }

            @Override
            public boolean doClassNameFilter(String javaClassName) {
                return patternMatching(javaClassName, pattern, patternType);
            }

            @Override
            public boolean doClassFilter(ClassDescriptor classDescriptor) {
                if ((classDescriptor.getAccess() & getWithAccess()) != getWithAccess()) {
                    return false;
                }

                if (!patternMatching(classDescriptor.getClassName(), pattern, patternType)) {
                    return false;
                }

                if (!patternMatching(classDescriptor.getSuperClassTypeJavaClassName(), superPatterns, patternType)) {
                    return false;
                }

                if (getHasInterfaceTypes().isNotEmpty()) {
                    if (!getHasInterfaceTypes().patternWith(classDescriptor.getInterfaceTypeJavaClassNameArray())) {
                        return false;
                    }
                }
                if (getHasAnnotationTypes().isNotEmpty()) {
                    if (!getHasAnnotationTypes().patternWith(classDescriptor.getAnnotationTypeJavaClassNameArray())) {
                        return false;
                    }
                }

                return true;
            }

            @Override
            public List<BuildingForListeners> doMethodFilter(MethodDescriptor methodDescriptor) {
                // nothing to matching
                if (bfBehaviors.isEmpty()) {
                    return Collections.EMPTY_LIST;
                }

                List<BuildingForListeners> list = new ArrayList<BuildingForListeners>();
                // matching any behavior
                for (final BehaviorMatchBuilder bfBehavior : bfBehaviors) {
                    final boolean matches = matches(methodDescriptor, bfBehavior);
                    if ((bfBehavior.isWithNot() && matches) || (!bfBehavior.isWithNot() && !matches)) {
                        continue;
                    }
                    list.addAll(bfBehavior.getListeners());
                }//for

                // non matched
                return list;
            }

            private boolean matches(MethodDescriptor methodDescriptor, BehaviorMatchBuilder bfBehavior) {
                if ((methodDescriptor.getAccess() & bfBehavior.getWithAccess()) != bfBehavior.getWithAccess()) {
                    return false;
                }
                if (!patternMatching(methodDescriptor.getMethodName(), bfBehavior.getPattern(), patternType)) {
                    return false;
                }
                if (!bfBehavior.getWithParameterTypes().matching(methodDescriptor.getParameterTypeJavaClassNameArray())) {
                    return false;
                }
                if (bfBehavior.getHasExceptionTypes().isNotEmpty()) {
                    if (!bfBehavior.getHasExceptionTypes().patternWith(methodDescriptor.getThrowsTypeJavaClassNameArray())) {
                        return false;
                    }
                }
                if (bfBehavior.getHasAnnotationTypes().isNotEmpty()) {
                    if (!bfBehavior.getHasAnnotationTypes().patternWith(methodDescriptor.getAnnotationTypeJavaClassNameArray())) {
                        return false;
                    }
                }
                return true;
            }

            @Override
            public List<BuildingForListeners> getAllListeners() {
                List<BuildingForListeners> listeners = new ArrayList<BuildingForListeners>();
                for (BehaviorMatchBuilder buildingForBehavior : bfBehaviors) {
                    listeners.addAll(buildingForBehavior.getListeners());
                }
                return listeners;
            }
        };//filter

        filters.add(filter);
        return new EventWatchCondition() {
            @Override
            public Filter[] getOrFilterArray() {
                return filters.toArray(new Filter[0]);
            }
        };
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
                    if (".*".equals(p)) {
                        return true;
                    }
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

    private ProgressGroup toProgressGroup(final List<Progress> progresses) {
        if (progresses.isEmpty()) {
            return null;
        }
        return new ProgressGroup(progresses);
    }

    @Override
    public IBehaviorMatchBuilder onAnyBehavior() {
        switch (patternType) {
            case REGEX:
                return CollectionUtils.add(bfBehaviors, new BehaviorMatchBuilder(this, patternType, ".*").withAnyParameters());
            case WILDCARD:
            default:
                return CollectionUtils.add(bfBehaviors, new BehaviorMatchBuilder(this, patternType, "*").withAnyParameters());
        }
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
