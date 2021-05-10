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
package com.shulie.instrument.simulator.api.filter;

import com.shulie.instrument.simulator.api.listener.ext.BuildingForListeners;
import com.shulie.instrument.simulator.api.util.StringUtil;

import java.util.Collections;
import java.util.List;

/**
 * 类名和方法名正则表达式匹配过滤器
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/9/30 11:32 下午
 */
public class NameRegexFilter implements ExtFilter {

    /**
     * 类名正则表达式
     */
    private final String javaNameRegex;

    /**
     * 方法名正则表达式
     */
    private final String javaMethodRegex;

    /**
     * 是否包含子类
     */
    private boolean isIncludeSubClasses;

    /**
     * 是否包含 bootstrap 加载的类
     */
    private boolean isIncludeBootstrap;

    /**
     * 构造名称正则表达式过滤器
     *
     * @param javaNameRegex   类名正则表达式
     * @param javaMethodRegex 方法名正则表达式
     */
    public NameRegexFilter(String javaNameRegex, String javaMethodRegex) {
        this(javaNameRegex, javaMethodRegex, false, false);
    }

    public NameRegexFilter(String javaNameRegex, String javaMethodRegex, boolean isIncludeBootstrap, boolean isIncludeSubClasses) {
        this.javaNameRegex = javaNameRegex;
        this.javaMethodRegex = javaMethodRegex;
        this.isIncludeBootstrap = isIncludeBootstrap;
        this.isIncludeSubClasses = isIncludeSubClasses;
    }

    @Override
    public boolean doClassNameFilter(String javaClassName) {
        return StringUtil.matching(javaClassName, javaNameRegex);
    }

    @Override
    public boolean doClassFilter(ClassDescriptor classDescriptor) {
        return StringUtil.matching(classDescriptor.getClassName(), javaNameRegex);
    }

    @Override
    public List<BuildingForListeners> doMethodFilter(MethodDescriptor methodDescriptor) {
        return Collections.EMPTY_LIST;
    }

    @Override
    public List<BuildingForListeners> getAllListeners() {
        return Collections.EMPTY_LIST;
    }

    @Override
    public boolean isIncludeSubClasses() {
        return isIncludeSubClasses;
    }

    @Override
    public boolean isIncludeBootstrap() {
        return isIncludeBootstrap;
    }
}
