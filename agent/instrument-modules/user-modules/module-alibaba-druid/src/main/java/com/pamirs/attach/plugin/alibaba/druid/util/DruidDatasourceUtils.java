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
package com.pamirs.attach.plugin.alibaba.druid.util;

import com.alibaba.druid.pool.DruidDataSource;
import com.pamirs.pradar.internal.config.ShadowDatabaseConfig;
import com.pamirs.pradar.pressurement.agent.shared.service.GlobalConfig;
import com.pamirs.pradar.pressurement.datasource.DatabaseUtils;
import com.pamirs.pradar.pressurement.datasource.util.DbUrlUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Create by xuyh at 2020/3/12 21:33.
 */
public class DruidDatasourceUtils {
    private static Logger logger = LoggerFactory.getLogger(DruidDatasourceUtils.class.getName());

    public static DruidDataSource generateDatasourceFromConfiguration(DruidDataSource sourceDatasource, Map<String, ShadowDatabaseConfig> shadowDbConfigurations) {
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
        if (StringUtils.isBlank(url) || StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            logger.error("[druid] shadow datasource config is invalid. [url/username/password] one in them or more is blank. url:{} username:{} password:{}", url, username, password);
            return null;
        }

        String driverClassName = ptDataSourceConf.getShadowDriverClassName();
        if(StringUtils.isBlank(driverClassName)) {
            driverClassName = sourceDatasource.getDriverClassName();
        }

        DruidDataSource target = new DruidDataSource();
        target.setUrl(url);
        target.setUsername(username);
        target.setPassword(password);
        target.setDriverClassName(driverClassName);
        try {
            Integer minIdle = ptDataSourceConf.getIntProperty("minIdle");
            if (minIdle != null) {
                target.setMinIdle(minIdle);
            } else {
                target.setMinIdle(sourceDatasource.getMinIdle());
            }

            Integer maxActive = ptDataSourceConf.getIntProperty("maxActive");
            if (maxActive != null) {
                target.setMaxActive(maxActive);
            } else {
                target.setMaxActive(sourceDatasource.getMaxActive());
            }

            Long maxWait = ptDataSourceConf.getLongProperty("maxWait");
            if (maxWait != null) {
                target.setMaxWait(maxWait);
            } else {
                target.setMaxWait(sourceDatasource.getMaxWait());
            }

            Long timeBetweenEvictionRunsMillis = ptDataSourceConf.getLongProperty("timeBetweenEvictionRunsMillis");
            if (timeBetweenEvictionRunsMillis != null) {
                target.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
            } else {
                target.setTimeBetweenEvictionRunsMillis(sourceDatasource.getTimeBetweenEvictionRunsMillis());
            }

            Long minEvictableIdleTimeMillis = ptDataSourceConf.getLongProperty("minEvictableIdleTimeMillis");
            if (minEvictableIdleTimeMillis != null) {
                target.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
            } else {
                target.setMinEvictableIdleTimeMillis(sourceDatasource.getMinEvictableIdleTimeMillis());
            }

            String validationQuery = ptDataSourceConf.getProperty("validationQuery");
            if (StringUtils.isNotBlank(validationQuery)) {
                target.setValidationQuery(validationQuery);
            } else {
                target.setValidationQuery(sourceDatasource.getValidationQuery());
            }

            Boolean testWhileIdle = ptDataSourceConf.getBooleanProperty("testWhileIdle");
            if (testWhileIdle != null) {
                target.setTestWhileIdle(testWhileIdle);
            } else {
                target.setTestWhileIdle(sourceDatasource.isTestWhileIdle());
            }

            Integer maxPoolPrepareStatementPerConnectionSize = ptDataSourceConf.getIntProperty("maxPoolPrepareStatementPerConnectionSize");
            if (maxPoolPrepareStatementPerConnectionSize != null) {
                target.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPrepareStatementPerConnectionSize);
            } else {
                target.setMaxPoolPreparedStatementPerConnectionSize(sourceDatasource.getMaxOpenPreparedStatements());
            }

            Boolean testOnBorrow = ptDataSourceConf.getBooleanProperty("testOnBorrow");
            if (testOnBorrow != null) {
                target.setTestOnBorrow(testOnBorrow);
            } else {
                target.setTestOnBorrow(sourceDatasource.isTestOnBorrow());
            }

            Boolean testOnReturn = ptDataSourceConf.getBooleanProperty("testOnReturn");
            if (testOnReturn != null) {
                target.setTestOnReturn(testOnReturn);
            } else {
                target.setTestOnReturn(sourceDatasource.isTestOnReturn());
            }

            Integer maxPoolPreparedStatementPerConnectionSize = ptDataSourceConf.getIntProperty("maxPoolPreparedStatementPerConnectionSize");
            if (maxPoolPreparedStatementPerConnectionSize != null) {
                target.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);
            } else {
                target.setMaxPoolPreparedStatementPerConnectionSize(sourceDatasource.getMaxPoolPreparedStatementPerConnectionSize());
            }

