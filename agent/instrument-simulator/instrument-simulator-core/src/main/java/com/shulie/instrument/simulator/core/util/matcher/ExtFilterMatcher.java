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

import com.shulie.instrument.simulator.api.filter.*;
import com.shulie.instrument.simulator.api.listener.ext.BuildingForListeners;
import com.shulie.instrument.simulator.api.listener.ext.EventWatchCondition;
import com.shulie.instrument.simulator.core.util.matcher.structure.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.shulie.instrument.simulator.api.filter.AccessFlags.*;
import static com.shulie.instrument.simulator.core.util.SimulatorStringUtils.toInternalClassName;

/**
 * 过滤器实现的匹配器
 */
public class ExtFilterMatcher implements Matcher {

    private final ExtFilter extFilter;

    public ExtFilterMatcher(final ExtFilter extFilter) {
        this.extFilter = extFilter;
    }

    // 获取需要匹配的类结构
    // 如果要匹配子类就需要将这个类的所有家族成员找出
    private Collection<ClassStructure> getWaitingMatchClassStructures(final ClassStructure classStructure) {
        final Collection<ClassStructure> waitingMatchClassStructures = new ArrayList<ClassStructure>();
        waitingMatchClassStructures.add(classStructure);
        if (extFilter.isIncludeSubClasses()) {
            waitingMatchClassStructures.addAll(classStructure.getFamilyTypeClassStructures());
        }
        return waitingMatchClassStructures;
    }

    private String[] toJavaClassNameArray(final Collection<ClassStructure> classStructures) {
        if (null == classStructures) {
            return null;
        }
        final List<String> javaClassNames = new ArrayList<String>();
        for (final ClassStructure classStructure : classStructures) {
            javaClassNames.add(classStructure.getJavaClassName());
        }
        return javaClassNames.toArray(new String[0]);
    }

