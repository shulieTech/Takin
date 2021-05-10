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
package com.pamirs.attach.plugin.c3p0.utils;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.pamirs.pradar.*;
import com.pamirs.pradar.pressurement.agent.shared.service.GlobalConfig;
import com.pamirs.pradar.pressurement.agent.shared.service.DataSourceMeta;
import com.pamirs.pradar.pressurement.agent.shared.service.ErrorReporter;
import com.pamirs.pradar.internal.config.ShadowDatabaseConfig;
import com.pamirs.pradar.pressurement.datasource.DatabaseUtils;
import com.pamirs.pradar.pressurement.datasource.DbMediatorDataSource;
import com.pamirs.pradar.pressurement.datasource.util.DbUrlUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyVetoException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @Auther: vernon
 * @Date: 2020/3/29 14:59
 * @Description:
 */
public class DataSourceWrapUtil {
    private static Logger logger = LoggerFactory.getLogger(DataSourceWrapUtil.class.getName());

    public static final ConcurrentHashMap<DataSourceMeta, C3p0MediaDataSource> pressureDataSources = new ConcurrentHashMap<DataSourceMeta, C3p0MediaDataSource>();

    public static void destroy() {
        Iterator<Map.Entry<DataSourceMeta, C3p0MediaDataSource>> it = pressureDataSources.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<DataSourceMeta, C3p0MediaDataSource> entry = it.next();
            it.remove();
            entry.getValue().close();
        }
        pressureDataSources.clear();
    }

    public static boolean validate(ComboPooledDataSource sourceDataSource) {
        try {
            String url = sourceDataSource.getJdbcUrl();
            String username = sourceDataSource.getUser();
            boolean contains = GlobalConfig.getInstance().containsShadowDatabaseConfig(DbUrlUtils.getKey(url, username));
            if (!contains) {
                return GlobalConfig.getInstance().containsShadowDatabaseConfig(DbUrlUtils.getKey(url, null));
            }
            return true;
        } catch (Throwable e) {
            return false;
        }
    }

    public static boolean shadowTable(ComboPooledDataSource sourceDataSource) {
        try {
            String url = sourceDataSource.getJdbcUrl();
            String username = sourceDataSource.getUser();
            return DatabaseUtils.isTestTable(url, username);
        } catch (Throwable e) {
            return true;
        }
    }

    /**
     * 是否是影子数据源
     *
     * @param target 目标数据源
     * @return
     */
    private static boolean isPerformanceDataSource(ComboPooledDataSource target) {
        for (Map.Entry<DataSourceMeta, C3p0MediaDataSource> entry : pressureDataSources.entrySet()) {
            C3p0MediaDataSource mediatorDataSource = entry.getValue();
            if (mediatorDataSource.getDataSourcePerformanceTest() == null) {
                continue;
            }
            if (StringUtils.equals(mediatorDataSource.getDataSourcePerformanceTest().getJdbcUrl(), target.getJdbcUrl()) &&
                    StringUtils.equals(mediatorDataSource.getDataSourcePerformanceTest().getUser(), target.getUser())) {
                return true;
            }
        }
        return false;
    }

    //  static AtomicBoolean inited = new AtomicBoolean(false);

    public static void init(DataSourceMeta<ComboPooledDataSource> dataSourceMeta) {
        if (pressureDataSources.containsKey(dataSourceMeta) && pressureDataSources.get(dataSourceMeta) != null) {
            return;
        }
        ComboPooledDataSource target = dataSourceMeta.getDataSource();
        if (isPerformanceDataSource(target)) {
            return;
        }
        if (!validate(target)) {
            logger.error("[c3p0] No configuration found for datasource, url: " + target.getJdbcUrl());
            //没有配置对应的影子表或影子库
            ErrorReporter.buildError()
                    .setErrorType(ErrorTypeEnum.DataSource)
                    .setErrorCode("datasource-0002")
                    .setMessage("没有配置对应的影子表或影子库！")
                    .setDetail("c3p0:DataSourceWrapUtil:业务库配置:::url: " + target.getJdbcUrl())
                    .report();

            C3p0MediaDataSource dbMediatorDataSource = new C3p0MediaDataSource();
            dbMediatorDataSource.setDataSourceBusiness(target);
            DbMediatorDataSource old = pressureDataSources.put(dataSourceMeta, dbMediatorDataSource);
            if (old != null) {
                logger.info("[c3p0] destroyed shadow table datasource success. url:{} ,username:{}", target.getJdbcUrl(), target.getUser());
                old.close();
            }
            return;
        }
        if (shadowTable(target)) {
            //影子表
            try {
                C3p0MediaDataSource dbMediatorDataSource = new C3p0MediaDataSource();
                dbMediatorDataSource.setDataSourceBusiness(target);
                DbMediatorDataSource old = pressureDataSources.put(dataSourceMeta, dbMediatorDataSource);
                if (old != null) {
                    logger.info("[c3p0] destroyed shadow table datasource success. url:{} ,username:{}", target.getJdbcUrl(), target.getUser());
                    old.close();
                }
            } catch (Throwable e) {
                ErrorReporter.buildError()
                        .setErrorType(ErrorTypeEnum.DataSource)
                        .setErrorCode("datasource-0002")
                        .setMessage("没有配置对应的影子表或影子库！")
                        .setDetail("c3p0:DataSourceWrapUtil:业务库配置:::url: " +
                                target.getJdbcUrl() + Throwables.getStackTraceAsString(e))
                        .closePradar(ConfigNames.SHADOW_DATABASE_CONFIGS)
                        .report();
                logger.error("[c3p0] init datasource err!", e);
            }
        } else {
            //影子库
            try {
                C3p0MediaDataSource dataSource = new C3p0MediaDataSource();
                /**
                 * 如果没有配置则为null
                 */
                ComboPooledDataSource ptDataSource = copy(target);
                dataSource.setDataSourcePerformanceTest(ptDataSource);
                dataSource.setDataSourceBusiness(target);
                DbMediatorDataSource old = pressureDataSources.put(dataSourceMeta, dataSource);
                if (old != null) {
                    logger.info("[c3p0] destroyed shadow table datasource success. url:{} ,username:{}", target.getJdbcUrl(), target.getUser());
                    old.close();
                }
                logger.info("[c3p0] create shadow datasource success. target:{} url:{} ,username:{} shadow-url:{},shadow-username:{}", target.hashCode(), target.getJdbcUrl(), target.getUser(), ptDataSource.getJdbcUrl(), ptDataSource.getUser());
            } catch (Throwable t) {
                logger.error("[c3p0] init datasource err!", t);
                ErrorReporter.buildError()
                        .setErrorType(ErrorTypeEnum.DataSource)
                        .setErrorCode("datasource-0003")
                        .setMessage("影子库初始化失败！")
                        .setDetail("c3p0:DataSourceWrapUtil:业务库配置:::url: " +
                                target.getJdbcUrl() + Throwables.getStackTraceAsString(t))
                        .closePradar(ConfigNames.SHADOW_DATABASE_CONFIGS)
                        .report();
            }
        }
    }

    private static ComboPooledDataSource copy(ComboPooledDataSource source) {
        ComboPooledDataSource target = generate(source);
        return target;
    }


    public static ComboPooledDataSource generate(ComboPooledDataSource sourceDatasource) {
        Map<String, ShadowDatabaseConfig> conf = GlobalConfig.getInstance().getShadowDatasourceConfigs();
        if (conf == null) {
            return null;
        }
        ShadowDatabaseConfig ptDataSourceConf
                = selectMatchPtDataSourceConfiguration(sourceDatasource, conf);
        if (ptDataSourceConf == null) {
            return null;
        }
        String url = ptDataSourceConf.getShadowUrl();
        String username = ptDataSourceConf.getShadowUsername();
        String password = ptDataSourceConf.getShadowPassword();


        if (StringUtils.isBlank(url) || StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            return null;
        }
        String driverClassName = ptDataSourceConf.getShadowDriverClassName();
        if(StringUtils.isBlank(driverClassName)) {
            driverClassName = sourceDatasource.getDriverClass();
        }

        ComboPooledDataSource target = null;
        String identityToken = sourceDatasource.getIdentityToken();
        String datasourceName = sourceDatasource.getDataSourceName();
        if (StringUtils.isNotBlank(datasourceName)) {
            try {
                target = new ComboPooledDataSource(Pradar.addClusterTestPrefix(datasourceName));
            } catch (Throwable e) {
                if (StringUtils.isNotBlank(identityToken)) {
                    try {
                        target = new ComboPooledDataSource(true);
                    } catch (Throwable t) {
                        target = new ComboPooledDataSource();
                    }
                } else {
                    target = new ComboPooledDataSource();
                }
            }
        } else if (StringUtils.isNotBlank(identityToken)) {
            try {
                target = new ComboPooledDataSource(true);
            } catch (Throwable t) {
                target = new ComboPooledDataSource();
            }
        } else {
            target = new ComboPooledDataSource();
        }
        target.setJdbcUrl(url);
        target.setUser(username);
        target.setPassword(password);
        try {
            target.setDriverClass(driverClassName);
        } catch (PropertyVetoException e) {
            logger.error("[c3p0] can't found driver class name:{}", driverClassName, e);
            PradarSwitcher.invalid();
            return null;
        }

        Integer checkoutTimeout = ptDataSourceConf.getIntProperty("checkoutTimeout");
        if (checkoutTimeout != null) {
            target.setCheckoutTimeout(checkoutTimeout);
        } else {
            target.setCheckoutTimeout(sourceDatasource.getCheckoutTimeout());
        }

        Integer acquireIncrement = ptDataSourceConf.getIntProperty("acquireIncrement");
        if (acquireIncrement != null) {
            target.setAcquireIncrement(Integer.valueOf(acquireIncrement));
        } else {
            target.setAcquireIncrement(sourceDatasource.getAcquireIncrement());
        }

        Integer acquireRetryDelay = ptDataSourceConf.getIntProperty("acquireRetryDelay");
        if (acquireRetryDelay != null) {
            target.setAcquireRetryDelay(Integer.valueOf(acquireRetryDelay));
        } else {
            target.setAcquireRetryDelay(sourceDatasource.getAcquireRetryDelay());
        }

        Boolean autoCommitOnClose = ptDataSourceConf.getBooleanProperty("autoCommitOnClose");
        if (autoCommitOnClose != null) {
            target.setAutoCommitOnClose(Boolean.valueOf(autoCommitOnClose));
        } else {
            target.setAutoCommitOnClose(sourceDatasource.isAutoCommitOnClose());
        }

        String connectionTesterClassName = ptDataSourceConf.getProperty("connectionTesterClassName");
        if (StringUtils.isNotBlank(connectionTesterClassName)) {
            try {
                target.setConnectionTesterClassName(connectionTesterClassName);
            } catch (PropertyVetoException e) {
                logger.error("[c3p0] can't found connectionTesterClassName:{}", connectionTesterClassName, e);
            }
        } else {
            try {
                target.setConnectionTesterClassName(sourceDatasource.getConnectionTesterClassName());
            } catch (PropertyVetoException e) {
                logger.error("[c3p0] can't found connectionTesterClassName:{}", sourceDatasource.getConnectionTesterClassName(), e);
            }
        }

        String automaticTestTable = ptDataSourceConf.getProperty("automaticTestTable");
        if (StringUtils.isNotBlank(automaticTestTable)) {
            target.setAutomaticTestTable(automaticTestTable);
        } else {
            target.setAutomaticTestTable(sourceDatasource.getAutomaticTestTable());
        }

        Boolean forceIgnoreUnresolvedTransactions = ptDataSourceConf.getBooleanProperty("forceIgnoreUnresolvedTransactions");
        if (forceIgnoreUnresolvedTransactions != null) {
            target.setForceIgnoreUnresolvedTransactions(forceIgnoreUnresolvedTransactions);
        } else {
            target.setForceIgnoreUnresolvedTransactions(sourceDatasource.isForceIgnoreUnresolvedTransactions());
        }

        Integer idleConnectionTestPeriod = ptDataSourceConf.getIntProperty("idleConnectionTestPeriod");
        if (idleConnectionTestPeriod != null) {
            target.setIdleConnectionTestPeriod(idleConnectionTestPeriod);
        } else {
            target.setIdleConnectionTestPeriod(sourceDatasource.getIdleConnectionTestPeriod());
        }

        Integer initialPoolSize = ptDataSourceConf.getIntProperty("initialPoolSize");
        if (initialPoolSize != null) {
            target.setInitialPoolSize(Integer.valueOf(initialPoolSize));
        } else {
            target.setInitialPoolSize(sourceDatasource.getInitialPoolSize());
        }

        Integer maxIdleTime = ptDataSourceConf.getIntProperty("maxIdleTime");
        if (maxIdleTime != null) {
            target.setMaxIdleTime(Integer.valueOf(maxIdleTime));
        } else {
            target.setMaxIdleTime(sourceDatasource.getMaxIdleTime());
        }

        Integer maxPoolSize = ptDataSourceConf.getIntProperty("maxPoolSize");
        if (maxPoolSize != null) {
            target.setMaxPoolSize(maxPoolSize);
        } else {
            target.setMaxPoolSize(sourceDatasource.getMaxPoolSize());
        }

        Integer maxStatements = ptDataSourceConf.getIntProperty("maxStatements");
        if (maxStatements != null) {
            target.setMaxStatements(Integer.valueOf(maxStatements));
        } else {
            target.setMaxStatements(sourceDatasource.getMaxStatements());
        }

        Integer maxStatementsPerConnection = ptDataSourceConf.getIntProperty("maxStatementsPerConnection");
        if (maxStatementsPerConnection != null) {
            target.setMaxStatementsPerConnection(maxStatementsPerConnection);
        } else {
            target.setMaxStatementsPerConnection(sourceDatasource.getMaxStatementsPerConnection());
        }

        Integer minPoolSize = ptDataSourceConf.getIntProperty("minPoolSize");
        if (minPoolSize != null) {
            target.setMinPoolSize(Integer.valueOf(minPoolSize));
        } else {
            target.setMinPoolSize(sourceDatasource.getMinPoolSize());
        }

        String overrideDefaultUser = ptDataSourceConf.getProperty("overrideDefaultUser");
        if (StringUtils.isNotBlank(overrideDefaultUser)) {
            target.setOverrideDefaultUser(overrideDefaultUser);
        } else {
            target.setOverrideDefaultUser(sourceDatasource.getOverrideDefaultUser());
        }

        String overrideDefaultPassword = ptDataSourceConf.getProperty("overrideDefaultPassword");
        if (StringUtils.isNotBlank(overrideDefaultPassword)) {
            target.setOverrideDefaultPassword(overrideDefaultPassword);
        } else {
            target.setOverrideDefaultPassword(sourceDatasource.getOverrideDefaultPassword());
        }

        Integer propertyCycle = ptDataSourceConf.getIntProperty("propertyCycle");
        if (propertyCycle != null) {
            target.setPropertyCycle(propertyCycle);
        } else {
            target.setPropertyCycle(sourceDatasource.getPropertyCycle());
        }

        Boolean breakAfterAcquireFailure = ptDataSourceConf.getBooleanProperty("breakAfterAcquireFailure");
        if (breakAfterAcquireFailure != null) {
            target.setBreakAfterAcquireFailure(breakAfterAcquireFailure);
        } else {
            target.setBreakAfterAcquireFailure(sourceDatasource.isBreakAfterAcquireFailure());
        }

        Boolean testConnectionOnCheckout = ptDataSourceConf.getBooleanProperty("testConnectionOnCheckout");
        if (testConnectionOnCheckout != null) {
            target.setTestConnectionOnCheckout(testConnectionOnCheckout);
        } else {
            target.setTestConnectionOnCheckout(sourceDatasource.isTestConnectionOnCheckout());
        }

        Boolean testConnectionOnCheckin = ptDataSourceConf.getBooleanProperty("testConnectionOnCheckin");
        if (testConnectionOnCheckin != null) {
            target.setTestConnectionOnCheckin(testConnectionOnCheckin);
        } else {
            target.setTestConnectionOnCheckin(sourceDatasource.isTestConnectionOnCheckin());
        }

        Boolean usesTraditionalReflectiveProxies = ptDataSourceConf.getBooleanProperty("usesTraditionalReflectiveProxies");
        if (usesTraditionalReflectiveProxies != null) {
            target.setUsesTraditionalReflectiveProxies(usesTraditionalReflectiveProxies);
        } else {
            target.setUsesTraditionalReflectiveProxies(sourceDatasource.isUsesTraditionalReflectiveProxies());
        }

        String preferredTestQuery = ptDataSourceConf.getProperty("preferredTestQuery");
        if (StringUtils.isNotBlank(preferredTestQuery)) {
            target.setPreferredTestQuery(preferredTestQuery);
        } else {
            target.setPreferredTestQuery(sourceDatasource.getPreferredTestQuery());
        }

        String userOverridesAsString = ptDataSourceConf.getProperty("userOverridesAsString");
        if (StringUtils.isNotBlank(userOverridesAsString)) {
            try {
                target.setUserOverridesAsString(userOverridesAsString);
            } catch (PropertyVetoException e) {
                logger.error("[c3p0] can't found userOverridesAsString:{}", userOverridesAsString, e);
            }
        } else {
            try {
                target.setUserOverridesAsString(sourceDatasource.getUserOverridesAsString());
            } catch (PropertyVetoException e) {
                logger.error("[c3p0] can't found userOverridesAsString:{}", sourceDatasource.getUserOverridesAsString(), e);
            }
        }

        Integer maxAdministrativeTaskTime = ptDataSourceConf.getIntProperty("maxAdministrativeTaskTime");
        if (maxAdministrativeTaskTime != null) {
            target.setMaxAdministrativeTaskTime(maxAdministrativeTaskTime);
        } else {
            target.setMaxAdministrativeTaskTime(sourceDatasource.getMaxAdministrativeTaskTime());
        }

        Integer maxIdleTimeExcessConnections = ptDataSourceConf.getIntProperty("maxIdleTimeExcessConnections");
        if (maxIdleTimeExcessConnections != null) {
            target.setMaxIdleTimeExcessConnections(maxIdleTimeExcessConnections);
        } else {
            target.setMaxIdleTimeExcessConnections(sourceDatasource.getMaxIdleTimeExcessConnections());
        }

        Integer maxConnectionAge = ptDataSourceConf.getIntProperty("maxConnectionAge");
        if (maxConnectionAge != null) {
            target.setMaxConnectionAge(maxConnectionAge);
        } else {
            target.setMaxConnectionAge(sourceDatasource.getMaxConnectionAge());
        }

        String connectionCustomizerClassName = ptDataSourceConf.getProperty("connectionCustomizerClassName");
        if (StringUtils.isNotBlank(connectionCustomizerClassName)) {
            target.setConnectionCustomizerClassName(connectionCustomizerClassName);
        } else {
            target.setConnectionCustomizerClassName(sourceDatasource.getConnectionCustomizerClassName());
        }

        Integer unreturnedConnectionTimeout = ptDataSourceConf.getIntProperty("unreturnedConnectionTimeout");
        if (unreturnedConnectionTimeout != null) {
            target.setUnreturnedConnectionTimeout(unreturnedConnectionTimeout);
        } else {
            target.setUnreturnedConnectionTimeout(sourceDatasource.getUnreturnedConnectionTimeout());
        }

        Boolean debugUnreturnedConnectionStackTraces = ptDataSourceConf.getBooleanProperty("debugUnreturnedConnectionStackTraces");
        if (debugUnreturnedConnectionStackTraces != null) {
            target.setDebugUnreturnedConnectionStackTraces(debugUnreturnedConnectionStackTraces);
        } else {
            target.setDebugUnreturnedConnectionStackTraces(sourceDatasource.isDebugUnreturnedConnectionStackTraces());
        }

        String factoryClassLocation = ptDataSourceConf.getProperty("factoryClassLocation");
        if (StringUtils.isNotBlank(factoryClassLocation)) {
            target.setFactoryClassLocation(factoryClassLocation);
        } else {
            target.setFactoryClassLocation(sourceDatasource.getFactoryClassLocation());
        }


        return target;
    }

    @SuppressWarnings("unchecked")
    private static ShadowDatabaseConfig selectMatchPtDataSourceConfiguration(ComboPooledDataSource source, Map<String, ShadowDatabaseConfig> shadowDbConfigurations) {
        ComboPooledDataSource dataSource = source;
        String key = DbUrlUtils.getKey(dataSource.getJdbcUrl(), dataSource.getUser());
        ShadowDatabaseConfig shadowDatabaseConfig = shadowDbConfigurations.get(key);
        if (shadowDatabaseConfig == null) {
            key = DbUrlUtils.getKey(dataSource.getJdbcUrl(), null);
            shadowDatabaseConfig = shadowDbConfigurations.get(key);
        }
        return shadowDatabaseConfig;
    }
}
