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

import com.pamirs.tro.common.enums.ds.DsTypeEnum;
import com.pamirs.tro.entity.domain.vo.dsmanage.DsServerVO;
import io.shulie.tro.web.app.cache.AbstractAgentConfigCache;
import io.shulie.tro.web.app.service.dsManage.DsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @author hengyu
 * create: 2021-04-14
 */
@Component
public class ShadowKafkaClusterConfigAgentCache extends AbstractAgentConfigCache<List<DsServerVO>> {

    public static final String CACHE_NAME = "t:a:c:shadow:es";

    @Autowired
    private DsService dsService;

    public ShadowKafkaClusterConfigAgentCache(@Autowired RedisTemplate redisTemplate) {
        super(CACHE_NAME, redisTemplate);
    }

    @Override
    protected List<DsServerVO> queryValue(String namespace) {
        return dsService.getShadowDsServerConfigs(namespace, DsTypeEnum.SHADOW_KAFKA_CLUSTER);
    }

}
