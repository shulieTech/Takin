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
package com.pamirs.pradar.upload;

import com.pamirs.pradar.common.HttpUtils;
import com.shulie.instrument.simulator.api.executors.ExecutorServiceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * 周期性轮询上报
 *
 * @author shiyajian
 * create: 2020-07-20
 */
public abstract class HttpScheduledUploader<T> extends HttpUploader<T> {

    private final static Logger LOGGER = LoggerFactory.getLogger(HttpScheduledUploader.class);

    /**
     * 已经上传的次数
     */
    protected long uploadCount;

    /**
     * 是否在运行中
     */
    private boolean running;

    /**
     * 时间周期，每过多少时间上报一次，单位:秒
     */
    protected int uploadPeriodSecond;

    protected ScheduledFuture future;

    public HttpScheduledUploader(String name, String postUrl, int uploadPeriodSecond) {
        super(name, postUrl);
        this.uploadCount = 0;
        this.running = false;
        this.uploadPeriodSecond = (uploadPeriodSecond <= 0) ? 3 : uploadPeriodSecond; // default 3
    }


    /**
     * 是否准备好了
     * 准备好了开始上报，否则不会进行上报
     */
    protected abstract boolean isReady();

    /**
     * 已经上传的次数
     */
    public long getUploadedCount() {
        return this.uploadCount;
    }

    @Override
    public synchronized void start() {
        if (!enabled()) {
            return;
        }
        if (running) {
            return;
        }
        running = true;

        future = ExecutorServiceFactory.GLOBAL_SCHEDULE_EXECUTOR_SERVICE.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if (!running) {
                    return;
                }
                if (!isReady()) {
                    return;
                }
                HttpUtils.HttpResult httpResult = innerUpload();
                if (!httpResult.isSuccess()) {
                    LOGGER.error("Agent Health Check failed，access Config Center failed! HttpStatus Code: " + httpResult.getStatus() + ",message ：" + httpResult.getResult());
                } else {
                    LOGGER.info("Agent Health Check success！");
                }
                uploadCount++;
            }
        }, uploadPeriodSecond, uploadPeriodSecond, TimeUnit.SECONDS);
    }

    public synchronized void stop() {
        if (!running) {
            return;
        }
        running = false;
        if (future != null && !future.isCancelled() && !future.isDone()) {
            future.cancel(true);
        }
    }
}
