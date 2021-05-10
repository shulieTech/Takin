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
import io.lettuce.core.ConnectionFuture;

import java.net.SocketAddress;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @Author <a href="tangyuhan@shulie.io">yuhan.tang</a>
 * @package: com.pamirs.attach.plugin.lettuce.shadowserver
 * @Date 2020/12/7 9:06 下午
 */
public class LettuceDefaultConnectionMediator extends RedisClientMediator<ConnectionFuture> implements ConnectionFuture {

    public LettuceDefaultConnectionMediator(ConnectionFuture performanceRedisClient, ConnectionFuture businessRedisClient) {
        super(businessRedisClient, performanceRedisClient);
    }

    public LettuceDefaultConnectionMediator(ConnectionFuture performanceRedisClient, ConnectionFuture businessRedisClient, boolean useKey) {
        super(performanceRedisClient, businessRedisClient, useKey);
    }

    private AtomicBoolean isUpdate = new AtomicBoolean(false);


    public ConnectionFuture getPreformanceClient() {
        if (isUpdate.compareAndSet(true, false)) {
            ConnectionFuture dynamic = (ConnectionFuture) LettuceFactory.getFactory().dynamic(businessRedisClient);
            performanceRedisClient = dynamic;
        }
        return performanceRedisClient;
    }

    @Override
    public void destroy() {
        if (performanceRedisClient != null) {
            try {
                performanceRedisClient.cancel(true);
            } catch (Throwable e) {
                logger.error("SIMULATOR: lettuce default connection close err! ", e);
            }
            performanceRedisClient = null;
        }
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        if (Pradar.isClusterTest()) {
            return getPreformanceClient().cancel(mayInterruptIfRunning);
        } else {
            return businessRedisClient.cancel(mayInterruptIfRunning);
        }
    }

    @Override
    public boolean isCancelled() {
        if (Pradar.isClusterTest()) {
            return getPreformanceClient().isCancelled();
        } else {
            return businessRedisClient.isCancelled();
        }
    }

    @Override
    public boolean isDone() {
        if (Pradar.isClusterTest()) {
            return getPreformanceClient().isDone();
        } else {
            return businessRedisClient.isDone();
        }
    }

    @Override
    public Object get() throws InterruptedException, ExecutionException {
        if (Pradar.isClusterTest()) {
            return getPreformanceClient().get();
        } else {
            return businessRedisClient.get();
        }
    }

