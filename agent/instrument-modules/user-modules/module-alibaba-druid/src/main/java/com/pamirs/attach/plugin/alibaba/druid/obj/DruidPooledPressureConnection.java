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
package com.pamirs.attach.plugin.alibaba.druid.obj;

import com.alibaba.druid.pool.DruidConnectionHolder;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.alibaba.druid.pool.DruidPooledPreparedStatement;
import com.alibaba.druid.proxy.jdbc.TransactionInfo;
import com.pamirs.attach.plugin.common.datasource.pressure.PressureCallableStatement;
import com.pamirs.attach.plugin.common.datasource.pressure.PressurePreparedStatement;
import com.pamirs.attach.plugin.common.datasource.pressure.PressureStatment;
import com.pamirs.attach.plugin.common.datasource.trace.CheckedTraceCallableStatement;
import com.pamirs.attach.plugin.common.datasource.trace.CheckedTracePreparedStatement;
import com.pamirs.attach.plugin.common.datasource.trace.CheckedTraceStatement;
import com.pamirs.pradar.internal.config.ShadowDatabaseConfig;
import com.pamirs.pradar.pressurement.agent.shared.service.GlobalConfig;
import com.pamirs.pradar.pressurement.datasource.SqlParser;
import com.pamirs.pradar.pressurement.datasource.util.DbType;
import com.pamirs.pradar.pressurement.datasource.util.SqlMetaData;
import com.shulie.instrument.simulator.api.reflect.Reflect;
import com.shulie.instrument.simulator.api.reflect.ReflectException;
import org.apache.commons.lang.StringUtils;

import javax.sql.ConnectionEventListener;
import javax.sql.StatementEventListener;
import java.sql.*;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

/**
 * Create by xuyh at 2020/3/22 15:00.
 */
public class DruidPooledPressureConnection extends DruidPooledConnection {

    private String dbConnectionKey;

    private String url;
    private String username;
    private String dbType;
    private SqlMetaData sqlMetaData;
    private DruidPooledConnection target;

    public DruidPooledPressureConnection(DruidPooledConnection connection, String dbConnectionKey, String url, String username, String dbType) {
        super(connection.getConnectionHolder());
        this.target = connection;
        this.dbConnectionKey = dbConnectionKey;
        this.url = url;
        this.username = username;
        this.dbType = dbType;
        DbType type = DbType.nameOf(dbType);
        if (type != null) {
            try {
                this.sqlMetaData = type.sqlMetaData(url);
            } catch (Throwable e) {
            }
        }
    }

    @Override
    public boolean isWrapperFor(Class<?> arg0) throws SQLException {
        return target.isWrapperFor(arg0);
    }

    @Override
    public <T> T unwrap(Class<T> arg0) throws SQLException {
        return target.unwrap(arg0);
    }

    @Override
    public void abort(Executor arg0) throws SQLException {
        try {
            target.abort(arg0);
        } catch (AbstractMethodError e) {
        } catch (NoSuchMethodError e) {
        }
    }

    @Override
    public void clearWarnings() throws SQLException {
        target.clearWarnings();
    }

    @Override
    public void close() throws SQLException {
        target.close();
    }

    @Override
    public void commit() throws SQLException {
        target.commit();
    }

    @Override
    public Array createArrayOf(String arg0, Object[] arg1) throws SQLException {
        return target.createArrayOf(arg0, arg1);
    }

    @Override
    public Blob createBlob() throws SQLException {
        return target.createBlob();
    }

    @Override
    public Clob createClob() throws SQLException {
        return target.createClob();
    }

    @Override
    public NClob createNClob() throws SQLException {
        return target.createNClob();
    }

    @Override
    public SQLXML createSQLXML() throws SQLException {
        return target.createSQLXML();
    }

    @Override
    public Statement createStatement() throws SQLException {
        return new CheckedTraceStatement(new PressureStatment(target.createStatement(), this.conn, this.dbConnectionKey, this.dbType), this.url, this.username, this.dbType, true, sqlMetaData);
    }

    @Override
    public Statement createStatement(int arg0, int arg1) throws SQLException {
        return new CheckedTraceStatement(new PressureStatment(target.createStatement(arg0, arg1), this.conn, this.dbConnectionKey, this.dbType), this.url, this.username, this.dbType, true, sqlMetaData);
    }

    @Override
    public Statement createStatement(int arg0, int arg1, int arg2)
            throws SQLException {
        return new CheckedTraceStatement(new PressureStatment(target.createStatement(arg0, arg1, arg2), this.conn, this.dbConnectionKey, this.dbType), this.url, this.username, this.dbType, true, sqlMetaData);
    }

    @Override
    public Struct createStruct(String typeName, Object[] arg1) throws SQLException {
        return target.createStruct(typeName, arg1);
    }

    @Override
    public boolean getAutoCommit() throws SQLException {
        return target.getAutoCommit();
    }

    @Override
    public String getCatalog() throws SQLException {
        return target.getCatalog();
    }

    @Override
    public Properties getClientInfo() throws SQLException {
        return target.getClientInfo();
    }

