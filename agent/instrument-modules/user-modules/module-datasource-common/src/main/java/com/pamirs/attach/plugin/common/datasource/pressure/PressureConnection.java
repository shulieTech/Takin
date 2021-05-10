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
package com.pamirs.attach.plugin.common.datasource.pressure;

import com.pamirs.attach.plugin.common.datasource.trace.CheckedTraceCallableStatement;
import com.pamirs.attach.plugin.common.datasource.trace.CheckedTracePreparedStatement;
import com.pamirs.attach.plugin.common.datasource.trace.CheckedTraceStatement;
import com.pamirs.pradar.internal.config.ShadowDatabaseConfig;
import com.pamirs.pradar.pressurement.agent.shared.service.GlobalConfig;
import com.pamirs.pradar.pressurement.datasource.SqlParser;
import com.pamirs.pradar.pressurement.datasource.util.DbType;
import com.pamirs.pradar.pressurement.datasource.util.SqlMetaData;
import org.apache.commons.lang.StringUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

/**
 * connection can replace db schema
 * 压测连接，只接受压测流量，非压测流量直接拒绝
 *
 * @author 311183
 */
public class PressureConnection implements Connection {
    protected DataSource dataSource;

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    protected Connection connection;

    /**
     * url
     */
    protected String url;

    /**
     * username
     */
    protected String username;

    /**
     * 数据库类型
     */
    protected String dbType;

    /**
     * 数据库唯一标识
     */
    protected String dbConnectionKey;

    /**
     * sql metadata
     */
    protected SqlMetaData sqlMetaData;

