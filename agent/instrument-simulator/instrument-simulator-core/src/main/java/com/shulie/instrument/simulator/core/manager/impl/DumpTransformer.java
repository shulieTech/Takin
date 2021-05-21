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
import com.shulie.instrument.simulator.core.CoreModule;
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
public class DumpTransformer extends SimulatorClassFileTransformer {
    public static final String BYTECODE_DUMP_BYTECODE_PATH = "simulator.dump.bytecode.path";

    private final BytecodeDumpService bytecodeDumpService;
    private final Map<String, File> dumpResult;
    private final boolean isEnableUnsafe;
    private final Matcher matcher;
    private final String dumpByteCodePath;
    private final int watchId;
    private final AffectStatistic affectStatistic = new AffectStatistic();
    private final Map<Integer, EventListener> eventListeners = new HashMap<Integer, EventListener>();
    private final List<BuildingForListeners> listeners;

    public static DumpTransformer build(CoreModule coreModule, SimulatorConfig profilerConfig, Matcher matcher, boolean isEnableUnsafe, int watchId) {
        return new DumpTransformer(coreModule, profilerConfig, matcher, isEnableUnsafe, watchId);
    }

    public static DumpTransformer build(CoreModule coreModule, SimulatorConfig profilerConfig, int watchId) {
        return new DumpTransformer(coreModule, profilerConfig, null, false, watchId);
    }

    private DumpTransformer(CoreModule coreModule, SimulatorConfig simulatorConfig, Matcher matcher, boolean isEnableUnsafe, int watchId) {
        if (simulatorConfig == null) {
            throw new NullPointerException("profilerConfig must not be null");
        }
        this.matcher = matcher;
        this.bytecodeDumpService = new ASMBytecodeDumpService(simulatorConfig);
        this.dumpResult = new HashMap<String, File>();
        this.isEnableUnsafe = isEnableUnsafe;
        this.dumpByteCodePath = simulatorConfig.getProperty(BYTECODE_DUMP_BYTECODE_PATH, ".");
        this.watchId = watchId;
        List<BuildingForListeners> listeners = matcher.getAllListeners();
        for (BuildingForListeners listener : listeners) {
            eventListeners.put(listener.getListenerId(), new LazyEventListenerProxy(coreModule, listener.getListeners()));
        }
        this.listeners = matcher.getAllListeners();
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        try {
            return null;
        } finally {
            boolean isDumpFile = isDumpMatches(loader, classBeingRedefined, classfileBuffer);
            if (isDumpFile) {
                this.bytecodeDumpService.dumpBytecode("original bytecode dump", dumpByteCodePath + File.separator + "dump", className, isDumpFile, classfileBuffer, loader);
                if (this.bytecodeDumpService.getDumpFile() != null) {
                    this.dumpResult.put(className.replace('/', '.'), this.bytecodeDumpService.getDumpFile());
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

    @Override
    public int getWatchId() {
        return watchId;
    }

    @Override
    public Map<Integer, EventListener> getEventListeners() {
        return eventListeners;
    }

    @Override
    public List<BuildingForListeners> getAllListeners() {
        return listeners;
    }

    @Override
    public Object getMatcher() {
        return matcher;
    }

    @Override
    public AffectStatistic getAffectStatistic() {
        return affectStatistic;
    }

    @Override
    public Map<String, File> getDumpResult() {
        return dumpResult;
    }
}
