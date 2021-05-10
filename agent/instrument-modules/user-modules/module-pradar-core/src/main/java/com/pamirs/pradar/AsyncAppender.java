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


import com.shulie.instrument.simulator.api.executors.ExecutorServiceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 异步提交日志，避免影响主线程
 */
class AsyncAppender extends PradarAppender {

    /**
     * 默认的消费者唤醒阈值，这个值需要让消费者能较持续的有事情做，
     * 这个值设置过小，会导致生产者频繁唤起消费者；
     * 设置过大，可能导致生产者速度过快导致队列满丢日志的问题。
     */
    private static final int DEFAULT_CONSUMER_THRESHOLD = 512;

    /**
     * 用于内部控制刷新日志的命令
     */
    static final Object EVENT_LOG_FLUSH = new Object();
    /**
     * 用于内部控制滚动日志的命令
     */
    static final Object EVENT_LOG_ROLLOVER = new Object();
    /**
     * 用于内部控制日志重载的命令
     */
    static final Object EVENT_LOG_RELOAD = new Object();

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncAppender.class);

    // RingBuffer 实现，size 必须为 2 的 n 次方
    private final Object[] entries;
    private final int queueSize;
    private final int indexMask;
    private final int consumerThreshold;
    // 在队列满的时候，业务线程最长尝试时间
    private final int maxWaitMillis;

    private final ReentrantLock lock;
    private final Condition notEmpty;

    // 下一个写的位置，一直递增
    private AtomicLong putIndex;
    // 最近丢弃的日志条数
    private AtomicLong discardCount;
    // 下一个读的位置，一直递增，不能大于 putIndex
    private AtomicLong takeIndex;

    private PradarAppender appender;
    private TraceEncoder encoder;
    private String workerName;

    private Future future;
    private AsyncRunnable task;
    private AtomicBoolean running;

    public AsyncAppender(int queueSize, int maxWaitMillis) {
        // queueSize 取大于或等于 value 的 2 的 n 次方数
        queueSize = 1 << (32 - Integer.numberOfLeadingZeros(queueSize - 1));

        this.queueSize = queueSize;
        this.maxWaitMillis = maxWaitMillis;
        this.entries = new Object[queueSize];
        this.indexMask = queueSize - 1;
        this.consumerThreshold = queueSize >= DEFAULT_CONSUMER_THRESHOLD ? DEFAULT_CONSUMER_THRESHOLD : queueSize;

        this.putIndex = new AtomicLong(0L);
        this.discardCount = new AtomicLong(0L);
        this.takeIndex = new AtomicLong(0L);

        this.running = new AtomicBoolean(false);

        this.lock = new ReentrantLock(false);
        this.notEmpty = lock.newCondition();
    }

    void start(PradarAppender appender, TraceEncoder encoder, String workerName) {
        if (appender instanceof AsyncAppender) {
            throw new IllegalArgumentException("nested AsyncAppender is not allow: " + workerName);
        }
        this.appender = PradarCoreUtils.checkNotNull(appender, "appender");
        this.encoder = encoder;
        this.workerName = workerName;

        task = new AsyncRunnable();
        future = ExecutorServiceFactory.GLOBAL_EXECUTOR_SERVICE.submit(task);
    }

    int size() {
        return (int) (putIndex.get() - takeIndex.get());
    }

    public boolean append(Object ctx) {
        final long qsize = queueSize;
        long startTime = 0;
        for (; ; ) {
            final long put = putIndex.get();
            final long size = put - takeIndex.get();
            if (size >= qsize) {
                if (maxWaitMillis <= 0) {
                    discardCount.incrementAndGet();
                    return false;
                } else {
                    long now = System.currentTimeMillis();
                    if (startTime == 0) {
                        startTime = now;
                    } else if (now - startTime >= maxWaitMillis) {
                        discardCount.incrementAndGet();
                        return false;
                    }
                    LockSupport.parkNanos(1000);
                    continue;
                }
            }
            if (putIndex.compareAndSet(put, put + 1)) {
                entries[(int) put & indexMask] = ctx;
                // 仅仅在队列的日志数超过阈值，且消费者不在运行，且获得锁，才唤醒消费者
                // 这个做法能保证只有必要时才立即通知消费者，减少上下文切换的开销
                if (size >= consumerThreshold && !running.get() && lock.tryLock()) {
                    try {
                        notEmpty.signal();
                    } catch (Throwable e) {
                        LOGGER.error("fail to signal notEmpty: {}", workerName, e);
                    } finally {
                        lock.unlock();
                    }
                }
                return true;
            }
        }
    }

    /**
     * 队列满时直接丢弃日志，不阻塞业务线程，返回日志是否被接受
     */
    public boolean append(BaseContext ctx) {
        final long qsize = queueSize;
        long startTime = 0;
        for (; ; ) {
            final long put = putIndex.get();
            final long size = put - takeIndex.get();
            if (size >= qsize) {
                if (maxWaitMillis <= 0) {
                    discardCount.incrementAndGet();
                    return false;
                } else {
                    long now = System.currentTimeMillis();
                    if (startTime == 0) {
                        startTime = now;
                    } else if (now - startTime >= maxWaitMillis) {
                        discardCount.incrementAndGet();
                        return false;
                    }
                    LockSupport.parkNanos(1000);
                    continue;
                }
            }
            if (putIndex.compareAndSet(put, put + 1)) {
                entries[(int) put & indexMask] = ctx;
                // 仅仅在队列的日志数超过阈值，且消费者不在运行，且获得锁，才唤醒消费者
                // 这个做法能保证只有必要时才立即通知消费者，减少上下文切换的开销
                if (size >= consumerThreshold && !running.get() && lock.tryLock()) {
                    try {
                        notEmpty.signal();
                    } catch (Throwable e) {
                        LOGGER.error("fail to signal notEmpty: {}", workerName, e);
                    } finally {
                        lock.unlock();
                    }
                }
                return true;
            }
        }
    }

    @Override
    public void append(String log) {
        final long qsize = queueSize;
        long startTime = 0;
        for (; ; ) {
            final long put = putIndex.get();
            final long size = put - takeIndex.get();
            if (size >= qsize) {
                if (maxWaitMillis <= 0) {
                    discardCount.incrementAndGet();
                    return;
                } else {
                    long now = System.currentTimeMillis();
                    if (startTime == 0) {
                        startTime = now;
                    } else if (now - startTime >= maxWaitMillis) {
                        discardCount.incrementAndGet();
                        return;
                    }
                    LockSupport.parkNanos(1000);
                    continue;
                }
            }
            if (putIndex.compareAndSet(put, put + 1)) {
                entries[(int) put & indexMask] = log;
                // 仅仅在队列的日志数超过阈值，且消费者不在运行，且获得锁，才唤醒消费者
                // 这个做法能保证只有必要时才立即通知消费者，减少上下文切换的开销
                if (size >= consumerThreshold && !running.get() && lock.tryLock()) {
                    try {
                        notEmpty.signal();
                    } catch (Throwable e) {
                        LOGGER.error("fail to signal notEmpty: {}", workerName, e);
                    } finally {
                        lock.unlock();
                    }
                }
                return;
            }
        }
    }

    @Override
    public void rollOver() {
        append(EVENT_LOG_ROLLOVER);
    }

    @Override
    public void reload() {
        append(EVENT_LOG_RELOAD);
    }

    @Override
    public void flush() {
        append(EVENT_LOG_FLUSH);
    }

    @Override
    public void close() {
        PradarAppender appender0 = this.appender;
        this.appender = new NoOpAppender();
        appender0.close();
        if (task != null) {
            task.shutdown();
        }
        if (future != null && !future.isCancelled() && !future.isDone()) {
            future.cancel(true);
        }
    }

    @Override
    public void cleanup() {
        this.appender.cleanup();
    }

    void flushAndWait() {
        append(EVENT_LOG_FLUSH);
        // 最多等待刷新的时间，避免数据一直在写导致无法返回
        long end = System.currentTimeMillis() + 500;
        while (size() > 0 && System.currentTimeMillis() <= end) {
            if (running.get()) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    break;
                }
            } else {
                if (lock.tryLock()) {
                    try {
                        notEmpty.signal();
                    } catch (Throwable e) {
                        LOGGER.error("fail to signal notEmpty: {}", workerName, e);
                    } finally {
                        lock.unlock();
                    }
                }
            }
        }
    }

    PradarAppender getPradarAppender() {
        return appender;
    }

    void setPradarAppender(PradarAppender appender) {
        this.appender = PradarCoreUtils.checkNotNull(appender, "appender");
    }

    class AsyncRunnable implements Runnable {
        private volatile boolean isRunning = true;

        public void shutdown() {
            isRunning = false;
        }

        @Override
        public void run() {
            final AsyncAppender parent = AsyncAppender.this;
            final int indexMask = parent.indexMask;
            final int queueSize = parent.queueSize;
            final TraceEncoder encoder = parent.encoder;
            final String workerName = parent.workerName;
            final Object[] entries = parent.entries;
            final AtomicLong putIndex = parent.putIndex;
            final AtomicLong takeIndex = parent.takeIndex;
            final AtomicLong discardCount = parent.discardCount;
            final AtomicBoolean running = parent.running;
            final ReentrantLock lock = parent.lock;
            final Condition notEmpty = parent.notEmpty;

            // 输出丢弃的日志数
            final long outputSpan = TimeUnit.MINUTES.toMillis(1);
            long lastOutputTime = System.currentTimeMillis();
            long now;

            while (isRunning) {
                try {
                    running.set(true);
                    long take = takeIndex.get();
                    long size = putIndex.get() - take;
                    if (size > 0) {
                        // 直接批量处理掉 size 个日志对象
                        do {
                            final int idx = (int) take & indexMask;
                            Object ctx = entries[idx];
                            // 从生产者 claim 到 putIndex 位置，到生产者把日志对象放入队列之间，有可能存在间隙
                            while (ctx == null) {
                                Thread.yield();
                                ctx = entries[idx];
                            }
                            entries[idx] = null;
                            takeIndex.set(++take); // 单个消费者，无需用 CAS
                            --size;
                            processContext(ctx, parent.appender, encoder);
                        } while (size > 0);

                        long discardNum = discardCount.get();
                        if (discardNum > 0 &&
                                (now = System.currentTimeMillis()) - lastOutputTime > outputSpan) {
                            discardNum = discardCount.get();
                            discardCount.lazySet(0); // 无需内存屏障，统计的数量稍微丢失一点
                            LOGGER.warn("{} discarded {} logs, queueSize={}" + queueSize, workerName, discardNum, queueSize);
                            lastOutputTime = now;
                        }

                        // 写完一批日志之后，做一次刷新
                        parent.appender.flush();
                    } else {
                        if (lock.tryLock()) {
                            try {
                                running.set(false);
                                notEmpty.await(1, TimeUnit.SECONDS);
                            } finally {
                                lock.unlock();
                            }
                        }
                    }
                } catch (InterruptedException e) {
                    LOGGER.warn("{} async thread is iterrupted", workerName);
                    break;
                } catch (Throwable e) {
                    LOGGER.error("Fail to async write log {}", workerName, e);
                }
            }
            running.set(false);
        }

        private final void processContext(final Object ctx,
                                          final PradarAppender appender, final TraceEncoder encoder) throws IOException {
            if (ctx == EVENT_LOG_FLUSH) {
                appender.flush();
            } else if (ctx == EVENT_LOG_RELOAD) {
                appender.rollOver();
            } else if (ctx == EVENT_LOG_ROLLOVER) {
                appender.reload();
            } else if (ctx instanceof BaseContext) {
                encoder.encode((BaseContext) ctx, appender);
            } else if (ctx instanceof String) {
                appender.append((String) ctx);
            }
        }
    }

    @Override
    public String toString() {
        return "AsyncAppender [appender=" + appender + "]";
    }
}
