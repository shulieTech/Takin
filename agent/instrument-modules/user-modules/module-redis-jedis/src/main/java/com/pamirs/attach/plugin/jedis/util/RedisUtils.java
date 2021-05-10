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
package com.pamirs.attach.plugin.jedis.util;

import org.apache.commons.lang.StringUtils;
import redis.clients.jedis.BinaryJedis;
import redis.clients.jedis.Client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author vincent
 */
public final class RedisUtils {

    private RedisUtils() { /* no instance */ }

    /**
     * redis 的 server 目前有 redis、 codis、 pika
     * redis 的 client 目前有 jedis、 redission、 lettuce
     * client 访问的接口大部分相同，小部分有差异。这个对象保存所有 client 的方法合集。
     */
    private final static Map<String, String> METHOD_NAMES = new HashMap<String, String>();

    /**
     * redis方法具有个key
     * key：方法名称
     * List<Integer>: 多个key参数索引
     */
    public final static Map<String, List<Integer>> METHOD_MORE_KEYS = new HashMap<String, List<Integer>>();

    public static final ConcurrentHashMap<String, String> HOST_SERVER_TYPE_MAP = new ConcurrentHashMap<String, String>();


    public static final Set<String> EVAL_METHOD_NAME = new HashSet<String>();

    public static final Set<String> IGNORE_NAME = new HashSet<String>();

    private final static int KEY_LENGTH_LIMITED = 200;

    public static final String REDIS_SERVER_REDIS = "Redis";

    public static final String REDIS_SERVER_PIKA = "Pika";

    public static final String REDIS_SERVER_CODIS = "Codis";

    public static final String REDIS_CLIENT_JEDIS = "Jedis";

    public static final String REDIS_CLIENT_LETTUCE = "Lettuce";

    public static final String REDIS_CLIENT_REDISSION = "Redisson";

    public static final String READ = "R";

    public static final String WRITE = "W";

