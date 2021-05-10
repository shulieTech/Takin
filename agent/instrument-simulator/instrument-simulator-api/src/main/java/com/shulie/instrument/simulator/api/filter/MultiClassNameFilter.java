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

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 多类名称过滤器
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/9/30 11:32 下午
 */
public class MultiClassNameFilter implements ExtFilter {
    /**
     * 类名列表
     */
    private Collection<String> classNames;
    /**
     * 是否包含子类
     */
    private boolean isIncludeSubClasses;
    /**
     * 是否包含 bootstrap 类
     */
    private boolean isIncludeBootstrap;

    public MultiClassNameFilter(Collection<String> classNames) {
        this(classNames, false);
    }

    public MultiClassNameFilter(Collection<String> classNames, boolean isIncludeBootstrap) {
        this(classNames, isIncludeBootstrap, false);
    }

    public MultiClassNameFilter(Collection<String> classNames, boolean isIncludeBootstrap, boolean isIncludeSubClasses) {
        this.classNames = classNames;
        if (this.classNames == null) {
            this.classNames = Collections.EMPTY_SET;
        }
        this.isIncludeBootstrap = isIncludeBootstrap;
        this.isIncludeSubClasses = isIncludeSubClasses;
    }

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
        return classNames.contains(javaClassName);
    }

    @Override
    public List<BuildingForListeners> getAllListeners() {
        return Collections.EMPTY_LIST;
    }

    @Override
    public boolean doClassFilter(ClassDescriptor classDescriptor) {
        return classNames.contains(classDescriptor.getClassName());
    }

    @Override
    public List<BuildingForListeners> doMethodFilter(MethodDescriptor methodDescriptor) {
        return Collections.EMPTY_LIST;
    }
}
