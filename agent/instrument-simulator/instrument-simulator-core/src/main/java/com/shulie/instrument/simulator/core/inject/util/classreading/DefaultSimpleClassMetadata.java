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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by xiaobin on 2017/1/19.
 */
public class DefaultSimpleClassMetadata implements SimpleClassMetadata {

    private final int version;

    private final int accessFlag;

    private final String className;

    private final String superClassName;

    private final List<String> interfaceNameList;

    private final byte[] classBinary;

    private final String path;

    public DefaultSimpleClassMetadata(int version, int accessFlag, String className, String superClassName, String[] interfaceNameList, byte[] classBinary, String path) {
        this.version = version;
        this.accessFlag = accessFlag;
        this.className = jvmNameToJavaName(className);
        this.superClassName = jvmNameToJavaName(superClassName);
        this.interfaceNameList = jvmNameToJavaName(interfaceNameList);
        this.classBinary = classBinary;
        this.path = path;
    }

    public static String jvmNameToJavaName(String jvmName) {
        if (jvmName == null) {
            throw new NullPointerException("jvmName must not be null");
        }
        return jvmName.replace('/', '.');
    }

    public static List<String> jvmNameToJavaName(String[] jvmNameArray) {
        if (jvmNameArray == null) {
            return Collections.emptyList();
        }
        List<String> list = new ArrayList<String>(jvmNameArray.length);
        for (String jvmName : jvmNameArray) {
            list.add(jvmNameToJavaName(jvmName));
        }
        return list;
    }

    @Override
    public int getVersion() {
        return version;
    }

    @Override
    public int getAccessFlag() {
        return accessFlag;
    }

    @Override
    public String getClassName() {
        return className;
    }

    @Override
    public String getSuperClassName() {
        return superClassName;
    }

    @Override
    public List<String> getInterfaceNames() {
        return interfaceNameList;
    }

    @Override
    public byte[] getClassBinary() {
        return classBinary;
    }

    @Override
    public String getPath() {
        return path;
    }
}