    private boolean matchingClassStructure(ClassStructure classStructure) {
        for (final ClassStructure wmCs : getWaitingMatchClassStructures(classStructure)) {

            ClassDescriptor classDescriptor = new ClassDescriptor() {
                @Override
                public int getAccess() {
                    return toFilterAccess(wmCs.getAccess());
                }

                @Override
                public String getClassName() {
                    return wmCs.getJavaClassName();
                }

                @Override
                public String getSuperClassTypeJavaClassName() {
                    return null == wmCs.getSuperClassStructure()
                            ? null
                            : wmCs.getSuperClassStructure().getJavaClassName();
                }

                @Override
                public String[] getInterfaceTypeJavaClassNameArray() {
                    return toJavaClassNameArray(wmCs.getFamilyInterfaceClassStructures());
                }

                @Override
                public String[] getAnnotationTypeJavaClassNameArray() {
                    return toJavaClassNameArray(wmCs.getFamilyAnnotationTypeClassStructures());
                }
            };
            // 匹配类结构
            if (extFilter.doClassFilter(classDescriptor)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<BuildingForListeners> getAllListeners() {
        return extFilter.getAllListeners();
    }

    @Override
    public boolean preMatching(String javaClassName) {
        return extFilter.doClassNameFilter(javaClassName);
    }

    @Override
    public MatchingResult matching(final ClassStructure classStructure) {

        try {
            return _matching(classStructure);
        } catch (NoClassDefFoundError error) {

            // 根据 #203 ClassStructureImplByJDK会存在类加载异步的问题
            // 所以这里对JDK实现的ClassStructure抛出NoClassDefFoundError的时候做一个兼容
            // 转换为ASM实现然后进行match
            if (classStructure instanceof ClassStructureImplByJDK
                    && classStructure.getClassLoader() != null) {
                final String javaClassResourceName = toInternalClassName(classStructure.getJavaClassName()).concat(".class");
                InputStream is = null;
                try {
                    is = classStructure.getClassLoader().getResourceAsStream(javaClassResourceName);
                    _matching(ClassStructureFactory.createClassStructure(is, classStructure.getClassLoader()));
                } finally {
                    IOUtils.closeQuietly(is);
                }
            }

            // 其他情况就直接抛出error
            throw error;
        }

    }

    private MatchingResult _matching(final ClassStructure classStructure) {
        // 如果不开启加载Bootstrap的类，遇到就过滤掉
        if (!extFilter.isIncludeBootstrap()
                && classStructure.getClassLoader() == null) {
            return MatchingResult.UN_MATCHED;
        }

        // 匹配ClassStructure
        if (!matchingClassStructure(classStructure)) {
            return MatchingResult.UN_MATCHED;
        }

        MatchingResult result = new MatchingResult(true);
        // 匹配BehaviorStructure
        for (final BehaviorStructure behaviorStructure : classStructure.getBehaviorStructures()) {
            MethodDescriptor methodDescriptor = new MethodDescriptor() {
                @Override
                public int getAccess() {
                    return toFilterAccess(behaviorStructure.getAccess());
                }

                @Override
                public String getMethodName() {
                    return behaviorStructure.getName();
                }

                @Override
                public String[] getParameterTypeJavaClassNameArray() {
                    return toJavaClassNameArray(behaviorStructure.getParameterTypeClassStructures());
                }

                @Override
                public String[] getThrowsTypeJavaClassNameArray() {
                    return toJavaClassNameArray(behaviorStructure.getExceptionTypeClassStructures());
                }

                @Override
                public String[] getAnnotationTypeJavaClassNameArray() {
                    return toJavaClassNameArray(behaviorStructure.getAnnotationTypeClassStructures());
                }
            };
            List<BuildingForListeners> list = extFilter.doMethodFilter(methodDescriptor);
            if (list != null && !list.isEmpty()) {
                result.addMatchingResult(behaviorStructure, list);
            }
        }
        return result;
    }


    /**
     * 转换为{@link AccessFlags}的Access体系
     *
     * @param access access flag
     * @return 部分兼容ASM的access flag
     */
    private static int toFilterAccess(final Access access) {
        int flag = 0;
        if (access.isPublic()) {
            flag |= ACF_PUBLIC;
        }
        if (access.isPrivate()) {
            flag |= ACF_PRIVATE;
        }
        if (access.isProtected()) {
            flag |= ACF_PROTECTED;
        }
        if (access.isStatic()) {
            flag |= ACF_STATIC;
        }
        if (access.isFinal()) {
            flag |= ACF_FINAL;
        }
        if (access.isInterface()) {
            flag |= ACF_INTERFACE;
        }
        if (access.isNative()) {
            flag |= ACF_NATIVE;
        }
        if (access.isAbstract()) {
            flag |= ACF_ABSTRACT;
        }
        if (access.isEnum()) {
            flag |= ACF_ENUM;
        }
        if (access.isAnnotation()) {
            flag |= ACF_ANNOTATION;
        }
        return flag;
    }

    /**
     * 兼容{@code simulator-api:1.0.10}时
     * 在{@link EventWatchCondition#getOrFilterArray()}中将{@link Filter}直接暴露出来的问题，
     * 所以这里做一个兼容性的强制转换
     *
     * <ul>
     * <li>如果filterArray[index]是一个{@link ExtFilter}，则不需要再次转换</li>
     * <li>如果filterArray[index]是一个{@link Filter}，则需要进行{@link ExtFilterFactory#make(Filter)}的转换</li>
     * </ul>
     *
     * @param filterArray 过滤器数组
     * @return 兼容的Matcher
     */
    public static Matcher toOrGroupMatcher(final Filter[] filterArray) {
        final ExtFilter[] extFilterArray = new ExtFilter[filterArray.length];
        for (int index = 0; index < filterArray.length; index++) {
            extFilterArray[index] = ExtFilterFactory.make(filterArray[index]);
        }
        return toOrGroupMatcher(extFilterArray);
    }

    /**
     * 将{@link ExtFilter}数组转换为Or关系的Matcher
     *
     * @param extFilterArray 增强过滤器数组
     * @return Or关系Matcher
     */
    public static Matcher toOrGroupMatcher(final ExtFilter[] extFilterArray) {
        final Matcher[] matcherArray = new Matcher[ArrayUtils.getLength(extFilterArray)];
        for (int index = 0; index < matcherArray.length; index++) {
            matcherArray[index] = new ExtFilterMatcher(extFilterArray[index]);
        }
        return new GroupMatcher.Or(matcherArray);
    }

}
