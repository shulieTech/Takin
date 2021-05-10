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
package com.pamirs.pradar;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author fabing.zhaofb
 */
public abstract class LoadMsg {
    static volatile AtomicBoolean isLoadFilter = new AtomicBoolean(false);

    public static boolean isLoadFilter() {
        return isLoadFilter.get();
    }

    /**
     * 尝试将加载Filter状态置成已加载
     *
     * @return 返回设置是否成功，如果成功返回true，如果设置失败，则代表已经加载过相关的Filter，则返回false
     */
    public static boolean trySetFilterLoad() {
        return isLoadFilter.compareAndSet(false, true);
    }
}
