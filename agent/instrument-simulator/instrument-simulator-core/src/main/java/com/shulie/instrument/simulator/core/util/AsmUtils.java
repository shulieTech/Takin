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
package com.shulie.instrument.simulator.core.util;

import com.shulie.instrument.simulator.core.util.matcher.structure.ClassStructure;
import com.shulie.instrument.simulator.core.util.matcher.structure.ClassStructureFactory;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;

/**
 * ASM工具集
 */
public class AsmUtils {

    /**
     * 获取两个类型的共同父类
     * just the same
     * {@code org.objectweb.asm.ClassWriter#getCommonSuperClass(String, String)}
     *
     * @param type1  类型1
     * @param type2  类型2
     * @param loader 所在ClassLoader
     * @return 共同的父类
     */
    public static String getCommonSuperClass(String type1, String type2, ClassLoader loader) {
        return getCommonSuperClassImplByAsm(type1, type2, loader);
    }

    // implements by ASM
    private static String getCommonSuperClassImplByAsm(String type1, String type2, ClassLoader targetClassLoader) {
        InputStream inputStreamOfType1 = null, inputStreamOfType2 = null;
        try {
            //targetClassLoader 为null，说明是BootStrapClassLoader，不能显式引用，故使用系统类加载器间接引用
            if (null == targetClassLoader) {
                targetClassLoader = ClassLoader.getSystemClassLoader();
            }
            if (null == targetClassLoader) {
                return "java/lang/Object";
            }
            inputStreamOfType1 = targetClassLoader.getResourceAsStream(type1 + ".class");
            if (null == inputStreamOfType1) {
                return "java/lang/Object";
            }
            inputStreamOfType2 = targetClassLoader.getResourceAsStream(type2 + ".class");
            if (null == inputStreamOfType2) {
                return "java/lang/Object";
            }
            final ClassStructure classStructureOfType1 = ClassStructureFactory.createClassStructure(inputStreamOfType1, targetClassLoader);
            final ClassStructure classStructureOfType2 = ClassStructureFactory.createClassStructure(inputStreamOfType2, targetClassLoader);
            if (classStructureOfType2.getFamilyTypeClassStructures().contains(classStructureOfType1)) {
                return type1;
            }
            if (classStructureOfType1.getFamilyTypeClassStructures().contains(classStructureOfType2)) {
                return type2;
            }
            if (classStructureOfType1.getAccess().isInterface()
                    || classStructureOfType2.getAccess().isInterface()) {
                return "java/lang/Object";
            }
            ClassStructure classStructure = classStructureOfType1;
            do {
                classStructure = classStructure.getSuperClassStructure();
                if (null == classStructure) {
                    return "java/lang/Object";
                }
            } while (!classStructureOfType2.getFamilyTypeClassStructures().contains(classStructure));
            return SimulatorStringUtils.toInternalClassName(classStructure.getJavaClassName());
        } finally {
            IOUtils.closeQuietly(inputStreamOfType1);
            IOUtils.closeQuietly(inputStreamOfType2);
        }
    }

}