            String connectionProperties = ptDataSourceConf.getProperty("connectionProperties");
            if (StringUtils.isNotBlank(connectionProperties)) {
                target.setConnectionProperties(connectionProperties);
            }

            Boolean poolPreparedStatements = ptDataSourceConf.getBooleanProperty("poolPreparedStatements");
            if (poolPreparedStatements != null) {
                target.setPoolPreparedStatements(Boolean.valueOf(poolPreparedStatements));
            } else {
                target.setPoolPreparedStatements(sourceDatasource.isPoolPreparedStatements());
            }

            Integer timeBetweenLogStatsMillis = ptDataSourceConf.getIntProperty("timeBetweenLogStatsMillis");
            if (timeBetweenLogStatsMillis != null) {
                target.setTimeBetweenLogStatsMillis(timeBetweenLogStatsMillis);
            } else {
                target.setTimeBetweenLogStatsMillis(sourceDatasource.getTimeBetweenLogStatsMillis());
            }

            Boolean useUnfairLock = ptDataSourceConf.getBooleanProperty("useUnfairLock");
            if (useUnfairLock != null) {
                target.setUseUnfairLock(useUnfairLock);
            } else {
                target.setUseUnfairLock(sourceDatasource.isUseUnfairLock());
            }

            Boolean useOracleImplicitCache = ptDataSourceConf.getBooleanProperty("useOracleImplicitCache");
            if (useOracleImplicitCache != null) {
                target.setUseOracleImplicitCache(useOracleImplicitCache);
            } else {
                target.setUseOracleImplicitCache(sourceDatasource.isUseOracleImplicitCache());
            }

            Integer transactionQueryTimeout = ptDataSourceConf.getIntProperty("transactionQueryTimeout");
            if (transactionQueryTimeout != null) {
                target.setTransactionQueryTimeout(transactionQueryTimeout);
            } else {
                target.setTransactionQueryTimeout(sourceDatasource.getTransactionQueryTimeout());
            }

            Boolean dupCloseLogEnable = ptDataSourceConf.getBooleanProperty("dupCloseLogEnable");
            if (dupCloseLogEnable != null) {
                target.setDupCloseLogEnable(dupCloseLogEnable);
            } else {
                target.setDupCloseLogEnable(sourceDatasource.isDupCloseLogEnable());
            }

            Boolean breakAfterAcquireFailure = ptDataSourceConf.getBooleanProperty("breakAfterAcquireFailure");
            if (breakAfterAcquireFailure != null) {
                target.setBreakAfterAcquireFailure(Boolean.valueOf(breakAfterAcquireFailure));
            } else {
                target.setBreakAfterAcquireFailure(sourceDatasource.isBreakAfterAcquireFailure());
            }

            Integer connectionErrorRetryAttempts = ptDataSourceConf.getIntProperty("connectionErrorRetryAttempts");
            if (connectionErrorRetryAttempts != null) {
                target.setConnectionErrorRetryAttempts(Integer.valueOf(connectionErrorRetryAttempts));
            } else {
                target.setConnectionErrorRetryAttempts(sourceDatasource.getConnectionErrorRetryAttempts());
            }

            Boolean sharePreparedStatements = ptDataSourceConf.getBooleanProperty("sharePreparedStatements");
            if (sharePreparedStatements != null) {
                target.setSharePreparedStatements(sharePreparedStatements);
            } else {
                target.setSharePreparedStatements(sourceDatasource.isSharePreparedStatements());
            }

            String validConnectionCheckerClass = ptDataSourceConf.getProperty("validConnectionCheckerClass");
            if (StringUtils.isNotBlank(validConnectionCheckerClass)) {
                target.setValidConnectionCheckerClassName(validConnectionCheckerClass);
            } else {
                target.setValidConnectionCheckerClassName(sourceDatasource.getValidConnectionCheckerClassName());
            }

