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

import com.shulie.instrument.simulator.api.resource.LoadedClassDataSource;
import com.shulie.instrument.simulator.core.util.matcher.Matcher;

import java.util.Collection;
import java.util.List;

/**
 * 内核使用的已加载类管理
 */
public interface CoreLoadedClassDataSource extends LoadedClassDataSource {

    /**
     * 使用{@link Matcher}来完成类的检索
     * <p>
     * 本次检索将会用于Class型变，所以会主动过滤掉不支持的类和行为
     * </p>
     *
     * @param matcher 类匹配
     * @return 匹配的类
     */
    List<Class<?>> findForReTransform(Matcher matcher);

    /**
     * 根据 className 来完成唯一类的检索
     *
     * @param className 类名称
     * @return
     */
    List<Class<?>> findForReTransform(String className);

    /**
     * 根据 className 来完成唯一类的检索
     *
     * @param classNames 类名称
     * @return
     */
    List<Class<?>> findForReTransform(String... classNames);

    /**
     * 根据 className 来完成唯一类的检索
     *
     * @param classNames 类名称
     * @return
     */
    List<Class<?>> findForReTransform(Collection<String> classNames);
}
