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
package com.shulie.instrument.simulator.api.listener;

/**
 * 是否可中断的接口, 可直接由 EventListener 或者是 AdviceListener 实现类实现
 * 添加了此方法后则说明此类是可被中断的，即此类执行异常时则直接中断业务执行，将异常直接
 * 抛至业务上层
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/11/6 7:45 下午
 */
public interface Interruptable {
    /**
     * 是否是中断
     *
     * @return
     */
    boolean isInterrupted();
}
