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
package com.shulie.instrument.module.register.register;

/**
 * @author xiaobin.zfb
 * @since 2020/8/20 9:55 上午
 */
public interface Register {
    String getName();

    /**
     * 初始化
     *
     * @param registerOptions
     */
    void init(RegisterOptions registerOptions);

    /**
     * 返回注册路径
     *
     * @return
     */
    String getPath();

    /**
     * 注册节点
     */
    void start();

    /**
     * 注销节点
     */
    void stop();

    /**
     * 刷新配置
     */
    void refresh();
}
