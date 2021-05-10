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
package com.shulie.instrument.simulator.core.enhance;

import com.shulie.instrument.simulator.api.listener.ext.BuildingForListeners;

import java.util.Map;
import java.util.Set;

/**
 * 代码增强
 */
public interface Enhancer {

    /**
     * 转换为增强后的字节码数组
     *
     * @param loader           目标类加载器
     * @param srcByteCodeArray 源字节码数组
     * @param signCodes        需要被增强的行为签名
     * @param namespace        命名空间
     * @return 增强后的字节码数组
     */
    byte[] toByteCodeArray(ClassLoader loader,
                           byte[] srcByteCodeArray,
                           Map<String, Set<BuildingForListeners>> signCodes,
                           String namespace);

}
