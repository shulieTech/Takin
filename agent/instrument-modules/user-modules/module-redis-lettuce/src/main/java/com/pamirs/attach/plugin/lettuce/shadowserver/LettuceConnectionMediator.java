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
package com.pamirs.attach.plugin.lettuce.shadowserver;

import com.pamirs.attach.plugin.common.datasource.redisserver.RedisClientMediator;
import com.pamirs.pradar.Pradar;
import io.lettuce.core.ClientOptions;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.api.reactive.RedisReactiveCommands;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.protocol.RedisCommand;
import io.lettuce.core.resource.ClientResources;

import java.time.Duration;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class LettuceConnectionMediator extends RedisClientMediator<StatefulRedisConnection> implements StatefulRedisConnection {

    public LettuceConnectionMediator(StatefulRedisConnection performanceRedisClient, StatefulRedisConnection businessRedisClient) {
        super(businessRedisClient, performanceRedisClient);
    }

    public LettuceConnectionMediator(StatefulRedisConnection performanceRedisClient, StatefulRedisConnection businessRedisClient, boolean useKey) {
        super(performanceRedisClient, businessRedisClient, useKey);
    }

    private AtomicBoolean isUpdate = new AtomicBoolean(false);

    @Override
    public boolean isMulti() {
        if (Pradar.isClusterTest()) {
            return getPreformanceClient().isMulti();
        } else {
            return businessRedisClient.isMulti();
        }
    }

    @Override
    public RedisCommands sync() {
        if (Pradar.isClusterTest()) {
            return getPreformanceClient().sync();
        } else {
            return businessRedisClient.sync();
        }
    }

    @Override
    public RedisAsyncCommands async() {
        if (Pradar.isClusterTest()) {
            return getPreformanceClient().async();
        } else {
            return businessRedisClient.async();
        }
    }

    @Override
    public RedisReactiveCommands reactive() {
        if (Pradar.isClusterTest()) {
            return getPreformanceClient().reactive();
        } else {
            return businessRedisClient.reactive();
        }
    }

    @Override
    public void setTimeout(Duration timeout) {
        if (Pradar.isClusterTest()) {
            getPreformanceClient().setTimeout(timeout);
        } else {
            businessRedisClient.setTimeout(timeout);
        }
    }

    @Override
    public void setTimeout(long timeout, TimeUnit unit) {
        if (Pradar.isClusterTest()) {
            getPreformanceClient().setTimeout(timeout, unit);
        } else {
            businessRedisClient.setTimeout(timeout, unit);
        }
    }

    @Override
    public Duration getTimeout() {
        if (Pradar.isClusterTest()) {
            return getPreformanceClient().getTimeout();
        } else {
            return businessRedisClient.getTimeout();
        }
    }

    @Override
    public void close() {
        if (Pradar.isClusterTest()) {
            getPreformanceClient().close();
        } else {
            businessRedisClient.close();
        }
    }

    @Override
    public CompletableFuture<Void> closeAsync() {
        if (Pradar.isClusterTest()) {
            return getPreformanceClient().closeAsync();
        } else {
            return businessRedisClient.closeAsync();
        }
    }

    @Override
    public boolean isOpen() {
        if (Pradar.isClusterTest()) {
            return getPreformanceClient().isOpen();
        } else {
            return businessRedisClient.isOpen();
        }
    }

    @Override
    public ClientOptions getOptions() {
        if (Pradar.isClusterTest()) {
            return getPreformanceClient().getOptions();
        } else {
            return businessRedisClient.getOptions();
        }
    }

    @Override
    public ClientResources getResources() {
        if (Pradar.isClusterTest()) {
            return getPreformanceClient().getResources();
        } else {
            return businessRedisClient.getResources();
        }
    }

    @Override
    public void reset() {
        if (Pradar.isClusterTest()) {
            getPreformanceClient().reset();
        } else {
            businessRedisClient.reset();
        }
    }

    @Override
    public void setAutoFlushCommands(boolean autoFlush) {
        if (Pradar.isClusterTest()) {
            getPreformanceClient().setAutoFlushCommands(autoFlush);
        } else {
            businessRedisClient.setAutoFlushCommands(autoFlush);
        }
    }

    @Override
    public void flushCommands() {
        if (Pradar.isClusterTest()) {
            getPreformanceClient().flushCommands();
        } else {
            businessRedisClient.flushCommands();
        }
    }

    @Override
    public Collection<RedisCommand> dispatch(Collection collection) {
        if (Pradar.isClusterTest()) {
            return getPreformanceClient().dispatch(collection);
        } else {
            return businessRedisClient.dispatch(collection);
        }
    }

    @Override
    public RedisCommand dispatch(RedisCommand command) {
        if (Pradar.isClusterTest()) {
            return getPreformanceClient().dispatch(command);
        } else {
            return businessRedisClient.dispatch(command);
        }
    }

    public StatefulRedisConnection getPreformanceClient() {
        if (isUpdate.compareAndSet(true, false)) {
            StatefulRedisConnection dynamic = (StatefulRedisConnection) LettuceFactory.getFactory().dynamic(businessRedisClient);
            performanceRedisClient = dynamic;
        }
        return performanceRedisClient;
    }

    @Override
    public void destroy() {
        if (performanceRedisClient != null) {
            try {
                performanceRedisClient.close();
            } catch (Throwable e) {
                logger.error("SIMULATOR: lettuce connection close err! ", e);
            }
            performanceRedisClient = null;
        }
    }
}
