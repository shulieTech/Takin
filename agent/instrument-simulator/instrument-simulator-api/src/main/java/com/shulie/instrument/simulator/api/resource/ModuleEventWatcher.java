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
import com.shulie.instrument.simulator.api.listener.ext.EventWatchCondition;
import com.shulie.instrument.simulator.api.listener.ext.Progress;
import com.shulie.instrument.simulator.api.listener.ext.WatchCallback;

/**
 * 模块事件观察者, 用于模块中自定义对类的增强操作
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/10/23 10:45 下午
 */
public interface ModuleEventWatcher {

    /**
     * 观察事件
     *
     * @param filter   观察类和方法过滤器
     *                 通过这个对象来告知{@code EventWatcher}观察的类和方法
     * @param progress 观察渲染进度报告
     * @return {@code watchId}，本次观察的唯一编号，{@code watcherId}对象将会是整个操作的唯一KEY，后续删除观察事件的时候也需要通过同一个{@code watcherId}来完成
     */
    int watch(Filter filter, Progress progress);

    /**
     * dump
     *
     * @param filter   过滤器
     * @param progress 进度条
     * @return 返回dump结果
     */
    DumpResult dump(Filter filter, Progress progress);

    /**
     * 观察事件
     *
     * @param filter 观察类和方法过滤器
     *               通过这个对象来告知{@code EventWatcher}观察的类和方法
     * @return {@code watchId}，本次观察的唯一编号，{@code watcherId}对象将会是整个操作的唯一KEY，后续删除观察事件的时候也需要通过同一个{@code watcherId}来完成
     */
    int watch(Filter filter);

    /**
     * 观察事件
     *
     * @param condition 事件观察条件，只有符合条件的类/方法才会被观察
     * @param progress  观察渲染进度报告
     * @return {@code watchId}，本次观察的唯一编号，{@code watcherId}对象将会是整个操作的唯一KEY，后续删除观察事件的时候也需要通过同一个{@code watcherId}来完成
     */
    int watch(EventWatchCondition condition, Progress progress);

    /**
     * 删除观察事件
     *
     * @param watcherId {@code watcherId}观察唯一编号
     *                  在{@link #watch(Filter, Progress)}方法返回值获得
     * @param progress  清除观察渲染进度报告
     *                  删除观察事件时，将会删除掉之前埋入的观察代码，需要对已经渲染的类从新进行代码渲染
     */
    void delete(int watcherId, Progress progress);

    /**
     * 删除观察事件
     *
     * @param watcherId {@code watcherId}观察唯一编号
     *                  在{@link #watch(Filter, Progress)}方法返回值获得
     */
    void delete(int watcherId);

    /**
     * 观察事件
     * <p>
     * 和{@link #watch(Filter, Progress)}不一样的地方是，当观察结束时会自动delete观察事件
     * 还原之前观察时被渲染的代码
     * </p>
     *
     * @param filter    观察类和方法过滤器
     * @param wProgress 观察渲染进度报告
     * @param watchCb   观察渲染完成回调
     * @param dProgress 清除观察渲染进度报告
     * @throws Throwable 观察渲染完成回调出错
     */
    void watching(Filter filter,
                  Progress wProgress,
                  WatchCallback watchCb,
                  Progress dProgress
    ) throws Throwable;

    /**
     * 观察事件
     * <p>
     * 和{@link #watch(Filter, Progress)}不一样的地方是，当观察结束时会自动delete观察事件
     * 还原之前观察时被渲染的代码
     * </p>
     *
     * @param filter  观察类和方法过滤器
     * @param watchCb 观察渲染完成回调
     * @throws Throwable 观察渲染完成回调出错
     */
    void watching(Filter filter,
                  WatchCallback watchCb
    ) throws Throwable;

    /**
     * 关闭
     */
    void close();
}
