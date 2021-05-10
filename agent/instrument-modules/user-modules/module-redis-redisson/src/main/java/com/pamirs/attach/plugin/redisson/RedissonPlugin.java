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
package com.pamirs.attach.plugin.redisson;

import com.pamirs.attach.plugin.redisson.factory.RedissonFactory;
import com.pamirs.attach.plugin.redisson.interceptor.*;
import com.pamirs.attach.plugin.redisson.utils.RedissonUtils;
import com.pamirs.pradar.interceptor.Interceptors;
import com.shulie.instrument.simulator.api.ExtensionModule;
import com.shulie.instrument.simulator.api.ModuleInfo;
import com.shulie.instrument.simulator.api.ModuleLifecycleAdapter;
import com.shulie.instrument.simulator.api.instrument.EnhanceCallback;
import com.shulie.instrument.simulator.api.instrument.InstrumentClass;
import com.shulie.instrument.simulator.api.instrument.InstrumentMethod;
import com.shulie.instrument.simulator.api.listener.Listeners;
import com.shulie.instrument.simulator.api.resource.ReleaseResource;
import com.shulie.instrument.simulator.api.scope.ExecutionPolicy;
import org.kohsuke.MetaInfServices;

@MetaInfServices(ExtensionModule.class)
@ModuleInfo(id = RedissonConstants.MODULE_NAME, version = "1.0.0", author = "xiaobin@shulie.io",description = "redis 的 redisson 客户端")
public class RedissonPlugin extends ModuleLifecycleAdapter implements ExtensionModule {

    private static final String[] EXCLUDE_METHOD_NAMES = new String[]{
            "getBuckets",
            "getMultiLock",
            "getRedLock",
            "getScript",
            "getPatternTopic",
            "getDelayedQueue",
            "getLongAdder",
            "getDoubleAdder",
            "createTransaction",
            "createBatch",
            "getLiveObjectService",
            "getPriorityBlockingQueue",
            "getEvictionScheduler",
            "getCommandExecutor",
            "getConnectionManager",
            "create",
            "createRx",
            "createReactive",
            "getConfig",
            "getNodesGroup",
            "getClusterNodesGroup",
            "equals",
            "hashCode",
            "toString",
            "wait",
            "notify",
            "notifyAll"
    };

