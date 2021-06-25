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

package com.pamirs.tro.common.redis;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

/**
 * 说明：验证码redis使用封装类
 *
 * @author shulie
 * @version 1.0
 * @date 2017年4月16日
 */
@SuppressWarnings("all")
@Component
public class RedisManager implements InitializingBean {

    @Autowired
    private RedisTemplate redisTemplate; // 不能设置泛型，否则报错

    @Override
    public void afterPropertiesSet() throws Exception {
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setDefaultSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.afterPropertiesSet();
    }

    public RedisTemplate getRedisTemplate() {
        return redisTemplate;
    }

    public <T> void valuePut(RedisKey redisKey, T t) {
        verifyRedisKey(redisKey.getKey());
        redisTemplate.opsForValue().set(redisKey.getKey(), t, redisKey.getTimeout(), TimeUnit.SECONDS);
    }

    /**
     * 说明：校验存储redis的key值
     *
     * @param redisKey redis的key值
     * @author shulie
     * @time：2017年12月28日 上午10:23:25
     */
    private void verifyRedisKey(String... redisKey) {
        for (String key : redisKey) {
            if (StringUtils.isEmpty(key)) {
                throw new RuntimeException("参数异常");
            }
        }
    }

    public Optional<Object> valueGet(String key) {
        verifyRedisKey(key);
        return Optional.ofNullable(redisTemplate.opsForValue().get(key));
    }

    public void removeKey(String key) {
        if (StringUtils.isNotEmpty(key)) {
            redisTemplate.delete(key);
        }
    }

    /**
     * 说明: redis lock
     *
     * @param redisKey redis的键
     * @return true可以获取锁, 反之不能
     * @author shulie
     * @date 2019/3/11 14:51
     */
    public boolean acquireLock(RedisKey redisKey) {
        // 通过SETNX试图获取一个lock
        AtomicBoolean success = new AtomicBoolean(false);
        String redisLock = redisKey.getKey();
        verifyRedisKey(redisLock);
        return (Boolean)redisTemplate.execute((RedisCallback)connection -> {
            long expireAt = System.currentTimeMillis() + (redisKey.getTimeout() * 1000);
            Boolean acquire = connection.setNX(redisLock.getBytes(), String.valueOf(expireAt).getBytes());
            //SETNX成功，则成功获取一个锁
            if (acquire) {
                success.set(true);
            } else {
                //SETNX失败，说明锁仍然被其他对象持有，检查其是否已经超时
                long oldValue = Long.valueOf(String.valueOf(this.redisTemplate.opsForValue().get(redisLock)));

                //超时
                if (oldValue < System.currentTimeMillis()) {
                    String getValue = String.valueOf(this.redisTemplate.opsForValue().getAndSet(redisLock, expireAt));
                    // 获取锁成功
                    if (Long.valueOf(getValue) == oldValue) {
                        success.set(true);
                    } else {
                        // 已被其他进程获取锁
                        success.set(false);
                    }
                } else {
                    //未超时，则直接返回失败
                    success.set(false);
                }
            }
            return success.get();
        });

    }

    /**
     * 说明：判断key是否存在
     *
     * @param key
     * @return
     * @author shulie
     * @time：2017年12月28日 上午10:36:03
     */
    public Boolean hasKey(String key) {
        verifyRedisKey(key);
        return redisTemplate.hasKey(key);
    }

}
