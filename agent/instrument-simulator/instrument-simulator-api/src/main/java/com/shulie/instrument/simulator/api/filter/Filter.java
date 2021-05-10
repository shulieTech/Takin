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

import java.util.List;

/**
 * 类和方法过滤器
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/10/23 10:45 下午
 */
public interface Filter {

    /**
     * 匹配className
     *
     * @param javaClassName
     * @return
     */
    boolean doClassNameFilter(String javaClassName);

    /**
     * 过滤出匹配的类
     *
     * @param classDescriptor 方法描述
     * @return true:匹配;false:不匹配;
     */
    boolean doClassFilter(ClassDescriptor classDescriptor);

    /**
     * 过滤出匹配的方法监听
     * <p>
     * 严格意义上来说，方法{@link Filter#doMethodFilter(MethodDescriptor)}被调用的时候，
     * 一定是{@link Filter#doClassFilter(ClassDescriptor)}上一次返回true的调用。
     * 所以可以通过简单的引用就可以在doMethodFilter执行的时候拿到doClassFilter的信息
     * </p>
     *
     * @param methodDescriptor 方法描述
     * @return 返回方法监听列表
     */
    List<BuildingForListeners> doMethodFilter(MethodDescriptor methodDescriptor);

    /**
     * 获取所有的监听器
     *
     * @return
     */
    List<BuildingForListeners> getAllListeners();
}
