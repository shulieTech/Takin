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
package com.pamirs.attach.plugin.apache.kafka.header;

import com.pamirs.attach.plugin.apache.kafka.header.impl.DefaultHeaderGetter;
import com.pamirs.attach.plugin.apache.kafka.header.impl.DefaultHeaderSetter;
import com.pamirs.attach.plugin.apache.kafka.header.impl.DisabledHeaderGetter;
import com.pamirs.attach.plugin.apache.kafka.header.impl.DisabledHeaderSetter;

import java.lang.reflect.Method;

/**
 * @Description
 * @Author xiaobin.zfb
 * @mail xiaobin@shulie.io
 * @Date 2020/7/10 7:57 下午
 */
public class HeaderProvider {
    public final static HeaderGetter getHeaderGetter(Object record) {
        try {
            final Class<?> aClass = record.getClass();
            final Method method = aClass.getMethod("headers");
            if (method != null) {
                return new DefaultHeaderGetter();
            }
        } catch (NoSuchMethodException e) {
            // ignore
        }
        return new DisabledHeaderGetter();
    }

    public final static HeaderSetter getHeaderSetter(Object record) {
        try {
            final Class<?> aClass = record.getClass();
            final Method method = aClass.getMethod("headers");
            if (method != null) {
                return new DefaultHeaderSetter();
            }
        } catch (NoSuchMethodException e) {
            // ignore
        }
        return new DisabledHeaderSetter();
    }
}
