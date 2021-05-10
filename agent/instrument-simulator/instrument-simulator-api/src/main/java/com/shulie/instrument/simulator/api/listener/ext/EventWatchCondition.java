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
package com.shulie.instrument.simulator.api.listener.ext;

import com.shulie.instrument.simulator.api.filter.ExtFilter;
import com.shulie.instrument.simulator.api.filter.Filter;

/**
 * 事件观察条件
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/10/23 10:45 下午
 */
public interface EventWatchCondition {

    /**
     * 获取"或"关系的查询过滤器数组
     * <p>
     * 这里会有点绕，因为在{@code simulator-api:1.0.10}的时候已经将这个接口披露出来了
     * 所以为了向下兼容API，必须保持{@link Filter}的声明。但实际上内部可能是一个{@link ExtFilter}，
     * 会在容器内部做兼容性判断。
     *
     * @return "或"关系的查询过滤器数组
     * </p>
     */
    Filter[] getOrFilterArray();

}
