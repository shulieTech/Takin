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
package com.pamirs.attach.plugin.common.datasource;

import com.shulie.druid.util.JdbcUtils;
import com.pamirs.attach.plugin.common.datasource.biz.BizConnection;
import com.pamirs.attach.plugin.common.datasource.normal.NormalConnection;
import com.pamirs.attach.plugin.common.datasource.pressure.PressureConnection;
import com.pamirs.pradar.ErrorTypeEnum;
import com.pamirs.pradar.Pradar;
import com.pamirs.pradar.exception.PressureMeasureError;
import com.pamirs.pradar.pressurement.agent.shared.service.ErrorReporter;
import com.pamirs.pradar.pressurement.datasource.DatabaseUtils;
import com.pamirs.pradar.pressurement.datasource.DbMediatorDataSource;
import com.pamirs.pradar.pressurement.datasource.SqlParser;
import com.pamirs.pradar.pressurement.datasource.util.DbUrlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Description
 * @Author xiaobin.zfb
 * @mail xiaobin@shulie.io
 * @Date 2020/7/15 4:43 下午
 */
public abstract class WrappedDbMediatorDataSource<T extends DataSource> extends DbMediatorDataSource<T> implements DataSource {
    protected final static Logger LOGGER = LoggerFactory.getLogger(WrappedDbMediatorDataSource.class.getName());

    protected volatile AtomicBoolean init = new AtomicBoolean(false);

    /**
     * 根据数据源获取用户
     *
     * @param datasource
     * @return
     */
    public abstract String getUsername(T datasource);

    /**
     * 根据数据源获取url
     *
     * @param datasource
     * @return
     */
    public abstract String getUrl(T datasource);

    /**
     * 获取数据源获取驱动名称
     *
     * @param datasource
     * @return
     */
    public abstract String getDriverClassName(T datasource);

    /**
     * 获取jndi名称
     *
     * @return
     */
    protected String getJndiName() {
        return null;
    }

    /**
     * 是否是jndi数据源
     *
     * @return
     */
    protected boolean isJndi() {
        return false;
    }