    @Override
    public void onActive() throws Throwable {
        /**
         * 如果有key，则将根据是否是压测流量置压测流量缓存key
         * 判断返回值是否是ConfigAccessor，如果是则设置Config,Config中包含连接信息
         */
        addRedissonConfigWrapperTransform("org.redisson.Redisson");
        addRedissonConfigWrapperTransform("org.redisson.RedissonRx");
        addRedissonConfigWrapperTransform("org.redisson.RedissonReactive");
        addRedissonConfigWrapperTransform("org.redisson.reactive.RedissonBatchReactive");
        addRedissonConfigWrapperTransform("org.redisson.RedissonBatch");
        addRedissonConfigWrapperTransform("org.redisson.RedissonBatchRx");

        /**
         * 操作redis的埋点以及压测缓存key设置
         */

        addRedissonMethodTransform("org.redisson.RedissonStream");
        addRedissonMethodTransform("org.redisson.RedissonBinaryStream");
        addRedissonMethodTransform("org.redisson.RedissonGeo");
        addRedissonMethodTransform("org.redisson.RedissonTimeSeries");
        addRedissonMethodTransform("org.redisson.RedissonBucket");
        addRedissonMethodTransform("org.redisson.RedissonRateLimiter");
        addRedissonMethodTransform("org.redisson.RedissonBuckets");
        addRedissonMethodTransform("org.redisson.RedissonHyperLogLog");
        addRedissonMethodTransform("org.redisson.RedissonList");
        addRedissonMethodTransform("org.redisson.RedissonListMultimap");
        addRedissonMethodTransform("org.redisson.RedissonLocalCachedMap");
        addRedissonMethodTransform("org.redisson.RedissonMap");
        addRedissonMethodTransform("org.redisson.RedissonSetMultimap");
        addRedissonMethodTransform("org.redisson.RedissonListMultimapCache");
        addRedissonMethodTransform("org.redisson.RedissonSetCache");
        addRedissonMethodTransform("org.redisson.RedissonMapCache");
        addRedissonMethodTransform("org.redisson.RedissonLock");
        addRedissonMethodTransform("org.redisson.RedissonFairLock");
        addRedissonMethodTransform("org.redisson.RedissonReadWriteLock");
        addRedissonMethodTransform("org.redisson.RedissonScript");
        addRedissonMethodTransform("org.redisson.RedissonExecutorService");
        addRedissonMethodTransform("org.redisson.RedissonRemoteService");
        addRedissonMethodTransform("org.redisson.RedissonSortedSet");
        addRedissonMethodTransform("org.redisson.RedissonScoredSortedSet");
        addRedissonMethodTransform("org.redisson.RedissonLexSortedSet");
        addRedissonMethodTransform("org.redisson.RedissonTopic");
        addRedissonMethodTransform("org.redisson.RedissonPatternTopic");
        addRedissonMethodTransform("org.redisson.RedissonDelayedQueue");
        addRedissonMethodTransform("org.redisson.RedissonQueue");
        addRedissonMethodTransform("org.redisson.RedissonBlockingQueue");
        addRedissonMethodTransform("org.redisson.RedissonDeque");
        addRedissonMethodTransform("org.redisson.RedissonBlockingDeque");
        addRedissonMethodTransform("org.redisson.RedissonAtomicLong");
        addRedissonMethodTransform("org.redisson.RedissonLongAdder");
        addRedissonMethodTransform("org.redisson.RedissonDoubleAdder");
        addRedissonMethodTransform("org.redisson.RedissonAtomicDouble");
        addRedissonMethodTransform("org.redisson.RedissonCountDownLatch");
        addRedissonMethodTransform("org.redisson.RedissonBitSet");
        addRedissonMethodTransform("org.redisson.RedissonSemaphore");
        addRedissonMethodTransform("org.redisson.RedissonPermitExpirableSemaphore");
        addRedissonMethodTransform("org.redisson.RedissonBloomFilter");
        addRedissonMethodTransform("org.redisson.RedissonKeys");
        addRedissonMethodTransform("org.redisson.RedissonBatch");
        addRedissonMethodTransform("org.redisson.RedissonLiveObjectService");
        addRedissonMethodTransform("org.redisson.RedissonPriorityQueue");
        addRedissonMethodTransform("org.redisson.RedissonPriorityBlockingQueue");
        addRedissonMethodTransform("org.redisson.RedissonPriorityBlockingDeque");
        addRedissonMethodTransform("org.redisson.RedissonPriorityDeque");
        addRedissonMethodTransform("org.redisson.RedissonRx");
        addRedissonMethodTransform("org.redisson.rx.RxProxyBuilder$1$1");
        addRedissonMethodTransform("org.redisson.RedissonReactive");
        addRedissonMethodTransform("org.redisson.reactive.RedissonReadWriteLockReactive");
        addRedissonMethodTransform("org.redisson.reactive.RedissonMapCacheReactive");
        addRedissonMethodTransform("org.redisson.reactive.RedissonListReactive");
        addRedissonMethodTransform("org.redisson.reactive.RedissonListMultimapReactive");
        addRedissonMethodTransform("org.redisson.reactive.RedissonSetMultimapReactive");
        addRedissonMethodTransform("org.redisson.reactive.RedissonMapReactive");
        addRedissonMethodTransform("org.redisson.reactive.RedissonSetReactive");
        addRedissonMethodTransform("org.redisson.reactive.RedissonScoredSortedSetReactive");
        addRedissonMethodTransform("org.redisson.reactive.RedissonLexSortedSetReactive");
        addRedissonMethodTransform("org.redisson.reactive.RedissonTopicReactive");
        addRedissonMethodTransform("org.redisson.reactive.RedissonBlockingQueueReactive");
        addRedissonMethodTransform("org.redisson.reactive.RedissonSetCacheReactive");
        addRedissonMethodTransform("org.redisson.reactive.RedissonBatchReactive");
        addRedissonMethodTransform("org.redisson.reactive.RedissonKeysReactive");
        addRedissonMethodTransform("org.redisson.reactive.RedissonTransactionReactive");
        addRedissonMethodTransform("org.redisson.reactive.RedissonBlockingDequeReactive");

        addReactiveProxyBuilderTransform("org.redisson.reactive.ReactiveProxyBuilder$1");
        addReactiveProxyBuilderTransform("org.redisson.reactive.ReactiveProxyBuilder$1$1");

        addRedissonCommandTransform("org.redisson.command.CommandAsyncService");
        addRedissonCommandTransform("org.redisson.command.CommandBatchService");
        addRedissonCommandTransform("org.redisson.command.CommandSyncService");
        addRedissonCommandTransform("org.redisson.reactive.CommandReactiveService");
        addRedissonCommandTransform("org.redisson.reactive.CommandReactiveBatchService");
        addRedissonCommandTransform("org.redisson.rx.CommandRxBatchService");

        addReleaseResource(new ReleaseResource(null) {
            @Override
            public void release() {
                RedissonFactory.destroy();
            }
        });
    }

