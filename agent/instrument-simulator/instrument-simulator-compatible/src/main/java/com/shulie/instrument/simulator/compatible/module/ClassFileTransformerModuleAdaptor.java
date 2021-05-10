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
package com.shulie.instrument.simulator.compatible.module;

import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

/**
 * ClassFileTransformer 适配器，适配多个不同的 jvm 版本
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/12/10 12:05 下午
 */
public interface ClassFileTransformerModuleAdaptor {
    /**
     * 转换字节码
     *
     * @param transformedModule   模块
     * @param loader              类加载器
     * @param className           类名
     * @param classBeingRedefined 加载的类
     * @param protectionDomain    protectionDomain
     * @param classfileBuffer     字节码
     * @return 转换后的字节码
     * @throws IllegalClassFormatException
     */
    byte[] transform(Object transformedModule, ClassLoader loader, String className, Class<?> classBeingRedefined,
                     ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException;
}
