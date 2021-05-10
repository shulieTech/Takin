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
package com.shulie.instrument.simulator.core.instrument;

import com.shulie.instrument.simulator.api.instrument.InstrumentClass;
import com.shulie.instrument.simulator.api.instrument.InstrumentMethod;
import com.shulie.instrument.simulator.api.listener.ext.EventWatcher;
import com.shulie.instrument.simulator.api.listener.ext.IBehaviorMatchBuilder;
import com.shulie.instrument.simulator.api.listener.ext.IClassMatchBuilder;

import java.util.Collection;

/**
 * 默认的增强类实现
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/10/11 6:08 下午
 */
public class DefaultInstrumentClass implements InstrumentClass {
    private IClassMatchBuilder buildingForClass;
    private EventWatcher eventWatcher;

    public DefaultInstrumentClass(IClassMatchBuilder buildingForClass) {
        this.buildingForClass = buildingForClass;
    }

    @Override
    public void includeBootstrap() {
        this.buildingForClass.includeBootstrap();
    }

    @Override
    public void isIncludeBootstrap(boolean isIncludeBootstrap) {
        this.buildingForClass.isIncludeBootstrap(isIncludeBootstrap);
    }

    @Override
    public InstrumentMethod getDeclaredMethod(String methodName, String... parameterTypes) {
        IBehaviorMatchBuilder buildingForBehavior = this.buildingForClass.onBehavior(methodName)
                .withParameterTypes(parameterTypes);
        return new DefaultInstrumentMethod(buildingForBehavior);
    }

    @Override
    public InstrumentMethod getDeclaredMethod(String methodName, int index1, String parameterType1) {
        IBehaviorMatchBuilder buildingForBehavior = this.buildingForClass.onBehavior(methodName)
                .withParameterType(index1, parameterType1);
        return new DefaultInstrumentMethod(buildingForBehavior);
    }

    @Override
    public InstrumentMethod getDeclaredMethod(String methodName, int index1, String parameterType1, int index2, String parameterType2) {
        IBehaviorMatchBuilder buildingForBehavior = this.buildingForClass.onBehavior(methodName)
                .withParameterType(index1, parameterType1)
                .withParameterType(index2, parameterType2);
        return new DefaultInstrumentMethod(buildingForBehavior);
    }

    @Override
    public InstrumentMethod getDeclaredMethod(String methodName, int index1, String parameterType1, int index2, String parameterType2, int index3, String parameterType3) {
        IBehaviorMatchBuilder buildingForBehavior = this.buildingForClass.onBehavior(methodName)
                .withParameterType(index1, parameterType1)
                .withParameterType(index2, parameterType2)
                .withParameterType(index3, parameterType3);
        return new DefaultInstrumentMethod(buildingForBehavior);
    }

    @Override
    public InstrumentMethod getDeclaredMethod(String methodName, int index1, String parameterType1, int index2, String parameterType2, int index3, String parameterType3, int index4, String parameterType4) {
        IBehaviorMatchBuilder buildingForBehavior = this.buildingForClass.onBehavior(methodName)
                .withParameterType(index1, parameterType1)
                .withParameterType(index2, parameterType2)
                .withParameterType(index3, parameterType3)
                .withParameterType(index4, parameterType4);
        return new DefaultInstrumentMethod(buildingForBehavior);
    }

    @Override
    public InstrumentMethod getDeclaredMethod(String methodName, int index1, String parameterType1, int index2, String parameterType2, int index3, String parameterType3, int index4, String parameterType4, int index5, String parameterType5) {
        IBehaviorMatchBuilder buildingForBehavior = this.buildingForClass.onBehavior(methodName)
                .withParameterType(index1, parameterType1)
                .withParameterType(index2, parameterType2)
                .withParameterType(index3, parameterType3)
                .withParameterType(index4, parameterType4)
                .withParameterType(index5, parameterType5);
        return new DefaultInstrumentMethod(buildingForBehavior);
    }

