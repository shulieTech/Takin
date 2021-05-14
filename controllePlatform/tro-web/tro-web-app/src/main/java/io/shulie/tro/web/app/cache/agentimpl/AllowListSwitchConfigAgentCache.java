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

import com.pamirs.tro.common.constant.ConfigConstants;
import com.pamirs.tro.entity.domain.entity.TBaseConfig;
import io.shulie.tro.web.app.cache.AbstractAgentConfigCache;
import io.shulie.tro.web.app.service.BaseConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @author shiyajian
 * create: 2020-12-17
 */
@Component
public class AllowListSwitchConfigAgentCache extends AbstractAgentConfigCache<Boolean> {

    public static final String CACHE_NAME = "t:a:c:allowlist:switch";

    @Autowired
    private BaseConfigService baseConfigService;

    public AllowListSwitchConfigAgentCache(@Autowired RedisTemplate redisTemplate) {
        super(CACHE_NAME, redisTemplate);
    }

    /**
     * 白名单开关，如果缓存中不存在，去数据库查
     */
    @Override
    protected Boolean queryValue(String namespace) {
        TBaseConfig tBaseConfig = baseConfigService.queryByConfigCode(ConfigConstants.WHITE_LIST_SWITCH);
        return "1".equals(tBaseConfig.getConfigValue());
    }
}