            String connectProperties = ptDataSourceConf.getProperty("connectProperties");
            if (StringUtils.isNotBlank(connectProperties)) {
                String[] connectPropertyArr = StringUtils.split(connectProperties, ';');
                Properties properties = new Properties();
                for (String str : connectPropertyArr) {
                    if (StringUtils.isBlank(str)) {
                        continue;
                    }
                    String[] arr = StringUtils.split(str, '=');
                    if (arr.length != 2) {
                        continue;
                    }
                    if (StringUtils.isBlank(arr[0]) || StringUtils.isBlank(arr[1])) {
                        continue;
                    }
                    properties.setProperty(StringUtils.trim(arr[0]), StringUtils.trim(arr[1]));
                }
                if (!properties.isEmpty()) {
                    target.setConnectProperties(properties);
                }
            } else {
                target.setConnectProperties(sourceDatasource.getConnectProperties());
            }

            String connectionInitSqls = ptDataSourceConf.getProperty("connectionInitSqls");
            if (StringUtils.isNotBlank(connectionInitSqls)) {
                String[] initSqls = StringUtils.split(connectionInitSqls, ';');
                List<String> list = new ArrayList<String>();
                for (String str : initSqls) {
                    if (StringUtils.isNotBlank(str)) {
                        list.add(StringUtils.trim(str));
                    }
                }
                if (!list.isEmpty()) {
                    target.setConnectionInitSqls(list);
                }
            } else {
                target.setConnectionInitSqls(sourceDatasource.getConnectionInitSqls());
            }

            Long timeBetweenConnectErrorMillis = ptDataSourceConf.getLongProperty("timeBetweenConnectErrorMillis");
            if (timeBetweenConnectErrorMillis != null) {
                target.setTimeBetweenConnectErrorMillis(Long.valueOf(timeBetweenConnectErrorMillis));
            } else {
                target.setTimeBetweenConnectErrorMillis(sourceDatasource.getTimeBetweenConnectErrorMillis());
            }

            Integer maxOpenPreparedStatements = ptDataSourceConf.getIntProperty("maxOpenPreparedStatements");
            if (maxOpenPreparedStatements != null) {
                target.setMaxOpenPreparedStatements(maxOpenPreparedStatements);
            } else {
                target.setMaxOpenPreparedStatements(sourceDatasource.getMaxOpenPreparedStatements());
            }

            Boolean logAbandoned = ptDataSourceConf.getBooleanProperty("logAbandoned");
            if (logAbandoned != null) {
                target.setLogAbandoned(logAbandoned);
            } else {
                target.setLogAbandoned(sourceDatasource.isLogAbandoned());
            }

            Integer removeAbandonedTimeout = ptDataSourceConf.getIntProperty("removeAbandonedTimeout");
            if (removeAbandonedTimeout != null) {
                target.setRemoveAbandonedTimeout(removeAbandonedTimeout);
            } else {
                target.setRemoveAbandonedTimeout(sourceDatasource.getRemoveAbandonedTimeout());
            }

            Long removeAbandonedTimeoutMillis = ptDataSourceConf.getLongProperty("removeAbandonedTimeoutMillis");
            if (removeAbandonedTimeoutMillis != null) {
                target.setRemoveAbandonedTimeoutMillis(removeAbandonedTimeoutMillis);
            } else {
                target.setRemoveAbandonedTimeoutMillis(sourceDatasource.getRemoveAbandonedTimeoutMillis());
            }

            Boolean removeAbandoned = ptDataSourceConf.getBooleanProperty("removeAbandoned");
            if (removeAbandoned != null) {
                target.setRemoveAbandoned(removeAbandoned);
            } else {
                target.setRemoveAbandoned(target.isRemoveAbandoned());
            }

            Integer maxWaitThreadCount = ptDataSourceConf.getIntProperty("maxWaitThreadCount");
            if (maxWaitThreadCount != null) {
                target.setMaxWaitThreadCount(maxWaitThreadCount);
            } else {
                target.setMaxWaitThreadCount(sourceDatasource.getMaxWaitThreadCount());
            }

            Integer validationQueryTimeout = ptDataSourceConf.getIntProperty("validationQueryTimeout");
            if (validationQueryTimeout != null) {
                target.setValidationQueryTimeout(validationQueryTimeout);
            } else {
                target.setValidationQueryTimeout(sourceDatasource.getValidationQueryTimeout());
            }

