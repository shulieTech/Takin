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

import java.util.Enumeration;
import java.util.NoSuchElementException;

/**
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/9/24 5:29 下午
 */
public class CompoundEnumeration<E> implements Enumeration<E> {
    private Enumeration<E>[] enums;
    private int index = 0;

    public CompoundEnumeration(Enumeration<E>[] enumerations) {
        this.enums = enumerations;
    }

    private boolean next() {
        while (this.index < this.enums.length) {
            if (this.enums[this.index] != null && this.enums[this.index].hasMoreElements()) {
                return true;
            }

            ++this.index;
        }

        return false;
    }

    @Override
    public boolean hasMoreElements() {
        return this.next();
    }

    @Override
    public E nextElement() {
        if (!this.next()) {
            throw new NoSuchElementException();
        } else {
            return this.enums[this.index].nextElement();
        }
    }
}
