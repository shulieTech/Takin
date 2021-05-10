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

import org.objectweb.asm.commons.AdviceAdapter;

/**
 * 用于方法调用的代码锁
 */
class AsmCallCodeLock extends AsmCodeLock {

    AsmCallCodeLock(AdviceAdapter aa) {
        super(
                aa,
                new int[]{
                        ICONST_2, POP
                },
                new int[]{
                        ICONST_3, POP
                }
        );
    }
}
