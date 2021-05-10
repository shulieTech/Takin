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
package com.pamirs.attach.plugin.common.datasource.biz;

import com.pamirs.attach.plugin.common.datasource.trace.CheckedTraceCallableStatement;
import com.pamirs.attach.plugin.common.datasource.trace.CheckedTracePreparedStatement;
import com.pamirs.attach.plugin.common.datasource.trace.CheckedTraceStatement;
import com.pamirs.pradar.pressurement.datasource.util.DbType;
import com.pamirs.pradar.pressurement.datasource.util.SqlMetaData;

import java.sql.*;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

/**
 * @Description 业务连接的包装连接，会在此类中进行兜底检查
 * @Author xiaobin.zfb
 * @mail xiaobin@shulie.io
 * @Date 2020/8/17 10:44 上午
 */
public class BizConnection implements Connection {
    protected Connection target;
    protected String url;
    protected String username;
    protected String dbType;
    protected SqlMetaData sqlMetaData;

    public BizConnection(Connection target, String url, String username, String dbType) {
        this.target = target;
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
    public Statement createStatement() throws SQLException {
        return new CheckedTraceStatement(new BizStatement(target.createStatement()), this.url, this.username, this.dbType, false, sqlMetaData);
    }

    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return new CheckedTracePreparedStatement(new BizPreparedStatement(target.prepareStatement(sql)), sql, this.url, this.username, this.dbType, false, sqlMetaData);
    }

    @Override
    public CallableStatement prepareCall(String sql) throws SQLException {
        return new CheckedTraceCallableStatement(new BizCallableStatement(target.prepareCall(sql)), sql, this.url, this.username, this.dbType, false, sqlMetaData);
    }

    @Override
    public String nativeSQL(String sql) throws SQLException {
        return target.nativeSQL(sql);
    }

    @Override
    public void setAutoCommit(boolean autoCommit) throws SQLException {
        target.setAutoCommit(autoCommit);
    }

    @Override
    public boolean getAutoCommit() throws SQLException {
        return target.getAutoCommit();
    }

    @Override
    public void commit() throws SQLException {
        target.commit();
    }

    @Override
    public void rollback() throws SQLException {
        target.rollback();
    }

    @Override
    public void close() throws SQLException {
        target.close();
    }

