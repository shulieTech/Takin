/*
 * Copyright 2021 Shulie Technology, Co.Ltd
 * Email: shulie@shulie.io
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.shulie.surge.data.runtime.digest;

import io.shulie.surge.data.common.lifecycle.Stoppable;

import java.io.Serializable;

/**
 * 实时数据处理
 *
 * @author pamirs
 */
public interface DataDigester<T extends Serializable> extends Stoppable {
    /**
     * 处理数据
     *
     * @param context
     */
    void digest(DigestContext<T> context);

    /**
     * 多线程执行的线程数，默认为 1
     *
     * @return 线程数
     */
    int threadCount();

}