    static {
        METHOD_NAMES.put("xreadGroup", READ);
        METHOD_NAMES.put("xread", READ);
        METHOD_NAMES.put("get", READ);
        METHOD_NAMES.put("type", READ);
        METHOD_NAMES.put("append", WRITE);
        METHOD_NAMES.put("keys", READ);
        METHOD_NAMES.put("set", WRITE);
        METHOD_NAMES.put("exists", READ);
        METHOD_NAMES.put("sort", WRITE);
        METHOD_NAMES.put("rename", WRITE);
        METHOD_NAMES.put("hvals", READ);
        METHOD_NAMES.put("scan", READ);
        METHOD_NAMES.put("hexists", READ);
        METHOD_NAMES.put("hmget", READ);
        METHOD_NAMES.put("hincrBy", READ);
        METHOD_NAMES.put("del", WRITE);
        //METHOD_NAMES.put("randomKey", READ);
        METHOD_NAMES.put("renamenx", WRITE);
        METHOD_NAMES.put("expire", WRITE);
        METHOD_NAMES.put("expireAt", WRITE);
        METHOD_NAMES.put("move", WRITE);
        METHOD_NAMES.put("ttl", WRITE);
        METHOD_NAMES.put("mget", READ);
        METHOD_NAMES.put("getSet", WRITE);
        METHOD_NAMES.put("setnx", WRITE);
        METHOD_NAMES.put("mset", WRITE);
        METHOD_NAMES.put("setex", WRITE);
        METHOD_NAMES.put("hlen", READ);
        METHOD_NAMES.put("hkeys", READ);
        METHOD_NAMES.put("hdel", WRITE);
        METHOD_NAMES.put("zrangeWithScores", READ);
        METHOD_NAMES.put("zrevrangeWithScores", READ);
        METHOD_NAMES.put("zrangeByScoreWithScores", READ);
        METHOD_NAMES.put("zrevrangeByScore", READ);
        METHOD_NAMES.put("zrevrangeByScoreWithScores", READ);
        METHOD_NAMES.put("zremrangeByRank", READ);
        METHOD_NAMES.put("zremrangeByScore", READ);
        METHOD_NAMES.put("objectRefcount", READ);
        METHOD_NAMES.put("objectEncoding", READ);
        METHOD_NAMES.put("objectIdletime", READ);
        METHOD_NAMES.put("incrBy", WRITE);
        METHOD_NAMES.put("decr", READ);
        METHOD_NAMES.put("incr", READ);
        METHOD_NAMES.put("decrBy", WRITE);
        METHOD_NAMES.put("msetnx", WRITE);
        METHOD_NAMES.put("hset", WRITE);
        METHOD_NAMES.put("substr", WRITE);
        METHOD_NAMES.put("hget", READ);
        METHOD_NAMES.put("hsetnx", WRITE);
        METHOD_NAMES.put("hmset", WRITE);
        METHOD_NAMES.put("hgetAll", READ);
        METHOD_NAMES.put("rpush", WRITE);
        METHOD_NAMES.put("lpush", WRITE);
        METHOD_NAMES.put("llen", READ);
        METHOD_NAMES.put("lrange", WRITE);
        METHOD_NAMES.put("ltrim", WRITE);
        METHOD_NAMES.put("lindex", WRITE);
        METHOD_NAMES.put("lset", WRITE);
        METHOD_NAMES.put("lrem", WRITE);
        METHOD_NAMES.put("lpop", WRITE);
        METHOD_NAMES.put("rpop", WRITE);
        METHOD_NAMES.put("rpoplpush", WRITE);
        METHOD_NAMES.put("sadd", WRITE);
        METHOD_NAMES.put("smembers", READ);
        METHOD_NAMES.put("srem", WRITE);
        METHOD_NAMES.put("spop", WRITE);
        METHOD_NAMES.put("smove", WRITE);
        METHOD_NAMES.put("scard", WRITE);
        METHOD_NAMES.put("sismember", READ);
        METHOD_NAMES.put("sinter", WRITE);
        METHOD_NAMES.put("sinterstore", WRITE);
        METHOD_NAMES.put("sunion", READ);
        METHOD_NAMES.put("sunionstore", WRITE);
        METHOD_NAMES.put("sdiff", READ);
        METHOD_NAMES.put("sdiffstore", WRITE);
        METHOD_NAMES.put("srandmember", READ);
        METHOD_NAMES.put("zadd", WRITE);
        METHOD_NAMES.put("zrange", WRITE);
        METHOD_NAMES.put("zrem", READ);
        METHOD_NAMES.put("zincrby", WRITE);
        METHOD_NAMES.put("zrank", READ);
        METHOD_NAMES.put("zrevrank", READ);
        METHOD_NAMES.put("zrevrange", READ);
        METHOD_NAMES.put("zcard", READ);
        METHOD_NAMES.put("zscore", READ);
        METHOD_NAMES.put("watch", READ);
        METHOD_NAMES.put("blpop", READ);
        METHOD_NAMES.put("brpop", READ);
        METHOD_NAMES.put("zcount", READ);
        METHOD_NAMES.put("zrangeByScore", READ);
        METHOD_NAMES.put("zunionstore", READ);
        METHOD_NAMES.put("zinterstore", READ);
        METHOD_NAMES.put("strlen", READ);
        METHOD_NAMES.put("lpushx", READ);
        METHOD_NAMES.put("persist", READ);
        METHOD_NAMES.put("rpushx", READ);
        METHOD_NAMES.put("echo", READ);
        METHOD_NAMES.put("linsert", READ);
        METHOD_NAMES.put("brpoplpush", READ);
        METHOD_NAMES.put("setbit", READ);
        METHOD_NAMES.put("getbit", READ);
        METHOD_NAMES.put("setrange", READ);
        METHOD_NAMES.put("getrange", READ);
        METHOD_NAMES.put("configGet", READ);
        METHOD_NAMES.put("configSet", READ);
        METHOD_NAMES.put("eval", READ);
        METHOD_NAMES.put("subscribe", READ);
        METHOD_NAMES.put("publish", WRITE);
        METHOD_NAMES.put("psubscribe", READ);
        METHOD_NAMES.put("evalsha", READ);
        METHOD_NAMES.put("scriptExists", READ);
        METHOD_NAMES.put("scriptLoad", WRITE);
        METHOD_NAMES.put("slowlogGet", READ);
        METHOD_NAMES.put("bitcount", READ);
        METHOD_NAMES.put("bitop", READ);
        METHOD_NAMES.put("dump", WRITE);
        METHOD_NAMES.put("restore", READ);
        METHOD_NAMES.put("pexpire", WRITE);
        METHOD_NAMES.put("pexpireAt", WRITE);
        METHOD_NAMES.put("pttl", WRITE);
        METHOD_NAMES.put("incrByFloat", WRITE);
        METHOD_NAMES.put("psetex", WRITE);
        METHOD_NAMES.put("clientKill", WRITE);
        METHOD_NAMES.put("clientSetname", WRITE);
        METHOD_NAMES.put("migrate", WRITE);
        METHOD_NAMES.put("hincrByFloat", WRITE);
        METHOD_NAMES.put("hscan", READ);
        METHOD_NAMES.put("sscan", READ);
        METHOD_NAMES.put("zscan", READ);
        METHOD_NAMES.put("shutdown", WRITE);
        METHOD_NAMES.put("debug", READ);
        METHOD_NAMES.put("save", WRITE);
        METHOD_NAMES.put("sync", WRITE);
        METHOD_NAMES.put("time", READ);
        METHOD_NAMES.put("select", READ);
        //METHOD_NAMES.put("resetState", READ);
        METHOD_NAMES.put("configResetStat", READ);
        METHOD_NAMES.put("randomBinaryKey", READ);
        METHOD_NAMES.put("monitor", READ);
        METHOD_NAMES.put("unwatch", READ);
        METHOD_NAMES.put("slowlogReset", WRITE);
        METHOD_NAMES.put("slowlogLen", READ);
        //METHOD_NAMES.put("ping", READ);
        //METHOD_NAMES.put("quit", WRITE);
        //METHOD_NAMES.put("flushDB", WRITE);
        //METHOD_NAMES.put("dbSize", READ);
        //METHOD_NAMES.put("flushAll", WRITE);
        //METHOD_NAMES.put("auth", WRITE);
        METHOD_NAMES.put("bgsave", WRITE);
        METHOD_NAMES.put("bgrewriteaof", WRITE);
        METHOD_NAMES.put("lastsave", WRITE);
        METHOD_NAMES.put("slaveof", READ);
        METHOD_NAMES.put("slaveofNoOne", WRITE);
        //METHOD_NAMES.put("getDB", READ);
        METHOD_NAMES.put("multi", READ);
        METHOD_NAMES.put("scriptFlush", WRITE);
        METHOD_NAMES.put("scriptKill", WRITE);
        METHOD_NAMES.put("clientGetname", READ);
        METHOD_NAMES.put("clientList", READ);
        METHOD_NAMES.put("slowlogGetBinary", READ);

        METHOD_NAMES.put("geohash", READ);
        METHOD_NAMES.put("georadius", READ);
        METHOD_NAMES.put("geodist", READ);
        METHOD_NAMES.put("geopos", READ);
        METHOD_NAMES.put("georadiusByMember", READ);
        METHOD_NAMES.put("geoadd", READ);



        METHOD_NAMES.put("xinfoStream", READ);
        METHOD_NAMES.put("xinfoGroup", READ);
        METHOD_NAMES.put("xrange", READ);
        METHOD_NAMES.put("xtrim", READ);
        METHOD_NAMES.put("xdel", READ);
        METHOD_NAMES.put("xlen", READ);
        METHOD_NAMES.put("xrevrange", READ);
        METHOD_NAMES.put("xadd", READ);
        METHOD_NAMES.put("xinfoConsumers", READ);
        METHOD_NAMES.put("xclaim", READ);

        //METHOD_NAMES.put("info", READ);

        // 韵达特有的方法
        METHOD_NAMES.put("ds_del", WRITE);
        METHOD_NAMES.put("ds_get", READ);
        METHOD_NAMES.put("ds_set", WRITE);
        METHOD_NAMES.put("ds_hset", WRITE);
        METHOD_NAMES.put("ds_hget", READ);

        // init METHOD_MORE_KEYS
        METHOD_MORE_KEYS.put("rpoplpush", Arrays.asList(0,1));
        METHOD_MORE_KEYS.put("smove", Arrays.asList(0,1));
        METHOD_MORE_KEYS.put("sinterstore", Arrays.asList(0,1));
        METHOD_MORE_KEYS.put("zunionstore", Arrays.asList(0,1));
        METHOD_MORE_KEYS.put("sunionstore", Arrays.asList(0,1));
        METHOD_MORE_KEYS.put("sdiffstore", Arrays.asList(0,1));
        METHOD_MORE_KEYS.put("sort", Arrays.asList(0));
        METHOD_MORE_KEYS.put("zinterstore", Arrays.asList(0,1));
        METHOD_MORE_KEYS.put("blpop", Arrays.asList(1));
        METHOD_MORE_KEYS.put("brpop", Arrays.asList(1));
        METHOD_MORE_KEYS.put("migrate", Arrays.asList(2));
        METHOD_MORE_KEYS.put("rename", Arrays.asList(0,1));

        //init eval method
        EVAL_METHOD_NAME.add("evalsha");
        EVAL_METHOD_NAME.add("eval");

        //init ignore method

        IGNORE_NAME.add("mqserverinfos");
        IGNORE_NAME.add("scriptLoad");

    }

