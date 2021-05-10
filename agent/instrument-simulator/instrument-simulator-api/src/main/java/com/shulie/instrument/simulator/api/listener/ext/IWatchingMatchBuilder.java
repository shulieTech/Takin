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

import com.shulie.instrument.simulator.api.event.EventType;
import com.shulie.instrument.simulator.api.listener.Listeners;

/**
 * 正在观察匹配构建器
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/10/23 10:47 下午
 */
public interface IWatchingMatchBuilder {

    /**
     * 添加渲染进度监听器，可以添加多个
     * <p>
     * 用于观察{@link #onWatching(Listeners)}和{@link #onWatching(Listeners, EventType...)}的渲染进度
     * </p>
     *
     * @param progress 渲染进度监听器
     * @return IBuildingForWatching
     */
    IWatchingMatchBuilder withProgress(Progress progress);

    /**
     * 观察行为内部的方法调用
     * 调用之后，
     * <ul>
     * <li>{@link AdviceListener#beforeCall(Advice, int, boolean, String, String, String)}</li>
     * <li>{@link AdviceListener#afterCallReturning(Advice, int, boolean, String, String, String)}</li>
     * <li>{@link AdviceListener#afterCallThrowing(Advice, int, boolean, String, String, String, Throwable)}</li>
     * </ul>
     * <p>
     * 将会被触发
     *
     * @return IBuildingForWatching
     */
    IWatchingMatchBuilder withCall();

    /**
     * 观察行为内部的行调用
     * 调用之后，
     * <ul>
     * <li>{@link AdviceListener#beforeLine(Advice, int)}</li>
     * </ul>
     * 将会被触发
     *
     * @return IBuildingForWatching
     */
    IWatchingMatchBuilder withLine();


    /**
     * 使用通知监听器观察
     *
     * @param listeners 通知监听器定义
     * @return EventWatcher
     */
    void onWatching(Listeners listeners) throws Throwable;

    /**
     * 使用事件监听器观察
     *
     * @param listeners           事件监听器定义
     * @param eventEventTypeArray 需要监听的事件
     * @return EventWatcher
     */
    void onWatching(Listeners listeners, EventType... eventEventTypeArray) throws Throwable;

}
