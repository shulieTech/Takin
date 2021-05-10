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
import com.shulie.instrument.simulator.core.util.matcher.structure.ClassStructure;

import java.util.List;

/**
 * 匹配器
 * <p>
 * 可以判断当前类结构是否符合要求
 * </p>
 */
public interface Matcher {

    /**
     * 获取所有的监听器
     *
     * @return
     */
    List<BuildingForListeners> getAllListeners();

    /**
     * 预匹配
     * @param javaClassName
     * @return
     */
    boolean preMatching(String javaClassName);

    /**
     * 匹配类结构
     *
     * @param classStructure 类结构
     * @return 匹配结果
     */
    MatchingResult matching(ClassStructure classStructure);

}
