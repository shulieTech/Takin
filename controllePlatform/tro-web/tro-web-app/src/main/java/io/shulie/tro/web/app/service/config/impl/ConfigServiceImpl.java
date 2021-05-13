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

package io.shulie.tro.web.app.service.config.impl;

import com.pamirs.tro.common.constant.ConfigConstants;
import com.pamirs.tro.entity.domain.entity.TBaseConfig;
import com.pamirs.tro.entity.domain.entity.user.User;
import io.shulie.tro.web.app.common.RestContext;
import io.shulie.tro.web.app.constant.SwitchKeyFactory;
import io.shulie.tro.web.app.init.sync.ConfigSyncService;
import io.shulie.tro.web.app.service.BaseConfigService;
import io.shulie.tro.web.app.service.config.ConfigService;
import io.shulie.tro.web.config.sync.api.SwitchSyncService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @author shiyajian
 * create: 2020-09-18
 */
@Component
@Slf4j
public class ConfigServiceImpl implements ConfigService {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private SwitchSyncService switchSyncService;
    @Autowired
    private BaseConfigService baseConfigService;
    @Autowired
    private ConfigSyncService configSyncService; //FIXME 这个引用不对,configSyncService是初始化那块用的，用switchSyncService；

    @Override
    public void updateClusterTestSwitch(String userAppKey, Boolean value) {
        if (value == null) {
            return;
        }
        redisTemplate.opsForValue().set(SwitchKeyFactory.getClusterTestSwitchRedisKey(userAppKey), value);
        User user = RestContext.getUser();
        configSyncService.syncClusterTestSwitch(user.getKey());
    }

    @Override
    public boolean getClusterTestSwitch(String userAppKey) {
        Object o = redisTemplate.opsForValue().get(SwitchKeyFactory.getClusterTestSwitchRedisKey(userAppKey));
        if (!(o instanceof Boolean)) {
            return true;
        }
        return (Boolean)o;
    }

    @Override
    public void updateAllowListSwitch(String userAppKey, Boolean value) {
        if (value == null) {
            return;
        }
        try {
            baseConfigService.checkExistAndInsert(ConfigConstants.WHITE_LIST_SWITCH);
            TBaseConfig config = new TBaseConfig();
            config.setConfigCode(ConfigConstants.WHITE_LIST_SWITCH);
            config.setConfigValue(value ? ConfigConstants.WHITE_LIST_OPEN : ConfigConstants.WHITE_LIST_CLOSE);
            baseConfigService.updateBaseConfig(config);
        } catch (Exception e) {
            log.error("发生错误", e);
        }

        User user = RestContext.getUser();
        redisTemplate.opsForValue().set(SwitchKeyFactory.getAllowListSwitchRedisKey(userAppKey), value);
        configSyncService.syncAllowListSwitch(user.getKey());
    }

    @Override
    public boolean getAllowListSwitch(String userAppKey) {
        TBaseConfig tBaseConfig = baseConfigService.queryByConfigCode(ConfigConstants.WHITE_LIST_SWITCH);
        boolean dbResult = false;
        if (tBaseConfig != null) {
            dbResult = "1".equals(tBaseConfig.getConfigValue());
        }
        Object o = redisTemplate.opsForValue().get(SwitchKeyFactory.getAllowListSwitchRedisKey(userAppKey));
        if (!(o instanceof Boolean)) {
            return dbResult;
        }
        return (Boolean)o & dbResult;
    }
}
