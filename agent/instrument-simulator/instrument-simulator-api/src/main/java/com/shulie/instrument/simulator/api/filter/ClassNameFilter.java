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
 * 类名称过滤器
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/9/30 11:32 下午
 */
public class ClassNameFilter implements ExtFilter {
    private String className;
    private boolean isIncludeSubClasses;
    private boolean isIncludeBootstrap;

    public ClassNameFilter(String className) {
        this(className, false, false);
    }

    public ClassNameFilter(String className, boolean isIncludeBootstrap, boolean isIncludeSubClasses) {
        this.className = className;
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
        return StringUtil.equals(javaClassName, className);
    }

    @Override
    public boolean doClassFilter(ClassDescriptor classDescriptor) {
        return StringUtil.equals(classDescriptor.getClassName(), className);
    }

    @Override
    public List<BuildingForListeners> doMethodFilter(MethodDescriptor methodDescriptor) {
        return Collections.EMPTY_LIST;
    }

    @Override
    public List<BuildingForListeners> getAllListeners() {
        return Collections.EMPTY_LIST;
    }
}
