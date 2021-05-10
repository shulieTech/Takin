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

import com.shulie.instrument.simulator.api.resource.SimulatorConfig;
import com.shulie.instrument.simulator.core.manager.BytecodeDumpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;

import static com.shulie.instrument.simulator.api.util.StringUtil.isEmpty;

/**
 * Created by xiaobin on 2017/1/19.
 */
public class ASMBytecodeDumpService implements BytecodeDumpService {

    private final Logger logger = LoggerFactory.getLogger(ASMBytecodeDumpService.class.getName());

    public static final String BYTECODE_DUMP_BYTECODE = "simulator.dump.bytecode";
    public static final boolean BYTECODE_DUMP_BYTECODE_DEFAULT_VALUE = false;

    public static final String BYTECODE_DUMP_BYTECODE_PATH = "simulator.dump.bytecode.path";


    public static final String BYTECODE_DUMP_VERIFY = "simulator.dump.verify";
    public static final boolean BYTECODE_DUMP_VERIFY_DEFAULT_VALUE = false;

    public static final String BYTECODE_DUMP_ASM = "simulator.dump.asm";
    public static final boolean BYTECODE_DUMP_ASM_DEFAULT_VALUE = false;

    public static final String DUMP_CLASS_LIST = "simulator.dump.class.list";

    private final boolean dumpBytecode;
    private final boolean dumpVerify;
    private final boolean dumpASM;
    private final Set<String> dumpJvmClassNameSet;
    private final Set<String> dumpHistorySet;

    private ASMBytecodeDisassembler disassembler;
    private File dumpFile;

    public ASMBytecodeDumpService(SimulatorConfig simulatorConfig) {
        if (simulatorConfig == null) {
            throw new NullPointerException("coreConfigure must not be null");
        }

        this.dumpBytecode = simulatorConfig.getBooleanProperty(BYTECODE_DUMP_BYTECODE, BYTECODE_DUMP_BYTECODE_DEFAULT_VALUE);
        this.dumpVerify = simulatorConfig.getBooleanProperty(BYTECODE_DUMP_VERIFY, BYTECODE_DUMP_VERIFY_DEFAULT_VALUE);
        this.dumpASM = simulatorConfig.getBooleanProperty(BYTECODE_DUMP_ASM, BYTECODE_DUMP_ASM_DEFAULT_VALUE);

        this.dumpJvmClassNameSet = getClassName(simulatorConfig);
        this.dumpHistorySet = new HashSet<String>();
        this.disassembler = new ASMBytecodeDisassembler(simulatorConfig.getProperty(BYTECODE_DUMP_BYTECODE_PATH, "."));
    }

    private Set<String> getClassName(SimulatorConfig simulatorConfig) {
        final String classNameList = simulatorConfig.getProperty(DUMP_CLASS_LIST, "");
        if (classNameList.isEmpty()) {
            return Collections.emptySet();
        } else {
            final List<String> classList = splitAndTrim(classNameList, ",");
            final List<String> jvmClassList = javaNameToJvmName(classList);
            return new HashSet<String>(jvmClassList);
        }
    }

    public static List<String> splitAndTrim(String value, String separator) {
        if (isEmpty(value)) {
            return Collections.emptyList();
        }
        if (separator == null) {
            throw new NullPointerException("separator must not be null");
        }
        final List<String> result = new ArrayList<String>();
        // TODO remove regex 'separator'
        final String[] split = value.split(separator);
        for (String method : split) {
            if (isEmpty(method)) {
                continue;
            }
            method = method.trim();
            if (method.isEmpty()) {
                continue;
            }
            result.add(method);
        }
        return result;
    }

    private List<String> javaNameToJvmName(List<String> classNameList) {
        List<String> jvmNameList = new ArrayList<String>(classNameList.size());

        for (String className : classNameList) {
            jvmNameList.add(javaNameToJvmName(className));
        }
        return jvmNameList;
    }

    /**
     * java.lang.String 转换成 java/lang/String
     *
     * @param javaName java类名
     * @return 返回jvm中的类表述
     */
    public static String javaNameToJvmName(String javaName) {
        if (javaName == null) {
            throw new NullPointerException("javaName must not be null");
        }
        return javaName.replace('.', '/');
    }

    @Override
    public void dumpBytecode(String dumpMessage, final String dumpPath, final String jvmClassName, boolean isDumpFile, final byte[] bytes, ClassLoader classLoader) {
        if (jvmClassName == null) {
            throw new NullPointerException("jvmClassName must not be null");
        }
        if (!dumpHistorySet.add(dumpPath + File.separator + jvmClassName)) {
            return;
        }

        if (!filterClassName(jvmClassName)) {
            return;
        }

        if (dumpBytecode || isDumpFile) {
            this.dumpFile = this.disassembler.dumpBytecode(dumpPath, jvmClassName, bytes);
        }

        if (dumpVerify) {
            if (classLoader == null) {
                if (logger.isDebugEnabled()) {
                    logger.debug("SIMULATOR: classLoader is null, jvmClassName:{}", jvmClassName);
                }
                classLoader = ClassLoader.getSystemClassLoader();
            }
            final String dumpVerify = this.disassembler.dumpVerify(bytes, classLoader);
            if (logger.isInfoEnabled()) {
                logger.info("SIMULATOR: {} class:{} verify:{}", dumpMessage, jvmClassName, dumpVerify);
            }
        }

        if (dumpASM) {
            final String dumpASM = this.disassembler.dumpASM(bytes);
            if (logger.isInfoEnabled()) {
                logger.info("SIMULATOR: {} class:{} asm:{}", dumpMessage, jvmClassName, dumpASM);
            }
        }
    }

    @Override
    public File getDumpFile() {
        return dumpFile;
    }

    private boolean filterClassName(String className) {
        return this.dumpJvmClassNameSet.isEmpty() || this.dumpJvmClassNameSet.contains(className);
    }
}