    @Override
    public InstrumentMethod getDeclaredMethod(String methodName, int index1, String parameterType1, int index2, String parameterType2, int index3, String parameterType3, int index4, String parameterType4, int index5, String parameterType5, int index6, String parameterType6) {
        IBehaviorMatchBuilder buildingForBehavior = this.buildingForClass.onBehavior(methodName)
                .withParameterType(index1, parameterType1)
                .withParameterType(index2, parameterType2)
                .withParameterType(index3, parameterType3)
                .withParameterType(index4, parameterType4)
                .withParameterType(index5, parameterType5)
                .withParameterType(index6, parameterType6);
        return new DefaultInstrumentMethod(buildingForBehavior);
    }

    @Override
    public InstrumentMethod getDeclaredMethod(String methodName, int index1, String parameterType1, int index2, String parameterType2, int index3, String parameterType3, int index4, String parameterType4, int index5, String parameterType5, int index6, String parameterType6, int index7, String parameterType7) {
        IBehaviorMatchBuilder buildingForBehavior = this.buildingForClass.onBehavior(methodName)
                .withParameterType(index1, parameterType1)
                .withParameterType(index2, parameterType2)
                .withParameterType(index3, parameterType3)
                .withParameterType(index4, parameterType4)
                .withParameterType(index5, parameterType5)
                .withParameterType(index6, parameterType6)
                .withParameterType(index7, parameterType7);
        return new DefaultInstrumentMethod(buildingForBehavior);
    }

    @Override
    public InstrumentMethod getDeclaredMethod(String methodName, int index1, String parameterType1, int index2, String parameterType2, int index3, String parameterType3, int index4, String parameterType4, int index5, String parameterType5, int index6, String parameterType6, int index7, String parameterType7, int index8, String parameterType8) {
        IBehaviorMatchBuilder buildingForBehavior = this.buildingForClass.onBehavior(methodName)
                .withParameterType(index1, parameterType1)
                .withParameterType(index2, parameterType2)
                .withParameterType(index3, parameterType3)
                .withParameterType(index4, parameterType4)
                .withParameterType(index5, parameterType5)
                .withParameterType(index6, parameterType6)
                .withParameterType(index7, parameterType7)
                .withParameterType(index8, parameterType8);
        return new DefaultInstrumentMethod(buildingForBehavior);
    }

    @Override
    public InstrumentMethod getDeclaredMethod(String methodName, int index1, String parameterType1, int index2, String parameterType2, int index3, String parameterType3, int index4, String parameterType4, int index5, String parameterType5, int index6, String parameterType6, int index7, String parameterType7, int index8, String parameterType8, int index9, String parameterType9) {
        IBehaviorMatchBuilder buildingForBehavior = this.buildingForClass.onBehavior(methodName)
                .withParameterType(index1, parameterType1)
                .withParameterType(index2, parameterType2)
                .withParameterType(index3, parameterType3)
                .withParameterType(index4, parameterType4)
                .withParameterType(index5, parameterType5)
                .withParameterType(index6, parameterType6)
                .withParameterType(index7, parameterType7)
                .withParameterType(index8, parameterType8)
                .withParameterType(index9, parameterType9);
        return new DefaultInstrumentMethod(buildingForBehavior);
    }

    @Override
    public InstrumentMethod getDeclaredMethod(String methodName, int index1, String parameterType1, int index2, String parameterType2, int index3, String parameterType3, int index4, String parameterType4, int index5, String parameterType5, int index6, String parameterType6, int index7, String parameterType7, int index8, String parameterType8, int index9, String parameterType9, int index10, String parameterType10) {
        IBehaviorMatchBuilder buildingForBehavior = this.buildingForClass.onBehavior(methodName)
                .withParameterType(index1, parameterType1)
                .withParameterType(index2, parameterType2)
                .withParameterType(index3, parameterType3)
                .withParameterType(index4, parameterType4)
                .withParameterType(index5, parameterType5)
                .withParameterType(index6, parameterType6)
                .withParameterType(index7, parameterType7)
                .withParameterType(index8, parameterType8)
                .withParameterType(index9, parameterType9)
                .withParameterType(index10, parameterType10);
        return new DefaultInstrumentMethod(buildingForBehavior);
    }

