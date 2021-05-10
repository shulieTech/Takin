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
package com.pamirs.pradar.pressurement.datasource;

import com.pamirs.pradar.internal.config.ShadowDatabaseConfig;
import com.pamirs.pradar.pressurement.agent.shared.service.GlobalConfig;
import com.pamirs.pradar.pressurement.datasource.util.DbUrlUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xiaobin.zfb
 * @since 2020/9/10 6:00 下午
 */
public final class DatabaseUtils {

    //判断当前数据源是否是影子库压测
    public static Map<String, String> getUrlUsername(Connection jdbcConnecton) throws SQLException {
        Map<String, String> map = new HashMap<String, String>();
        String username = null;
        String url = null;
        if (url == null && jdbcConnecton != null && jdbcConnecton.getMetaData() != null) {
            url = jdbcConnecton.getMetaData().getURL();
            username = jdbcConnecton.getMetaData().getUserName();
        }
        map.put("username", username);
        map.put("url", url);
        return map;
    }

    /**
     * 判断是否是影子数据源
     *
     * @param url
     * @param username
     * @return
     */
    public static boolean isShadowDatasource(String url, String username) {
        String key = DbUrlUtils.getKey(url, username);
        ShadowDatabaseConfig shadowDatabaseConfig = GlobalConfig.getInstance().getShadowDatabaseConfig(key);
        if (shadowDatabaseConfig == null) {
            /**
             * 解决现在影子表配置没有username的问题,再尝试使用非用户名的判断一次
             */
            key = DbUrlUtils.getKey(url, null);
        }

        shadowDatabaseConfig = GlobalConfig.getInstance().getShadowDatabaseConfig(key);
        if (shadowDatabaseConfig == null) {
            return false;
        }
        return shadowDatabaseConfig.isShadowDatabase();
    }

    public static boolean isTestTable(String url, String username) throws SQLException {
        String key = DbUrlUtils.getKey(url, username);
        ShadowDatabaseConfig shadowDatabaseConfig = GlobalConfig.getInstance().getShadowDatabaseConfig(key);
        if (shadowDatabaseConfig == null) {
            /**
             * 解决现在影子表配置没有username的问题,再尝试使用非用户名的判断一次
             */
            key = DbUrlUtils.getKey(url, null);
        }

        shadowDatabaseConfig = GlobalConfig.getInstance().getShadowDatabaseConfig(key);
        if (shadowDatabaseConfig == null) {
            return false;
        }
        return shadowDatabaseConfig.isShadowTable();

    }
}