    //init method
    public void init() {
        if (!init.compareAndSet(false, true)) {
            return;
        }
        SqlParser.lowerCase = lowerCase;
        Connection jdbcConnection = null;
        try {
            /**
             * 根据配置判断是否是影子表
             */
            String url = getUrl(dataSourceBusiness);
            String username = getUsername(dataSourceBusiness);
            if (url == null || username == null) {
                jdbcConnection = dataSourceBusiness.getConnection();
                Map<String, String> urlUsername = DatabaseUtils.getUrlUsername(jdbcConnection);
                String newUsername = urlUsername.get("username");
                String newUrl = urlUsername.get("url");
                if (username == null && newUsername != null) {
                    username = newUsername;
                }
                if (url == null && newUrl != null) {
                    url = newUrl;
                }
            }

            if (url == null) {
                url = getJndiName();
            }

            this.url = url;
            this.username = username;

            if (isJndi()) {
                this.useTable = !DatabaseUtils.isShadowDatasource(getJndiName(), null);
                this.dbConnectionKey = DbUrlUtils.getKey(getJndiName(), null);
            } else {
                this.useTable = !DatabaseUtils.isShadowDatasource(url, username);
                this.dbConnectionKey = DbUrlUtils.getKey(url, username);
            }

            String driverClassName = getDriverClassName(dataSourceBusiness);
            if(driverClassName == null) {
                driverClassName = JdbcUtils.getDriverClassName(url);
            }
            this.dbType = JdbcUtils.getDbType(url,  driverClassName);

        } catch (SQLException e) {
            init.set(false);
            throw new RuntimeException(e);
        } catch (Throwable e) {
            init.set(false);
            throw new RuntimeException(e);
        } finally {
            try {
                if (jdbcConnection != null) {
                    jdbcConnection.close();
                }
            } catch (SQLException e) {
            }
        }
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    @Override
    public PrintWriter getLogWriter() throws SQLException {
        if (Pradar.isClusterTest()) {
            init();
            if (useTable) {
                return dataSourceBusiness.getLogWriter();
            } else {
                return dataSourcePerformanceTest.getLogWriter();
            }
        } else {
            return dataSourceBusiness.getLogWriter();
        }
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    @Override
    public int getLoginTimeout() throws SQLException {
        if (Pradar.isClusterTest()) {
            init();
            if (useTable) {
                return dataSourceBusiness.getLoginTimeout();
            } else {
                return dataSourcePerformanceTest.getLoginTimeout();
            }
        } else {
            return dataSourceBusiness.getLoginTimeout();
        }
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        if (Pradar.isClusterTest()) {
            init();
            if (useTable) {
                dataSourceBusiness.setLogWriter(out);
            } else {
                dataSourcePerformanceTest.setLogWriter(out);
            }
        } else {
            dataSourceBusiness.setLogWriter(out);
        }
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    @Override
    public void setLoginTimeout(int second) throws SQLException {
        if (Pradar.isClusterTest()) {
            init();
            if (useTable) {
                dataSourceBusiness.setLoginTimeout(second);
            } else {
                dataSourcePerformanceTest.setLoginTimeout(second);
            }
        } else {
            dataSourceBusiness.setLoginTimeout(second);
        }
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        if (Pradar.isClusterTest()) {
            init();
            if (useTable) {
                return dataSourceBusiness.isWrapperFor(iface);
            } else {
                return dataSourcePerformanceTest.isWrapperFor(iface);
            }
        } else {
            return dataSourceBusiness.isWrapperFor(iface);
        }
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        if (Pradar.isClusterTest()) {
            init();
            if (useTable) {
                return dataSourceBusiness.unwrap(iface);
            } else {
                return dataSourcePerformanceTest.unwrap(iface);
            }
        } else {
            return dataSourceBusiness.unwrap(iface);
        }
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    @Override
    public Connection getConnection() throws SQLException {
        Throwable t = null;
        try {
            if (Pradar.isClusterTest()) {
                init();
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
                    return new PressureConnection(dataSourcePerformanceTest, dataSourcePerformanceTest.getConnection(), url, username, dbConnectionKey, dbType);
                }

            } else {
                return new BizConnection(dataSourceBusiness.getConnection(), url, username, dbType);
            }
        } catch (Throwable e) {
            ErrorReporter.Error error = ErrorReporter.buildError()
                    .setErrorType(ErrorTypeEnum.DataSource)
                    .setErrorCode("datasource-0001")
                    .setMessage("数据源获取链接失败！" + (Pradar.isClusterTest() ? "(压测流量)" : ""))
                    .setDetail("get connection failed by dbMediatorDataSource, message: " + e.getMessage() + "\r\n" + printStackTrace(e));
            /*if (Pradar.isClusterTest()) {
                error.closePradar(ConfigNames.SHADOW_DATABASE_CONFIGS);
            }*/
            error.report();
            t = e;
        }
        if (Pradar.isClusterTest()) {
            throw new PressureMeasureError("pressure test flow get connection fail " + t.getMessage());
        }

        return dataSourceBusiness.getConnection();
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    @Override
    public Connection getConnection(String username, String password)
            throws SQLException {
        Throwable t = null;
        try {
            if (Pradar.isClusterTest()) {
                init();
                if (useTable) {
                    if (dataSourceBusiness == null) {
                        throw new PressureMeasureError("Business dataSource is null.");
                    }
                    return new NormalConnection(dataSourceBusiness, dataSourceBusiness.getConnection(username, password), dbConnectionKey, url, username, dbType);
                } else {
                    if (dataSourcePerformanceTest == null) {
                        throw new PressureMeasureError("Performance dataSource is null.");
                    }
                    return new PressureConnection(dataSourcePerformanceTest, dataSourcePerformanceTest.getConnection(username, password), url, username, dbConnectionKey, dbType);
                }
            } else {
                return new BizConnection(dataSourceBusiness.getConnection(username, password), url, username, dbType);
            }
        } catch (Throwable e) {
            ErrorReporter.Error error = ErrorReporter.buildError()
                    .setErrorType(ErrorTypeEnum.DataSource)
                    .setErrorCode("datasource-0001")
                    .setMessage("数据源获取链接失败！" + (Pradar.isClusterTest() ? "(压测流量)" : ""))
                    .setDetail("get connection failed by dbMediatorDataSource, message: " + e.getMessage() + "\r\n" + printStackTrace(e));
            /*if (Pradar.isClusterTest()) {
                error.closePradar(ConfigNames.SHADOW_DATABASE_CONFIGS);
            }*/
            error.report();
            t = e;
        }
        if (Pradar.isClusterTest()) {
            throw new PressureMeasureError("pressure test flow get connection fail " + t.getMessage());
        }
        return dataSourceBusiness.getConnection(username, password);
    }

    protected static String printStackTrace(Throwable e) {
        if (e == null) {
            return "";
        }
        if (e.getStackTrace() == null || e.getStackTrace().length == 0) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (StackTraceElement element : e.getStackTrace()) {
            stringBuilder.append(element.toString());
        }
        return stringBuilder.toString();
    }

    /**
     * 说明: 该方法应用为Jdk1.6之后新增
     */
    @Override
    public java.util.logging.Logger getParentLogger() {
        if (Pradar.isClusterTest()) {
            init();
            if (useTable) {
                Class clazz = dataSourceBusiness.getClass();
                Method m = null;
                java.util.logging.Logger l = null;
                try {
                    m = clazz.getDeclaredMethod("getParentLogger");
                    l = (java.util.logging.Logger) m.invoke(dataSourceBusiness);
                } catch (Throwable e) {
                    LOGGER.error("", e);
                }
                return l;
            } else {
                Class clazz = dataSourcePerformanceTest.getClass();
                Method m = null;
                java.util.logging.Logger l = null;
                try {
                    m = clazz.getDeclaredMethod("getParentLogger");
                    l = (java.util.logging.Logger) m.invoke(dataSourcePerformanceTest);
                } catch (Throwable e) {
                    LOGGER.error("", e);
                }
                return l;
            }

        } else {
            Class clazz = dataSourceBusiness.getClass();
            Method m = null;
            java.util.logging.Logger l = null;
            try {
                m = clazz.getDeclaredMethod("getParentLogger");
                l = (java.util.logging.Logger) m.invoke(dataSourceBusiness);
            } catch (Throwable e) {
                LOGGER.error("", e);
            }
            return l;
        }
    }

    @Override
    public void close() {
    }
}
