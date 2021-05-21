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

package io.shulie.surge.data.runtime.processor;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.shulie.surge.data.common.aggregation.Scheduler;
import io.shulie.surge.data.common.pool.DataPoolExecutors;
import io.shulie.surge.data.common.utils.DateUtils;
import io.shulie.surge.data.common.utils.FormatUtils;
import io.shulie.surge.data.runtime.digest.DataDigester;
import io.shulie.surge.data.runtime.digest.DigestContext;
import io.shulie.surge.data.runtime.digest.handler.DigestJob;
import io.shulie.surge.data.runtime.digest.handler.DigesterWorkerHanlder;
import io.shulie.surge.data.runtime.disruptor.InsufficientCapacityException;
import io.shulie.surge.data.runtime.disruptor.RingBuffer;
import io.shulie.surge.data.runtime.disruptor.RingBufferIllegalStateException;
import io.shulie.surge.data.runtime.disruptor.SleepingWaitStrategy;
import io.shulie.surge.data.runtime.disruptor.dsl.Disruptor;
import io.shulie.surge.data.runtime.disruptor.dsl.ProducerType;
import io.shulie.surge.data.runtime.parser.DataParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import static io.shulie.surge.data.common.utils.CommonUtils.divide;

/**
 * 默认执行器
 *
 * @param <IN,OUT>
 * @author vincent
 */
public abstract class DefaultProcessor<IN extends Serializable, OUT extends Serializable> implements DataQueue<IN, DigestContext<OUT>> {

    private static final Logger logger = LoggerFactory.getLogger(DefaultProcessor.class);

    private long[] digesterTimeCost;

    private ProcessorConfigSpec processorConfig;
    private RingBuffer<DigestJob> ringBuffer;
    private Disruptor<DigestJob> disruptor;
    private AtomicLong processCount = new AtomicLong(0);

    /**
     * 开始运行
     */
    @Override
    public void start() throws Exception {
        this.digesterTimeCost = new long[processorConfig.getDigesters().length];
        // disruptor实现
        ExecutorService es = DataPoolExecutors.newDefaultNoQueueExecutors(processorConfig.getExecuteSize(), processorConfig.getExecuteSize() * 2, 3, TimeUnit.SECONDS, new ThreadFactoryBuilder().setNameFormat(processorConfig.getName() + "-Processor_thread_-%d").build(), null);
        disruptor = new Disruptor<>(DigestJob.EVENT_FACTORY, processorConfig.getRingBufferSize(), es, ProducerType.MULTI, new SleepingWaitStrategy());
        int digesterCount = processorConfig.getDigesters().length;

        for (int i = 0; i < digesterCount; i++) {
            DataDigester digester = processorConfig.getDigesters()[i];
            int poolSize = digester.threadCount();
            DigesterWorkerHanlder[] workers = new DigesterWorkerHanlder[poolSize];
            for (int j = 0; j < poolSize; j++) {
                workers[j] = new DigesterWorkerHanlder(digester, i, digesterTimeCost);
            }
            disruptor.handleEventsWithWorkerPool(workers);
        }

        ringBuffer = disruptor.start();

        monitor();
    }

    /**
     * 停止运行。如果已经停止，则应该不会有任何效果。
     * 建议实现使用同步方式执行。
     */
    @Override
    public void stop() throws Exception {
        disruptor.shutdown();
    }

    /**
     * 检查当前是否在运行状态
     */
    @Override
    public boolean isRunning() {
        return true;
    }

    /**
     * 判断是否还有空余空间提交数据条数
     *
     * @param size
     * @return
     */
    @Override
    public boolean canPublish(int size) {
        if (ringBuffer.hasAvailableCapacity((int) (processorConfig.getRingBufferRemainRate() * processorConfig.getRingBufferSize()))) {
            return true;
        }
        String logText = processorConfig.getName() + "ringbuffer remaining capacity:" + ringBuffer.remainingCapacity() + " total capacity:" + ringBuffer.getBufferSize();
        logger.error(logText);
        throw new RingBufferIllegalStateException(logText);
    }