    private void addRedissonConfigWrapperTransform(String className) {
        this.enhanceTemplate.enhance(this, className, new EnhanceCallback() {
            @Override
            public void doEnhance(InstrumentClass target) {
                InstrumentMethod method = target.getDeclaredMethods(EXCLUDE_METHOD_NAMES).withNot();
                method.addInterceptor(Listeners.dynamicScope(RedissonMethodInterceptor.class, ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

                InstrumentMethod method0 = target.getDeclaredMethods(RedissonUtils.combine);
                method0.addInterceptor(Listeners.of(ShadowDbMethodInterceptor.class));

                InstrumentMethod shutdownMethod = target.getDeclaredMethod("shutdown");
                shutdownMethod.addInterceptor(Listeners.of(ShutdownMethodInterceptor.class));
            }
        });
    }


    private void addRedissonMethodTransform(String className) {
        this.enhanceTemplate.enhance(this, className, new EnhanceCallback() {
            @Override
            public void doEnhance(InstrumentClass target) {
                InstrumentMethod method = target.getDeclaredMethods(
                        "createGroup", "createGroupAsync", "ackAsync", "ack", "listPendingAsync", "listPending", "listPendingAsync", "listPending", "fastClaim", "fastClaimAsync",
                        "claimAsync", "claim", "readGroupAsync", "readGroup", "addAll", "addAllAsync", "sizeAsync", "read", "readAsync", "addAsync", "add", "rangeAsync", "range", "rangeReversedAsync",
                        "rangeReversed", "remove", "trimAsync", "trimNonStrictAsync", "trim", "trimNonStrict", "removeGroupAsync", "removeGroup", "removeConsumerAsync", "removeConsumer", "updateGroupMessageIdAsync",
                        "updateGroupMessageId");
                method.addInterceptor(Listeners.dynamicScope(RedissonTraceMethodInterceptor.class, ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));
            }
        });
    }

    private void addReactiveProxyBuilderTransform(String className) {
        this.enhanceTemplate.enhance(this, className, new EnhanceCallback() {
            @Override
            public void doEnhance(InstrumentClass target) {
                final InstrumentMethod executeMethod = target.getDeclaredMethod("execute", "java.lang.reflect.Method", "java.lang.Object", "java.lang.Object[]");
                executeMethod.addInterceptor(Listeners.of(ReactiveMethodInterceptor.class));
            }
        });
    }

    private void addRedissonCommandTransform(String className) {
        this.enhanceTemplate.enhance(this, className, new EnhanceCallback() {
            @Override
            public void doEnhance(InstrumentClass target) {
                InstrumentMethod method = target.getDeclaredMethods("async");
                method.addInterceptor(Listeners.of(CommandAsyncServiceMethodInterceptor.class));
            }
        });
    }
}
