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
package com.pamirs.attach.plugin.dbcp2.utils;

import com.pamirs.pradar.ConfigNames;
import com.pamirs.pradar.ErrorTypeEnum;
import com.pamirs.pradar.Throwables;
import com.pamirs.pradar.pressurement.agent.shared.service.DataSourceMeta;
import com.pamirs.pradar.pressurement.agent.shared.service.ErrorReporter;
import com.pamirs.pradar.pressurement.agent.shared.service.GlobalConfig;
import com.pamirs.pradar.internal.config.ShadowDatabaseConfig;
import com.pamirs.pradar.pressurement.datasource.DatabaseUtils;
import com.pamirs.pradar.pressurement.datasource.DbMediatorDataSource;
import com.pamirs.pradar.pressurement.datasource.util.DbUrlUtils;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DataSourceWrapUtil {
    private static Logger logger = LoggerFactory.getLogger(BasicDataSource.class.getName());

    public static final ConcurrentHashMap<DataSourceMeta, DbcpMediaDataSource> pressureDataSources = new ConcurrentHashMap<DataSourceMeta, DbcpMediaDataSource>();

    public static void destroy() {
        Iterator<Map.Entry<DataSourceMeta, DbcpMediaDataSource>> it = pressureDataSources.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<DataSourceMeta, DbcpMediaDataSource> entry = it.next();
            it.remove();
            entry.getValue().close();
        }
        pressureDataSources.clear();
    }

    public static boolean validate(BasicDataSource sourceDataSource) {
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

    public static boolean shadowTable(BasicDataSource sourceDataSource) {
        try {
            String url = sourceDataSource.getUrl();
            String username = sourceDataSource.getUsername();
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
    private static boolean isPerformanceDataSource(BasicDataSource target) {
        for (Map.Entry<DataSourceMeta, DbcpMediaDataSource> entry : pressureDataSources.entrySet()) {
            DbcpMediaDataSource mediatorDataSource = entry.getValue();
            if (mediatorDataSource.getDataSourcePerformanceTest() == null) {
                continue;
            }

            if (StringUtils.equals(mediatorDataSource.getDataSourcePerformanceTest().getUrl(), target.getUrl()) &&
                    StringUtils.equals(mediatorDataSource.getDataSourcePerformanceTest().getUsername(), target.getUsername())) {
                return true;
            }
        }
        return false;
    }

    //  static AtomicBoolean inited = new AtomicBoolean(false);

    public static void init(DataSourceMeta<BasicDataSource> dataSourceMeta) {
        if (pressureDataSources.containsKey(dataSourceMeta) && pressureDataSources.get(dataSourceMeta) != null) {
            return;
        }
        BasicDataSource target = dataSourceMeta.getDataSource();
        if (isPerformanceDataSource(target)) {
            return;
        }
        if (!validate(target)) {
            //没有配置对应的影子表或影子库
            ErrorReporter.buildError()
                    .setErrorType(ErrorTypeEnum.DataSource)
                    .setErrorCode("datasource-0002")
                    .setMessage("没有配置对应的影子表或影子库！")
                    .setDetail("dbcp2:DataSourceWrapUtil:业务库配置:::url: " + target.getUrl())
                    .closePradar(ConfigNames.SHADOW_DATABASE_CONFIGS)
                    .report();

            DbcpMediaDataSource dbMediatorDataSource = new DbcpMediaDataSource();
            dbMediatorDataSource.setDataSourceBusiness(target);
            DbMediatorDataSource old = pressureDataSources.put(dataSourceMeta, dbMediatorDataSource);
            if (old != null) {
                logger.info("[dbcp2] destroyed shadow table datasource success. url:{} ,username:{}", target.getUrl(), target.getUsername());
                old.close();
            }
            return;
        }
        if (shadowTable(target)) {
            //影子表
            try {
                DbcpMediaDataSource dbMediatorDataSource = new DbcpMediaDataSource();
                dbMediatorDataSource.setDataSourceBusiness(target);
                DbMediatorDataSource old = pressureDataSources.put(dataSourceMeta, dbMediatorDataSource);
                if (old != null) {
                    logger.info("[dbcp2] destroyed shadow table datasource success. url:{} ,username:{}", target.getUrl(), target.getUsername());
                    old.close();
                }
            } catch (Throwable e) {
                ErrorReporter.buildError()
                        .setErrorType(ErrorTypeEnum.DataSource)
                        .setErrorCode("datasource-0003")
                        .setMessage("影子表设置初始化异常！")
                        .setDetail("dbcp2:DataSourceWrapUtil:业务库配置:::url: " + target.getUrl() + "|||" + Throwables.getStackTraceAsString(e))
                        .closePradar(ConfigNames.SHADOW_DATABASE_CONFIGS)
                        .report();
                logger.error("[dbcp2] init datasource err!", e);
            }
        } else {
            //影子库
            try {
                DbcpMediaDataSource dataSource = new DbcpMediaDataSource();
                BasicDataSource ptDataSource = copy(target);
                dataSource.setDataSourcePerformanceTest(ptDataSource);
                dataSource.setDataSourceBusiness(target);
                DbMediatorDataSource old = pressureDataSources.put(dataSourceMeta, dataSource);
                if (old != null) {
                    logger.info("[dbcp2] destroyed shadow table datasource success. url:{} ,username:{}", target.getUrl(), target.getUsername());
                    old.close();
                }
                logger.info("[dbcp2] create shadow datasource success. target:{} url:{} ,username:{} shadow-url:{},shadow-username:{}", target.hashCode(), target.getUrl(), target.getUsername(), ptDataSource.getUrl(), ptDataSource.getUsername());
            } catch (Throwable t) {
                logger.error("[dbcp2] init datasource err!", t);
                ErrorReporter.buildError()
                        .setErrorType(ErrorTypeEnum.DataSource)
                        .setErrorCode("datasource-0003")
                        .setMessage("影子库设置初始化异常！")
                        .setDetail("dbcp2:DataSourceWrapUtil:业务库配置:::url: " + target.getUrl()
                                + "|||" + Throwables.getStackTraceAsString(t))
                        .closePradar(ConfigNames.SHADOW_DATABASE_CONFIGS)
                        .report();
            }
        }
    }

    private static BasicDataSource copy(BasicDataSource source) {
        BasicDataSource target = generate(source);
        return target;
    }


    public static BasicDataSource generate(BasicDataSource sourceDatasource) {
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
            driverClassName = sourceDatasource.getDriverClassName();
        }

        BasicDataSource target = new BasicDataSource();
        target.setUrl(url);
        target.setUsername(username);
        target.setPassword(password);
        target.setDriverClassName(driverClassName);

        Boolean defaultAutoCommit = ptDataSourceConf.getBooleanProperty("defaultAutoCommit");
        if (defaultAutoCommit != null) {
            target.setDefaultAutoCommit(defaultAutoCommit);
        } else {
            target.setDefaultAutoCommit(sourceDatasource.getDefaultAutoCommit());
        }

        Boolean defaultReadOnly = ptDataSourceConf.getBooleanProperty("defaultReadOnly");
        if (defaultReadOnly != null) {
            target.setDefaultReadOnly(defaultReadOnly);
        } else {
            target.setDefaultReadOnly(sourceDatasource.getDefaultReadOnly());
        }

        Integer defaultTransactionIsolation = ptDataSourceConf.getIntProperty("defaultTransactionIsolation");
        if (defaultTransactionIsolation != null) {
            target.setDefaultTransactionIsolation(defaultTransactionIsolation);
        } else {
            target.setDefaultTransactionIsolation(sourceDatasource.getDefaultTransactionIsolation());
        }

        String defaultCatalog = ptDataSourceConf.getProperty("defaultCatalog");
        if (StringUtils.isNotBlank(defaultCatalog)) {
            target.setDefaultCatalog(defaultCatalog);
        } else {
            target.setDefaultCatalog(sourceDatasource.getDefaultCatalog());
        }

        String evictionPolicyClassName = ptDataSourceConf.getProperty("evictionPolicyClassName");
        if (StringUtils.isNotBlank(evictionPolicyClassName)) {
            target.setEvictionPolicyClassName(evictionPolicyClassName);
        } else {
            target.setEvictionPolicyClassName(sourceDatasource.getEvictionPolicyClassName());
        }

        Boolean fastFailValidation = ptDataSourceConf.getBooleanProperty("fastFailValidation");
        if (fastFailValidation != null) {
            target.setFastFailValidation(fastFailValidation);
        } else {
            target.setFastFailValidation(sourceDatasource.getFastFailValidation());
        }

        Integer maxIdle = ptDataSourceConf.getIntProperty("maxIdle");
        if (maxIdle != null) {
            target.setMaxIdle(maxIdle);
        } else {
            target.setMaxIdle(sourceDatasource.getMaxIdle());
        }

        Integer minIdle = ptDataSourceConf.getIntProperty("minIdle");
        if (minIdle != null) {
            target.setMinIdle(minIdle);
        } else {
            target.setMinIdle(sourceDatasource.getMinIdle());
        }

        Integer initialSize = ptDataSourceConf.getIntProperty("initialSize");
        if (initialSize != null) {
            target.setInitialSize(initialSize);
        } else {
            target.setInitialSize(sourceDatasource.getInitialSize());
        }


        Boolean poolPreparedStatements = ptDataSourceConf.getBooleanProperty("poolPreparedStatements");
        if (poolPreparedStatements != null) {
            target.setPoolPreparedStatements(poolPreparedStatements);
        } else {
            target.setPoolPreparedStatements(sourceDatasource.isPoolPreparedStatements());
        }

        Long softMinEvictableIdleTimeMillis = ptDataSourceConf.getLongProperty("softMinEvictableIdleTimeMillis");
        if (softMinEvictableIdleTimeMillis != null) {
            target.setSoftMinEvictableIdleTimeMillis(softMinEvictableIdleTimeMillis);
        } else {
            target.setSoftMinEvictableIdleTimeMillis(sourceDatasource.getSoftMinEvictableIdleTimeMillis());
        }

        Integer maxOpenPreparedStatements = ptDataSourceConf.getIntProperty("maxOpenPreparedStatements");
        if (maxOpenPreparedStatements != null) {
            target.setMaxOpenPreparedStatements(maxOpenPreparedStatements);
        } else {
            target.setMaxOpenPreparedStatements(sourceDatasource.getMaxOpenPreparedStatements());
        }

        Boolean testOnBorrow = ptDataSourceConf.getBooleanProperty("testOnBorrow");
        if (testOnBorrow != null) {
            target.setTestOnBorrow(testOnBorrow);
        } else {
            target.setTestOnBorrow(sourceDatasource.getTestOnBorrow());
        }

        Integer maxTotal = ptDataSourceConf.getIntProperty("maxTotal");
        if (maxTotal != null) {
            target.setMaxTotal(maxTotal);
        } else {
            target.setMaxTotal(sourceDatasource.getMaxTotal());
        }

        Boolean testOnCreate = ptDataSourceConf.getBooleanProperty("testOnCreate");
        if (testOnCreate != null) {
            target.setTestOnCreate(testOnCreate);
        } else {
            target.setTestOnCreate(sourceDatasource.getTestOnCreate());
        }

        Boolean testOnReturn = ptDataSourceConf.getBooleanProperty("testOnReturn");
        if (testOnReturn != null) {
            target.setTestOnReturn(testOnReturn);
        } else {
            target.setTestOnReturn(sourceDatasource.getTestOnReturn());
        }

        Long timeBetweenEvictionRunsMillis = ptDataSourceConf.getLongProperty("timeBetweenEvictionRunsMillis");
        if (timeBetweenEvictionRunsMillis != null) {
            target.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        } else {
            target.setTimeBetweenEvictionRunsMillis(sourceDatasource.getTimeBetweenEvictionRunsMillis());
        }

        Integer numTestsPerEvictionRun = ptDataSourceConf.getIntProperty("numTestsPerEvictionRun");
        if (numTestsPerEvictionRun != null) {
            target.setNumTestsPerEvictionRun(Integer.valueOf(numTestsPerEvictionRun));
        } else {
            target.setNumTestsPerEvictionRun(sourceDatasource.getNumTestsPerEvictionRun());
        }

        Long minEvictableIdleTimeMillis = ptDataSourceConf.getLongProperty("minEvictableIdleTimeMillis");
        if (minEvictableIdleTimeMillis != null) {
            target.setMinEvictableIdleTimeMillis(Long.valueOf(minEvictableIdleTimeMillis));
        } else {
            target.setMinEvictableIdleTimeMillis(sourceDatasource.getMinEvictableIdleTimeMillis());
        }

        Boolean testWhileIdle = ptDataSourceConf.getBooleanProperty("testWhileIdle");
        if (testWhileIdle != null) {
            target.setTestWhileIdle(testWhileIdle);
        } else {
            target.setTestWhileIdle(sourceDatasource.getTestWhileIdle());
        }

        String validationQuery = ptDataSourceConf.getProperty("validationQuery");
        if (StringUtils.isNotBlank(validationQuery)) {
            target.setValidationQuery(validationQuery);
        } else {
            target.setValidationQuery(sourceDatasource.getValidationQuery());
        }

        Integer validationQueryTimeout = ptDataSourceConf.getIntProperty("validationQueryTimeout");
        if (validationQueryTimeout != null) {
            target.setValidationQueryTimeout(validationQueryTimeout);
        } else {
            target.setValidationQueryTimeout(sourceDatasource.getValidationQueryTimeout());
        }

        String connectionInitSqls = ptDataSourceConf.getProperty("connectionInitSqls");
        if (StringUtils.isNotBlank(connectionInitSqls)) {
            target.setConnectionInitSqls(Arrays.asList(StringUtils.split(connectionInitSqls, ';')));
        } else {
            target.setConnectionInitSqls(sourceDatasource.getConnectionInitSqls());
        }

        Boolean accessToUnderlyingConnectionAllowed = ptDataSourceConf.getBooleanProperty("accessToUnderlyingConnectionAllowed");
        if (accessToUnderlyingConnectionAllowed != null) {
            target.setAccessToUnderlyingConnectionAllowed(accessToUnderlyingConnectionAllowed);
        } else {
            target.setAccessToUnderlyingConnectionAllowed(sourceDatasource.isAccessToUnderlyingConnectionAllowed());
        }

        Integer removeAbandonedTimeout = ptDataSourceConf.getIntProperty("removeAbandonedTimeout");
        if (removeAbandonedTimeout != null) {
            target.setRemoveAbandonedTimeout(removeAbandonedTimeout);
        } else {
            target.setRemoveAbandonedTimeout(sourceDatasource.getRemoveAbandonedTimeout());
        }

        Boolean logAbandoned = ptDataSourceConf.getBooleanProperty("logAbandoned");
        if (logAbandoned != null) {
            target.setLogAbandoned(logAbandoned);
        } else {
            target.setLogAbandoned(sourceDatasource.getLogAbandoned());
        }


        return target;
    }

    @SuppressWarnings("unchecked")
    private static ShadowDatabaseConfig selectMatchPtDataSourceConfiguration(BasicDataSource source, Map<String, ShadowDatabaseConfig> shadowDbConfigurations) {
        BasicDataSource dataSource = source;
        String key = DbUrlUtils.getKey(dataSource.getUrl(), dataSource.getUsername());
        ShadowDatabaseConfig shadowDatabaseConfig = shadowDbConfigurations.get(key);
        if (shadowDatabaseConfig == null) {
            key = DbUrlUtils.getKey(dataSource.getUrl(), null);
            shadowDatabaseConfig = shadowDbConfigurations.get(key);
        }
        return shadowDatabaseConfig;
    }
}
