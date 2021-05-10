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
package com.shulie.instrument.simulator.api.listener.ext;


import java.util.List;

/**
 * 观察进度组
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/10/23 10:47 下午
 */
class ProgressGroup implements Progress {

    private final List<Progress> progresses;

    ProgressGroup(List<Progress> progresses) {
        this.progresses = progresses;
    }

    @Override
    public void begin(int total) {
        for (final Progress progress : progresses) {
            progress.begin(total);
        }
    }

    @Override
    public void progressOnSuccess(Class<?> clazz, int index) {
        for (final Progress progress : progresses) {
            progress.progressOnSuccess(clazz, index);
        }
    }

    @Override
    public void progressOnFailed(Class<?> clazz, int index, Throwable cause) {
        for (final Progress progress : progresses) {
            progress.progressOnFailed(clazz, index, cause);
        }
    }

    @Override
    public void finish(int cCnt, int mCnt) {
        for (final Progress progress : progresses) {
            progress.finish(cCnt, mCnt);
        }
    }

}
