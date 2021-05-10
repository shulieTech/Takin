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
package com.pamirs.attach.plugin.dbcp2;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 监听器注册状态
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/12/22 4:47 下午
 */
public class ListenerRegisterStatus {

    private static ListenerRegisterStatus INSTANCE;

    private final AtomicBoolean isInited = new AtomicBoolean(false);

    public static ListenerRegisterStatus getInstance() {
        if (INSTANCE == null) {
            synchronized (ListenerRegisterStatus.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ListenerRegisterStatus();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 判断是否已经初始化
     *
     * @return 返回初始化状态, true|false
     */
    public boolean isInited() {
        return isInited.get();
    }

    /**
     * 进行初始化操作，如果失败则返回 false
     *
     * @return 如果初始化失败则返回 false
     */
    public boolean init() {
        return isInited.compareAndSet(false, true);
    }
}