    @Override
    public boolean isClosed() throws SQLException {
        return target.isClosed();
    }

    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
        return target.getMetaData();
    }

    @Override
    public void setReadOnly(boolean readOnly) throws SQLException {
        target.setReadOnly(readOnly);
    }

    @Override
    public boolean isReadOnly() throws SQLException {
        return target.isReadOnly();
    }

    @Override
    public void setCatalog(String catalog) throws SQLException {
        target.setCatalog(catalog);
    }

    @Override
    public String getCatalog() throws SQLException {
        return target.getCatalog();
    }

    @Override
    public void setTransactionIsolation(int level) throws SQLException {
        target.setTransactionIsolation(level);
    }

    @Override
    public int getTransactionIsolation() throws SQLException {
        return target.getTransactionIsolation();
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        return target.getWarnings();
    }

    @Override
    public void clearWarnings() throws SQLException {
        target.clearWarnings();
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        return new CheckedTraceStatement(new BizStatement(target.createStatement(resultSetType, resultSetConcurrency)), this.url, this.username, this.dbType, false, sqlMetaData);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        return new CheckedTracePreparedStatement(new BizPreparedStatement(target.prepareStatement(sql, resultSetType, resultSetConcurrency)), sql, this.url, this.username, this.dbType, false, sqlMetaData);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        return new CheckedTraceCallableStatement(new BizCallableStatement(target.prepareCall(sql, resultSetType, resultSetConcurrency)), sql, this.url, this.username, this.dbType, false, sqlMetaData);
    }

    @Override
    public Map<String, Class<?>> getTypeMap() throws SQLException {
        return target.getTypeMap();
    }

    @Override
    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
        target.setTypeMap(map);
    }

    @Override
    public void setHoldability(int holdability) throws SQLException {
        target.setHoldability(holdability);
    }

    @Override
    public int getHoldability() throws SQLException {
        return target.getHoldability();
    }

    @Override
    public Savepoint setSavepoint() throws SQLException {
        return target.setSavepoint();
    }

    @Override
    public Savepoint setSavepoint(String name) throws SQLException {
        return target.setSavepoint(name);
    }

    @Override
    public void rollback(Savepoint savepoint) throws SQLException {
        target.rollback(savepoint);
    }

    @Override
    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        target.releaseSavepoint(savepoint);
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return new CheckedTraceStatement(new BizStatement(target.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability)), this.url, this.username, this.dbType, false, sqlMetaData);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return new CheckedTracePreparedStatement(new BizPreparedStatement(target.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability)), sql, this.url, this.username, this.dbType, false, sqlMetaData);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return new CheckedTraceCallableStatement(new BizCallableStatement(target.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability)), sql, this.url, this.username, this.dbType, false, sqlMetaData);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
        return new CheckedTracePreparedStatement(new BizPreparedStatement(target.prepareStatement(sql, autoGeneratedKeys)), sql, this.url, this.username, this.dbType, false, sqlMetaData);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
        return new CheckedTracePreparedStatement(new BizPreparedStatement(target.prepareStatement(sql, columnIndexes)), sql, this.url, this.username, this.dbType, false, sqlMetaData);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
        return new CheckedTracePreparedStatement(new BizPreparedStatement(target.prepareStatement(sql, columnNames)), sql, this.url, this.username, this.dbType, false, sqlMetaData);
    }

    @Override
    public Clob createClob() throws SQLException {
        return target.createClob();
    }

    @Override
    public Blob createBlob() throws SQLException {
        return target.createBlob();
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
    public boolean isValid(int timeout) throws SQLException {
        return target.isValid(timeout);
    }

    @Override
    public void setClientInfo(String name, String value) throws SQLClientInfoException {
        target.setClientInfo(name, value);
    }

    @Override
    public void setClientInfo(Properties properties) throws SQLClientInfoException {
        target.setClientInfo(properties);
    }

    @Override
    public String getClientInfo(String name) throws SQLException {
        return target.getClientInfo(name);
    }

    @Override
    public Properties getClientInfo() throws SQLException {
        return target.getClientInfo();
    }

    @Override
    public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
        return target.createArrayOf(typeName, elements);
    }

    @Override
    public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
        return target.createStruct(typeName, attributes);
    }

    public void setSchema(String schema) throws SQLException {
        try {
            target.setSchema(schema);
        } catch (AbstractMethodError e) {
        } catch (NoSuchMethodError e) {
        }
    }

    public String getSchema() throws SQLException {
        try {
            return target.getSchema();
        } catch (AbstractMethodError e) {
        } catch (NoSuchMethodError e) {
        }
        return null;
    }

    public void abort(Executor executor) throws SQLException {
        try {
            target.abort(executor);
        } catch (AbstractMethodError e) {
        } catch (NoSuchMethodError e) {
        }
    }

    public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
        try {
            target.setNetworkTimeout(executor, milliseconds);
        } catch (AbstractMethodError e) {
        } catch (NoSuchMethodError e) {
        }
    }

    public int getNetworkTimeout() throws SQLException {
        try {
            return target.getNetworkTimeout();
        } catch (AbstractMethodError e) {
        } catch (NoSuchMethodError e) {
        }
        return 0;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        try {
            return target.unwrap(iface);
        } catch (AbstractMethodError e) {
        } catch (NoSuchMethodError e) {
        }
        return (T) this;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        try {
            return target.isWrapperFor(iface);
        } catch (AbstractMethodError e) {
        } catch (NoSuchMethodError e) {
        }
        return false;
    }
}
