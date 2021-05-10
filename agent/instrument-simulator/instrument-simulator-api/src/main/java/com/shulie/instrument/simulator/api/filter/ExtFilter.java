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

/**
 * 增强过滤器
 * <p>
 * 原有的{@link Filter}表现形式过于单薄而且纵深扩展能力偏差，所以使用了一些Annotation进行扩展，
 * 扩展之后的结果最终会以ExtFilter的形式在容器内部运转
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/10/23 10:45 下午
 */
public interface ExtFilter extends Filter {

    /**
     * 是否搜索子类或实现类
     *
     * @return TRUE:搜索子类或实现类;FALSE:不搜索
     */
    boolean isIncludeSubClasses();

    /**
     * 是否搜索来自BootstrapClassLoader所加载的类
     *
     * @return TRUE:搜索；FALSE：不搜索；最终容器是否会对BootstrapClassLoader所加载的类进行处理，
     * 还需要参考{@code simulator.properties#unsafe.enable=true}配合使用才能生效
     */
    boolean isIncludeBootstrap();

}