    @Override
    public InstrumentMethod getConstructor(String... parameterTypes) {
        IBehaviorMatchBuilder buildingForBehavior = this.buildingForClass.onBehavior("<init>")
                .withParameterTypes(parameterTypes);
        return new DefaultInstrumentMethod(buildingForBehavior);
    }

    @Override
    public InstrumentMethod getConstructor(int index1, String parameterType1) {
        IBehaviorMatchBuilder buildingForBehavior = this.buildingForClass.onBehavior("<init>")
                .withParameterType(index1, parameterType1);
        return new DefaultInstrumentMethod(buildingForBehavior);
    }

    @Override
    public InstrumentMethod getConstructor(int index1, String parameterType1, int index2, String parameterType2) {
        IBehaviorMatchBuilder buildingForBehavior = this.buildingForClass.onBehavior("<init>")
                .withParameterType(index1, parameterType1)
                .withParameterType(index2, parameterType2);
        return new DefaultInstrumentMethod(buildingForBehavior);
    }

    @Override
    public InstrumentMethod getConstructor(int index1, String parameterType1, int index2, String parameterType2, int index3, String parameterType3) {
        IBehaviorMatchBuilder buildingForBehavior = this.buildingForClass.onBehavior("<init>")
                .withParameterType(index1, parameterType1)
                .withParameterType(index2, parameterType2)
                .withParameterType(index3, parameterType3);
        return new DefaultInstrumentMethod(buildingForBehavior);
    }

    @Override
    public InstrumentMethod getConstructor(int index1, String parameterType1, int index2, String parameterType2, int index3, String parameterType3, int index4, String parameterType4) {
        IBehaviorMatchBuilder buildingForBehavior = this.buildingForClass.onBehavior("<init>")
                .withParameterType(index1, parameterType1)
                .withParameterType(index2, parameterType2)
                .withParameterType(index3, parameterType3)
                .withParameterType(index4, parameterType4);
        return new DefaultInstrumentMethod(buildingForBehavior);
    }

    @Override
    public InstrumentMethod getConstructor(int index1, String parameterType1, int index2, String parameterType2, int index3, String parameterType3, int index4, String parameterType4, int index5, String parameterType5) {
        IBehaviorMatchBuilder buildingForBehavior = this.buildingForClass.onBehavior("<init>")
                .withParameterType(index1, parameterType1)
                .withParameterType(index2, parameterType2)
                .withParameterType(index3, parameterType3)
                .withParameterType(index4, parameterType4)
                .withParameterType(index5, parameterType5);
        return new DefaultInstrumentMethod(buildingForBehavior);
    }

    @Override
    public InstrumentMethod getConstructor(int index1, String parameterType1, int index2, String parameterType2, int index3, String parameterType3, int index4, String parameterType4, int index5, String parameterType5, int index6, String parameterType6) {
        IBehaviorMatchBuilder buildingForBehavior = this.buildingForClass.onBehavior("<init>")
                .withParameterType(index1, parameterType1)
                .withParameterType(index2, parameterType2)
                .withParameterType(index3, parameterType3)
                .withParameterType(index4, parameterType4)
                .withParameterType(index5, parameterType5)
                .withParameterType(index6, parameterType6);
        return new DefaultInstrumentMethod(buildingForBehavior);
    }

    @Override
    public InstrumentMethod getConstructor(int index1, String parameterType1, int index2, String parameterType2, int index3, String parameterType3, int index4, String parameterType4, int index5, String parameterType5, int index6, String parameterType6, int index7, String parameterType7) {
        IBehaviorMatchBuilder buildingForBehavior = this.buildingForClass.onBehavior("<init>")
                .withParameterType(index1, parameterType1)
                .withParameterType(index2, parameterType2)
                .withParameterType(index3, parameterType3)
                .withParameterType(index4, parameterType4)
                .withParameterType(index5, parameterType5)
                .withParameterType(index6, parameterType6)
                .withParameterType(index7, parameterType7);
        return new DefaultInstrumentMethod(buildingForBehavior);
    }

