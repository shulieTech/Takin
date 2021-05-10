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
package com.shulie.instrument.simulator.core.manager.impl;

import com.shulie.instrument.simulator.core.inject.ClassInjector;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

/**
 * 内部的一些包需要对业务类加载器开放时，通过这个类文件转换，将需要注入的包注入到对应的业务类加载器中
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/11/24 2:30 下午
 */
public class InternalClassFileTransformer implements ClassFileTransformer {
    private ClassInjector classInjector;

    public InternalClassFileTransformer(ClassInjector classInjector) {
        this.classInjector = classInjector;
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        injectJar(loader, className);
        return null;
    }

    /**
     * 注入对应的jar, 对应biz-classloader-inject.properties
     *
     * @param classLoader       当前类的类加载器
     * @param internalClassName 内部class名称,需要转换成java class名称
     */
    private void injectJar(ClassLoader classLoader, String internalClassName) {
        if (internalClassName == null) {
            return;
        }
        String className = internalClassName.replace('/', '.');
        classInjector.injectClass(classLoader, className);
    }
}
