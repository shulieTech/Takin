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
import io.lettuce.core.ReadFrom;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.cluster.api.async.RedisAdvancedClusterAsyncCommands;
import io.lettuce.core.cluster.api.reactive.RedisAdvancedClusterReactiveCommands;
import io.lettuce.core.cluster.api.sync.RedisAdvancedClusterCommands;
import io.lettuce.core.cluster.models.partitions.Partitions;
import io.lettuce.core.protocol.RedisCommand;
import io.lettuce.core.resource.ClientResources;

import java.time.Duration;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author qianfan
 * @package: com.pamirs.attach.plugin.lettuce.shadowserver
 * @Date 2020/12/1 10:28 下午
 */
public class LettuceClusterConnectionMediator extends RedisClientMediator<StatefulRedisClusterConnection> implements StatefulRedisClusterConnection {

    public LettuceClusterConnectionMediator(StatefulRedisClusterConnection performanceRedisClient, StatefulRedisClusterConnection businessRedisClient) {
        super(businessRedisClient, performanceRedisClient);
    }

    public LettuceClusterConnectionMediator(StatefulRedisClusterConnection performanceRedisClient, StatefulRedisClusterConnection businessRedisClient, boolean useKey) {
        super(performanceRedisClient, businessRedisClient, useKey);
    }

    private AtomicBoolean isUpdate = new AtomicBoolean(false);

    @Override
    public RedisAdvancedClusterCommands sync() {
        if (Pradar.isClusterTest()) {
            return getPerformanceClient().sync();
        } else {
            return businessRedisClient.sync();
        }
    }

    @Override
    public RedisAdvancedClusterAsyncCommands async() {
        if (Pradar.isClusterTest()) {
            return getPerformanceClient().async();
        } else {
            return businessRedisClient.async();
        }
    }

    @Override
    public RedisAdvancedClusterReactiveCommands reactive() {
        if (Pradar.isClusterTest()) {
            return getPerformanceClient().reactive();
        } else {
            return businessRedisClient.reactive();
        }
    }

    @Override
    public StatefulRedisConnection getConnection(String nodeId) {
        return null;
    }

    @Override
    public CompletableFuture<StatefulRedisConnection> getConnectionAsync(String nodeId) {
        return null;
    }

    @Override
    public StatefulRedisConnection getConnection(String host, int port) {
        return null;
    }

    @Override
    public CompletableFuture<StatefulRedisConnection> getConnectionAsync(String host, int port) {
        return null;
    }

    @Override
    public void setReadFrom(ReadFrom readFrom) {

    }

    @Override
    public ReadFrom getReadFrom() {
        return null;
    }

    @Override
    public Partitions getPartitions() {
        return null;
    }

    @Override
    public void setTimeout(Duration timeout) {
        if (Pradar.isClusterTest()) {
            getPerformanceClient().setTimeout(timeout);
        } else {
            businessRedisClient.setTimeout(timeout);
        }
    }

    @Override
    public void setTimeout(long timeout, TimeUnit unit) {
        if (Pradar.isClusterTest()) {
            getPerformanceClient().setTimeout(timeout, unit);
        } else {
            businessRedisClient.setTimeout(timeout, unit);
        }
    }

    @Override
    public Duration getTimeout() {
        if (Pradar.isClusterTest()) {
            return getPerformanceClient().getTimeout();
        } else {
            return businessRedisClient.getTimeout();
        }
    }

    @Override
    public void close() {
        if (Pradar.isClusterTest()) {
            getPerformanceClient().close();
        } else {
            businessRedisClient.close();
        }
    }

    @Override
    public CompletableFuture<Void> closeAsync() {
        if (Pradar.isClusterTest()) {
            return getPerformanceClient().closeAsync();
        } else {
            return businessRedisClient.closeAsync();
        }
    }

    @Override
    public boolean isOpen() {
        if (Pradar.isClusterTest()) {
            return getPerformanceClient().isOpen();
        } else {
            return businessRedisClient.isOpen();
        }
    }

    @Override
    public ClientOptions getOptions() {
        if (Pradar.isClusterTest()) {
            return getPerformanceClient().getOptions();
        } else {
            return businessRedisClient.getOptions();
        }
    }

    @Override
    public ClientResources getResources() {
        if (Pradar.isClusterTest()) {
            return getPerformanceClient().getResources();
        } else {
            return businessRedisClient.getResources();
        }
    }

    @Override
    public void reset() {
        if (Pradar.isClusterTest()) {
            getPerformanceClient().reset();
        } else {
            businessRedisClient.reset();
        }
    }

    @Override
    public void setAutoFlushCommands(boolean autoFlush) {
        if (Pradar.isClusterTest()) {
            getPerformanceClient().setAutoFlushCommands(autoFlush);
        } else {
            businessRedisClient.setAutoFlushCommands(autoFlush);
        }
    }

    @Override
    public void flushCommands() {
        if (Pradar.isClusterTest()) {
            getPerformanceClient().flushCommands();
        } else {
            businessRedisClient.flushCommands();
        }
    }

    @Override
    public Collection<RedisCommand> dispatch(Collection collection) {
        if (Pradar.isClusterTest()) {
            return getPerformanceClient().dispatch(collection);
        } else {
            return businessRedisClient.dispatch(collection);
        }
    }

    @Override
    public RedisCommand dispatch(RedisCommand command) {
        if (Pradar.isClusterTest()) {
            return getPerformanceClient().dispatch(command);
        } else {
            return businessRedisClient.dispatch(command);
        }
    }

    public StatefulRedisClusterConnection getPerformanceClient() {
        if (isUpdate.compareAndSet(true, false)) {
            StatefulRedisClusterConnection dynamic = (StatefulRedisClusterConnection) LettuceFactory.getFactory().dynamic(businessRedisClient);
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
                logger.error("SIMULATOR: lettuce cluster connection close err! ", e);
            }
            performanceRedisClient = null;
        }
    }
}
