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

package io.shulie.tro.web.app.cache.agentimpl;

import java.util.List;

import com.pamirs.tro.entity.domain.entity.simplify.TShadowJobConfig;
import io.shulie.tro.web.app.cache.AbstractAgentConfigCache;
import io.shulie.tro.web.app.service.simplify.ShadowJobConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @author shiyajian
 * create: 2020-12-17
 */
@Component
public class ShadowJobConfigAgentCache extends AbstractAgentConfigCache<List<TShadowJobConfig>> {

    public static final String CACHE_NAME = "t:a:c:shadow:job";
    @Autowired
    private ShadowJobConfigService shadowJobConfigService;

    public ShadowJobConfigAgentCache(@Autowired RedisTemplate redisTemplate) {
        super(CACHE_NAME, redisTemplate);
    }

    @Override
    protected List<TShadowJobConfig> queryValue(String namespace) {
        return shadowJobConfigService.queryByAppName(namespace);
    }

}
