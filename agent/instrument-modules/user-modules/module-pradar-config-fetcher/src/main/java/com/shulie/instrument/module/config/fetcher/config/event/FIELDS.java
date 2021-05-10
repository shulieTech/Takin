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
package com.shulie.instrument.module.config.fetcher.config.event;

import com.pamirs.pradar.ConfigNames;
import com.shulie.instrument.module.config.fetcher.config.AbstractConfig;
import com.shulie.instrument.module.config.fetcher.config.event.model.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: wangjian
 * @since : 2020/8/17 19:58
 */
public enum FIELDS {
    URL_WHITE_LIST(ConfigNames.URL_WHITE_LIST, "URL_WHITE_LIST", UrlWhiteList.getInstance()),
    MQ_WHITE_LIST(ConfigNames.MQ_WHITE_LIST, "MQ_WHITE_LIST", MQWhiteList.getInstance()),
    WHITE_LIST(ConfigNames.WHITE_LIST, "WHITE_LIST", null),
    DUBBO_ALLOW_LIST(ConfigNames.DUBBO_ALLOW_LIST, "DUBBO_ALLOW_LIST", RpcAllowList.getInstance()),
    WHITE_LIST_SWITCHON(ConfigNames.WHITE_LIST_SWITCH_ON, "WHITE_LIST_SWITCH_ON", WhiteListSwitch.getInstance()),
    CONTEXT_PATH_BLOCK_LIST(ConfigNames.CONTEXT_PATH_BLOCK_LIST, "CONTEXT_PATH_BLOCK_LIST", ContextPathBlockList.getInstance()),
    SEARCH_KEY_WHITE_LIST(ConfigNames.SEARCH_KEY_WHITE_LIST, "SEARCH_KEY_WHITE_LIST", SearchKeyWhiteList.getInstance()),
    CACHE_KEY_ALLOW_LIST(ConfigNames.CACHE_KEY_ALLOW_LIST, "CACHE_KEY_ALLOW_LIST", CacheKeyAllowList.getInstance()),
    SHADOW_DATABASE_CONFIGS(ConfigNames.SHADOW_DATABASE_CONFIGS, "SHADOW_DATABASE_CONFIGS", ShadowDatabaseConfigs.getInstance()),
    SHADOW_JOB_CONFIGS(ConfigNames.SHADOW_JOB_CONFIGS, "SHADOW_JOB_CONFIGS", ShadowJobConfig.getInstance()),
    MOCK_CONFIGS("MOCK_CONFIGS", "MOCK_CONFIGS", MockConfigChanger.getInstance()),
    GLOBAL_SWITCHON(ConfigNames.GLOBAL_SWITCH_ON, "GLOBAL_SWITCH_ON", GlobalSwitch.getInstance()),
    SHADOW_REDIS_SERVER_CONFIG("SHADOW_REDIS_SERVER_CONFIG", "SHADOW_REDIS_SERVER_CONFIG", RedisShadowServerConfig.getInstance()),
    SHADOW_ES_SERVER_CONFIG("SHADOW_ES_SERVER_CONFIG", "SHADOW_ES_SERVER_CONFIG", EsShadowServerConfig.getInstance()),
    SHADOW_HBASE_SERVER_CONFIG("SHADOW_HBASE_SERVER_CONFIG", "SHADOW_HBASE_SERVER_CONFIG", ShadowHbaseConfigs.getInstance());


    private static final Logger LOGGER = LoggerFactory.getLogger(FIELDS.class);

    private String fileName;

    private String desc;

    private IChange change;

    FIELDS(String fileName, String desc, IChange change) {
        this.fileName = fileName;
        this.desc = desc;
        this.change = change;
    }

    public String getFileName() {
        return fileName;
    }

    public String getDesc() {
        return desc;
    }

    public static FIELDS of(String fileName) {
        FIELDS[] values = FIELDS.values();
        for (FIELDS value : values) {
            if (StringUtils.equals(value.fileName, fileName)) {
                return value;
            }
        }
        return null;
    }

    public <E> Boolean compareIsChangeAndSet(AbstractConfig config, E newValue) {
        Boolean res = change.compareIsChangeAndSet(config, newValue);
        switch (this) {
            case SHADOW_DATABASE_CONFIGS:
                return res;
            default:
                break;
        }
        if (res) {
            LOGGER.info("new config:" + newValue);
        }
        return res;
    }

}
