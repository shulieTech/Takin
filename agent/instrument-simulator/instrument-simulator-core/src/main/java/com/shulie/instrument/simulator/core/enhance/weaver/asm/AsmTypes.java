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
package com.shulie.instrument.simulator.core.enhance.weaver.asm;

import com.shulie.instrument.simulator.message.Messager;
import com.shulie.instrument.simulator.message.Result;
import org.objectweb.asm.Type;

/**
 * 常用的ASM type集合
 * 省得我到处声明
 */
public interface AsmTypes {

    Type ASM_TYPE_MESSAGER = Type.getType(Messager.class);
    Type ASM_TYPE_OBJECT = Type.getType(Object.class);
    Type ASM_TYPE_INT = Type.getType(int.class);
    Type ASM_TYPE_MESSAGER_RESULT = Type.getType(Result.class);
    Type ASM_TYPE_THROWABLE = Type.getType(Throwable.class);

}