    /**
     * 发布一个数据。如果队列已满，丢出异常
     *
     * @param data
     * @throws InterruptedException 阻塞期间被打断时抛出的异常
     */
    @Override
    public void publish(DigestContext<OUT> data) throws InterruptedException {
        canPublish(1);
        if (data == null || removeDelay(data.getEventTime())) {
            return;
        }
        long seq = ringBuffer.next();
        DigestJob job = ringBuffer.get(seq);
        job.context = data;
        ringBuffer.publish(seq);
        processCount.incrementAndGet();
    }

    /**
     * 发布多个数据。如果队列已满，丢出异常
     *
     * @throws InterruptedException 阻塞期间被打断时抛出的异常
     */
    @Override
    public void publish(List<DigestContext<OUT>> datas) throws InterruptedException {
        try {
            if (datas.isEmpty()) {
                return;
            }
            int size = datas.size();
            long seq = ringBuffer.tryNext(size);
            for (int i = 0; i < size; i++) {
                DigestJob job = ringBuffer.get(seq - i);
                job.context = datas.get(i);
                ringBuffer.publish(seq - i);
                processCount.incrementAndGet();
            }
        } catch (InsufficientCapacityException e) {
            String logText = processorConfig.getName() + "ringbuffer remaining capacity:" + ringBuffer.remainingCapacity() + " total capacity:" + ringBuffer.getBufferSize();
            logger.error(logText);
            throw new RingBufferIllegalStateException(logText);
        }
    }

    /**
     * 发布一个数据。如果队列已满，丢出异常
     *
     * @param data
     * @throws InterruptedException 阻塞期间被打断时抛出的异常
     */
    @Override
    public void publish(Map<String, Object> header, IN data) throws InterruptedException {
        DataParser<IN, OUT> dataParser = getDataParser(header);
        DigestContext<OUT> context = null;
        try {
            context = dataParser.createContext(header, data);
        } catch (Exception e) {
            //ignore
        }
        publish(context);
    }

    /**
     * 发布一个数据。如果队列已满，丢出异常
     *
     * @param datas
     * @throws InterruptedException 阻塞期间被打断时抛出的异常
     */
    @Override
    public void publish(Map<String, Object> header, List<IN> datas) throws InterruptedException {

        canPublish(datas.size());
        DataParser<IN, OUT> dataParser = getDataParser(header);
        List<DigestContext<OUT>> list = Lists.newArrayList();
        for (IN data : datas) {
            DigestContext<OUT> context = null;
            try {
                context = dataParser.createContext(header, data);
                if (context == null || removeDelay(context.getEventTime())) {
                    continue;
                }
                list.add(context);
            } catch (Exception e) {
                continue;
            }
            //publish(context);
        }
        publish(list);
    }

    private void monitor() {
        Scheduler scheduler = new Scheduler(1);
        long interval = TimeUnit.MINUTES.toMillis(1);
        long now = System.currentTimeMillis();
        long delay = DateUtils.truncateToMinute(now + interval) - now;
        scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                StringBuilder appender = new StringBuilder(256);
                appender.append(processorConfig.getName() + " Process Time");
                for (int i = 0; i < processorConfig.getDigesters().length; ++i) {
                    long timeCost = digesterTimeCost[i];
                    appender.append("\n  ").append(processorConfig.getDigesters()[i].getClass().getSimpleName()).append(": ")
                            .append(FormatUtils.humanReadableTimeSpan(timeCost)).append(", about ")
                            .append(FormatUtils.roundx4(divide(timeCost, processCount.get()))).append(" ms/line").append(" line:").append(processCount.get());
                    digesterTimeCost[i] = 0;
                }
                processCount = new AtomicLong(0);
                logger.warn(appender.toString());
            }
        }, delay, interval, TimeUnit.MILLISECONDS);
    }

    /**
     * 获取解析器
     *
     * @param header
     * @return
     */
    protected abstract DataParser<IN, OUT> getDataParser(Map<String, Object> header);

    public void setProcessorConfig(ProcessorConfigSpec processorConfig) {
        this.processorConfig = processorConfig;
    }
}
