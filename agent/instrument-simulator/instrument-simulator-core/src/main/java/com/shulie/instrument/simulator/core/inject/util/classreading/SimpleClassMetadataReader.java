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
package com.shulie.instrument.simulator.core.inject.util.classreading;

import org.objectweb.asm.ClassReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by xiaobin on 2017/1/19.
 */
public class SimpleClassMetadataReader {
    private final Logger logger = LoggerFactory.getLogger(SimpleClassMetadataReader.class);

    private final SimpleClassMetadata simpleClassMetadata;

    public static SimpleClassMetadata readSimpleClassMetadata(byte[] classBinary, String path) {
        SimpleClassMetadataReader simpleClassMetadataReader = new SimpleClassMetadataReader(classBinary, path);
        return simpleClassMetadataReader.getSimpleClassMetadata();
    }

    SimpleClassMetadataReader(byte[] classBinary, String path) {
        if (classBinary == null) {
            throw new NullPointerException("classBinary must not be null");
        }

        ClassReader classReader = new ClassReader(classBinary);
        int accessFlag = classReader.getAccess();
        String className = classReader.getClassName();
        String superClassName = classReader.getSuperName();
        String[] interfaceNameList = classReader.getInterfaces();

        int version = classReader.readShort(6);

        this.simpleClassMetadata = new DefaultSimpleClassMetadata(version, accessFlag, className, superClassName, interfaceNameList, classBinary, path);
    }

    public SimpleClassMetadata getSimpleClassMetadata() {
        return simpleClassMetadata;
    }


}

