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

package io.shulie.surge.data.runtime.disruptor;

import java.util.concurrent.*;


/**
 * 默认的连接池
 */
public class PradarPoolExecutors {
    /**
     * 无队列线程池
     *
     * @param coreSize
     * @return
     */
    public static ExecutorService newDefaultNoQueueExecutors(int coreSize, int maxPoolSize, int keepAliveTime, TimeUnit timeUnit, ThreadFactory threadFactory, RejectedExecutionHandler rejectedExecutionHandler) {

        BlockingQueue blockingQueue = new SynchronousQueue<>();
        if (rejectedExecutionHandler != null) {
            return new ThreadPoolExecutor(coreSize, maxPoolSize, keepAliveTime,
                    timeUnit, blockingQueue, threadFactory, rejectedExecutionHandler);
        }
        return new ThreadPoolExecutor(coreSize, maxPoolSize, keepAliveTime,
                timeUnit, blockingQueue, threadFactory);
    }


    /**
     * 有队列队列线程池
     *
     * @param coreSize
     * @return
     */
    public static ExecutorService newDefaultQueueExecutors(int coreSize, int maxSize, int keepAliveTime, int queueSize, TimeUnit timeUnit, ThreadFactory threadFactory, RejectedExecutionHandler rejectedExecutionHandler) {

        BlockingQueue blockingQueue = new ArrayBlockingQueue(queueSize);
        ThreadPoolExecutor executor = new ThreadPoolExecutor(coreSize, maxSize, keepAliveTime,
                timeUnit, blockingQueue, threadFactory, rejectedExecutionHandler);
        return executor;
    }


}

