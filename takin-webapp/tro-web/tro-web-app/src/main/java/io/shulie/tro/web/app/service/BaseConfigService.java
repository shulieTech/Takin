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

package io.shulie.tro.web.app.service;

import com.pamirs.tro.common.constant.ConfigConstants;
import com.pamirs.tro.common.constant.TROErrorEnum;
import com.pamirs.tro.common.exception.TROModuleException;
import com.pamirs.tro.entity.domain.entity.TBaseConfig;
import com.pamirs.tro.entity.domain.entity.user.User;
import io.shulie.tro.web.app.common.CommonService;
import io.shulie.tro.web.app.common.RestContext;
import io.shulie.tro.web.common.constant.BaseConfigConstant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * 基础配置表
 */
@Service
public class BaseConfigService extends CommonService {

    /**
     * 通过主键查询
     *
     * @param configCode
     * @return
     */
    public TBaseConfig queryByConfigCode(String configCode) {
        return tbaseConfigDao.selectByPrimaryKey(configCode);
    }

    public void checkExistAndInsert(String configCode) {
        TBaseConfig config = queryByConfigCode(configCode);
        if (config != null) {
            return;
        }
        config = new TBaseConfig();
        config.setConfigCode(configCode);
        config.setConfigValue(ConfigConstants.WHITE_LIST_OPEN);
        config.setConfigDesc("白名单开关：0-关闭 1-开启");
        tbaseConfigDao.insertSelective(config);
    }

    /**
     * 更新基础配置的值
     *
     * @param tBaseConfig
     * @throws TROModuleException
     */
    public void updateBaseConfig(TBaseConfig tBaseConfig) throws TROModuleException {
        if (StringUtils.isEmpty(tBaseConfig.getConfigCode()) || StringUtils.isEmpty(tBaseConfig.getConfigValue())) {
            throw new TROModuleException(TROErrorEnum.API_TRO_CONFCENTER_UPDATE_BASE_CONFIG_PARAM_EXCEPTION);
        }
        if (tBaseConfig.getConfigValue().length() > 128) {
            throw new TROModuleException(TROErrorEnum.API_TRO_CONFCENTER_UPDATE_BASE_CONFIG_VALUE_TOO_LONG_EXCEPTION);
        }
        tbaseConfigDao.updateByPrimaryKeySelective(tBaseConfig);

    }

    /**
     * 更新基础配置的值
     *
     * @param tBaseConfig
     * @throws TROModuleException
     */
    public void addBaseConfig(TBaseConfig tBaseConfig) throws TROModuleException {
        if (StringUtils.isEmpty(tBaseConfig.getConfigCode()) || StringUtils.isEmpty(tBaseConfig.getConfigValue())) {
            throw new TROModuleException(TROErrorEnum.API_TRO_CONFCENTER_ADD_BASE_CONFIG_EXCEPTION);
        }
        TBaseConfig source = tbaseConfigDao.selectByPrimaryKey(tBaseConfig.getConfigCode());
        if(source != null) {
            throw new TROModuleException(TROErrorEnum.API_TRO_CONFCENTER_ADD_BASE_CONFIG_EXIST);
        }
        if (tBaseConfig.getConfigValue().length() > 128) {
            throw new TROModuleException(TROErrorEnum.API_TRO_CONFCENTER_UPDATE_BASE_CONFIG_VALUE_TOO_LONG_EXCEPTION);
        }
        tbaseConfigDao.insertSelective(tBaseConfig);
    }

}
