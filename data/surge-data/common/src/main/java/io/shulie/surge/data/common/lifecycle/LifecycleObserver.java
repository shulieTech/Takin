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

package io.shulie.surge.data.common.lifecycle;

/**
 * 监听类 T 的对象生命周期事件，处理中不应该抛出异常，
 * 否则可能影响 {@link Lifecycle} 的执行流程。
 *
 * @author pamirs
 */
public interface LifecycleObserver<T extends Lifecycle> {

    void beforeStart(T target);
    void afterStart(T target);
    void beforeStop(T target);
    void afterStop(T target);
}
