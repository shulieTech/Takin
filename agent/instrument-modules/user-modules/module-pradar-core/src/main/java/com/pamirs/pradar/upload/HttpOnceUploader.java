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
 * 只会上报一次
 *
 * @author shiyajian
 * create: 2020-07-20
 */
public abstract class HttpOnceUploader<T> extends HttpUploader<T> {

    private final static Logger LOGGER = LoggerFactory.getLogger(HttpOnceUploader.class);

    /**
     * 是否已经上传过了
     */
    private boolean isUploaded;

    /**
     * 是否已经上传成功
     */
    private volatile boolean isSuccess;

    /**
     * 已经重试过的次数
     */
    private long retriedTimes;

    /**
     * 重试次数，如果为负数，表示无限制重置，直到成功为止
     */
    protected long retryTimes;

    /**
     * 假如上传失败，隔多少秒之后再次上传，直到上传成功为止
     * 小于0，表示上传失败后就不再上传了
     */
    protected int retrySecond;

    protected ScheduledFuture future;

    public HttpOnceUploader(String name, String postUrl, long retryMaxTimes, int retrySecond) {
        super(name, postUrl);
        this.isUploaded = false;
        this.isSuccess = false;
        this.retriedTimes = 0;
        this.retryTimes = retryMaxTimes;
        this.retrySecond = retrySecond;
    }

    /**
     * 新起线程只上传一次
     */
    @Override
    public synchronized void start() {
        if (!enabled()) {
            return;
        }
        if (isUploaded) {
            return;
        }
        isUploaded = true;
        ExecutorServiceFactory.GLOBAL_EXECUTOR_SERVICE.submit(new Runnable() {
            @Override
            public void run() {
                HttpUtils.HttpResult httpResult = innerUpload();
                if (httpResult.isSuccess()) {
                    isSuccess = true;
                    LOGGER.error("Agent Info upload success");
                    return;
                }
                retry();
                LOGGER.error("Agent upload failed, can't access Config Center! HttpStatus Code: " + httpResult.getStatus() + ",message:" + httpResult.getResult());
            }
        });

    }

    public void shutdown() {
        if (future != null && !future.isDone() && !future.isCancelled()) {
            future.cancel(true);
        }
    }

    private void retry() {
        future = ExecutorServiceFactory.GLOBAL_SCHEDULE_EXECUTOR_SERVICE.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                // 不重试
                if (retryTimes == 0) {
                    return;
                }
                // 重试次数已经超过最大限制
                if (retryTimes > 0 && retryTimes < retriedTimes) {
                    shutdown();
                    return;
                }
                // 已经成功
                if (isSuccess) {
                    shutdown();
                    return;
                }
                HttpUtils.HttpResult httpResult = innerUpload();
                if (httpResult.isSuccess()) {
                    isSuccess = true;
                    shutdown();
                }
                retriedTimes++;
                LOGGER.error(String.format("Agent Info upload failed ，access Config Center failed! Retry after %s second", retrySecond));
            }
        }, retrySecond, retrySecond, TimeUnit.SECONDS);
    }

    public boolean isSuccess() {
        return this.isSuccess;
    }
}