    @Override
    public Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        if (Pradar.isClusterTest()) {
            return getPreformanceClient().get(timeout, unit);
        } else {
            return businessRedisClient.get(timeout, unit);
        }
    }

    @Override
    public SocketAddress getRemoteAddress() {
        if (Pradar.isClusterTest()) {
            return getPreformanceClient().getRemoteAddress();
        } else {
            return businessRedisClient.getRemoteAddress();
        }
    }

    @Override
    public Object join() {
        if (Pradar.isClusterTest()) {
            return getPreformanceClient().join();
        } else {
            return businessRedisClient.join();
        }
    }

    @Override
    public ConnectionFuture<Void> thenAccept(Consumer action) {
        if (Pradar.isClusterTest()) {
            return getPreformanceClient().thenAccept(action);
        } else {
            return businessRedisClient.thenAccept(action);
        }
    }

    @Override
    public ConnectionFuture<Void> thenAcceptAsync(Consumer action) {
        if (Pradar.isClusterTest()) {
            return getPreformanceClient().thenAcceptAsync(action);
        } else {
            return businessRedisClient.thenAcceptAsync(action);
        }
    }

    @Override
    public ConnectionFuture<Void> thenAcceptAsync(Consumer action, Executor executor) {
        if (Pradar.isClusterTest()) {
            return getPreformanceClient().thenAcceptAsync(action, executor);
        } else {
            return businessRedisClient.thenAcceptAsync(action, executor);
        }
    }

    @Override
    public ConnectionFuture<Void> thenRun(Runnable action) {
        if (Pradar.isClusterTest()) {
            return getPreformanceClient().thenRun(action);
        } else {
            return businessRedisClient.thenRun(action);
        }
    }

    @Override
    public ConnectionFuture<Void> thenRunAsync(Runnable action) {
        if (Pradar.isClusterTest()) {
            return getPreformanceClient().thenRunAsync(action);
        } else {
            return businessRedisClient.thenRunAsync(action);
        }
    }

    @Override
    public ConnectionFuture<Void> thenRunAsync(Runnable action, Executor executor) {
        if (Pradar.isClusterTest()) {
            return getPreformanceClient().thenRunAsync(action, executor);
        } else {
            return businessRedisClient.thenRunAsync(action, executor);
        }
    }

    @Override
    public ConnectionFuture<Void> acceptEither(CompletionStage other, Consumer action) {
        if (Pradar.isClusterTest()) {
            return getPreformanceClient().acceptEither(other, action);
        } else {
            return businessRedisClient.acceptEither(other, action);
        }
    }

    @Override
    public ConnectionFuture<Void> acceptEitherAsync(CompletionStage other, Consumer action) {
        if (Pradar.isClusterTest()) {
            return getPreformanceClient().acceptEitherAsync(other, action);
        } else {
            return businessRedisClient.acceptEitherAsync(other, action);
        }
    }

    @Override
    public ConnectionFuture<Void> acceptEitherAsync(CompletionStage other, Consumer action, Executor executor) {
        if (Pradar.isClusterTest()) {
            return getPreformanceClient().acceptEitherAsync(other, action, executor);
        } else {
            return businessRedisClient.acceptEitherAsync(other, action, executor);
        }
    }

    @Override
    public CompletableFuture toCompletableFuture() {
        if (Pradar.isClusterTest()) {
            return getPreformanceClient().toCompletableFuture();
        } else {
            return businessRedisClient.toCompletableFuture();
        }
    }

    @Override
    public ConnectionFuture handleAsync(BiFunction fn, Executor executor) {
        if (Pradar.isClusterTest()) {
            return getPreformanceClient().handleAsync(fn, executor);
        } else {
            return businessRedisClient.handleAsync(fn, executor);
        }
    }

    @Override
    public ConnectionFuture handleAsync(BiFunction fn) {
        if (Pradar.isClusterTest()) {
            return getPreformanceClient().handleAsync(fn);
        } else {
            return businessRedisClient.handleAsync(fn);
        }
    }

    @Override
    public ConnectionFuture handle(BiFunction fn) {
        if (Pradar.isClusterTest()) {
            return getPreformanceClient().handle(fn);
        } else {
            return businessRedisClient.handle(fn);
        }
    }

    @Override
    public ConnectionFuture whenCompleteAsync(BiConsumer action, Executor executor) {
        if (Pradar.isClusterTest()) {
            return getPreformanceClient().whenCompleteAsync(action, executor);
        } else {
            return businessRedisClient.whenCompleteAsync(action, executor);
        }
    }

    @Override
    public ConnectionFuture whenCompleteAsync(BiConsumer action) {
        if (Pradar.isClusterTest()) {
            return getPreformanceClient().whenCompleteAsync(action);
        } else {
            return businessRedisClient.whenCompleteAsync(action);
        }
    }

    @Override
    public ConnectionFuture whenComplete(BiConsumer action) {
        if (Pradar.isClusterTest()) {
            return getPreformanceClient().whenComplete(action);
        } else {
            return businessRedisClient.whenComplete(action);
        }
    }

    @Override
    public ConnectionFuture exceptionally(Function fn) {
        if (Pradar.isClusterTest()) {
            return getPreformanceClient().exceptionally(fn);
        } else {
            return businessRedisClient.exceptionally(fn);
        }
    }

    @Override
    public ConnectionFuture thenComposeAsync(Function fn, Executor executor) {
        if (Pradar.isClusterTest()) {
            return getPreformanceClient().thenComposeAsync(fn, executor);
        } else {
            return businessRedisClient.thenComposeAsync(fn, executor);
        }
    }

    @Override
    public ConnectionFuture thenComposeAsync(Function fn) {
        if (Pradar.isClusterTest()) {
            return getPreformanceClient().thenComposeAsync(fn);
        } else {
            return businessRedisClient.thenComposeAsync(fn);
        }
    }

    @Override
    public ConnectionFuture thenCompose(BiFunction fn) {
        if (Pradar.isClusterTest()) {
            return getPreformanceClient().thenCompose(fn);
        } else {
            return businessRedisClient.thenCompose(fn);
        }
    }

    @Override
    public ConnectionFuture thenCompose(Function fn) {
        if (Pradar.isClusterTest()) {
            return getPreformanceClient().thenCompose(fn);
        } else {
            return businessRedisClient.thenCompose(fn);
        }
    }

    @Override
    public ConnectionFuture<Void> runAfterEitherAsync(CompletionStage other, Runnable action, Executor executor) {
        if (Pradar.isClusterTest()) {
            return getPreformanceClient().runAfterEitherAsync(other, action, executor);
        } else {
            return businessRedisClient.runAfterEitherAsync(other, action, executor);
        }
    }

    @Override
    public ConnectionFuture<Void> runAfterEitherAsync(CompletionStage other, Runnable action) {
        if (Pradar.isClusterTest()) {
            return getPreformanceClient().runAfterEitherAsync(other, action);
        } else {
            return businessRedisClient.runAfterEitherAsync(other, action);
        }
    }

    @Override
    public ConnectionFuture<Void> runAfterEither(CompletionStage other, Runnable action) {
        if (Pradar.isClusterTest()) {
            return getPreformanceClient().runAfterEither(other, action);
        } else {
            return businessRedisClient.runAfterEither(other, action);
        }
    }

    @Override
    public ConnectionFuture applyToEitherAsync(CompletionStage other, Function fn, Executor executor) {
        if (Pradar.isClusterTest()) {
            return getPreformanceClient().applyToEitherAsync(other, fn, executor);
        } else {
            return businessRedisClient.applyToEitherAsync(other, fn, executor);
        }
    }

    @Override
    public ConnectionFuture applyToEitherAsync(CompletionStage other, Function fn) {
        if (Pradar.isClusterTest()) {
            return getPreformanceClient().applyToEitherAsync(other, fn);
        } else {
            return businessRedisClient.applyToEitherAsync(other, fn);
        }
    }

    @Override
    public ConnectionFuture applyToEither(CompletionStage other, Function fn) {
        if (Pradar.isClusterTest()) {
            return getPreformanceClient().applyToEither(other, fn);
        } else {
            return businessRedisClient.applyToEither(other, fn);
        }
    }

    @Override
    public ConnectionFuture<Void> runAfterBothAsync(CompletionStage other, Runnable action, Executor executor) {
        if (Pradar.isClusterTest()) {
            return getPreformanceClient().runAfterBothAsync(other, action, executor);
        } else {
            return businessRedisClient.runAfterBothAsync(other, action, executor);
        }
    }

    @Override
    public ConnectionFuture<Void> runAfterBothAsync(CompletionStage other, Runnable action) {
        if (Pradar.isClusterTest()) {
            return getPreformanceClient().runAfterBothAsync(other, action);
        } else {
            return businessRedisClient.runAfterBothAsync(other, action);
        }
    }

    @Override
    public ConnectionFuture<Void> runAfterBoth(CompletionStage other, Runnable action) {
        if (Pradar.isClusterTest()) {
            return getPreformanceClient().runAfterBoth(other, action);
        } else {
            return businessRedisClient.runAfterBoth(other, action);
        }
    }

    @Override
    public ConnectionFuture<Void> thenAcceptBothAsync(CompletionStage other, BiConsumer action, Executor executor) {
        if (Pradar.isClusterTest()) {
            return getPreformanceClient().thenAcceptBothAsync(other, action);
        } else {
            return businessRedisClient.thenAcceptBothAsync(other, action);
        }
    }

    @Override
    public ConnectionFuture<Void> thenAcceptBothAsync(CompletionStage other, BiConsumer action) {
        if (Pradar.isClusterTest()) {
            return getPreformanceClient().thenAcceptBothAsync(other, action);
        } else {
            return businessRedisClient.thenAcceptBothAsync(other, action);
        }
    }

    @Override
    public ConnectionFuture<Void> thenAcceptBoth(CompletionStage other, BiConsumer action) {
        if (Pradar.isClusterTest()) {
            return getPreformanceClient().thenAcceptBoth(other, action);
        } else {
            return businessRedisClient.thenAcceptBoth(other, action);
        }
    }

    @Override
    public ConnectionFuture thenCombineAsync(CompletionStage other, BiFunction fn, Executor executor) {
        if (Pradar.isClusterTest()) {
            return getPreformanceClient().thenCombineAsync(other, fn);
        } else {
            return businessRedisClient.thenCombineAsync(other, fn);
        }
    }

    @Override
    public ConnectionFuture thenCombineAsync(CompletionStage other, BiFunction fn) {
        if (Pradar.isClusterTest()) {
            return getPreformanceClient().thenCombineAsync(other, fn);
        } else {
            return businessRedisClient.thenCombineAsync(other, fn);
        }
    }

    @Override
    public ConnectionFuture thenCombine(CompletionStage other, BiFunction fn) {
        if (Pradar.isClusterTest()) {
            return getPreformanceClient().thenCombine(other, fn);
        } else {
            return businessRedisClient.thenCombine(other, fn);
        }
    }

    @Override
    public ConnectionFuture thenApplyAsync(Function fn, Executor executor) {
        if (Pradar.isClusterTest()) {
            return getPreformanceClient().thenApplyAsync(fn, executor);
        } else {
            return businessRedisClient.thenApplyAsync(fn, executor);
        }
    }

    @Override
    public ConnectionFuture thenApplyAsync(Function fn) {
        if (Pradar.isClusterTest()) {
            return getPreformanceClient().thenApplyAsync(fn);
        } else {
            return businessRedisClient.thenApplyAsync(fn);
        }
    }

    @Override
    public ConnectionFuture thenApply(Function fn) {
        if (Pradar.isClusterTest()) {
            return getPreformanceClient().thenApply(fn);
        } else {
            return businessRedisClient.thenApply(fn);
        }
    }
}
