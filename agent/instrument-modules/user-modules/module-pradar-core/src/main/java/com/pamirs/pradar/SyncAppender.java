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

/**
 * 给 Appender 的所有操作加上同步
 */
class SyncAppender extends PradarAppender {

    private final PradarAppender delegate;

    public SyncAppender(PradarAppender delegate) {
        this.delegate = delegate;
    }

    @Override
    public synchronized void append(String log) {
        delegate.append(log);
    }

    @Override
    public synchronized void flush() {
        delegate.flush();
    }

    @Override
    public synchronized void rollOver() {
        delegate.rollOver();
    }

    @Override
    public synchronized void reload() {
        delegate.reload();
    }

    @Override
    public synchronized void close() {
        delegate.close();
    }

    /**
     * 清理动作和当前写入的文件无关，因此没有加同步
     */
    @Override
    public void cleanup() {
        delegate.cleanup();
    }

    @Override
    public String toString() {
        return "SyncAppender [appender=" + delegate + "]";
    }
}
