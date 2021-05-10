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
package com.pamirs.attach.plugin.lettuce;

import com.pamirs.attach.plugin.common.datasource.redisserver.AbstractRedisServerFactory;
import com.pamirs.attach.plugin.lettuce.interceptor.*;
import com.pamirs.attach.plugin.lettuce.shadowserver.LettuceFactory;
import com.pamirs.pradar.interceptor.Interceptors;
import com.shulie.instrument.simulator.api.ExtensionModule;
import com.shulie.instrument.simulator.api.ModuleInfo;
import com.shulie.instrument.simulator.api.ModuleLifecycleAdapter;
import com.shulie.instrument.simulator.api.instrument.EnhanceCallback;
import com.shulie.instrument.simulator.api.instrument.InstrumentClass;
import com.shulie.instrument.simulator.api.instrument.InstrumentMethod;
import com.shulie.instrument.simulator.api.listener.Listeners;
import com.shulie.instrument.simulator.api.resource.DynamicFieldManager;
import com.shulie.instrument.simulator.api.resource.ReleaseResource;
import com.shulie.instrument.simulator.api.scope.ExecutionPolicy;
import org.kohsuke.MetaInfServices;

import javax.annotation.Resource;

@MetaInfServices(ExtensionModule.class)
@ModuleInfo(id = LettuceConstants.MODULE_NAME, version = "1.0.0", author = "xiaobin@shulie.io", description = "redis 的 lettuce 客户端")
public class LettucePlugin extends ModuleLifecycleAdapter implements ExtensionModule {
    @Resource
    protected DynamicFieldManager manager;

    private static final String[] EXCLUDE_METHOD_NAMES = new String[]{"_$SIMULATOR$_getDynamicField", "clone", "equals", "finalize", "getClass", "hashCode", "notify", "notifyAll", "toString", "wait", "dispatch", "getConnection", "setAutoFlushCommands", "setTimeout", "createMono", "createDissolvingFlux"};

    private static final String[] FIRST_ARGS_INCLUDE_METHODS = new String[]{
            "append",
            "bitcount",
            "bitfield",
            "bitpos",
            "clusterKeyslot",
            "decr",
            "decrby",
            "del",
            "dump",
            "exists",
            "expire",
            "expireat",
            "geoadd",
            "geodist",
            "geohash",
            "geopos",
            "georadius",
            "georadius_ro",
            "georadiusbymember",
            "georadiusbymember_ro",
            "get",
            "getbit",
            "getrange",
            "getset",
            "hdel",
            "hexists",
            "hget",
            "hincrby",
            "hincrbyfloat",
            "hlen",
            "hmset",
            "hset",
            "hsetnx",
            "hstrlen",
            "incr",
            "incrby",
            "incrbyfloat",
            "lindex",
            "linsert",
            "llen",
            "lpop",
            "lpush",
            "lpushx",
            "lrem",
            "lset",
            "ltrim",
            "move",
            "mset",
            "setnx",
            "msetnx",
            "objectEncoding",
            "objectIdletime",
            "objectRefcount",
            "persist",
            "pexpire",
            "pexpireat",
            "pfadd",
            "pfcount",
            "psetex",
            "pttl",
            "restore",
            "rpop",
            "rpush",
            "rpushx",
            "sadd",
            "scard",
            "sdiff",
            "set",
            "setbit",
            "setex",
            "setrange",
            "sinter",
            "sismember",
            "smembers",
            "smove",
            "sortStore",
            "spop",
            "srem",
            "strlen",
            "touch",
            "ttl",
            "type",
            "unlink",
            "watch",
            "xack",
            "xclaim",
            "xdel",
            "xgroupDelconsumer",
            "xgroupDestroy",
            "xlen",
            "xpending",
            "xrange",
            "xrevrange",
            "xtrim",
            "zadd",
            "zaddincr",
            "zcard",
            "zcount",
            "zincrby",
            "zlexcount",
            "zpopmin",
            "zpopmax",
            "mget",
            "sort",
            "srandmember",
            "sscan",
            "sunion",
            "zrangebylex",
            "zrank",
            "zrem",
            "zremrangebylex",
            "zremrangebyrank",
            "zremrangebyscore",
            "zrevrangebylex",
            "zrevrank",
            "zscore",
            "clientSetname",
            "publish",
            "pubsubChannels",
            "pubsubNumsub",
            "xinfoStream",
            "xinfoGroups",
            "xinfoConsumers",
            "debugObject",
            "debugSdslen"

    };

    @Override
    public void onActive() throws Throwable {
        AbstractRedisServerFactory.setDynamicFieldManager(manager);

        addRedisClient();
        addDefaultConnectionFuture();
        addRedisCommands();
        addMasterSlave();

        addReleaseResource(new ReleaseResource(null) {
            @Override
            public void release() {
                LettuceFactory.destroy();
            }
        });
    }

    private void addMasterSlave() {
        this.enhanceTemplate.enhance(this, "io.lettuce.core.masterslave.MasterSlave", new EnhanceCallback() {
            @Override
            public void doEnhance(InstrumentClass target) {
                InstrumentMethod method = target.getDeclaredMethods("connect", "connectAsync");
                // Attach endpoint
                method.addInterceptor(Listeners.dynamicScope(RedisMasterSlaveAttachRedisURIsInterceptor.class, ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));
                method.addInterceptor(Listeners.dynamicScope(RedisMasterSlavePerformanceInterceptor.class, ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));
            }
        });
    }