    @Override
    public String getClientInfo(String arg0) throws SQLException {
        return target.getClientInfo(arg0);
    }

    @Override
    public int getHoldability() throws SQLException {
        return target.getHoldability();
    }

    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
        return target.getMetaData();
    }

    @Override
    public int getNetworkTimeout() throws SQLException {
        try {
            return target.getNetworkTimeout();
        } catch (AbstractMethodError e) {
        } catch (NoSuchMethodError e) {
        }
        return 0;
    }


    @Override
    public int getTransactionIsolation() throws SQLException {
        return target.getTransactionIsolation();
    }

    @Override
    public String getSchema() throws SQLException {
        try {
            return target.getSchema();
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

    @Override
    public String nativeSQL(String arg0) throws SQLException {
        arg0 = SqlParser.replaceSchema(arg0, this.dbConnectionKey, this.dbType);
        return target.nativeSQL(arg0);
    }


    @Override
    public CallableStatement prepareCall(String arg0) throws SQLException {
        return new CheckedTraceCallableStatement(new PressureCallableStatement(target.prepareCall(arg0), this.conn, this.dbConnectionKey, this.dbType), arg0, this.url, this.username, this.dbType, true, sqlMetaData);
    }

    @Override
    public CallableStatement prepareCall(String arg0, int arg1, int arg2)
            throws SQLException {
        return new CheckedTraceCallableStatement(new PressureCallableStatement(target.prepareCall(arg0, arg1, arg2), this.conn, this.dbConnectionKey, this.dbType), arg0, this.url, this.username, this.dbType, true, sqlMetaData);
    }


    @Override
    public CallableStatement prepareCall(String arg0, int arg1, int arg2,
                                         int arg3) throws SQLException {
        return new CheckedTraceCallableStatement(new PressureCallableStatement(target.prepareCall(arg0, arg1, arg2, arg3), this.conn, this.dbConnectionKey, this.dbType), arg0, this.url, this.username, this.dbType, true, sqlMetaData);
    }


    @Override
    public PreparedStatement prepareStatement(String arg0) throws SQLException {
        arg0 = SqlParser.replaceSchema(arg0, this.dbConnectionKey, this.dbType);
        return new CheckedTracePreparedStatement(new PressurePreparedStatement(target.prepareStatement(arg0), this.conn, this.dbConnectionKey, this.dbType), arg0, this.url, this.username, this.dbType, true, sqlMetaData);
    }

    @Override
    public PreparedStatement prepareStatement(String arg0, int arg1)
            throws SQLException {
        arg0 = SqlParser.replaceSchema(arg0, this.dbConnectionKey, this.dbType);
        return new CheckedTracePreparedStatement(new PressurePreparedStatement(target.prepareStatement(arg0, arg1), this.conn, this.dbConnectionKey, this.dbType), arg0, this.url, this.username, this.dbType, true, sqlMetaData);
    }

    @Override
    public PreparedStatement prepareStatement(String arg0, int[] arg1)
            throws SQLException {
        arg0 = SqlParser.replaceSchema(arg0, this.dbConnectionKey, this.dbType);
        return new CheckedTracePreparedStatement(new PressurePreparedStatement(target.prepareStatement(arg0, arg1), this.conn, this.dbConnectionKey, this.dbType), arg0, this.url, this.username, this.dbType, true, sqlMetaData);
    }

    @Override
    public PreparedStatement prepareStatement(String arg0, String[] arg1)
            throws SQLException {
        arg0 = SqlParser.replaceSchema(arg0, this.dbConnectionKey, this.dbType);
        return new CheckedTracePreparedStatement(new PressurePreparedStatement(target.prepareStatement(arg0, arg1), this.conn, this.dbConnectionKey, this.dbType), arg0, this.url, this.username, this.dbType, true, sqlMetaData);
    }

    @Override
    public PreparedStatement prepareStatement(String arg0, int arg1, int arg2)
            throws SQLException {
        arg0 = SqlParser.replaceSchema(arg0, this.dbConnectionKey, this.dbType);
        return new CheckedTracePreparedStatement(new PressurePreparedStatement(target.prepareStatement(arg0, arg1, arg2), this.conn, this.dbConnectionKey, this.dbType), arg0, this.url, this.username, this.dbType, true, sqlMetaData);
    }

    @Override
    public PreparedStatement prepareStatement(String arg0, int arg1, int arg2,
                                              int arg3) throws SQLException {
        arg0 = SqlParser.replaceSchema(arg0, this.dbConnectionKey, this.dbType);
        return new CheckedTracePreparedStatement(new PressurePreparedStatement(target.prepareStatement(arg0, arg1, arg2, arg3), this.conn, this.dbConnectionKey, this.dbType), arg0, this.url, this.username, this.dbType, true, sqlMetaData);
    }

    @Override
    public void setSchema(String arg0) throws SQLException {
        arg0 = SqlParser.replaceSchema(arg0, this.dbConnectionKey, this.dbType);
        try {
            target.setSchema(arg0);
        } catch (AbstractMethodError e) {
        } catch (NoSuchMethodError e) {
        }
    }

    @Override
    public Map<String, Class<?>> getTypeMap() throws SQLException {
        return target.getTypeMap();
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        return target.getWarnings();
    }

    @Override
    public boolean isClosed() throws SQLException {
        return target.isClosed();
    }

    @Override
    public boolean isReadOnly() throws SQLException {
        return target.isReadOnly();
    }

    @Override
    public boolean isValid(int arg0) throws SQLException {
        return target.isValid(arg0);
    }

    @Override
    public void releaseSavepoint(Savepoint arg0) throws SQLException {
        target.releaseSavepoint(arg0);
    }

    @Override
    public void rollback() throws SQLException {
        target.rollback();
    }

    @Override
    public void rollback(Savepoint arg0) throws SQLException {
        target.rollback(arg0);
    }

    @Override
    public void setAutoCommit(boolean arg0) throws SQLException {
        target.setAutoCommit(arg0);
    }

    @Override
    public void setCatalog(String arg0) throws SQLException {
        target.setCatalog(arg0);
    }

    @Override
    public void setClientInfo(Properties arg0) throws SQLClientInfoException {
        target.setClientInfo(arg0);
    }

    @Override
    public void setClientInfo(String arg0, String arg1)
            throws SQLClientInfoException {
        target.setClientInfo(arg0, arg1);
    }

    @Override
    public void setHoldability(int arg0) throws SQLException {
        target.setHoldability(arg0);
    }

    @Override
    public void setNetworkTimeout(Executor arg0, int arg1) throws SQLException {

        try {
            target.setNetworkTimeout(arg0, arg1);
        } catch (NoSuchMethodError e) {
        } catch (AbstractMethodError e) {
        }
    }

    @Override
    public void setReadOnly(boolean arg0) throws SQLException {
        target.setReadOnly(arg0);
    }

    @Override
    public Savepoint setSavepoint() throws SQLException {
        return target.setSavepoint();
    }

    @Override
    public Savepoint setSavepoint(String arg0) throws SQLException {
        return target.setSavepoint(arg0);
    }

    @Override
    public void setTransactionIsolation(int arg0) throws SQLException {
        target.setTransactionIsolation(arg0);
    }

    @Override
    public void setTypeMap(Map<String, Class<?>> arg0) throws SQLException {
        target.setTypeMap(arg0);
    }

    @Override
    public void recycle() throws SQLException {
        target.recycle();
    }

    @Override
    public Thread getOwnerThread() {
        return target.getOwnerThread();
    }

    @Override
    public StackTraceElement[] getConnectStackTrace() {
        return target.getConnectStackTrace();
    }

    @Override
    public void setConnectStackTrace(StackTraceElement[] connectStackTrace) {
        target.setConnectStackTrace(connectStackTrace);
    }

    @Override
    public long getConnectedTimeNano() {
        return target.getConnectedTimeNano();
    }

    @Override
    public void setConnectedTimeNano() {
        target.setConnectedTimeNano();
    }

    @Override
    public void setConnectedTimeNano(long connectedTimeNano) {
        target.setConnectedTimeNano(connectedTimeNano);
    }

    @Override
    public boolean isTraceEnable() {
        return target.isTraceEnable();
    }

    @Override
    public void setTraceEnable(boolean traceEnable) {
        target.setTraceEnable(traceEnable);
    }

    @Override
    public SQLException handleException(Throwable t) throws SQLException {
        return target.handleException(t);
    }

    @Override
    public boolean isOracle() {
        return target.isOracle();
    }

    @Override
    public void closePoolableStatement(DruidPooledPreparedStatement stmt) throws SQLException {
        target.closePoolableStatement(stmt);
    }

    @Override
    public DruidConnectionHolder getConnectionHolder() {
        return target.getConnectionHolder();
    }

    @Override
    public Connection getConnection() {
        return target.getConnection();
    }

    @Override
    public void disable() {
        target.disable();
    }

    @Override
    public void disable(Throwable error) {
        target.disable(error);
    }

    @Override
    public boolean isDisable() {
        return target.isDisable();
    }

    @Override
    public synchronized void syncClose() throws SQLException {
        target.syncClose();
    }

    @Override
    public void transactionRecord(String sql) throws SQLException {
        try {
            Reflect.on(target).call("transactionRecord", sql).get();
        } catch (ReflectException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public TransactionInfo getTransactionInfo() {
        return target.getTransactionInfo();
    }

    @Override
    public boolean isAbandonded() {
        return target.isAbandonded();
    }

    @Override
    public void addConnectionEventListener(ConnectionEventListener listener) {
        target.addConnectionEventListener(listener);
    }

    @Override
    public void removeConnectionEventListener(ConnectionEventListener listener) {
        target.removeConnectionEventListener(listener);
    }

    @Override
    public void addStatementEventListener(StatementEventListener listener) {
        target.addStatementEventListener(listener);
    }

    @Override
    public void removeStatementEventListener(StatementEventListener listener) {
        target.removeStatementEventListener(listener);
    }

    @Override
    public Throwable getDisableError() {
        return target.getDisableError();
    }

    @Override
    public void checkState() throws SQLException {
        target.checkState();
    }

    @Override
    public void abandond() {
        target.abandond();
    }
}
