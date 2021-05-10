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
package com.shulie.instrument.module.register.zk;

/**
 * @author xiaobin.zfb
 * @since 2020/8/7 8:30 下午
 */
public interface Lifecycle extends Stoppable {

    /**
     * 开始运行
     */
    void start() throws Exception;

    /**
     * 停止运行。如果已经停止，则应该不会有任何效果。
     * 建议实现使用同步方式执行。
     */
    void stop() throws Exception;

    /**
     * 检查当前是否在运行状态
     */
    boolean isRunning();
}