    private void addRedisClient() {
        this.enhanceTemplate.enhance(this, "io.lettuce.core.RedisClient", new EnhanceCallback() {
            @Override
            public void doEnhance(InstrumentClass target) {
                // set RedisURI
                final InstrumentMethod constructor = target.getConstructor("io.lettuce.core.resource.ClientResources", "io.lettuce.core.RedisURI");
                constructor.addInterceptor(Listeners.of(RedisClientConstructorInterceptor.class));

                InstrumentMethod method = target.getDeclaredMethods("connect", "connectAsync", "connectPubSub", "connectPubSubAsync", "connectSentinel", "connectSentinelAsync");
                method.addInterceptor(Listeners.dynamicScope(RedisClientAttachRedisURIsInterceptor.class, ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));
                method.addInterceptor(Listeners.dynamicScope(RedisClientPerformanceInterceptor.class, ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));
            }
        });

        this.enhanceTemplate.enhance(this, "io.lettuce.core.AbstractRedisClient", new EnhanceCallback() {
            @Override
            public void doEnhance(InstrumentClass target) {
                InstrumentMethod shutdownAsyncMethod = target.getDeclaredMethod("shutdownAsync", "long", "long", "java.util.concurrent.TimeUnit");
                shutdownAsyncMethod.addInterceptor(Listeners.of(LettuceMethodShutdownInterceptor.class));
            }
        });

        this.enhanceTemplate.enhance(this, "io.lettuce.core.cluster.RedisClusterClient", new EnhanceCallback() {
            @Override
            public void doEnhance(InstrumentClass target) {
                // set RedisURI
                final InstrumentMethod constructor = target.getConstructor("io.lettuce.core.resource.ClientResources", "java.lang.Iterable");
                constructor.addInterceptor(Listeners.of(RedisClusterClientConstructorInterceptor.class));

                InstrumentMethod method = target.getDeclaredMethods("connect", "connectAsync", "connectPubSub", "connectPubSubAsync", "connectSentinel", "connectSentinelAsync");
                method.addInterceptor(Listeners.dynamicScope(RedisClientAttachRedisURIsInterceptor.class, ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));
                method.addInterceptor(Listeners.dynamicScope(RedisClientPerformanceInterceptor.class, ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));
            }
        });
    }

    private void addDefaultConnectionFuture() {
        this.enhanceTemplate.enhance(this, "io.lettuce.core.DefaultConnectionFuture", new EnhanceCallback() {
            @Override
            public void doEnhance(InstrumentClass target) {
                InstrumentMethod method = target.getDeclaredMethods("get", "join");
                // Attach endpoint
                method.addInterceptor(Listeners.of(RedisClientAttachRedisURIsInterceptor.class, "LETTUCE_SCOPE", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));
            }
        });
    }

    private void addRedisCommands() {
        this.enhanceTemplate.enhance(this, "io.lettuce.core.AbstractRedisAsyncCommands", callback);

        this.enhanceTemplate.enhance(this, "io.lettuce.core.AbstractRedisReactiveCommands", callback);

        this.enhanceTemplate.enhance(this, "io.lettuce.core.RedisAsyncCommandsImpl", callback);

        this.enhanceTemplate.enhance(this, "io.lettuce.core.cluster.RedisAdvancedClusterAsyncCommandsImpl", callback);

        this.enhanceTemplate.enhance(this, "io.lettuce.core.cluster.RedisClusterPubSubAsyncCommandsImpl", callback);

        this.enhanceTemplate.enhance(this, "io.lettuce.core.pubsub.RedisPubSubAsyncCommandsImpl", callback);

        this.enhanceTemplate.enhance(this, "io.lettuce.core.cluster.RedisAdvancedClusterReactiveCommandsImpl", callback);

        this.enhanceTemplate.enhance(this, "io.lettuce.core.cluster.RedisClusterPubSubReactiveCommandsImpl", callback);

        this.enhanceTemplate.enhance(this, "io.lettuce.core.pubsub.RedisPubSubReactiveCommandsImpl", callback);

        this.enhanceTemplate.enhance(this, "io.lettuce.core.RedisReactiveCommandsImpl", callback);

        this.enhanceTemplate.enhance(this, "io.lettuce.core.sentinel.RedisSentinelReactiveCommandsImpl", callback);
    }

