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

import com.shulie.instrument.simulator.api.ModuleException;

/**
 * 模块控制接口
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/10/23 10:45 下午
 */
public interface ModuleController {

    /**
     * 激活当前模块
     *
     * @throws ModuleException 激活模块失败
     */
    void active() throws ModuleException;

    /**
     * 冻结当前模块
     *
     * @throws ModuleException 冻结失败
     */
    void frozen() throws ModuleException;

    /**
     * 添加可释放资源
     * 所有的模块都有唯一一个对应的 ModuleController,如果该模块需要添加可释放资源，则使用 ModuleController 添加可释放资源
     *
     * @param releaseResource
     * @param <T>
     */
    <T> void addReleaseResource(ReleaseResource<T> releaseResource);
}
