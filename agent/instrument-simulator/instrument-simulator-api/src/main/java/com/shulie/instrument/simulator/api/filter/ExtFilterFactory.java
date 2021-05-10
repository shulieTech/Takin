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


import com.shulie.instrument.simulator.api.annotation.IncludeBootstrap;
import com.shulie.instrument.simulator.api.annotation.IncludeSubClasses;
import com.shulie.instrument.simulator.api.listener.ext.BuildingForListeners;

import java.util.List;

/**
 * 增强过滤器工厂类
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/10/23 10:45 下午
 */
public class ExtFilterFactory {

    /**
     * 生产增强过滤器
     *
     * @param filter              原生过滤器
     * @param isIncludeSubClasses 是否包含子类
     * @param isIncludeBootstrap  是否搜索BootstrapClassLoader所加载的类
     * @return 增强过滤器
     */
    public static ExtFilter make(final Filter filter,
                                 final boolean isIncludeSubClasses,
                                 final boolean isIncludeBootstrap) {
        return new ExtFilter() {

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
                return filter.doClassNameFilter(javaClassName);
            }

            @Override
            public boolean doClassFilter(ClassDescriptor classDescriptor) {
                return filter.doClassFilter(classDescriptor);
            }

            @Override
            public List<BuildingForListeners> doMethodFilter(MethodDescriptor methodDescriptor) {
                return filter.doMethodFilter(methodDescriptor);
            }

            @Override
            public List<BuildingForListeners> getAllListeners() {
                return filter.getAllListeners();
            }
        };
    }

    /**
     * 生产增强过滤器
     *
     * @param filter 原生过滤器
     * @return 增强过滤器
     */
    public static ExtFilter make(final Filter filter) {
        return
                filter instanceof ExtFilter
                        ? (ExtFilter) filter
                        : make(
                        filter,
                        filter.getClass().isAnnotationPresent(IncludeSubClasses.class),
                        filter.getClass().isAnnotationPresent(IncludeBootstrap.class)
                );
    }

}