    public static Map<String, String> get() {
        return Collections.unmodifiableMap(METHOD_NAMES);
    }

    public static boolean isReadMethod(String methodName) {
        return READ.equals(METHOD_NAMES.get(methodName));
    }

    public static boolean isWriteMethod(String methodName) {
        return WRITE.equals(METHOD_NAMES.get(methodName));
    }

    public static String getMethodNameExt(Object... args) {
        if (args == null || args.length == 0) {
            return "";
        }
        return ParameterUtils.toString(KEY_LENGTH_LIMITED, args[0]);
    }

    /**
     * 服务端可能是 redis、codis、pika
     * 客户端链接工具用的是 jedis
     */
    public static String jedisServiceName(BinaryJedis binaryJedis) {
        Client client = binaryJedis.getClient();

        // get from cache
        String cacheKey = remoteIpStr(client.getHost(), client.getPort());
        String serviceName = HOST_SERVER_TYPE_MAP.get(cacheKey);
        if (serviceName != null) {
            return serviceName;
        }

        String info;
        try {
            info = binaryJedis.info();
        } catch (Throwable throwable) {
            // 如果 redis 读客户的 info 接口报错，那么就直接用默认的名称
            return defaultServiceStr(cacheKey);
        }
        if (info == null || info.length() == 0) {
            // 读不到 redis 服务器上面的信息
            return defaultServiceStr(cacheKey);
        }
        String[] props = StringUtils.split(info, "\r\n");
        if (props.length == 0) {
            // redis 服务器上面的信息为空
            return defaultServiceStr(cacheKey);
        }
        for (String prop : props) {
            if (prop.startsWith("#")) {
                continue; // this is comment
            }
            if (prop.startsWith("redis_version")) {
                HOST_SERVER_TYPE_MAP.put(cacheKey, REDIS_SERVER_REDIS);
                return REDIS_SERVER_REDIS;
            }
            if (prop.startsWith("pika_version")) {
                HOST_SERVER_TYPE_MAP.put(cacheKey, REDIS_SERVER_PIKA);
                return REDIS_SERVER_PIKA;
            }
            // other server be add
        }
        return defaultServiceStr(cacheKey);
    }

    public static String remoteIpStr(String host, Integer port) {
        if (host == null || host.trim().length() == 0) {
            return "Unknown Host";
        }
        if (port == null) {
            port = 80;
        }
        return host + ":" + port;
    }


    public static String methodStr(String methodName, String argsExt) {
        if (argsExt == null || argsExt.trim().length() == 0) {
            return methodName;
        }
        return methodName + "~" + argsExt;
    }

    private static String defaultServiceStr(String cacheKey) {
        HOST_SERVER_TYPE_MAP.put(cacheKey, REDIS_SERVER_REDIS);
        return REDIS_SERVER_REDIS;
    }
}
