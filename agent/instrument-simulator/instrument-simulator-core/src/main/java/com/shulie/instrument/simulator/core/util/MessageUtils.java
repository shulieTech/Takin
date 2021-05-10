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

import com.shulie.instrument.simulator.core.enhance.weaver.EventListenerHandler;
import com.shulie.instrument.simulator.message.Messager;

/**
 * Messager类操作工具类
 */
public class MessageUtils {


    /**
     * 初始化Messager类
     *
     * @param namespace 命名空间
     */
    public synchronized static void init(final String namespace) {

        if (!Messager.isInit(namespace)) {
            Messager.init(namespace, EventListenerHandler.getSingleton());
        }

    }

    /**
     * 清理Messager中的命名空间
     *
     * @param namespace 命名空间
     */
    public synchronized static void clean(final String namespace) {
        Messager.clean(namespace);
    }

}
