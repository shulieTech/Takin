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

package io.shulie.tro.web.common.future;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @Description
 * @Author xiaobin.zfb
 * @mail xiaobin@shulie.io
 * @Date 2020/8/14 2:50 下午
 */
public class ResponseFuture<T> {
    private final long timeoutMillis;
    private final long startInMillis = System.currentTimeMillis();
    private final CountDownLatch countDownLatch = new CountDownLatch(1);

    private volatile T response;
    private volatile Throwable cause;
    private volatile boolean success;
    private volatile boolean isDone;

    public ResponseFuture(long timeoutMillis) {
        this.timeoutMillis = timeoutMillis;
    }

    public boolean isTimeout() {
        return System.currentTimeMillis() - this.startInMillis >= this.timeoutMillis;
    }

    public boolean isTimeout(long timeoutMillis) {
        return System.currentTimeMillis() - this.startInMillis >= timeoutMillis;
    }

    public T waitFor() throws InterruptedException {
        this.countDownLatch.await(timeoutMillis, TimeUnit.MILLISECONDS);
        return this.response;
    }

    public void success(final T response) {
        this.response = response;
        this.success = true;
        this.isDone = true;
        this.countDownLatch.countDown();
    }

    public void failure(Throwable throwable) {
        this.cause = throwable;
        this.success = false;
        this.isDone = true;
        this.countDownLatch.countDown();
    }

    public T getResponse() {
        return response;
    }

    public Throwable getCause() {
        return cause;
    }

    public boolean isSuccess() {
        return success;
    }

    public boolean isDone() {
        return isDone;
    }

    public long getStartInMillis() {
        return startInMillis;
    }

    public long getTimeoutMillis() {
        return timeoutMillis;
    }

    @Override
    public String toString() {
        return "ReplyFuture [response=" + response + ", cause=" + cause + "]";
    }
}
