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
package com.shulie.instrument.simulator.api.resource;

import com.shulie.instrument.simulator.api.filter.Filter;

import java.util.Iterator;
import java.util.Set;

/**
 * 已加载类数据源
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/10/23 10:45 下午
 */
public interface LoadedClassDataSource {

    /**
     * 获取所有已加载的类集合
     *
     * @return 所有已加载的类集合
     */
    Set<Class<?>> list();

    /**
     * 根据过滤器搜索出匹配的类集合
     *
     * @param filter 扩展过滤器
     * @return 匹配的类集合
     */
    Set<Class<?>> find(Filter filter);

    /**
     * 获取所有已加载类的集合迭代器
     * <p>
     * 对比 {@link #list()} 而言，有更优的内存、CPU开销
     *
     * @return 所有已加载的类集合迭代器
     */
    Iterator<Class<?>> iteratorForLoadedClasses();

}
