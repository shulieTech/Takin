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

package io.shulie.tro.cloud.app.conf;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author shiyajian
 * create: 2020-10-04
 */
@Configuration
public class ThreadPoolConfig {
    @Value("${thread.pool.config.corePoolSize:10}")
    private String corePoolSize;
    @Value("${thread.pool.config.maximumPoolSize:20}")
    private String maximumPoolSize;

    @Bean(name = "enginePodThreadPoolExecutor")
    public ThreadPoolExecutor modifyMonitorExecutor() {
        ThreadFactory nameThreadFactory = new ThreadFactoryBuilder().setNameFormat("local-thread-engine-pod-%d").build();
        return new ThreadPoolExecutor(Integer.parseInt(corePoolSize), Integer.parseInt(maximumPoolSize), 60L, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(100), nameThreadFactory, new ThreadPoolExecutor.CallerRunsPolicy());
    }

}
