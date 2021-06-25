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

package io.shulie.tro.web.app.cache;

import java.util.Set;

import javax.annotation.PostConstruct;

import io.shulie.tro.web.app.common.RestContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author shiyajian
 * create: 2020-12-17
 */
@Slf4j
public abstract class AbstractAgentConfigCache<T> implements AgentCacheSupport<T> {

    private final String cacheName;
    private final RedisTemplate redisTemplate;

    public AbstractAgentConfigCache(String cacheName, RedisTemplate redisTemplate) {
        this.cacheName = cacheName;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public T get(String namespace) {
        T result = (T)redisTemplate.opsForValue().get(getCacheKey(namespace));
        if (result == null) {
            result = queryValue(namespace);
            redisTemplate.opsForValue().set(getCacheKey(namespace), result);
        }
        return result;
    }

    @Override
    public void evict(String namespace) {
        redisTemplate.delete(getCacheKey(namespace));
    }

    /**
     * 项目重启之后，缓存清空下
     */
    @PostConstruct
    private void reset() {
        String beClearKey = this.cacheName + "*";
        if (!"*".equals(beClearKey)) {
            Set keys = redisTemplate.keys(beClearKey);
            if (CollectionUtils.isNotEmpty(keys)) {
                keys.forEach(key -> {
                    redisTemplate.delete(key);
                });
                log.info("清除key:{}对应的缓存成功", beClearKey);
            }
        }
    }

    private String getCacheKey(String namespace) {
        String key = cacheName + ":" + RestContext.getUser().getCustomerId();
        if (namespace != null) {
            key += ":";
            key += namespace;
        }
        return key;
    }

    protected abstract T queryValue(String namespace);

}
