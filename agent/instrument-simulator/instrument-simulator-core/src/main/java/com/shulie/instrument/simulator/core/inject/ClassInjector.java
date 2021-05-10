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
package com.shulie.instrument.simulator.core.inject;

/**
 * 类注入器定义，用于将自定义的类注入到指定的类加载器中
 */
public interface ClassInjector {

    /**
     * 将指定的 class 注入到指定的 classloader 中
     *
     * @param targetClassLoader 需要注入的目标 classloader
     * @param className         需要注入的类名
     */
    void injectClass(ClassLoader targetClassLoader, String className);

}
