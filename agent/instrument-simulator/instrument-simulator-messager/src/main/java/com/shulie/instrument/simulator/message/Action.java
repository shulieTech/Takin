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
package com.shulie.instrument.simulator.message;

/**
 * Action
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/11/9 11:13 下午
 */
public interface Action {
    /**
     * 未找到 Action
     */
    Object ACTION_NOT_FOUND = new Object();

    /**
     * 执行动作
     *
     * @param action 动作名称
     * @param args   参数
     * @return 返回动作执行的结果
     */
    Object onAction(String action, Object... args);
}
