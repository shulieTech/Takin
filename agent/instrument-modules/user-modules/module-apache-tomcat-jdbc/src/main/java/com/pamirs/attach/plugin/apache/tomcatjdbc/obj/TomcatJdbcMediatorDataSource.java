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
package com.pamirs.attach.plugin.apache.tomcatjdbc.obj;

import com.pamirs.attach.plugin.apache.tomcatjdbc.util.TomcatJdbcConstants;
import com.pamirs.attach.plugin.common.datasource.WrappedDbMediatorDataSource;
import com.pamirs.attach.plugin.common.datasource.biz.BizConnection;
import com.pamirs.attach.plugin.common.datasource.normal.NormalConnection;
import com.pamirs.attach.plugin.common.datasource.pressure.PressureConnection;
import com.pamirs.pradar.ConfigNames;
import com.pamirs.pradar.ErrorTypeEnum;
import com.pamirs.pradar.Pradar;
import com.pamirs.pradar.exception.PressureMeasureError;
import com.pamirs.pradar.pressurement.agent.shared.service.ErrorReporter;
import com.shulie.druid.util.JdbcUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author angju
 * @date 2020/8/11 15:04
 */
public class TomcatJdbcMediatorDataSource extends WrappedDbMediatorDataSource<org.apache.tomcat.jdbc.pool.DataSource> implements DataSource {

    @Override
    public String getUsername(org.apache.tomcat.jdbc.pool.DataSource datasource) {
        return datasource.getUsername();
    }

    @Override
    public String getUrl(org.apache.tomcat.jdbc.pool.DataSource datasource) {
        return datasource.getUrl();
    }

    @Override
    public String getDriverClassName(org.apache.tomcat.jdbc.pool.DataSource datasource) {
        return datasource.getDriverClassName();
    }

    @Override
    public Connection getConnection() throws SQLException {
        if (Pradar.isClusterTest()) {
            try {
                init();
                TomcatJdbcConstants.dbConnectionKey = dbConnectionKey;
                TomcatJdbcConstants.dbType = dbType;
                TomcatJdbcConstants.useTable = useTable;
                if (useTable) {
                    //影子表
                    if (dataSourceBusiness == null) {
                        throw new PressureMeasureError("Business dataSource is null.");
                    }
                    return new NormalConnection(dataSourceBusiness, dataSourceBusiness.getConnection(), dbConnectionKey, url, username, dbType);
                } else {
                    //影子库
                    if (dataSourcePerformanceTest == null) {
                        throw new PressureMeasureError("Performance dataSource is null.");
                    }
                    return new PressureConnection(dataSourceBusiness, dataSourcePerformanceTest.getConnection(), dataSourcePerformanceTest.getUrl(), dataSourcePerformanceTest.getUsername(),dbConnectionKey, dbType);
                }
            } catch (Throwable e) {
                ErrorReporter.Error error = ErrorReporter.buildError()
                        .setErrorType(ErrorTypeEnum.DataSource)
                        .setErrorCode("datasource-0001")
                        .setMessage("数据源获取链接失败！" + (Pradar.isClusterTest() ? "(压测流量)" : ""))
                        .setDetail("get connection failed by dbMediatorDataSource, message: " + e.getMessage() + "\r\n" + printStackTrace(e));
//                error.closePradar(ConfigNames.SHADOW_DATABASE_CONFIGS);
                error.report();
                throw new PressureMeasureError("tomcat jdbc get connection failed by dbMediatorDataSource");
            }
        } else {
            String dbType = JdbcUtils.getDbType(dataSourceBusiness.getUrl(), JdbcUtils.getDriverClassName(dataSourceBusiness.getUrl()));
            return new BizConnection(dataSourceBusiness.getConnection(), dataSourceBusiness.getUrl(), dataSourceBusiness.getUsername(), dbType);
        }
    }


    @Override
    public Connection getConnection(String username, String password)
            throws SQLException {
        if (Pradar.isClusterTest()) {
            try {
                init();
                TomcatJdbcConstants.dbConnectionKey = dbConnectionKey;
                TomcatJdbcConstants.dbType = dbType;
                TomcatJdbcConstants.useTable = useTable;
                if (useTable) {
                    //影子表
                    if (dataSourceBusiness == null) {
                        throw new PressureMeasureError("Business dataSource is null.");
                    }
                    return new NormalConnection(dataSourceBusiness, dataSourceBusiness.getConnection(username, password), dbConnectionKey, url, username, dbType);
                } else {
                    //影子库
                    if (dataSourcePerformanceTest == null) {
                        throw new PressureMeasureError("Performance dataSource is null.");
                    }
                    return new PressureConnection(dataSourceBusiness, dataSourcePerformanceTest.getConnection(username, password), url, username, dbConnectionKey, dbType);
                }
            } catch (Throwable e) {
                ErrorReporter.Error error = ErrorReporter.buildError()
                        .setErrorType(ErrorTypeEnum.DataSource)
                        .setErrorCode("datasource-0001")
                        .setMessage("数据源获取链接失败！" + (Pradar.isClusterTest() ? "(压测流量)" : ""))
                        .setDetail("get connection failed by dbMediatorDataSource, message: " + e.getMessage() + "\r\n" + printStackTrace(e));
//                error.closePradar(ConfigNames.SHADOW_DATABASE_CONFIGS);
                error.report();
                throw new PressureMeasureError("tomcat jdbc get connection failed by dbMediatorDataSource");
            }
        } else {
            String dbType = JdbcUtils.getDbType(dataSourceBusiness.getUrl(), JdbcUtils.getDriverClassName(dataSourceBusiness.getUrl()));
            return new BizConnection(dataSourceBusiness.getConnection(username, password), dataSourceBusiness.getUrl(), dataSourceBusiness.getUsername(), dbType);
        }
    }

    @Override
    public void close() {
        if (dataSourcePerformanceTest != null) {
            try {
                dataSourcePerformanceTest.close();
            } catch (Throwable e) {
                LOGGER.error("[tomcat datasource] dataSourcePerformanceTest close err!", e);
            }
        }
    }


}
