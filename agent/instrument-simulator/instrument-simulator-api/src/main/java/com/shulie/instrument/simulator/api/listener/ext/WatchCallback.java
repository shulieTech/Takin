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
 * 观察回调
 * <p>
 * 在{@link IClassMatchBuilder#onWatch()} 完成watch类渲染时回调
 * </p>
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/10/23 10:47 下午
 */
public interface WatchCallback {

    /**
     * 观察渲染完成
     * <p>
     * {@code return}或者抛出异常，都将会让{@link IClassMatchBuilder#onWatch()}
     * 方法进入到结束流程，在结束流程中将会冻结和删除被渲染的代码
     * </p>
     *
     * @throws Throwable 回掉出错
     */
    void watchCompleted() throws Throwable;

}
