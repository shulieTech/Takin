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

package io.shulie.tro.web.app.conf;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.client.RestTemplate;

/**
 * @author shiyajian
 * create: 2020-10-04
 */
@Configuration
public class ThreadPoolConfig {

    @Bean(name = "fastDebugThreadPool")
    public ThreadPoolExecutor fastDebug() {
        ThreadFactory nameThreadFactory = new ThreadFactoryBuilder().setNameFormat("fast-debug-%d").build();
        return new ThreadPoolExecutor(10, 20, 60L, TimeUnit.SECONDS, new ArrayBlockingQueue<>(500), nameThreadFactory,
            new ThreadPoolExecutor.CallerRunsPolicy());
    }

    @Bean(name = "modifyMonitorThreadPool")
    public ThreadPoolExecutor modifyMonitorExecutor() {
        ThreadFactory nameThreadFactory = new ThreadFactoryBuilder().setNameFormat("modify-monitor-%d").build();
        return new ThreadPoolExecutor(10, 20, 60L, TimeUnit.SECONDS, new ArrayBlockingQueue<>(100), nameThreadFactory,
            new ThreadPoolExecutor.CallerRunsPolicy());
    }

    @Bean(name = "schedulerPool")
    public TaskScheduler scheduler() {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(5);
        ThreadFactory threadFactory = new BasicThreadFactory.Builder().namingPattern("pradar-scheduler-thread-%d")
            .daemon(true).build();
        taskScheduler.setThreadFactory(threadFactory);
        taskScheduler.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return taskScheduler;
    }

    @Bean(name = "ScriptThreadPool")
    public ThreadPoolExecutor runShellTaskExecutor() {
        ThreadFactory nameThreadFactory = new ThreadFactoryBuilder().setNameFormat("script-thread-%d").build();
        return new ThreadPoolExecutor(10, 20, 60L, TimeUnit.SECONDS, new ArrayBlockingQueue<>(100), nameThreadFactory,
            new ThreadPoolExecutor.CallerRunsPolicy());
    }

    @Bean(name = "loadDataThreadPool")
    public ThreadPoolExecutor loadDataTaskExecutor() {
        ThreadFactory nameThreadFactory = new ThreadFactoryBuilder().setNameFormat("loaddata-thread-%d").build();
        return new ThreadPoolExecutor(10, 20, 60L, TimeUnit.SECONDS, new ArrayBlockingQueue<>(100), nameThreadFactory,
            new ThreadPoolExecutor.CallerRunsPolicy());
    }

    @Bean(name = "agentDataThreadPool")
    public ThreadPoolExecutor agentDataTaskExecutor() {
        ThreadFactory nameThreadFactory = new ThreadFactoryBuilder().setNameFormat("agentdata-thread-%d").build();
        return new ThreadPoolExecutor(5, 10, 60L, TimeUnit.SECONDS, new ArrayBlockingQueue<>(10000), nameThreadFactory,
            new ThreadPoolExecutor.CallerRunsPolicy());
    }

    @Bean(name = "backgroundMonitorThreadPool")
    public ThreadPoolExecutor backgroundMonitorThreadPool() {
        ThreadFactory nameThreadFactory = new ThreadFactoryBuilder().setNameFormat("background-monitor-thread-%d").build();
        return new ThreadPoolExecutor(20, 20, 60L, TimeUnit.SECONDS, new ArrayBlockingQueue<>(10000), nameThreadFactory,
            new ThreadPoolExecutor.CallerRunsPolicy());
    }

    @Bean("asynExecuteScriptThreadPool")
    public Executor myAsync() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //核心线程数
        executor.setCorePoolSize(10);
        //最大核心线程数
        executor.setMaxPoolSize(20);
        //心态检测，超过设置时间回收线程,线程空闲时的存活时间
        executor.setKeepAliveSeconds(0);
        //队列深度
        executor.setQueueCapacity(100);
        //线程名称
        executor.setThreadNamePrefix("myThreadA00-");
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60);
        //拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }

    @Primary
    @Bean
    public RestTemplate restTemplate() {
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setConnectTimeout(3000);
        requestFactory.setReadTimeout(15000);
        return new RestTemplate(requestFactory);
    }

    @Qualifier("consumingRestTemplate")
    @Bean
    public RestTemplate consumingRestTemplate() {
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setConnectTimeout(5000);
        requestFactory.setReadTimeout(60000);
        return new RestTemplate(requestFactory);
    }

    /**
     * 说明: 文件下载
     *
     * @author shulie
     * @date 2018/10/10 10:37
     */
    @Bean
    public HttpMessageConverters restFileDownloadSupport() {
        ByteArrayHttpMessageConverter arrayHttpMessageConverter = new ByteArrayHttpMessageConverter();
        return new HttpMessageConverters(arrayHttpMessageConverter);
    }
}