            Boolean accessToUnderlyingConnectionAllowed = ptDataSourceConf.getBooleanProperty("accessToUnderlyingConnectionAllowed");
            if (accessToUnderlyingConnectionAllowed != null) {
                target.setAccessToUnderlyingConnectionAllowed(accessToUnderlyingConnectionAllowed);
            } else {
                target.setAccessToUnderlyingConnectionAllowed(sourceDatasource.isAccessToUnderlyingConnectionAllowed());
            }

            Boolean defaultAutoCommit = ptDataSourceConf.getBooleanProperty("defaultAutoCommit");
            if (defaultAutoCommit != null) {
                target.setDefaultAutoCommit(defaultAutoCommit);
            } else {
                target.setDefaultAutoCommit(sourceDatasource.isDefaultAutoCommit());
            }

            Boolean defaultReadOnly = ptDataSourceConf.getBooleanProperty("defaultReadOnly");
            if (defaultReadOnly != null) {
                target.setDefaultReadOnly(defaultReadOnly);
            } else {
                target.setDefaultReadOnly(sourceDatasource.getDefaultReadOnly());
            }

            Integer defaultTransactionIsolation = ptDataSourceConf.getIntProperty("defaultTransactionIsolation");
            if (defaultTransactionIsolation != null) {
                target.setDefaultTransactionIsolation(Integer.valueOf(defaultTransactionIsolation));
            } else {
                target.setDefaultTransactionIsolation(sourceDatasource.getDefaultTransactionIsolation());
            }

            String defaultCatalog = ptDataSourceConf.getProperty("defaultCatalog");
            if (StringUtils.isNotBlank(defaultCatalog)) {
                target.setDefaultCatalog(defaultCatalog);
            } else {
                target.setDefaultCatalog(sourceDatasource.getDefaultCatalog());
            }

            Integer queryTimeout = ptDataSourceConf.getIntProperty("queryTimeout");
            if (queryTimeout != null) {
                target.setQueryTimeout(queryTimeout);
            } else {
                target.setQueryTimeout(sourceDatasource.getQueryTimeout());
            }

            Long maxWaitMillis = ptDataSourceConf.getLongProperty("maxWaitMillis");
            if (maxWaitMillis != null) {
                target.setMaxWait(maxWaitMillis);
            } else {
                target.setMaxWait(sourceDatasource.getMaxWait());
            }

            Integer initialSize = ptDataSourceConf.getIntProperty("initialSize");
            if (initialSize != null) {
                target.setInitialSize(Integer.valueOf(initialSize));
            } else {
                target.setInitialSize(sourceDatasource.getInitialSize());
            }

            Integer loginTimeout = ptDataSourceConf.getIntProperty("loginTimeout");
            if (loginTimeout != null) {
                target.setLoginTimeout(Integer.valueOf(loginTimeout));
            } else {
                target.setLoginTimeout(sourceDatasource.getLoginTimeout());
            }

            target.setBreakAfterAcquireFailure(true);
        } catch (Throwable e) {
            logger.warn("init druid datasource failed.", e);
        }
        return target;
    }

    @SuppressWarnings("unchecked")
    private static ShadowDatabaseConfig selectMatchPtDataSourceConfiguration(DruidDataSource source, Map<String, ShadowDatabaseConfig> shadowDbConfigurations) {
        DruidDataSource dataSource = source;
        String key = DbUrlUtils.getKey(dataSource.getUrl(), dataSource.getUsername());
        ShadowDatabaseConfig shadowDatabaseConfig = shadowDbConfigurations.get(key);
        if (shadowDatabaseConfig == null) {
            key = DbUrlUtils.getKey(dataSource.getUrl(), null);
            shadowDatabaseConfig = shadowDbConfigurations.get(key);
        }
        return shadowDatabaseConfig;
    }

    public static boolean configured(DruidDataSource sourceDataSource) {
        try {
            String url = sourceDataSource.getUrl();
            String username = sourceDataSource.getUsername();
            boolean contains = GlobalConfig.getInstance().containsShadowDatabaseConfig(DbUrlUtils.getKey(url, username));
            if (!contains) {
                return GlobalConfig.getInstance().containsShadowDatabaseConfig(DbUrlUtils.getKey(url, null));
            }
            return true;
        } catch (Throwable e) {
            return false;
        }
    }

    public static boolean shadowTable(DruidDataSource sourceDataSource) {
        try {
            String url = sourceDataSource.getUrl();
            String username = sourceDataSource.getUsername();
            return DatabaseUtils.isTestTable(url, username);
        } catch (Throwable e) {
            return true;
        }
    }
}
