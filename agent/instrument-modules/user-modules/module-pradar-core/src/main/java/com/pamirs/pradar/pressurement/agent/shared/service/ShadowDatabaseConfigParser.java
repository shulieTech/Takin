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
package com.pamirs.pradar.pressurement.agent.shared.service;

import com.pamirs.pradar.Pradar;
import com.pamirs.pradar.internal.config.ShadowDatabaseConfig;
import org.apache.commons.lang.StringUtils;

import java.util.*;

/**
 * @author xiaobin.zfb | xiaobin@shulie.io
 * @since 2020/9/10 12:31 下午
 */
public class ShadowDatabaseConfigParser {
    private static ShadowDatabaseConfigParser INSTANCE;

    public static ShadowDatabaseConfigParser getInstance() {
        if (INSTANCE == null) {
            synchronized (ShadowDatabaseConfigParser.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ShadowDatabaseConfigParser();
                }
            }
        }
        return INSTANCE;
    }

    public List<ShadowDatabaseConfig> parse(List<Map<String, Object>> list) {
        if (list == null || list.isEmpty()) {
            return Collections.EMPTY_LIST;
        }

        List<ShadowDatabaseConfig> configs = new ArrayList<ShadowDatabaseConfig>();
        for (Map<String, Object> map : list) {
            ShadowDatabaseConfig shadowDatabaseConfig = parse(map);
            if (shadowDatabaseConfig != null) {
                configs.add(shadowDatabaseConfig);
            }
        }

        return configs;
    }

    private String toString(Object object) {
        if (object == null) {
            return null;
        }
        return StringUtils.trim(object.toString());
    }

    public ShadowDatabaseConfig parse(Map<String, Object> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        int dsType = Integer.valueOf(map.get("dsType") == null ? "-1" : map.get("dsType").toString());
        final String shadowTableConfig = StringUtils.trim(toString(map.get("shadowTableConfig")));
        Map<String, Object> shadowDbConfig = (Map<String, Object>) map.get("shadowDbConfig");

        /**
         * 这块判断一下，如果是影子库则需要校验 shadowDbConfig 不为空
         * 如果是影子表则需要校验 shadowTableConfig 不为空
         */
        if (dsType == 0) {
            if (shadowDbConfig == null) {
                return null;
            }
        } else if (dsType == 1) {
            if (shadowTableConfig == null) {
                return null;
            }
        } else {
            return null;
        }

        ShadowDatabaseConfig shadowDatabaseConfig = new ShadowDatabaseConfig();

        final String applicationName = StringUtils.trim(toString(map.get("applicationName")));
        shadowDatabaseConfig.setApplicationName(applicationName);


        shadowDatabaseConfig.setDsType(dsType);

        final String url = StringUtils.trim(toString(map.get("url")));
        shadowDatabaseConfig.setUrl(url);


        if (StringUtils.isNotBlank(shadowTableConfig)) {
            String[] arr = StringUtils.split(shadowTableConfig, ',');
            if (arr != null && arr.length != 0) {
                Map<String, String> businessTables = new HashMap<String, String>();
                for (String str : arr) {
                    if (StringUtils.isNotBlank(str)) {
                        /**
                         * 现在配置不支持影子表名的自定义，由系统自动生成，后面如果控制台支持后，修改会非常容易
                         */
                        businessTables.put(StringUtils.trim(str), Pradar.addClusterTestPrefix(StringUtils.trim(str)));
                    }
                }
                shadowDatabaseConfig.setBusinessShadowTables(businessTables);
            }
        }


        if (shadowDbConfig == null) {
            return shadowDatabaseConfig;
        }
        Map<String, Object> datasourceMediator = (Map<String, Object>) shadowDbConfig.get("datasourceMediator");
        if (datasourceMediator == null) {
            return shadowDatabaseConfig;
        }

        String businessDataSourceName = null, shadowDataSourceName = null;
        businessDataSourceName = toString(datasourceMediator.get("dataSourceBusiness"));
        shadowDataSourceName = toString(datasourceMediator.get("dataSourcePerformanceTest"));

        if (businessDataSourceName == null || shadowDataSourceName == null) {
            return shadowDatabaseConfig;
        }

        List<Map<String, Object>> list = (List<Map<String, Object>>) shadowDbConfig.get("dataSources");
        if (list == null || list.isEmpty()) {
            return shadowDatabaseConfig;
        }

        Map<String, Object> businessMap = null, shadowMap = null;
        for (Map<String, Object> m : list) {
            String id = toString(m.get("id"));
            if (StringUtils.equals(id, businessDataSourceName)) {
                businessMap = m;
            } else if (StringUtils.equals(id, shadowDataSourceName)) {
                shadowMap = m;
            }
        }

        if (businessMap == null || shadowMap == null) {
            return shadowDatabaseConfig;
        }

        for (Map.Entry<String, Object> entry : businessMap.entrySet()) {
            if (StringUtils.equals(entry.getKey(), "id")) {
                continue;
            }
            if (StringUtils.equals(entry.getKey(), "url")) {
                shadowDatabaseConfig.setUrl(toString(entry.getValue()));
            } else if (StringUtils.equals(entry.getKey(), "username")) {
                shadowDatabaseConfig.setUsername(toString(entry.getValue()));
            } else if (StringUtils.equals(entry.getKey(), "schema")) {
                shadowDatabaseConfig.setSchema(toString(entry.getValue()));
            }
        }

        Map<String, String> properties = new HashMap<String, String>();
        for (Map.Entry<String, Object> entry : shadowMap.entrySet()) {
            if (StringUtils.equals(entry.getKey(), "id")) {
                continue;
            }
            if (StringUtils.equals(entry.getKey(), "url")) {
                shadowDatabaseConfig.setShadowUrl(toString(entry.getValue()));
            } else if (StringUtils.equals(entry.getKey(), "username")) {
                shadowDatabaseConfig.setShadowUsername(toString(entry.getValue()));
            } else if (StringUtils.equals(entry.getKey(), "password")) {
                shadowDatabaseConfig.setShadowPassword(toString(entry.getValue()));
            } else if (StringUtils.equals(entry.getKey(), "driverClassName")) {
                shadowDatabaseConfig.setShadowDriverClassName(toString(entry.getValue()));
            } else if (StringUtils.equals(entry.getKey(), "schema")) {
                shadowDatabaseConfig.setShadowSchema(toString(entry.getValue()));
            } else {
                String str = toString(entry.getValue());
                if (StringUtils.isBlank(str)) {
                    continue;
                }
                properties.put(entry.getKey(), str);
            }
        }

        shadowDatabaseConfig.setProperties(properties);
        return shadowDatabaseConfig;
    }
}
