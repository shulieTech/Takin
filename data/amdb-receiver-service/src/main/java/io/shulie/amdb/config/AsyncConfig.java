package io.shulie.amdb.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@Slf4j
public class AsyncConfig implements AsyncConfigurer {

    @Value("${thread.pool.corePoolSize:10}")
    private int corePoolSize;

    @Value("${thread.pool.maxPoolSize:20}")
    private int maxPoolSize;

    @Value("${thread.pool.keepAliveSeconds:4}")
    private int keepAliveSeconds;

    @Value("${thread.pool.queueCapacity:512}")
    private int queueCapacity;

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setKeepAliveSeconds(keepAliveSeconds);
        executor.setQueueCapacity(queueCapacity);
        executor.setRejectedExecutionHandler((Runnable r, ThreadPoolExecutor exe) -> {
            log.warn("当前任务线程池队列已满.");
        });
        executor.initialize();
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new AsyncUncaughtExceptionHandler() {
            @Override
            public void handleUncaughtException(Throwable ex , Method method , Object... params) {
                log.error("线程池执行任务发生未知异常.", ex);
            }
        };
    }
}