    private EnhanceCallback callback = new EnhanceCallback() {
        @Override
        public void doEnhance(InstrumentClass target) {
            InstrumentMethod method0 = target.getDeclaredMethods(EXCLUDE_METHOD_NAMES).withNot();
            method0.addInterceptor(Listeners.dynamicScope(LettuceMethodInterceptor.class, ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod method = target.getDeclaredMethods(FIRST_ARGS_INCLUDE_METHODS);
            method.addInterceptor(Listeners.dynamicScope(LettuceMethodClusterTestFirstArgsInterceptor.class, ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod method1 = target.getDeclaredMethods("blpop", "brpop", "bzpopmin", "bzpopmax");
            method1.addInterceptor(Listeners.dynamicScope(LettuceMethodClusterTestSecondArgsInterceptor.class, ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod method2 = target.getDeclaredMethods("eval", "evalsha");
            method2.addInterceptor(Listeners.dynamicScope(LettuceMethodClusterTestThirdArgsInterceptor.class, ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod method3 = target.getDeclaredMethods("bitopAnd", "bitopNot", "bitopOr", "bitopXor", "rpoplpush", "sdiffstore", "sinterstore", "smove", "sunionstore", "pfmerge", "rename", "renamenx");
            method3.addInterceptor(Listeners.dynamicScope(LettuceMethodClusterTestFirstSecondArgsInterceptor.class, ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod method4 = target.getDeclaredMethods("sortStore");
            method4.addInterceptor(Listeners.of(LettuceMethodClusterTestFirstThirdArgsInterceptor.class, "Lettuce_sortStore", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod method5 = target.getDeclaredMethods("brpoplpush");
            method5.addInterceptor(Listeners.of(LettuceMethodClusterTestSecondThirdArgsInterceptor.class, "Lettuce_brpoplpush", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod xadd0 = target.getDeclaredMethod("xadd", "java.lang.Object", "java.lang.Object[]");
            xadd0.addInterceptor(Listeners.of(LettuceMethodClusterTestFirstSecondArgsArraySplitInterceptor.class, "Lettuce_xadd", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod xadd1 = target.getDeclaredMethod("xadd", "java.lang.Object", "io.lettuce.core.XAddArgs", "java.lang.Object[]");
            xadd1.addInterceptor(Listeners.of(LettuceMethodClusterTestFirstThirdArgsArraySplitInterceptor.class, "Lettuce_xadd", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod xadd2 = target.getDeclaredMethod("xadd", "java.lang.Object", "java.util.Map");
            xadd2.addInterceptor(Listeners.of(LettuceMethodClusterTestFirstSecondArgsArraySplitInterceptor.class, "Lettuce_xadd", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod xadd3 = target.getDeclaredMethod("xadd", "java.lang.Object", "io.lettuce.core.XAddArgs", "java.util.Map");
            xadd3.addInterceptor(Listeners.of(LettuceMethodClusterTestFirstThirdArgsArraySplitInterceptor.class, "Lettuce_xadd", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod xadd4 = target.getDeclaredMethod("xadd", "java.lang.Object", "java.lang.Object[]");
            xadd4.addInterceptor(Listeners.of(LettuceMethodClusterTestFirstThirdArgsArraySplitInterceptor.class, "Lettuce_xadd", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod xgroupCreate = target.getDeclaredMethod("xgroupCreate", "io.lettuce.core.XReadArgs$StreamOffset", "java.lang.Object");
            xgroupCreate.addInterceptor(Listeners.of(LettuceMethodFirstKeyStreamOffsetInterceptor.class, "Lettuce_xgroupCreate", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod xgroupCreate1 = target.getDeclaredMethod("xgroupCreate", "io.lettuce.core.XReadArgs$StreamOffset", "java.lang.Object", "io.lettuce.core.XGroupCreateArgs");
            xgroupCreate1.addInterceptor(Listeners.of(LettuceMethodFirstKeyStreamOffsetInterceptor.class, "Lettuce_xgroupCreate", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod xgroupSetid = target.getDeclaredMethod("xgroupSetid", "io.lettuce.core.XReadArgs$StreamOffset", "java.lang.Object");
            xgroupSetid.addInterceptor(Listeners.of(LettuceMethodFirstKeyStreamOffsetInterceptor.class, "Lettuce_xgroupSetid", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod xread = target.getDeclaredMethod("xread", "io.lettuce.core.XReadArgs$StreamOffset[]");
            xread.addInterceptor(Listeners.of(LettuceMethodFirstKeyStreamOffsetArrayInterceptor.class, "Lettuce_xread", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod xread1 = target.getDeclaredMethod("xread", "io.lettuce.core.XReadArgs", "io.lettuce.core.XReadArgs$StreamOffset[]");
            xread1.addInterceptor(Listeners.of(LettuceMethodFirstKeyStreamOffsetArrayInterceptor.class, "Lettuce_xread", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod xreadgroup = target.getDeclaredMethod("xreadgroup", "io.lettuce.core.Consumer", "io.lettuce.core.XReadArgs$StreamOffset[]");
            xreadgroup.addInterceptor(Listeners.of(LettuceMethodFirstKeyStreamOffsetArrayInterceptor.class, "Lettuce_xreadgroup", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod xreadgroup1 = target.getDeclaredMethod("xreadgroup", "io.lettuce.core.Consumer", "io.lettuce.core.XReadArgs", "io.lettuce.core.XReadArgs$StreamOffset[]");
            xreadgroup1.addInterceptor(Listeners.of(LettuceMethodFirstKeyStreamOffsetArrayInterceptor.class, "Lettuce_xreadgroup", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod zinterstore = target.getDeclaredMethod("zinterstore", "java.lang.Object", "java.lang.Object[]");
            zinterstore.addInterceptor(Listeners.of(LettuceMethodFirstKeyAndOtherKeysInterceptor.class, "Lettuce_zinterstore", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod zinterstore1 = target.getDeclaredMethod("zinterstore", "java.lang.Object", "io.lettuce.core.ZStoreArgs", "java.lang.Object[]");
            zinterstore1.addInterceptor(Listeners.of(LettuceMethodFirstKeyAndOtherKeysInterceptor.class, "Lettuce_zinterstore", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod zrange = target.getDeclaredMethod("zrange", "java.lang.Object", "long", "long");
            zrange.addInterceptor(Listeners.of(LettuceMethodClusterTestFirstArgsInterceptor.class, "Lettuce_zrange", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod zrange1 = target.getDeclaredMethod("zrange", "io.lettuce.core.output.ValueStreamingChannel", "java.lang.Object", "long", "long");
            zrange1.addInterceptor(Listeners.of(LettuceMethodClusterTestSecondArgsInterceptor.class, "Lettuce_zrange", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod zrangeWithScores = target.getDeclaredMethod("zrangeWithScores", "java.lang.Object", "long", "long");
            zrangeWithScores.addInterceptor(Listeners.of(LettuceMethodClusterTestFirstArgsInterceptor.class, "Lettuce_zrangeWithScores", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod zrangeWithScores1 = target.getDeclaredMethod("zrangeWithScores", "io.lettuce.core.output.ScoredValueStreamingChannel", "java.lang.Object", "long", "long");
            zrangeWithScores1.addInterceptor(Listeners.of(LettuceMethodClusterTestSecondArgsInterceptor.class, "Lettuce_zrangeWithScores", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod zrangebyscore = target.getDeclaredMethod("zrangebyscore", "java.lang.Object", "double", "double");
            zrangebyscore.addInterceptor(Listeners.of(LettuceMethodClusterTestFirstArgsInterceptor.class, "Lettuce_zrangebyscore", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod zrangebyscore1 = target.getDeclaredMethod("zrangebyscore", "java.lang.Object", "java.lang.String", "java.lang.String");
            zrangebyscore1.addInterceptor(Listeners.of(LettuceMethodClusterTestFirstArgsInterceptor.class, "Lettuce_zrangebyscore", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod zrangebyscore2 = target.getDeclaredMethod("zrangebyscore", "java.lang.Object", "io.lettuce.core.Range");
            zrangebyscore2.addInterceptor(Listeners.of(LettuceMethodClusterTestFirstArgsInterceptor.class, "Lettuce_zrangebyscore", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod zrangebyscore3 = target.getDeclaredMethod("zrangebyscore", "java.lang.Object", "double", "double", "long", "long");
            zrangebyscore3.addInterceptor(Listeners.of(LettuceMethodClusterTestFirstArgsInterceptor.class, "Lettuce_zrangebyscore", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod zrangebyscore4 = target.getDeclaredMethod("zrangebyscore", "java.lang.Object", "java.lang.String", "java.lang.String", "long", "long");
            zrangebyscore4.addInterceptor(Listeners.of(LettuceMethodClusterTestFirstArgsInterceptor.class, "Lettuce_zrangebyscore", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod zrangebyscore5 = target.getDeclaredMethod("zrangebyscore", "java.lang.Object", "io.lettuce.core.Range", "io.lettuce.core.Limit");
            zrangebyscore5.addInterceptor(Listeners.of(LettuceMethodClusterTestFirstArgsInterceptor.class, "Lettuce_zrangebyscore", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod zrangebyscore6 = target.getDeclaredMethod("zrangebyscore", "io.lettuce.core.output.ValueStreamingChannel", "java.lang.Object", "double", "double");
            zrangebyscore6.addInterceptor(Listeners.of(LettuceMethodClusterTestSecondArgsInterceptor.class, "Lettuce_zrangebyscore", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod zrangebyscore7 = target.getDeclaredMethod("zrangebyscore", "io.lettuce.core.output.ValueStreamingChannel", "java.lang.Object", "java.lang.String", "java.lang.String");
            zrangebyscore7.addInterceptor(Listeners.of(LettuceMethodClusterTestSecondArgsInterceptor.class, "Lettuce_zrangebyscore", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod zrangebyscore8 = target.getDeclaredMethod("zrangebyscore", "io.lettuce.core.output.ValueStreamingChannel", "java.lang.Object", "io.lettuce.core.Range");
            zrangebyscore8.addInterceptor(Listeners.of(LettuceMethodClusterTestSecondArgsInterceptor.class, "Lettuce_zrangebyscore", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod zrangebyscore9 = target.getDeclaredMethod("zrangebyscore", "io.lettuce.core.output.ValueStreamingChannel", "java.lang.Object", "double", "double", "long", "long");
            zrangebyscore9.addInterceptor(Listeners.of(LettuceMethodClusterTestSecondArgsInterceptor.class, "Lettuce_zrangebyscore", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod zrangebyscore10 = target.getDeclaredMethod("zrangebyscore", "io.lettuce.core.output.ValueStreamingChannel", "java.lang.Object", "java.lang.String", "java.lang.String", "long", "long");
            zrangebyscore10.addInterceptor(Listeners.of(LettuceMethodClusterTestSecondArgsInterceptor.class, "Lettuce_zrangebyscore", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod zrangebyscore11 = target.getDeclaredMethod("zrangebyscore", "io.lettuce.core.output.ValueStreamingChannel", "java.lang.Object", "io.lettuce.core.Range", "io.lettuce.core.Limit");
            zrangebyscore11.addInterceptor(Listeners.of(LettuceMethodClusterTestSecondArgsInterceptor.class, "Lettuce_zrangebyscore", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod zrangebyscoreWithScores = target.getDeclaredMethod("zrangebyscoreWithScores", "java.lang.Object", "double", "double");
            zrangebyscoreWithScores.addInterceptor(Listeners.of(LettuceMethodClusterTestFirstArgsInterceptor.class, "Lettuce_zrangebyscoreWithScores", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod zrangebyscoreWithScores1 = target.getDeclaredMethod("zrangebyscoreWithScores", "java.lang.Object", "java.lang.String", "java.lang.String");
            zrangebyscoreWithScores1.addInterceptor(Listeners.of(LettuceMethodClusterTestFirstArgsInterceptor.class, "Lettuce_zrangebyscoreWithScores", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod zrangebyscoreWithScores2 = target.getDeclaredMethod("zrangebyscoreWithScores", "java.lang.Object", "io.lettuce.core.Range");
            zrangebyscoreWithScores2.addInterceptor(Listeners.of(LettuceMethodClusterTestFirstArgsInterceptor.class, "Lettuce_zrangebyscoreWithScores", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod zrangebyscoreWithScores3 = target.getDeclaredMethod("zrangebyscoreWithScores", "java.lang.Object", "double", "double", "long", "long");
            zrangebyscoreWithScores3.addInterceptor(Listeners.of(LettuceMethodClusterTestFirstArgsInterceptor.class, "Lettuce_zrangebyscoreWithScores", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod zrangebyscoreWithScores4 = target.getDeclaredMethod("zrangebyscoreWithScores", "java.lang.Object", "java.lang.String", "java.lang.String", "long", "long");
            zrangebyscoreWithScores4.addInterceptor(Listeners.of(LettuceMethodClusterTestFirstArgsInterceptor.class, "Lettuce_zrangebyscoreWithScores", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod zrangebyscoreWithScores5 = target.getDeclaredMethod("zrangebyscoreWithScores", "java.lang.Object", "io.lettuce.core.Range", "io.lettuce.core.Limit");
            zrangebyscoreWithScores5.addInterceptor(Listeners.of(LettuceMethodClusterTestFirstArgsInterceptor.class, "Lettuce_zrangebyscoreWithScores", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod zrangebyscoreWithScores6 = target.getDeclaredMethod("zrangebyscoreWithScores", "io.lettuce.core.output.ScoredValueStreamingChannel", "java.lang.Object", "double", "double");
            zrangebyscoreWithScores6.addInterceptor(Listeners.of(LettuceMethodClusterTestSecondArgsInterceptor.class, "Lettuce_zrangebyscoreWithScores", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod zrangebyscoreWithScores7 = target.getDeclaredMethod("zrangebyscoreWithScores", "io.lettuce.core.output.ScoredValueStreamingChannel", "java.lang.Object", "java.lang.String", "java.lang.String");
            zrangebyscoreWithScores7.addInterceptor(Listeners.of(LettuceMethodClusterTestSecondArgsInterceptor.class, "Lettuce_zrangebyscoreWithScores", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod zrangebyscoreWithScores8 = target.getDeclaredMethod("zrangebyscoreWithScores", "io.lettuce.core.output.ScoredValueStreamingChannel", "java.lang.Object", "io.lettuce.core.Range");
            zrangebyscoreWithScores8.addInterceptor(Listeners.of(LettuceMethodClusterTestSecondArgsInterceptor.class, "Lettuce_zrangebyscoreWithScores", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod zrangebyscoreWithScores9 = target.getDeclaredMethod("zrangebyscoreWithScores", "io.lettuce.core.output.ScoredValueStreamingChannel", "java.lang.Object", "double", "double", "long", "long");
            zrangebyscoreWithScores9.addInterceptor(Listeners.of(LettuceMethodClusterTestSecondArgsInterceptor.class, "Lettuce_zrangebyscoreWithScores", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod zrangebyscoreWithScores10 = target.getDeclaredMethod("zrangebyscoreWithScores", "io.lettuce.core.output.ScoredValueStreamingChannel", "java.lang.Object", "java.lang.String", "java.lang.String", "long", "long");
            zrangebyscoreWithScores10.addInterceptor(Listeners.of(LettuceMethodClusterTestSecondArgsInterceptor.class, "Lettuce_zrangebyscoreWithScores", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod zrangebyscoreWithScores11 = target.getDeclaredMethod("zrangebyscoreWithScores", "io.lettuce.core.output.ScoredValueStreamingChannel", "java.lang.Object", "io.lettuce.core.Range", "io.lettuce.core.Limit");
            zrangebyscoreWithScores11.addInterceptor(Listeners.of(LettuceMethodClusterTestSecondArgsInterceptor.class, "Lettuce_zrangebyscoreWithScores", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod zrevrange = target.getDeclaredMethod("zrevrange", "java.lang.Object", "long", "long");
            zrevrange.addInterceptor(Listeners.of(LettuceMethodClusterTestFirstArgsInterceptor.class, "Lettuce_zrevrange", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod zrevrange1 = target.getDeclaredMethod("zrevrange", "io.lettuce.core.output.ValueStreamingChannel", "java.lang.Object", "long", "long");
            zrevrange1.addInterceptor(Listeners.of(LettuceMethodClusterTestSecondArgsInterceptor.class, "Lettuce_zrevrange", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod zrevrangeWithScores = target.getDeclaredMethod("zrevrangeWithScores", "java.lang.Object", "long", "long");
            zrevrangeWithScores.addInterceptor(Listeners.of(LettuceMethodClusterTestFirstArgsInterceptor.class, "Lettuce_zrevrangeWithScores", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod zrevrangeWithScores1 = target.getDeclaredMethod("zrevrangeWithScores", "io.lettuce.core.output.ValueStreamingChannel", "java.lang.Object", "long", "long");
            zrevrangeWithScores1.addInterceptor(Listeners.of(LettuceMethodClusterTestSecondArgsInterceptor.class, "Lettuce_zrevrangeWithScores", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod zrevrangebyscore = target.getDeclaredMethod("zrevrangebyscore", "java.lang.Object", "double", "double");
            zrevrangebyscore.addInterceptor(Listeners.of(LettuceMethodClusterTestFirstArgsInterceptor.class, "Lettuce_zrevrangebyscore", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod zrevrangebyscore1 = target.getDeclaredMethod("zrevrangebyscore", "java.lang.Object", "java.lang.String", "java.lang.String");
            zrevrangebyscore1.addInterceptor(Listeners.of(LettuceMethodClusterTestFirstArgsInterceptor.class, "Lettuce_zrevrangebyscore", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod zrevrangebyscore2 = target.getDeclaredMethod("zrevrangebyscore", "java.lang.Object", "io.lettuce.core.Range");
            zrevrangebyscore2.addInterceptor(Listeners.of(LettuceMethodClusterTestFirstArgsInterceptor.class, "Lettuce_zrevrangebyscore", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod zrevrangebyscore3 = target.getDeclaredMethod("zrevrangebyscore", "java.lang.Object", "double", "double", "long", "long");
            zrevrangebyscore3.addInterceptor(Listeners.of(LettuceMethodClusterTestFirstArgsInterceptor.class, "Lettuce_zrevrangebyscore", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod zrevrangebyscore4 = target.getDeclaredMethod("zrevrangebyscore", "java.lang.Object", "java.lang.String", "java.lang.String", "long", "long");
            zrevrangebyscore4.addInterceptor(Listeners.of(LettuceMethodClusterTestFirstArgsInterceptor.class, "Lettuce_zrevrangebyscore", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod zrevrangebyscore5 = target.getDeclaredMethod("zrevrangebyscore", "java.lang.Object", "io.lettuce.core.Range", "io.lettuce.core.Limit");
            zrevrangebyscore5.addInterceptor(Listeners.of(LettuceMethodClusterTestFirstArgsInterceptor.class, "Lettuce_zrevrangebyscore", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod zrevrangebyscore6 = target.getDeclaredMethod("zrevrangebyscore", "io.lettuce.core.output.ValueStreamingChannel", "java.lang.Object", "double", "double");
            zrevrangebyscore6.addInterceptor(Listeners.of(LettuceMethodClusterTestSecondArgsInterceptor.class, "Lettuce_zrevrangebyscore", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod zrevrangebyscore7 = target.getDeclaredMethod("zrevrangebyscore", "io.lettuce.core.output.ValueStreamingChannel", "java.lang.Object", "java.lang.String", "java.lang.String");
            zrevrangebyscore7.addInterceptor(Listeners.of(LettuceMethodClusterTestSecondArgsInterceptor.class, "Lettuce_zrevrangebyscore", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod zrevrangebyscore8 = target.getDeclaredMethod("zrevrangebyscore", "io.lettuce.core.output.ValueStreamingChannel", "java.lang.Object", "io.lettuce.core.Range");
            zrevrangebyscore8.addInterceptor(Listeners.of(LettuceMethodClusterTestSecondArgsInterceptor.class, "Lettuce_zrevrangebyscore", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod zrevrangebyscore9 = target.getDeclaredMethod("zrevrangebyscore", "io.lettuce.core.output.ValueStreamingChannel", "java.lang.Object", "double", "double", "long", "long");
            zrevrangebyscore9.addInterceptor(Listeners.of(LettuceMethodClusterTestSecondArgsInterceptor.class, "Lettuce_zrevrangebyscore", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod zrevrangebyscore10 = target.getDeclaredMethod("zrevrangebyscore", "io.lettuce.core.output.ValueStreamingChannel", "java.lang.Object", "java.lang.String", "java.lang.String", "long", "long");
            zrevrangebyscore10.addInterceptor(Listeners.of(LettuceMethodClusterTestSecondArgsInterceptor.class, "Lettuce_zrevrangebyscore", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod zrevrangebyscore11 = target.getDeclaredMethod("zrevrangebyscore", "io.lettuce.core.output.ValueStreamingChannel", "java.lang.Object", "io.lettuce.core.Range", "io.lettuce.core.Limit");
            zrevrangebyscore11.addInterceptor(Listeners.of(LettuceMethodClusterTestSecondArgsInterceptor.class, "Lettuce_zrevrangebyscore", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod zrevrangebyscoreWithScores = target.getDeclaredMethod("zrevrangebyscoreWithScores", "java.lang.Object", "double", "double");
            zrevrangebyscoreWithScores.addInterceptor(Listeners.of(LettuceMethodClusterTestFirstArgsInterceptor.class, "Lettuce_zrevrangebyscoreWithScores", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod zrevrangebyscoreWithScores1 = target.getDeclaredMethod("zrevrangebyscoreWithScores", "java.lang.Object", "java.lang.String", "java.lang.String");
            zrevrangebyscoreWithScores1.addInterceptor(Listeners.of(LettuceMethodClusterTestFirstArgsInterceptor.class, "Lettuce_zrevrangebyscoreWithScores", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod zrevrangebyscoreWithScores2 = target.getDeclaredMethod("zrevrangebyscoreWithScores", "java.lang.Object", "io.lettuce.core.Range");
            zrevrangebyscoreWithScores2.addInterceptor(Listeners.of(LettuceMethodClusterTestFirstArgsInterceptor.class, "Lettuce_zrevrangebyscoreWithScores", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod zrevrangebyscoreWithScores3 = target.getDeclaredMethod("zrevrangebyscoreWithScores", "java.lang.Object", "double", "double", "long", "long");
            zrevrangebyscoreWithScores3.addInterceptor(Listeners.of(LettuceMethodClusterTestFirstArgsInterceptor.class, "Lettuce_zrevrangebyscoreWithScores", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod zrevrangebyscoreWithScores4 = target.getDeclaredMethod("zrevrangebyscoreWithScores", "java.lang.Object", "java.lang.String", "java.lang.String", "long", "long");
            zrevrangebyscoreWithScores4.addInterceptor(Listeners.of(LettuceMethodClusterTestFirstArgsInterceptor.class, "Lettuce_zrevrangebyscoreWithScores", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod zrevrangebyscoreWithScores5 = target.getDeclaredMethod("zrevrangebyscoreWithScores", "java.lang.Object", "io.lettuce.core.Range", "io.lettuce.core.Limit");
            zrevrangebyscoreWithScores5.addInterceptor(Listeners.of(LettuceMethodClusterTestFirstArgsInterceptor.class, "Lettuce_zrevrangebyscoreWithScores", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod zrevrangebyscoreWithScores6 = target.getDeclaredMethod("zrevrangebyscoreWithScores", "io.lettuce.core.output.ValueStreamingChannel", "java.lang.Object", "double", "double");
            zrevrangebyscoreWithScores6.addInterceptor(Listeners.of(LettuceMethodClusterTestSecondArgsInterceptor.class, "Lettuce_zrevrangebyscoreWithScores", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod zrevrangebyscoreWithScores7 = target.getDeclaredMethod("zrevrangebyscoreWithScores", "io.lettuce.core.output.ValueStreamingChannel", "java.lang.Object", "java.lang.String", "java.lang.String");
            zrevrangebyscoreWithScores7.addInterceptor(Listeners.of(LettuceMethodClusterTestSecondArgsInterceptor.class, "Lettuce_zrevrangebyscoreWithScores", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod zrevrangebyscoreWithScores8 = target.getDeclaredMethod("zrevrangebyscoreWithScores", "io.lettuce.core.output.ValueStreamingChannel", "java.lang.Object", "io.lettuce.core.Range");
            zrevrangebyscoreWithScores8.addInterceptor(Listeners.of(LettuceMethodClusterTestSecondArgsInterceptor.class, "Lettuce_zrevrangebyscoreWithScores", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod zrevrangebyscoreWithScores9 = target.getDeclaredMethod("zrevrangebyscoreWithScores", "io.lettuce.core.output.ValueStreamingChannel", "java.lang.Object", "double", "double", "long", "long");
            zrevrangebyscoreWithScores9.addInterceptor(Listeners.of(LettuceMethodClusterTestSecondArgsInterceptor.class, "Lettuce_zrevrangebyscoreWithScores", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod zrevrangebyscoreWithScores10 = target.getDeclaredMethod("zrevrangebyscoreWithScores", "io.lettuce.core.output.ValueStreamingChannel", "java.lang.Object", "java.lang.String", "java.lang.String", "long", "long");
            zrevrangebyscoreWithScores10.addInterceptor(Listeners.of(LettuceMethodClusterTestSecondArgsInterceptor.class, "Lettuce_zrevrangebyscoreWithScores", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod zrevrangebyscoreWithScores11 = target.getDeclaredMethod("zrevrangebyscoreWithScores", "io.lettuce.core.output.ValueStreamingChannel", "java.lang.Object", "io.lettuce.core.Range", "io.lettuce.core.Limit");
            zrevrangebyscoreWithScores11.addInterceptor(Listeners.of(LettuceMethodClusterTestSecondArgsInterceptor.class, "Lettuce_zrevrangebyscoreWithScores", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod zscan = target.getDeclaredMethod("zscan", "java.lang.Object");
            zscan.addInterceptor(Listeners.of(LettuceMethodClusterTestFirstArgsInterceptor.class, "Lettuce_zscan", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod zscan1 = target.getDeclaredMethod("zscan", "java.lang.Object", "io.lettuce.core.ScanArgs");
            zscan1.addInterceptor(Listeners.of(LettuceMethodClusterTestFirstArgsInterceptor.class, "Lettuce_zscan", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod zscan2 = target.getDeclaredMethod("zscan", "java.lang.Object", "io.lettuce.core.ScanCursor", "io.lettuce.core.ScanArgs");
            zscan2.addInterceptor(Listeners.of(LettuceMethodClusterTestFirstArgsInterceptor.class, "Lettuce_zscan", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod zscan3 = target.getDeclaredMethod("zscan", "java.lang.Object", "io.lettuce.core.ScanCursor");
            zscan3.addInterceptor(Listeners.of(LettuceMethodClusterTestFirstArgsInterceptor.class, "Lettuce_zscan", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod zscan4 = target.getDeclaredMethod("zscan", "io.lettuce.core.output.ScoredValueStreamingChannel", "java.lang.Object");
            zscan4.addInterceptor(Listeners.of(LettuceMethodClusterTestSecondArgsInterceptor.class, "Lettuce_zscan", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod zscan5 = target.getDeclaredMethod("zscan", "io.lettuce.core.output.ScoredValueStreamingChannel", "java.lang.Object", "io.lettuce.core.ScanArgs");
            zscan5.addInterceptor(Listeners.of(LettuceMethodClusterTestSecondArgsInterceptor.class, "Lettuce_zscan", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod zscan6 = target.getDeclaredMethod("zscan", "io.lettuce.core.output.ScoredValueStreamingChannel", "java.lang.Object", "io.lettuce.core.ScanCursor", "io.lettuce.core.ScanArgs");
            zscan6.addInterceptor(Listeners.of(LettuceMethodClusterTestSecondArgsInterceptor.class, "Lettuce_zscan", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod zscan7 = target.getDeclaredMethod("zscan", "io.lettuce.core.output.ScoredValueStreamingChannel", "java.lang.Object", "io.lettuce.core.ScanCursor");
            zscan7.addInterceptor(Listeners.of(LettuceMethodClusterTestSecondArgsInterceptor.class, "Lettuce_zscan", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod zunionstore = target.getDeclaredMethod("zunionstore", "java.lang.Object", "java.lang.Object[]");
            zunionstore.addInterceptor(Listeners.of(LettuceMethodFirstKeyAndOtherKeysInterceptor.class, "Lettuce_zunionstore", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod zunionstore1 = target.getDeclaredMethod("zunionstore", "java.lang.Object", "io.lettuce.core.ZStoreArgs", "java.lang.Object[]");
            zunionstore1.addInterceptor(Listeners.of(LettuceMethodFirstKeyAndOtherKeysInterceptor.class, "Lettuce_zunionstore", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod hscan = target.getDeclaredMethod("hscan", "java.lang.Object");
            hscan.addInterceptor(Listeners.of(LettuceMethodClusterTestFirstArgsInterceptor.class, "Lettuce_hscan", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod hscan1 = target.getDeclaredMethod("hscan", "java.lang.Object", "io.lettuce.core.ScanArgs");
            hscan1.addInterceptor(Listeners.of(LettuceMethodClusterTestFirstArgsInterceptor.class, "Lettuce_hscan", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod hscan2 = target.getDeclaredMethod("hscan", "java.lang.Object", "io.lettuce.core.ScanCursor", "io.lettuce.core.ScanArgs");
            hscan2.addInterceptor(Listeners.of(LettuceMethodClusterTestFirstArgsInterceptor.class, "Lettuce_hscan", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod hscan3 = target.getDeclaredMethod("hscan", "java.lang.Object", "io.lettuce.core.ScanCursor");
            hscan3.addInterceptor(Listeners.of(LettuceMethodClusterTestFirstArgsInterceptor.class, "Lettuce_hscan", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod hscan4 = target.getDeclaredMethod("hscan", "io.lettuce.core.output.KeyValueStreamingChannel", "java.lang.Object");
            hscan4.addInterceptor(Listeners.of(LettuceMethodClusterTestSecondArgsInterceptor.class, "Lettuce_hscan", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod hscan5 = target.getDeclaredMethod("hscan", "io.lettuce.core.output.KeyValueStreamingChannel", "java.lang.Object", "io.lettuce.core.ScanArgs");
            hscan5.addInterceptor(Listeners.of(LettuceMethodClusterTestSecondArgsInterceptor.class, "Lettuce_hscan", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod hscan6 = target.getDeclaredMethod("hscan", "io.lettuce.core.output.KeyValueStreamingChannel", "java.lang.Object", "io.lettuce.core.ScanCursor", "io.lettuce.core.ScanArgs");
            hscan6.addInterceptor(Listeners.of(LettuceMethodClusterTestSecondArgsInterceptor.class, "Lettuce_hscan", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod hscan7 = target.getDeclaredMethod("hscan", "io.lettuce.core.output.KeyValueStreamingChannel", "java.lang.Object", "io.lettuce.core.ScanCursor");
            hscan7.addInterceptor(Listeners.of(LettuceMethodClusterTestSecondArgsInterceptor.class, "Lettuce_hscan", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod hgetall = target.getDeclaredMethod("hgetall", "java.lang.Object");
            hgetall.addInterceptor(Listeners.of(LettuceMethodClusterTestFirstArgsInterceptor.class, "Lettuce_hgetall", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod hgetall1 = target.getDeclaredMethod("hgetall", "io.lettuce.core.output.KeyValueStreamingChannel", "java.lang.Object");
            hgetall1.addInterceptor(Listeners.of(LettuceMethodClusterTestSecondArgsInterceptor.class, "Lettuce_hgetall", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod hkeys = target.getDeclaredMethod("hkeys", "java.lang.Object");
            hkeys.addInterceptor(Listeners.of(LettuceMethodClusterTestFirstArgsInterceptor.class, "Lettuce_hkeys", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod hkeys1 = target.getDeclaredMethod("hkeys", "io.lettuce.core.output.KeyStreamingChannel", "java.lang.Object");
            hkeys1.addInterceptor(Listeners.of(LettuceMethodClusterTestSecondArgsInterceptor.class, "Lettuce_hkeys", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod keys = target.getDeclaredMethod("keys", "java.lang.Object");
            keys.addInterceptor(Listeners.of(LettuceMethodClusterTestFirstArgsInterceptor.class, "Lettuce_keys", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod keys1 = target.getDeclaredMethod("keys", "io.lettuce.core.output.KeyStreamingChannel", "java.lang.Object");
            keys1.addInterceptor(Listeners.of(LettuceMethodClusterTestSecondArgsInterceptor.class, "Lettuce_keys", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod hmget = target.getDeclaredMethod("hmget", "java.lang.Object", "java.lang.Object[]");
            hmget.addInterceptor(Listeners.of(LettuceMethodClusterTestFirstArgsInterceptor.class, "Lettuce_hmget", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod hmget1 = target.getDeclaredMethod("hmget", "io.lettuce.core.output.KeyValueStreamingChannel", "java.lang.Object", "java.lang.Object[]");
            hmget1.addInterceptor(Listeners.of(LettuceMethodClusterTestSecondArgsInterceptor.class, "Lettuce_hmget", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod hvals = target.getDeclaredMethod("hvals", "java.lang.Object");
            hvals.addInterceptor(Listeners.of(LettuceMethodClusterTestFirstArgsInterceptor.class, "Lettuce_hvals", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod hvals1 = target.getDeclaredMethod("hvals", "io.lettuce.core.output.ValueStreamingChannel", "java.lang.Object");
            hvals1.addInterceptor(Listeners.of(LettuceMethodClusterTestSecondArgsInterceptor.class, "Lettuce_hvals", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod lrange = target.getDeclaredMethod("lrange", "java.lang.Object", "long", "long");
            lrange.addInterceptor(Listeners.of(LettuceMethodClusterTestFirstArgsInterceptor.class, "Lettuce_lrange", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod lrange1 = target.getDeclaredMethod("lrange", "io.lettuce.core.output.ValueStreamingChannel", "java.lang.Object", "long", "long");
            lrange1.addInterceptor(Listeners.of(LettuceMethodClusterTestSecondArgsInterceptor.class, "Lettuce_lrange", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod mget = target.getDeclaredMethod("mget", "java.lang.Object[]");
            mget.addInterceptor(Listeners.of(LettuceMethodClusterTestFirstArgsInterceptor.class, "Lettuce_mget", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod mget1 = target.getDeclaredMethod("mget", "java.lang.Iterable");
            mget1.addInterceptor(Listeners.of(LettuceMethodClusterTestFirstArgsInterceptor.class, "Lettuce_mget", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod mget2 = target.getDeclaredMethod("mget", "io.lettuce.core.output.KeyValueStreamingChannel", "java.lang.Object[]");
            mget2.addInterceptor(Listeners.of(LettuceMethodClusterTestSecondArgsInterceptor.class, "Lettuce_mget", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod mget3 = target.getDeclaredMethod("mget", "io.lettuce.core.output.KeyValueStreamingChannel", "java.lang.Iterable");
            mget3.addInterceptor(Listeners.of(LettuceMethodClusterTestSecondArgsInterceptor.class, "Lettuce_mget", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod migrate = target.getDeclaredMethod("migrate", "java.lang.String", "int", "java.lang.Object", "int", "long");
            migrate.addInterceptor(Listeners.of(LettuceMethodClusterTestThirdArgsInterceptor.class, "Lettuce_migrate", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));

            InstrumentMethod migrate1 = target.getDeclaredMethod("migrate", "java.lang.String", "int", "int", "long", "io.lettuce.core.MigrateArgs");
            migrate1.addInterceptor(Listeners.of(LettuceMethodMigrateInterceptor.class, "Lettuce_migrate", ExecutionPolicy.BOUNDARY, Interceptors.SCOPE_CALLBACK));
        }
    };
}
