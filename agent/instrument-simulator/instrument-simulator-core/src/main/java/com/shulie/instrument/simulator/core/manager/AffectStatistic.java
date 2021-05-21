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
package com.shulie.instrument.simulator.core.manager;

import com.shulie.instrument.simulator.api.listener.ext.BuildingForListeners;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * 增强类的统计器
 * <p>
 * 影响类和方法的统计信息
 * </p>
 */
public class AffectStatistic {

    // 影响类去重码集合
    private final Set<String> affectClassUniqueSet = new HashSet<String>();

    // 影响方法去重码集合
    private final Set<String> affectMethodUniqueSet = new HashSet<String>();

    // 计算唯一编码
    private String computeUniqueCode(final ClassLoader loader, final String javaClassName) {
        return new StringBuilder()
                .append(System.identityHashCode(loader))
                .append("_c_")
                .append(javaClassName)
                .toString();
    }

    private Set<String> computeUniqueCode(final ClassLoader loader,
                                          final Set<String> behaviorSignCodes) {
        final Set<String> uniqueCodes = new LinkedHashSet<String>();
        for (final String behaviorSignCode : behaviorSignCodes) {
            uniqueCodes.add(
                    System.identityHashCode(loader)
                            + "_h_"
                            + behaviorSignCode
            );
        }
        return uniqueCodes;
    }

    /**
     * 统计影响的类个数
     *
     * @param loader        加载的ClassLoader
     * @param javaClassName 类名
     */
    public void statisticAffectClass(final ClassLoader loader,
                                     final String javaClassName) {
        affectClassUniqueSet.add(computeUniqueCode(loader, javaClassName));
    }

    /**
     * 统计影响的行为个数
     *
     * @param loader            加载的ClassLoader
     * @param behaviorSignCodes 行为签名集合
     */
    public void statisticAffectMethod(final ClassLoader loader,
                                      final Map<String, Set<BuildingForListeners>> behaviorSignCodes) {
        affectMethodUniqueSet.addAll(computeUniqueCode(loader, behaviorSignCodes.keySet()));
    }

    /**
     * 对本次影响范围进行统计
     *
     * @param loader            加载的ClassLoader
     * @param internalClassName 类名
     * @param behaviorSignCodes 行为签名集合
     */
    public void statisticAffect(final ClassLoader loader,
                                final String internalClassName,
                                final Map<String, Set<BuildingForListeners>> behaviorSignCodes) {
        statisticAffectClass(loader, internalClassName);
        statisticAffectMethod(loader, behaviorSignCodes);
    }


    /**
     * 获取影响类数量
     *
     * @return 影响类数量
     */
    public int getEffectClassCount() {
        return affectClassUniqueSet.size();
    }

    /**
     * 获取影响方法数量
     *
     * @return 影响方法数量
     */
    public int getEffectMethodCount() {
        return affectMethodUniqueSet.size();
    }

}
