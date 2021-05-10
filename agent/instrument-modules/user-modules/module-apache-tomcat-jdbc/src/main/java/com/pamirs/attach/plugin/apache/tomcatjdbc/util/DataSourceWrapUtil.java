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

import com.pamirs.attach.plugin.apache.tomcatjdbc.obj.TomcatJdbcMediatorDataSource;
import com.pamirs.pradar.ErrorTypeEnum;
import com.pamirs.pradar.pressurement.agent.shared.service.GlobalConfig;
import com.pamirs.pradar.pressurement.agent.shared.service.DataSourceMeta;
import com.pamirs.pradar.pressurement.agent.shared.service.ErrorReporter;
import com.pamirs.pradar.pressurement.datasource.DbMediatorDataSource;
import org.apache.commons.lang.StringUtils;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author angju
 * @date 2020/8/12 17:59
 */
public class DataSourceWrapUtil {
    private final static Logger LOGGER = LoggerFactory.getLogger(DataSourceWrapUtil.class.getName());

    public static final ConcurrentHashMap<DataSourceMeta, TomcatJdbcMediatorDataSource> pressureDataSources = new ConcurrentHashMap<DataSourceMeta, TomcatJdbcMediatorDataSource>();

    public static void destroy() {
        Iterator<Map.Entry<DataSourceMeta, TomcatJdbcMediatorDataSource>> it = pressureDataSources.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<DataSourceMeta, TomcatJdbcMediatorDataSource> entry = it.next();
            it.remove();
            entry.getValue().close();
        }
        pressureDataSources.clear();
    }

    public static void doWrap(DataSourceMeta<DataSource> dataSourceMeta) {
        DataSource target = dataSourceMeta.getDataSource();
        if (pressureDataSources.containsKey(dataSourceMeta) && pressureDataSources.get(dataSourceMeta) != null) {
            return;
        }
        if (isPerformanceDataSource(target)) {
            return;
        }

        if (!TomcatJdbcDatasourceUtils.configured(target)) {//没有配置对应的影子表或影子库
            LOGGER.error("[tomcat-jdbc] No configuration found for datasource, url:{} username:{}", target.getUrl(), target.getUsername());
            ErrorReporter.buildError()
                    .setErrorType(ErrorTypeEnum.DataSource)
                    .setErrorCode("datasource-0002")
                    .setMessage("没有配置对应的影子表或影子库！")
                    .setDetail("业务库配置:::url: " + target.getUrl() + " ;username: " + target.getUsername())
                    .report();
            TomcatJdbcMediatorDataSource dbMediatorDataSource = new TomcatJdbcMediatorDataSource();
            dbMediatorDataSource.setDataSourceBusiness(target);
            DbMediatorDataSource old = pressureDataSources.put(dataSourceMeta, dbMediatorDataSource);
            if (old != null) {
                LOGGER.info("[tomcat-jdbc] destroyed shadow table datasource success. url:{} ,username:{}", target.getUrl(), target.getUsername());
                old.close();
            }
            return;
        }

        if (TomcatJdbcDatasourceUtils.shadowTable(target)) {//影子表
            TomcatJdbcMediatorDataSource dbMediatorDataSource = new TomcatJdbcMediatorDataSource();
            dbMediatorDataSource.setDataSourceBusiness(target);
            DbMediatorDataSource old = pressureDataSources.put(dataSourceMeta, dbMediatorDataSource);
            if (old != null) {
                LOGGER.info("[tomcat-jdbc] destroyed shadow table datasource success. url:{} ,username:{}", target.getUrl(), target.getUsername());
                old.close();
            }
        } else {//影子库
            // 初始化影子数据源配置
            TomcatJdbcMediatorDataSource dbMediatorDataSource = new TomcatJdbcMediatorDataSource();
            dbMediatorDataSource.setDataSourceBusiness(target);
            DataSource ptDataSource = TomcatJdbcDatasourceUtils.generateDatasourceFromConfiguration(target, GlobalConfig.getInstance().getShadowDatasourceConfigs());
            if (ptDataSource == null) {
                LOGGER.error("[tomcat-jdbc] Configuration error for datasource, url: {} username:{} configurations:{}", target.getUrl(), target.getUsername(), GlobalConfig.getInstance().getShadowDatasourceConfigs());
                ErrorReporter.buildError()
                        .setErrorType(ErrorTypeEnum.DataSource)
                        .setErrorCode("datasource-0002")
                        .setMessage("没有配置对应的影子表或影子库！")
                        .setDetail("TomcatJdbcDataSourceWrapUtil:业务库配置:::url: " + target.getUrl() + " ;username: " + target.getUsername())
                        .report();
                return;
            }
            dbMediatorDataSource.setDataSourcePerformanceTest(ptDataSource);
            DbMediatorDataSource old = pressureDataSources.put(dataSourceMeta, dbMediatorDataSource);
            if (old != null) {
                LOGGER.info("[tomcat-jdbc] destroyed shadow table datasource success. url:{} ,username:{}", target.getUrl(), target.getUsername());
                old.close();
            }
            LOGGER.info("[tomcat-jdbc] create shadow datasource success. target:{} url:{} ,username:{} shadow-url:{},shadow-username:{}", target.hashCode(), target.getUrl(), target.getUsername(), ptDataSource.getUrl(), ptDataSource.getUsername());
        }
    }


    /**
     * 是否是影子数据源
     *
     * @param target 目标数据源
     * @return
     */
    private static boolean isPerformanceDataSource(DataSource target) {
        for (Map.Entry<DataSourceMeta, TomcatJdbcMediatorDataSource> entry : pressureDataSources.entrySet()) {
            TomcatJdbcMediatorDataSource mediatorDataSource = entry.getValue();
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
}
