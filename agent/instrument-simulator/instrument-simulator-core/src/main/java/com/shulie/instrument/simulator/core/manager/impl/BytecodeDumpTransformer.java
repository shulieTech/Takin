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

import com.shulie.instrument.simulator.api.listener.EventListener;
import com.shulie.instrument.simulator.api.listener.ext.BuildingForListeners;
import com.shulie.instrument.simulator.api.resource.SimulatorConfig;
import com.shulie.instrument.simulator.core.manager.AffectStatistic;
import com.shulie.instrument.simulator.core.manager.BytecodeDumpService;
import com.shulie.instrument.simulator.core.manager.SimulatorClassFileTransformer;
import com.shulie.instrument.simulator.core.util.matcher.Matcher;
import com.shulie.instrument.simulator.core.util.matcher.MatchingResult;
import com.shulie.instrument.simulator.core.util.matcher.UnsupportedMatcher;
import com.shulie.instrument.simulator.core.util.matcher.structure.ClassStructure;
import com.shulie.instrument.simulator.core.util.matcher.structure.ClassStructureFactory;

import java.io.File;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xiaobin on 2017/1/19.
 */
public class BytecodeDumpTransformer extends SimulatorClassFileTransformer {
    public static final String BYTECODE_DUMP_BYTECODE_PATH = "simulator.dump.bytecode.path";

    private final SimulatorClassFileTransformer delegate;

    private final BytecodeDumpService bytecodeDumpService;
    private final boolean isDumpFile;
    private final Map<String, File> dumpResult;
    private final boolean isEnableUnsafe;
    private final Matcher matcher;
    private final String dumpByteCodePath;

    public static BytecodeDumpTransformer wrap(SimulatorClassFileTransformer classFileTransformer, SimulatorConfig profilerConfig, Matcher matcher, boolean isDumpFile, boolean isEnableUnsafe) {
        return new BytecodeDumpTransformer(classFileTransformer, profilerConfig, matcher, isDumpFile, isEnableUnsafe);
    }

    public static BytecodeDumpTransformer wrap(SimulatorClassFileTransformer classFileTransformer, SimulatorConfig profilerConfig) {
        return new BytecodeDumpTransformer(classFileTransformer, profilerConfig, null, false, false);
    }

    private BytecodeDumpTransformer(SimulatorClassFileTransformer delegate, SimulatorConfig simulatorConfig, Matcher matcher, boolean isDumpFile, boolean isEnableUnsafe) {
        if (delegate == null) {
            throw new NullPointerException("delegate must not be null");
        }
        if (simulatorConfig == null) {
            throw new NullPointerException("profilerConfig must not be null");
        }
        this.matcher = matcher;
        this.delegate = delegate;
        this.bytecodeDumpService = new ASMBytecodeDumpService(simulatorConfig);
        this.dumpResult = new HashMap<String, File>();
        this.isDumpFile = isDumpFile;
        this.isEnableUnsafe = isEnableUnsafe;
        this.dumpByteCodePath = simulatorConfig.getProperty(BYTECODE_DUMP_BYTECODE_PATH, ".");
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {

        byte[] transformBytes = null;
        boolean success = false;
        try {
            transformBytes = delegate.transform(loader, className, classBeingRedefined, protectionDomain, classfileBuffer);
            if (transformBytes != null) {
                success = true;
            }
            return transformBytes;
        } finally {
            boolean isDumpFile = this.isDumpFile && isDumpMatches(loader, classBeingRedefined, classfileBuffer);
            if (success || isDumpFile) {
                this.bytecodeDumpService.dumpBytecode("original bytecode dump", dumpByteCodePath + File.separator + "_old", className, isDumpFile, classfileBuffer, loader);
                if (isDumpFile && this.bytecodeDumpService.getDumpFile() != null && transformBytes == null) {
                    this.dumpResult.put(className.replace('/', '.'), this.bytecodeDumpService.getDumpFile());
                }
                if (isChanged(classfileBuffer, transformBytes)) {
                    this.bytecodeDumpService.dumpBytecode("enhance bytecode dump", dumpByteCodePath, className, isDumpFile, transformBytes, loader);
                    if (isDumpFile && this.bytecodeDumpService.getDumpFile() != null) {
                        this.dumpResult.put(className.replace('/', '.'), this.bytecodeDumpService.getDumpFile());
                    }
                }
            }
        }
    }

    private boolean isDumpMatches(ClassLoader loader, Class classBeingRedefined, byte[] classfileBuffer) {
        if (matcher == null) {
            return false;
        }
        final ClassStructure classStructure = getClassStructure(loader, classBeingRedefined, classfileBuffer);
        final MatchingResult matchingResult = new UnsupportedMatcher(loader, isEnableUnsafe).and(matcher).matching(classStructure);
        return matchingResult.isMatched();
    }

    // 获取当前类结构
    private ClassStructure getClassStructure(final ClassLoader loader,
                                             final Class<?> classBeingRedefined,
                                             final byte[] srcByteCodeArray) {
        return null == classBeingRedefined
                ? ClassStructureFactory.createClassStructure(srcByteCodeArray, loader)
                : ClassStructureFactory.createClassStructure(classBeingRedefined);
    }

    private boolean isChanged(byte[] classfileBuffer, byte[] transformBytes) {
        if (transformBytes == null) {
            return false;
        }
        if (classfileBuffer == transformBytes) {
            return false;
        }
        return true;
    }


    @Override
    public int getWatchId() {
        return delegate.getWatchId();
    }

    @Override
    public Map<Integer, EventListener> getEventListeners() {
        return delegate.getEventListeners();
    }

    @Override
    public List<BuildingForListeners> getAllListeners() {
        return delegate.getAllListeners();
    }

    @Override
    public Object getMatcher() {
        return delegate.getMatcher();
    }

    @Override
    public AffectStatistic getAffectStatistic() {
        return delegate.getAffectStatistic();
    }

    @Override
    public Map<String, File> getDumpResult() {
        return dumpResult;
    }
}
