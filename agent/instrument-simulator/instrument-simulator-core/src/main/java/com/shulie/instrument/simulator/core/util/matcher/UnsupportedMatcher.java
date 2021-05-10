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

import com.shulie.instrument.simulator.api.annotation.Stealth;
import com.shulie.instrument.simulator.api.listener.ext.BuildingForListeners;
import com.shulie.instrument.simulator.core.util.matcher.structure.Access;
import com.shulie.instrument.simulator.core.util.matcher.structure.BehaviorStructure;
import com.shulie.instrument.simulator.core.util.matcher.structure.ClassStructure;
import com.shulie.instrument.simulator.core.util.matcher.structure.ClassStructureFactory;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import java.util.*;

/**
 * 不支持的类匹配
 */
public class UnsupportedMatcher implements Matcher {

    private final ClassLoader loader;
    private final boolean isEnableUnsafe;

    public UnsupportedMatcher(final ClassLoader loader,
                              final boolean isEnableUnsafe) {
        this.loader = loader;
        this.isEnableUnsafe = isEnableUnsafe;
    }

    /**
     * 是否因Simulator容器本身缺陷所暂时无法支持的类
     *
     * @param classStructure 类结构
     * @return 返回是否支持
     */
    private boolean isUnsupportedClass(final ClassStructure classStructure) {
        return containsAny(
                classStructure.getJavaClassName(),
                "$$Lambda$",
                "$$FastClassBySpringCGLIB$$",
                "$$EnhancerBySpringCGLIB$$",
                "$$EnhancerByCGLIB$$",
                "$$FastClassByCGLIB$$"
        );
    }

    public static boolean containsAny(final String cs, final String... searchCharSequences) {
        if (StringUtils.isEmpty(cs) || ArrayUtils.isEmpty(searchCharSequences)) {
            return false;
        }
        for (final String searchCharSequence : searchCharSequences) {
            if (StringUtils.contains(cs, searchCharSequence)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否是Simulator容器本身的类
     * 因为多命名空间的原因，所以这里不能简单的用ClassLoader来进行判断
     *
     * @param classStructure 类结构
     * @return 返回是否是仿真器类
     */
    private boolean isInstrumentSimulatorClass(final ClassStructure classStructure) {
        return classStructure.getJavaClassName().startsWith("com.shulie.instrument.simulator.");
    }

    private Set<String> takeJavaClassNames(final Set<ClassStructure> classStructures) {
        final Set<String> javaClassNames = new LinkedHashSet<String>();
        for (final ClassStructure classStructure : classStructures) {
            javaClassNames.add(classStructure.getJavaClassName());
        }
        return javaClassNames;
    }

    /*
     * 判断是否隐形类
     */
    private boolean isStealthClass(final ClassStructure classStructure) {
        return takeJavaClassNames(classStructure.getFamilyAnnotationTypeClassStructures())
                .contains(Stealth.class.getName());
    }

    /*
     * 判断是否ClassLoader家族中是否有隐形基因
     */
    private boolean isFromStealthClassLoader() {
        if (null == loader) {
            return !isEnableUnsafe;
        }
        return takeJavaClassNames(ClassStructureFactory.createClassStructure(loader.getClass()).getFamilyTypeClassStructures())
                .contains(Stealth.class.getName());
    }

    /*
     * 是否是负责启动的main函数
     * 这个函数如果被增强了会引起错误,所以千万不能增强,嗯嗯
     * public static void main(String[]);
     */
    private boolean isJavaMainBehavior(final BehaviorStructure behaviorStructure) {
        final Access access = behaviorStructure.getAccess();
        final List<ClassStructure> parameterTypeClassStructures = behaviorStructure.getParameterTypeClassStructures();
        return access.isPublic()
                && access.isStatic()
                && "void".equals(behaviorStructure.getReturnTypeClassStructure().getJavaClassName())
                && "main".equals(behaviorStructure.getName())
                && parameterTypeClassStructures.size() == 1
                && "java.lang.String[]".equals(parameterTypeClassStructures.get(0).getJavaClassName());
    }

    /*
     * 是否不支持的方法修饰
     * 1. abstract的方法没有实现，没有必要增强
     * 2. native的方法暂时无法支持
     */
    private boolean isUnsupportedBehavior(final BehaviorStructure behaviorStructure) {
        final Access access = behaviorStructure.getAccess();
        return access.isAbstract()
                || access.isNative();
    }

    @Override
    public List<BuildingForListeners> getAllListeners() {
        return Collections.EMPTY_LIST;
    }

    @Override
    public boolean preMatching(String javaClassName) {
        return true;
    }

    @Override
    public MatchingResult matching(final ClassStructure classStructure) {
        if (isUnsupportedClass(classStructure)
                || isInstrumentSimulatorClass(classStructure)
                || isFromStealthClassLoader()
                || isStealthClass(classStructure)) {
            return MatchingResult.UN_MATCHED;
        }

        final MatchingResult result = new MatchingResult(true);
        for (final BehaviorStructure behaviorStructure : classStructure.getBehaviorStructures()) {
            if (isJavaMainBehavior(behaviorStructure)
                    || isUnsupportedBehavior(behaviorStructure)) {
                continue;
            }
            result.addMatchingResult(behaviorStructure, new ArrayList<BuildingForListeners>());
        }
        return result;
    }


    /**
     * 构造AND关系的组匹配
     * <p>
     * 一般{@link UnsupportedMatcher}都与其他Matcher配合使用，
     * 所以这里对AND关系做了一层封装
     * </p>
     *
     * @param matcher 发生AND关系的{@link Matcher}
     * @return GroupMatcher.and(matcher, this)
     */
    public Matcher and(final Matcher matcher) {
        return new GroupMatcher.And(
                matcher,
                this
        );
    }

}
