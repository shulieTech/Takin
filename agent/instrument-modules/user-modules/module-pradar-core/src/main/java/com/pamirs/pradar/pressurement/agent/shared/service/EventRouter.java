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
package com.pamirs.pradar.pressurement.agent.shared.service;

import com.pamirs.pradar.ErrorTypeEnum;
import com.pamirs.pradar.pressurement.agent.event.IEvent;
import com.pamirs.pradar.pressurement.agent.listener.EventResult;
import com.pamirs.pradar.pressurement.agent.listener.PradarEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.*;

/**
 * @Description
 * @Author xiaobin.zfb
 * @mail xiaobin@shulie.io
 * @Date 2020/7/10 9:41 上午
 */
public final class EventRouter {
    private final static Logger LOGGER = LoggerFactory.getLogger(EventRouter.class.getName());
    /**
     * 同时支撑1000个事件的发布
     */
    private BlockingQueue<IEvent> queue = new LinkedBlockingQueue<IEvent>(1000);

    private Future future;
    private ExecutorService service;
    private boolean isRunning;

    private EventRouter() {
        this.isRunning = true;
        this.service = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r, "Simulator-Event-Router-Service");
                t.setDaemon(true);
                t.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
                    @Override
                    public void uncaughtException(Thread t, Throwable e) {
                        LOGGER.error("Thread {} caught a unknow exception with UncaughtExceptionHandler", t.getName(), e);
                    }
                });
                return t;
            }
        });
        /**
         * 将事件的发布与订阅拆分开来
         */
        this.future = this.service.submit(new Runnable() {
            @Override
            public void run() {
                while (isRunning) {
                    IEvent event = null;
                    try {
                        event = queue.take();
                    } catch (InterruptedException e) {
                        continue;
                    }
                    if (event == null) {
                        continue;
                    }
                    for (PradarEventListener listener : listeners) {
                        try {
                            EventResult result = listener.onEvent(event);
                            if (result == null) {
                                ErrorReporter.buildError()
                                        .setErrorType(ErrorTypeEnum.AgentError)
                                        .setErrorCode("agent-0002")
                                        .setMessage("开启监听器执行失败")
                                        .setDetail(listener.getClass().getName())
                                        .report();
                                continue;
                            }
                            if (result == EventResult.IGNORE) {
                                continue;
                            }
                            if (!result.getSuccess()) {
                                String uniqueKey = result.getTarget() == null ? listener.getClass().getName() : result.getTarget().toString();
                                if (null != uniqueKey && null != result.getErrorMsg()) {
                                    ErrorReporter.Error error = ErrorReporter.buildError()
                                            .setErrorType(ErrorTypeEnum.AgentError)
                                            .setErrorCode("agent-0003")
                                            .setMessage("监听器执行失败")
                                            .setDetail(uniqueKey + "||" + result.getErrorMsg());
                                    if (result.getClosePradar()) {
                                        error.closePradar(result.getConfigName());
                                    }
                                    error.report();
                                }
                            }
                        } catch (Throwable e) {
                            LOGGER.warn("", e);
                            ErrorReporter.buildError()
                                    .setErrorType(ErrorTypeEnum.AgentError)
                                    .setErrorCode("agent-0002")
                                    .setMessage("开启监听器执行失败")
                                    .setDetail(listener.getClass().getName() + "||" + e.getMessage())
                                    .report();
                        }

                    }
                }
            }
        });
    }

    private static EventRouter INSTANCE;

    public static EventRouter router() {
        if (INSTANCE == null) {
            synchronized (EventRouter.class) {
                if (INSTANCE == null) {
                    INSTANCE = new EventRouter();
                }
            }
        }
        return INSTANCE;
    }

    private Set<PradarEventListener> listeners = new TreeSet<PradarEventListener>(new Comparator<PradarEventListener>() {
        @Override
        public int compare(PradarEventListener o1, PradarEventListener o2) {
            int r = o1.order() - o2.order() > 0 ? 1 : (o1.order() - o2.order() < 0 ? -1 : 0);
            if (r == 0) {
                LOGGER.warn("listener order 一致 o1 is :{}, o2 is :{}", o1.getClass().getName(), o2.getClass().getName());
            }
            return r;
        }
    });

    /**
     * 添加事件监听器
     *
     * @param listener
     */
    public synchronized EventRouter addListener(PradarEventListener listener) {
        this.listeners.add(listener);
        return this;
    }

    /**
     * 移除事件监听器
     *
     * @param listener
     * @return
     */
    public synchronized EventRouter removeListener(PradarEventListener listener) {
        this.listeners.remove(listener);
        return this;
    }

    /**
     * 发布事件
     *
     * @param event
     */
    public boolean publish(IEvent event) {
        return queue.offer(event);
    }

    public void shutdown() {
        this.isRunning = false;
        if (future != null && !future.isCancelled() && !future.isDone()) {
            future.cancel(true);
        }
        this.service.shutdown();
    }
}
