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

import java.net.URL;
import java.util.Enumeration;

/**
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/9/24 5:30 下午
 */
public class UseFastConnectionExceptionsEnumeration implements Enumeration<URL> {

    private final Enumeration<URL> delegate;

    public UseFastConnectionExceptionsEnumeration(Enumeration<URL> delegate) {
        this.delegate = delegate;
    }

    @Override
    public boolean hasMoreElements() {
        return this.delegate.hasMoreElements();

    }

    @Override
    public URL nextElement() {
        return this.delegate.nextElement();
    }
}
