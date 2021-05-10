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
package com.shulie.instrument.simulator.compatible.lambada;

/**
 * Lambda 字节码处理器
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/12/10 11:57 上午
 */
public interface LambdaBytecodeHandler {
    /**
     * 处理 lambda 字节码
     *
     * @param clazz     目标类
     * @param data      字节码
     * @param cpPatches cpPatches
     * @return
     */
    byte[] handleLambdaBytecode(Class<?> clazz, byte[] data, Object[] cpPatches);
}
