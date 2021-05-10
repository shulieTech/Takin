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
package com.pamirs.attach.plugin.apache.tomcatjdbc.util;

import com.pamirs.pradar.internal.config.ShadowDatabaseConfig;
import com.pamirs.pradar.pressurement.agent.shared.service.GlobalConfig;
import com.pamirs.pradar.pressurement.datasource.DatabaseUtils;
import com.pamirs.pradar.pressurement.datasource.util.DbUrlUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author angju
 * @date 2020/8/12 18:59
 */
public class TomcatJdbcDatasourceUtils {
    private static Logger logger = LoggerFactory.getLogger(TomcatJdbcDatasourceUtils.class.getName());

    public static boolean configured(DataSource sourceDataSource) {
        try {
            String url = sourceDataSource.getUrl();
            String username = sourceDataSource.getUsername();
            boolean contains = GlobalConfig.getInstance().containsShadowDatabaseConfig(DbUrlUtils.getKey(url, username));
            if (!contains) {
                return GlobalConfig.getInstance().containsShadowDatabaseConfig(DbUrlUtils.getKey(url, null));
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean shadowTable(DataSource sourceDataSource) {
        try {
            String url = sourceDataSource.getUrl();
            String username = sourceDataSource.getUsername();
            return DatabaseUtils.isTestTable(url, username);
        } catch (Exception e) {
            return true;
        }
    }


    public static DataSource generateDatasourceFromConfiguration(DataSource sourceDatasource, Map<String, ShadowDatabaseConfig> shadowDbConfigurations) {
        if (shadowDbConfigurations == null) {
            return null;
        }
        ShadowDatabaseConfig ptDataSourceConf = selectMatchPtDataSourceConfiguration(sourceDatasource, shadowDbConfigurations);
        if (ptDataSourceConf == null) {
            return null;
        }
        String url = ptDataSourceConf.getShadowUrl();
        String username = ptDataSourceConf.getShadowUsername();
        String password = ptDataSourceConf.getShadowPassword();
        String initialSize = ptDataSourceConf.getProperty("initialSize");
        String minIdle = ptDataSourceConf.getProperty("minIdle");
        String maxActive = ptDataSourceConf.getProperty("maxActive");
        String maxWait = ptDataSourceConf.getProperty("maxWait");
        String timeBetweenEvictionRunsMillis = ptDataSourceConf.getProperty("timeBetweenEvictionRunsMillis");
        String minEvictableIdleTimeMillis = ptDataSourceConf.getProperty("minEvictableIdleTimeMillis");
        String validationQuery = ptDataSourceConf.getProperty("validationQuery");
        String testWhileIdle = ptDataSourceConf.getProperty("testWhileIdle");
        String testOnBorrow = ptDataSourceConf.getProperty("testOnBorrow");
        String testOnReturn = ptDataSourceConf.getProperty("testOnReturn");

        if (StringUtils.isBlank(url) || StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            return null;
        }

        String driverClassName = ptDataSourceConf.getShadowDriverClassName();
        if(StringUtils.isBlank(driverClassName)) {
            driverClassName = sourceDatasource.getDriverClassName();
        }

        DataSource target = new DataSource();
        target.setUrl(url);
        target.setUsername(username);
        target.setPassword(password);
        target.setDriverClassName(driverClassName);
        try {
            if (initialSize != null) {
                target.setInitialSize(Integer.parseInt(initialSize));
            } else {
                target.setInitialSize(sourceDatasource.getInitialSize());
            }

            if (minIdle != null) {
                target.setMinIdle(Integer.parseInt(minIdle));
            } else {
                target.setMinIdle(sourceDatasource.getMinIdle());
            }

            if (maxActive != null) {
                target.setMaxActive(Integer.parseInt(maxActive));
                target.setMaxIdle(Integer.parseInt(maxActive));
            } else {
                target.setMaxActive(sourceDatasource.getMaxActive());
                target.setMaxIdle(sourceDatasource.getMaxIdle());
            }

            if (maxWait != null) {
                target.setMaxWait(Integer.parseInt(maxWait));
            } else {
                target.setMaxWait(sourceDatasource.getMaxWait());
            }

            if (timeBetweenEvictionRunsMillis != null) {
                target.setTimeBetweenEvictionRunsMillis(Integer.parseInt(timeBetweenEvictionRunsMillis));
            } else {
                target.setTimeBetweenEvictionRunsMillis(sourceDatasource.getTimeBetweenEvictionRunsMillis());
            }

            if (minEvictableIdleTimeMillis != null) {
                target.setMinEvictableIdleTimeMillis(Integer.parseInt(minEvictableIdleTimeMillis));
            } else {
                target.setMinEvictableIdleTimeMillis(sourceDatasource.getMinEvictableIdleTimeMillis());
            }

            if (validationQuery != null) {
                target.setValidationQuery(validationQuery);
            } else {
                target.setValidationQuery(sourceDatasource.getValidationQuery());
            }

            if (testWhileIdle != null) {
                target.setTestWhileIdle(Boolean.parseBoolean(testWhileIdle));
            } else {
                target.setTestWhileIdle(sourceDatasource.isTestWhileIdle());
            }

            if (testOnBorrow != null) {
                target.setTestOnBorrow(Boolean.parseBoolean(testOnBorrow));
            } else {
                target.setTestOnBorrow(sourceDatasource.isTestOnBorrow());
            }

            if (testOnReturn != null) {
                target.setTestOnReturn(Boolean.parseBoolean(testOnReturn));
            } else {
                target.setTestOnReturn(sourceDatasource.isTestOnReturn());
            }

        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
        return target;
    }


    private static ShadowDatabaseConfig selectMatchPtDataSourceConfiguration(DataSource source, Map<String, ShadowDatabaseConfig> shadowDbConfigurations) {
        DataSource dataSource = source;
        String key = DbUrlUtils.getKey(dataSource.getUrl(), dataSource.getUsername());
        ShadowDatabaseConfig shadowDatabaseConfig = shadowDbConfigurations.get(key);
        if (shadowDatabaseConfig == null) {
            key = DbUrlUtils.getKey(dataSource.getUrl(), null);
            shadowDatabaseConfig = shadowDbConfigurations.get(key);
        }
        return shadowDatabaseConfig;
    }
}