    public PressureConnection(DataSource dataSource, Connection connection, String url, String username, String dbConnectionKey, String dbType) {
        this.dataSource = dataSource;
        this.connection = connection;
        this.url = url;
        this.username = username;
        this.dbConnectionKey = dbConnectionKey;
        this.dbType = dbType;
        DbType type = DbType.nameOf(dbType);
        if (type != null) {
            try {
                this.sqlMetaData = type.sqlMetaData(url);
            } catch (Throwable e) {
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
    public boolean isWrapperFor(Class<?> arg0) throws SQLException {
        return connection.isWrapperFor(arg0);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    @Override
    public <T> T unwrap(Class<T> arg0) throws SQLException {
        return connection.unwrap(arg0);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public void abort(Executor arg0) throws SQLException {
        try {
            connection.abort(arg0);
        } catch (AbstractMethodError e) {
        } catch (NoSuchMethodError e) {
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
    public void clearWarnings() throws SQLException {
        connection.clearWarnings();
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    @Override
    public void close() throws SQLException {
        connection.close();
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    @Override
    public void commit() throws SQLException {
        connection.commit();
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    @Override
    public Array createArrayOf(String arg0, Object[] arg1) throws SQLException {
        return connection.createArrayOf(arg0, arg1);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    @Override
    public Blob createBlob() throws SQLException {
        return connection.createBlob();
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    @Override
    public Clob createClob() throws SQLException {
        return connection.createClob();
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    @Override
    public NClob createNClob() throws SQLException {
        return connection.createNClob();
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    @Override
    public SQLXML createSQLXML() throws SQLException {
        return connection.createSQLXML();
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    @Override
    public Statement createStatement() throws SQLException {
        return new CheckedTraceStatement(new PressureStatment(connection.createStatement(), this.connection, this.dbConnectionKey, this.dbType), this.url, this.username, this.dbType, true, sqlMetaData);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    @Override
    public Statement createStatement(int arg0, int arg1) throws SQLException {
        return new CheckedTraceStatement(new PressureStatment(connection.createStatement(arg0, arg1), this.connection, this.dbConnectionKey, this.dbType), this.url, this.username, this.dbType, true, sqlMetaData);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    @Override
    public Statement createStatement(int arg0, int arg1, int arg2)
            throws SQLException {
        return new CheckedTraceStatement(new PressureStatment(connection.createStatement(arg0, arg1, arg2), this.connection, this.dbConnectionKey, this.dbType), this.url, this.username, this.dbType, true, sqlMetaData);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    @Override
    public Struct createStruct(String typeName, Object[] arg1) throws SQLException {
        return connection.createStruct(typeName, arg1);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    @Override
    public boolean getAutoCommit() throws SQLException {
        return connection.getAutoCommit();
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    @Override
    public String getCatalog() throws SQLException {
        return connection.getCatalog();
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    @Override
    public Properties getClientInfo() throws SQLException {
        return connection.getClientInfo();
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    @Override
    public String getClientInfo(String arg0) throws SQLException {
        return connection.getClientInfo(arg0);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    @Override
    public int getHoldability() throws SQLException {
        return connection.getHoldability();
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
        return connection.getMetaData();
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public int getNetworkTimeout() throws SQLException {
        try {
            return connection.getNetworkTimeout();
        } catch (AbstractMethodError e) {
        } catch (NoSuchMethodError e) {
        }
        return 0;
    }


    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    @Override
    public int getTransactionIsolation() throws SQLException {
        return connection.getTransactionIsolation();
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public String getSchema() throws SQLException {
        try {
            return connection.getSchema();
        } catch (AbstractMethodError e) {
        } catch (NoSuchMethodError e) {
        }

        String schema = "";
        for (Map.Entry<String, ShadowDatabaseConfig> entry : GlobalConfig.getInstance().getShadowDatasourceConfigs().entrySet()) {
            String schemaOriginal = entry.getValue().getSchema();
            if (schemaOriginal == null) {
                continue;
            }
            if (StringUtils.equalsIgnoreCase(schema, schemaOriginal)) {
                return StringUtils.upperCase(entry.getValue().getShadowSchema());
            }
        }
        return schema;
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    @Override
    public String nativeSQL(String arg0) throws SQLException {
        arg0 = SqlParser.replaceSchema(arg0, this.dbConnectionKey, this.dbType);
        return connection.nativeSQL(arg0);
    }


    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    @Override
    public CallableStatement prepareCall(String arg0) throws SQLException {
        return new CheckedTraceCallableStatement(new PressureCallableStatement(connection.prepareCall(arg0), this.connection, this.dbConnectionKey, this.dbType), arg0, this.url, this.username, this.dbType, true, sqlMetaData);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    @Override
    public CallableStatement prepareCall(String arg0, int arg1, int arg2)
            throws SQLException {
        return new CheckedTraceCallableStatement(new PressureCallableStatement(connection.prepareCall(arg0, arg1, arg2), this.connection, this.dbConnectionKey, this.dbType), arg0, this.url, this.username, this.dbType, true, sqlMetaData);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    @Override
    public CallableStatement prepareCall(String arg0, int arg1, int arg2,
                                         int arg3) throws SQLException {
        return new CheckedTraceCallableStatement(new PressureCallableStatement(connection.prepareCall(arg0, arg1, arg2, arg3), this.connection, this.dbConnectionKey, this.dbType), arg0, this.url, this.username, this.dbType, true, sqlMetaData);
    }


    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    @Override
    public PreparedStatement prepareStatement(String arg0) throws SQLException {
        arg0 = SqlParser.replaceSchema(arg0, this.dbConnectionKey, this.dbType);
        return new CheckedTracePreparedStatement(new PressurePreparedStatement(connection.prepareStatement(arg0), this.connection, this.dbConnectionKey, this.dbType), arg0, this.url, this.username, this.dbType, true, sqlMetaData);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    @Override
    public PreparedStatement prepareStatement(String arg0, int arg1)
            throws SQLException {
        arg0 = SqlParser.replaceSchema(arg0, this.dbConnectionKey, this.dbType);
        return new CheckedTracePreparedStatement(new PressurePreparedStatement(connection.prepareStatement(arg0, arg1), this.connection, this.dbConnectionKey, this.dbType), arg0, this.url, this.username, this.dbType, true, sqlMetaData);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    @Override
    public PreparedStatement prepareStatement(String arg0, int[] arg1)
            throws SQLException {
        arg0 = SqlParser.replaceSchema(arg0, this.dbConnectionKey, this.dbType);
        return new CheckedTracePreparedStatement(new PressurePreparedStatement(connection.prepareStatement(arg0, arg1), this.connection, this.dbConnectionKey, this.dbType), arg0, this.url, this.username, this.dbType, true, sqlMetaData);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    @Override
    public PreparedStatement prepareStatement(String arg0, String[] arg1)
            throws SQLException {
        arg0 = SqlParser.replaceSchema(arg0, this.dbConnectionKey, this.dbType);
        return new CheckedTracePreparedStatement(new PressurePreparedStatement(connection.prepareStatement(arg0, arg1), this.connection, this.dbConnectionKey, this.dbType), arg0, this.url, this.username, this.dbType, true, sqlMetaData);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    @Override
    public PreparedStatement prepareStatement(String arg0, int arg1, int arg2)
            throws SQLException {
        arg0 = SqlParser.replaceSchema(arg0, this.dbConnectionKey, this.dbType);
        return new CheckedTracePreparedStatement(new PressurePreparedStatement(connection.prepareStatement(arg0, arg1, arg2), this.connection, this.dbConnectionKey, this.dbType), arg0, this.url, this.username, this.dbType, true, sqlMetaData);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    @Override
    public PreparedStatement prepareStatement(String arg0, int arg1, int arg2,
                                              int arg3) throws SQLException {
        arg0 = SqlParser.replaceSchema(arg0, this.dbConnectionKey, this.dbType);
        return new CheckedTracePreparedStatement(new PressurePreparedStatement(connection.prepareStatement(arg0, arg1, arg2, arg3), this.connection, this.dbConnectionKey, this.dbType), arg0, this.url, this.username, this.dbType, true, sqlMetaData);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public void setSchema(String arg0) throws SQLException {
        arg0 = SqlParser.replaceSchema(arg0, this.dbConnectionKey, this.dbType);
        try {
            connection.setSchema(arg0);
        } catch (AbstractMethodError e) {
        } catch (NoSuchMethodError e) {
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
    public Map<String, Class<?>> getTypeMap() throws SQLException {
        return connection.getTypeMap();
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    @Override
    public SQLWarning getWarnings() throws SQLException {
        return connection.getWarnings();
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    @Override
    public boolean isClosed() throws SQLException {
        return connection.isClosed();
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    @Override
    public boolean isReadOnly() throws SQLException {
        return connection.isReadOnly();
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    @Override
    public boolean isValid(int arg0) throws SQLException {
        return connection.isValid(arg0);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    @Override
    public void releaseSavepoint(Savepoint arg0) throws SQLException {
        connection.releaseSavepoint(arg0);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    @Override
    public void rollback() throws SQLException {
        connection.rollback();
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    @Override
    public void rollback(Savepoint arg0) throws SQLException {
        connection.rollback(arg0);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    @Override
    public void setAutoCommit(boolean arg0) throws SQLException {
        connection.setAutoCommit(arg0);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    @Override
    public void setCatalog(String arg0) throws SQLException {
        connection.setCatalog(arg0);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    @Override
    public void setClientInfo(Properties arg0) throws SQLClientInfoException {
        connection.setClientInfo(arg0);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    @Override
    public void setClientInfo(String arg0, String arg1)
            throws SQLClientInfoException {
        connection.setClientInfo(arg0, arg1);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    @Override
    public void setHoldability(int arg0) throws SQLException {
        connection.setHoldability(arg0);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public void setNetworkTimeout(Executor arg0, int arg1) throws SQLException {
        try {
            connection.setNetworkTimeout(arg0, arg1);
        } catch (AbstractMethodError e) {
        } catch (NoSuchMethodError e) {
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
    public void setReadOnly(boolean arg0) throws SQLException {
        connection.setReadOnly(arg0);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    @Override
    public Savepoint setSavepoint() throws SQLException {
        return connection.setSavepoint();
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    @Override
    public Savepoint setSavepoint(String arg0) throws SQLException {
        return connection.setSavepoint(arg0);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    @Override
    public void setTransactionIsolation(int arg0) throws SQLException {
        connection.setTransactionIsolation(arg0);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    @Override
    public void setTypeMap(Map<String, Class<?>> arg0) throws SQLException {
        connection.setTypeMap(arg0);
    }
}
