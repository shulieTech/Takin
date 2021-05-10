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

/**
 * 删除观察匹配构建器
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/10/23 10:47 下午
 */
public interface IUnWatchingMatchBuilder {

    /**
     * 添加渲染进度监听器，可以添加多个
     * <p>
     * 用于观察{@link #onUnWatched()}方法渲染类的进度
     * </p>
     *
     * @param progress 渲染进度监听器
     * @return IBuildingForWatching
     */
    IUnWatchingMatchBuilder withProgress(Progress progress);

    /**
     * 删除观察者
     */
    void onUnWatched();

}