    @Override
    public InstrumentMethod getConstructor(int index1, String parameterType1, int index2, String parameterType2, int index3, String parameterType3, int index4, String parameterType4, int index5, String parameterType5, int index6, String parameterType6, int index7, String parameterType7, int index8, String parameterType8) {
        IBehaviorMatchBuilder buildingForBehavior = this.buildingForClass.onBehavior("<init>")
                .withParameterType(index1, parameterType1)
                .withParameterType(index2, parameterType2)
                .withParameterType(index3, parameterType3)
                .withParameterType(index4, parameterType4)
                .withParameterType(index5, parameterType5)
                .withParameterType(index6, parameterType6)
                .withParameterType(index7, parameterType7)
                .withParameterType(index8, parameterType8);
        return new DefaultInstrumentMethod(buildingForBehavior);
    }

    @Override
    public InstrumentMethod getConstructor(int index1, String parameterType1, int index2, String parameterType2, int index3, String parameterType3, int index4, String parameterType4, int index5, String parameterType5, int index6, String parameterType6, int index7, String parameterType7, int index8, String parameterType8, int index9, String parameterType9) {
        IBehaviorMatchBuilder buildingForBehavior = this.buildingForClass.onBehavior("<init>")
                .withParameterType(index1, parameterType1)
                .withParameterType(index2, parameterType2)
                .withParameterType(index3, parameterType3)
                .withParameterType(index4, parameterType4)
                .withParameterType(index5, parameterType5)
                .withParameterType(index6, parameterType6)
                .withParameterType(index7, parameterType7)
                .withParameterType(index8, parameterType8)
                .withParameterType(index9, parameterType9);
        return new DefaultInstrumentMethod(buildingForBehavior);
    }

    @Override
    public InstrumentMethod getConstructor(int index1, String parameterType1, int index2, String parameterType2, int index3, String parameterType3, int index4, String parameterType4, int index5, String parameterType5, int index6, String parameterType6, int index7, String parameterType7, int index8, String parameterType8, int index9, String parameterType9, int index10, String parameterType10) {
        IBehaviorMatchBuilder buildingForBehavior = this.buildingForClass.onBehavior("<init>")
                .withParameterType(index1, parameterType1)
                .withParameterType(index2, parameterType2)
                .withParameterType(index3, parameterType3)
                .withParameterType(index4, parameterType4)
                .withParameterType(index5, parameterType5)
                .withParameterType(index6, parameterType6)
                .withParameterType(index7, parameterType7)
                .withParameterType(index8, parameterType8)
                .withParameterType(index9, parameterType9)
                .withParameterType(index10, parameterType10);
        return new DefaultInstrumentMethod(buildingForBehavior);
    }

    @Override
    public InstrumentMethod getConstructors() {
        IBehaviorMatchBuilder buildingForBehavior = this.buildingForClass.onAnyBehavior("<init>");
        return new DefaultInstrumentMethod(buildingForBehavior);
    }

    @Override
    public InstrumentMethod getStaticInitBlock() {
        IBehaviorMatchBuilder buildingForBehavior = this.buildingForClass.onBehavior("<cinit>");
        return new DefaultInstrumentMethod(buildingForBehavior);
    }

    @Override
    public InstrumentMethod getDeclaredMethods(Collection<String> methodNames) {
        IBehaviorMatchBuilder buildingForBehavior = this.buildingForClass.onAnyBehavior(methodNames.toArray(new String[methodNames.size()]));
        return new DefaultInstrumentMethod(buildingForBehavior);
    }

    @Override
    public InstrumentMethod getDeclaredMethods(String... methodNames) {
        IBehaviorMatchBuilder buildingForBehavior = this.buildingForClass.onAnyBehavior(methodNames);
        return new DefaultInstrumentMethod(buildingForBehavior);
    }

    public void execute() {
        this.eventWatcher = this.buildingForClass.onWatch();
    }

    public EventWatcher getEventWatcher() {
        return eventWatcher;
    }

    @Override
    public void reset() {
        eventWatcher.onUnWatched();
    }
}
