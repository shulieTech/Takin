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
package com.shulie.instrument.simulator.api.resource;

import com.shulie.instrument.simulator.api.listener.ext.Progress;

/**
 * 无操作的进度器
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/9/27 7:34 下午
 */
public class NoActionProgress implements Progress {
    @Override
    public void begin(int total) {

    }

    @Override
    public void progressOnSuccess(Class<?> clazz, int index) {

    }

    @Override
    public void progressOnFailed(Class<?> clazz, int index, Throwable cause) {

    }

    @Override
    public void finish(int cCnt, int mCnt) {

    }
}
