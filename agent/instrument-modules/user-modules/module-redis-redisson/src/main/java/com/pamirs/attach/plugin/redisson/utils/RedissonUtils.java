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
package com.pamirs.attach.plugin.redisson.utils;


import java.util.*;

/**
 * @Auther: vernon
 * @Date: 2020/11/26 14:08
 * @Description:
 */
public class RedissonUtils<T> {

    public static List<String> redissonMethodFilter =
            Arrays.asList(
                    "getTimeSeries",
                    "getStream",
                    "getRateLimiter",
                    "getBinaryStream",
                    "getGeo",
                    "getSetCache",
                    "getMapCache",
                    "getBucket",
                    "getBuckets",
                    "getHyperLogLog",
                    "getList",
                    "getListMultimap",
                    "getListMultimapCache",
                    "getLocalCachedMap",
                    "getMap",
                    "getSetMultimap",
                    "getSetMultimapCache",
                    "getSemaphore",
                    "getPermitExpirableSemaphore",
                    "getLock",
                    "getMultiLock",
                    "getRedLock",
                    "getFairLock",
                    "getReadWriteLock",
                    "getSet",
                    "getSortedSet",
                    "getScoredSortedSet",
                    "getLexSortedSet",
                    "getTopic",
                    "getPatternTopic",
                    "getQueue",
                    "getTransferQueue",
                    "getDelayedQueue",
                    "getRingBuffer",
                    "getPriorityQueue",
                    "getPriorityBlockingQueue",
                    "getPriorityBlockingDeque",
                    "getPriorityDeque",
                    "getBlockingQueue",
                    "getBoundedBlockingQueue",
                    "getDeque",
                    "getBlockingDeque",
                    "getAtomicLong",
                    "getAtomicDouble",
                    "getLongAdder",
                    "getDoubleAdder",
                    "getCountDownLatch",
                    "getBitSet",
                    "getBloomFilter",
                    "getScript",
                    "getExecutorService",
                    "getRemoteService",
                    "createTransaction",
                    "createBatch",
                    "getKeys",
                    "getLiveObjectService",
                    "shutdown",
                    "getConfig",
                    "getRedisNodes",
                    "getNodesGroup",
                    "getClusterNodesGroup",
                    "isShutdown",
                    "isShuttingDown",
                    "getId"
            );

    public static List<String> redissonRxMethodFilter =
            Arrays.asList(
                    "getTimeSeries",
                    "getStream",
                    "getGeo",
                    "getRateLimiter",
                    "getBinaryStream",
                    "getSemaphore",
                    "getPermitExpirableSemaphore",
                    "getReadWriteLock",
                    "getFairLock",
                    "getLock",
                    "getMultiLock",
                    "getRedLock",
                    "getCountDownLatch",
                    "getSetCache",
                    "getMapCache",
                    "getBucket",
                    "getBuckets",
                    "getHyperLogLog",
                    "getList",
                    "getListMultimap",
                    "getListMultimap",
                    "getSetMultimap",
                    "getMap",
                    "getSet",
                    "getScoredSortedSet",
                    "getLexSortedSet",
                    "getTopic",
                    "getPatternTopic",
                    "getQueue",
                    "getRingBuffer",
                    "getBlockingQueue",
                    "getBlockingDeque",
                    "getTransferQueue",
                    "getDeque",
                    "getAtomicLong",
                    "getAtomicDouble",
                    "getRemoteService",
                    "getBitSet",
                    "getScript",
                    "createTransaction",
                    "createBatch",
                    "getKeys",
                    "shutdown",
                    "getConfig",
                    "getNodesGroup",
                    "getClusterNodesGroup",
                    "isShutdown",
                    "isShuttingDown",
                    "getId"

            );


    public static List<String> redissonReactiveMethodFilter =
            Arrays.asList(
                    "getTimeSeries",
                    "getStream",
                    "getGeo",
                    "getRateLimiter",
                    "getBinaryStream",
                    "getSemaphore",
                    "getPermitExpirableSemaphore",
                    "getReadWriteLock",
                    "getFairLock",
                    "getLock",
                    "getMultiLock",
                    "getRedLock",
                    "getCountDownLatch",
                    "getSetCache",
                    "getMapCache",
                    "getBucket",
                    "getBuckets",
                    "findBuckets",
                    "getHyperLogLog",
                    "getList",
                    "getListMultimap",
                    "getSetMultimap",
                    "getMap",
                    "getSet",
                    "getScoredSortedSet",
                    "getLexSortedSet",
                    "getTopic",
                    "getPatternTopic",
                    "getQueue",
                    "getRingBuffer",
                    "getBlockingQueue",
                    "getBlockingDeque",
                    "getTransferQueue",
                    "getDeque",
                    "getAtomicLong",
                    "getAtomicDouble",
                    "getRemoteService",
                    "getBitSet",
                    "getScript",
                    "createTransaction",
                    "createBatch",
                    "getKeys",
                    "shutdown",
                    "getConfig",
                    "getNodesGroup",
                    "getClusterNodesGroup",
                    "isShutdown",
                    "isShuttingDown",
                    "getId"
            );


    public static List combine = new ArrayList();

    static {
        combine.addAll(redissonMethodFilter);
        combine.addAll(redissonRxMethodFilter);
        combine.addAll(redissonReactiveMethodFilter);
    }


    public static List<String> removePre(Object obj) {
        if (obj == null) {
            return null;
        }
        List<String> result = new ArrayList<String>();
        if (obj instanceof String) {
            result.add((String) ((String) obj).replaceAll("redis://", ""));
            return result;
        } else if (obj instanceof Collection) {
            Iterator iterator = ((Collection) obj).iterator();
            while (iterator.hasNext()) {
                String str = (String) iterator.next();
                result.add(str.replaceAll("redis://", ""));
            }
        }
        return result;
    }

    public static String addPre(String str) {
        if (str == null) {
            return null;
        }
        if (str.contains("redis://")) {
            return str;
        }
        return new StringBuilder("redis://").append(str).toString();
    }


    public static List<String> addPre(List<String> list) {
        if (list == null){
            return null;
        }
        List<String> result = new ArrayList<String>(list.size());
        for (String str : list) {
            if (!str.contains("redis://")) {
                result.add(new StringBuilder("redis://").append(str).toString());
            } else {
                result.add(str);
            }
        }
        return result;
    }
}
