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
package com.pamirs.attach.plugin.common.datasource.trace;


import com.pamirs.pradar.MiddlewareType;
import com.pamirs.pradar.Pradar;
import com.pamirs.pradar.ResultCode;
import com.pamirs.pradar.interceptor.InterceptorInvokerHelper;
import com.pamirs.pradar.pressurement.datasource.util.SqlMetaData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Calendar;

/**
 * 带压测检测功能的可追踪的 PreparedStatement
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/12/22 10:26 上午
 */
public class CheckedTracePreparedStatement extends CheckedTraceStatement implements PreparedStatement {
    protected final static Logger LOGGER = LoggerFactory.getLogger(CheckedTracePreparedStatement.class.getName());
    protected PreparedStatement targetStatement;

    public CheckedTracePreparedStatement(PreparedStatement preparedStatement, String sql, String url, String username, String dbType, boolean isPressureConnection, SqlMetaData sqlMetaData) {
        this(preparedStatement, sql, url, username, dbType, isPressureConnection, true, sqlMetaData);
    }

    public CheckedTracePreparedStatement(PreparedStatement preparedStatement, String sql, String url, String username, String dbType, boolean isPressureConnection, boolean isChecked, SqlMetaData sqlMetaData) {
        super(preparedStatement, url, username, dbType, isPressureConnection, isChecked, sqlMetaData);
        this.targetStatement = preparedStatement;
        if (sql != null) {
            this.sqlMetaData.setSql(sql);
        }
    }

    @Override
    public void addBatch() throws SQLException {
        targetStatement.addBatch();
    }

    @Override
    public void clearParameters() throws SQLException {
        targetStatement.clearParameters();
    }

    private boolean isPradarPrepareStatement() {
        try {
            return targetStatement instanceof CheckedTracePreparedStatement;
        } catch (Throwable e) {
            InterceptorInvokerHelper.handleException(e);
            return true;
        }
    }

    @Override
    public boolean execute() throws SQLException {
        if (isPradarPrepareStatement()) {
            return (targetStatement).execute();
        }
        boolean isStartSuccess = false;
        try {
            isStartSuccess = PradarHelper.startRpc(sqlMetaData);
        } catch (Throwable e) {
            LOGGER.error("[jdbc] startRpc err!", e);
        }
        String traceId = Pradar.getTraceId();
        String rpcId = Pradar.getInvokeId();
        int logType = Pradar.getLogType();
        recordDebugFlow(traceId, rpcId, logType, sqlMetaData.getParameters(), null, "executeFirst");
        boolean isException = false;
        Object ex = null;
        try {
            boolean result = targetStatement.execute();
            ex = true;
            return result;
        } catch (SQLException e) {
            ex = e;
            isException = true;
            throw e;
        } catch (Throwable e) {
            ex = e;
            isException = true;
            throw new SQLException(e);
        } finally {
            try {
                if (isStartSuccess) {
                    PradarHelper.endRpc(sqlMetaData, ex);
                }
            } catch (Throwable e) {
                LOGGER.error("[jdbc] endRpc err!", e);
                if (isStartSuccess) {
                    Pradar.endClientInvoke(isException ? ResultCode.INVOKE_RESULT_FAILED : ResultCode.INVOKE_RESULT_SUCCESS, MiddlewareType.TYPE_DB);
                }
            }
            recordDebugFlow(traceId, rpcId, logType, sqlMetaData.getParameters(), ex, "executeLast");
        }
    }

    @Override
    public ResultSet executeQuery() throws SQLException {
        if (isPradarPrepareStatement()) {
            return targetStatement.executeQuery();
        }
        boolean isStartSuccess = false;
        try {
            isStartSuccess = PradarHelper.startRpc(sqlMetaData);
        } catch (Throwable e) {
            LOGGER.error("[jdbc] startRpc err!", e);
        }
        String traceId = Pradar.getTraceId();
        String rpcId = Pradar.getInvokeId();
        int logType = Pradar.getLogType();
        recordDebugFlow(traceId, rpcId, logType, sqlMetaData.getParameters(), null, "executeQueryFirst");
        boolean isException = false;
        Object ex = null;
        try {
            ResultSet resultSet = targetStatement.executeQuery();
            ex = true;
            return resultSet;
        } catch (SQLException e) {
            ex = e;
            isException = true;
            throw e;
        } catch (Throwable e) {
            ex = e;
            isException = true;
            throw new SQLException(e);
        } finally {
            try {
                if (isStartSuccess) {
                    PradarHelper.endRpc(sqlMetaData, ex);
                }
            } catch (Throwable e) {
                LOGGER.error("[jdbc] endRpc err!", e);
                if (isStartSuccess) {
                    Pradar.endClientInvoke(isException ? ResultCode.INVOKE_RESULT_FAILED : ResultCode.INVOKE_RESULT_SUCCESS, MiddlewareType.TYPE_DB);
                }
            }
            recordDebugFlow(traceId, rpcId, logType, sqlMetaData.getParameters(), ex, "executeQueryLast");
        }
    }

    @Override
    public int executeUpdate() throws SQLException {
        if (isPradarPrepareStatement()) {
            return targetStatement.executeUpdate();
        }
        boolean isStartSuccess = false;
        try {
            isStartSuccess = PradarHelper.startRpc(sqlMetaData);
        } catch (Throwable e) {
            LOGGER.error("[jdbc] startRpc err!", e);
        }
        String traceId = Pradar.getTraceId();
        String rpcId = Pradar.getInvokeId();
        int logType = Pradar.getLogType();
        recordDebugFlow(traceId, rpcId, logType, sqlMetaData.getParameters(), null, "executeUpdateFirst");
        boolean isException = false;
        Object ex = null;
        try {
            int result = targetStatement.executeUpdate();
            ex = result;
            return result;
        } catch (SQLException e) {
            ex = e;
            isException = true;
            throw e;
        } catch (Throwable e) {
            ex = e;
            isException = true;
            throw new SQLException(e);
        } finally {
            try {
                if (isStartSuccess) {
                    PradarHelper.endRpc(sqlMetaData, ex);
                }
            } catch (Throwable e) {
                LOGGER.error("[jdbc] endRpc err!", e);
                if (isStartSuccess) {
                    Pradar.endClientInvoke(isException ? ResultCode.INVOKE_RESULT_FAILED : ResultCode.INVOKE_RESULT_SUCCESS, MiddlewareType.TYPE_DB);
                }
            }
            recordDebugFlow(traceId, rpcId, logType, sqlMetaData.getParameters(), ex, "executeUpdateLast");
        }
    }

    @Override
    public ResultSetMetaData getMetaData() throws SQLException {
        // 这里直接返回元数据
        return targetStatement.getMetaData();
    }

    @Override
    public ParameterMetaData getParameterMetaData() throws SQLException {
        // 这里直接返回原数据
        return targetStatement.getParameterMetaData();
    }

    @Override
    public void setArray(int parameterIndex, Array x) throws SQLException {

        targetStatement.setArray(parameterIndex, x);
        try {
            SqlTraceMetaData.checkAndAddParameter(sqlMetaData, parameterIndex, x);
        } catch (Throwable e) {
            InterceptorInvokerHelper.handleException(e);
        }
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException {
        targetStatement.setAsciiStream(parameterIndex, x, length);
        try {
            SqlTraceMetaData.checkAndAddParameter(sqlMetaData, parameterIndex, x);
        } catch (Throwable e) {
            InterceptorInvokerHelper.handleException(e);
        }
    }

    @Override
    public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException {
        targetStatement.setBigDecimal(parameterIndex, x);
        try {
            SqlTraceMetaData.checkAndAddParameter(sqlMetaData, parameterIndex, x);
        } catch (Throwable e) {
            InterceptorInvokerHelper.handleException(e);
        }
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException {
        targetStatement.setBinaryStream(parameterIndex, x, length);
        try {
            SqlTraceMetaData.checkAndAddParameter(sqlMetaData, parameterIndex, "BinaryStream");
        } catch (Throwable e) {
            InterceptorInvokerHelper.handleException(e);
        }
    }

    @Override
    public void setBlob(int i, Blob x) throws SQLException {
        targetStatement.setBlob(i, x);
        try {
            SqlTraceMetaData.checkAndAddParameter(sqlMetaData, i, "Blob");
        } catch (Throwable e) {
            InterceptorInvokerHelper.handleException(e);
        }
    }

    @Override
    public void setBoolean(int parameterIndex, boolean x) throws SQLException {
        targetStatement.setBoolean(parameterIndex, x);
        try {
            SqlTraceMetaData.checkAndAddParameter(sqlMetaData, parameterIndex, x);
        } catch (Throwable e) {
            InterceptorInvokerHelper.handleException(e);
        }
    }

    @Override
    public void setByte(int parameterIndex, byte x) throws SQLException {
        targetStatement.setByte(parameterIndex, x);
        try {
            SqlTraceMetaData.checkAndAddParameter(sqlMetaData, parameterIndex, x);
        } catch (Throwable e) {
            InterceptorInvokerHelper.handleException(e);
        }
    }

    @Override
    public void setBytes(int parameterIndex, byte[] x) throws SQLException {
        targetStatement.setBytes(parameterIndex, x);
        try {
            SqlTraceMetaData.checkAndAddParameter(sqlMetaData, parameterIndex, x);
        } catch (Throwable e) {
            InterceptorInvokerHelper.handleException(e);
        }
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException {
        targetStatement.setCharacterStream(parameterIndex, reader, length);
        try {
            SqlTraceMetaData.checkAndAddParameter(sqlMetaData, parameterIndex, "CharacterStream");
        } catch (Throwable e) {
            InterceptorInvokerHelper.handleException(e);
        }
    }

    @Override
    public void setClob(int i, Clob x) throws SQLException {
        targetStatement.setClob(i, x);
        try {
            SqlTraceMetaData.checkAndAddParameter(sqlMetaData, i, "Clob");
        } catch (Throwable e) {
            InterceptorInvokerHelper.handleException(e);
        }
    }

    @Override
    public void setDate(int parameterIndex, Date x) throws SQLException {
        targetStatement.setDate(parameterIndex, x);
        try {
            SqlTraceMetaData.checkAndAddParameter(sqlMetaData, parameterIndex, x.toGMTString());
        } catch (Throwable e) {
            InterceptorInvokerHelper.handleException(e);
        }
    }

    @Override
    public void setDate(int parameterIndex, Date x, Calendar cal) throws SQLException {
        targetStatement.setDate(parameterIndex, x, cal);
        try {
            SqlTraceMetaData.checkAndAddParameter(sqlMetaData, parameterIndex, x);
        } catch (Throwable e) {
            InterceptorInvokerHelper.handleException(e);
        }
    }

    @Override
    public void setDouble(int parameterIndex, double x) throws SQLException {
        targetStatement.setDouble(parameterIndex, x);
        try {
            SqlTraceMetaData.checkAndAddParameter(sqlMetaData, parameterIndex, x);
        } catch (Throwable e) {
            InterceptorInvokerHelper.handleException(e);
        }
    }

    @Override
    public void setFloat(int parameterIndex, float x) throws SQLException {
        targetStatement.setFloat(parameterIndex, x);
        try {
            SqlTraceMetaData.checkAndAddParameter(sqlMetaData, parameterIndex, x);
        } catch (Throwable e) {
            InterceptorInvokerHelper.handleException(e);
        }
    }

    @Override
    public void setInt(int parameterIndex, int x) throws SQLException {
        targetStatement.setInt(parameterIndex, x);
        try {
            SqlTraceMetaData.checkAndAddParameter(sqlMetaData, parameterIndex, x);
        } catch (Throwable e) {
            InterceptorInvokerHelper.handleException(e);
        }
    }

    @Override
    public void setLong(int parameterIndex, long x) throws SQLException {
        targetStatement.setLong(parameterIndex, x);
        try {
            SqlTraceMetaData.checkAndAddParameter(sqlMetaData, parameterIndex, x);
        } catch (Throwable e) {
            InterceptorInvokerHelper.handleException(e);
        }
    }

    @Override
    public void setNull(int parameterIndex, int sqlType) throws SQLException {
        targetStatement.setNull(parameterIndex, sqlType);
        try {
            SqlTraceMetaData.checkAndAddParameter(sqlMetaData, parameterIndex, null);
        } catch (Throwable e) {
            InterceptorInvokerHelper.handleException(e);
        }
    }

    @Override
    public void setNull(int parameterIndex, int sqlType, String typeName) throws SQLException {
        targetStatement.setNull(parameterIndex, sqlType, typeName);
        try {
            SqlTraceMetaData.checkAndAddParameter(sqlMetaData, parameterIndex, null);
        } catch (Throwable e) {
            InterceptorInvokerHelper.handleException(e);
        }

    }

    @Override
    public void setObject(int parameterIndex, Object x) throws SQLException {
        targetStatement.setObject(parameterIndex, x);
        try {
            SqlTraceMetaData.checkAndAddParameter(sqlMetaData, parameterIndex, x);
        } catch (Throwable e) {
            InterceptorInvokerHelper.handleException(e);
        }

    }

    @Override
    public void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException {
        targetStatement.setObject(parameterIndex, x, targetSqlType);
        try {
            SqlTraceMetaData.checkAndAddParameter(sqlMetaData, parameterIndex, x);
        } catch (Throwable e) {
            InterceptorInvokerHelper.handleException(e);
        }

    }

    @Override
    public void setObject(int parameterIndex, Object x, int targetSqlType, int scale) throws SQLException {
        targetStatement.setObject(parameterIndex, x, targetSqlType, scale);
        try {
            SqlTraceMetaData.checkAndAddParameter(sqlMetaData, parameterIndex, x);
        } catch (Throwable e) {
            InterceptorInvokerHelper.handleException(e);
        }
    }

    @Override
    public void setRef(int i, Ref x) throws SQLException {
        targetStatement.setRef(i, x);
        try {
            SqlTraceMetaData.checkAndAddParameter(sqlMetaData, i, "Ref");
        } catch (Throwable e) {
            InterceptorInvokerHelper.handleException(e);
        }
    }

    @Override
    public void setShort(int parameterIndex, short x) throws SQLException {
        targetStatement.setShort(parameterIndex, x);
        try {
            SqlTraceMetaData.checkAndAddParameter(sqlMetaData, parameterIndex, x);
        } catch (Throwable e) {
            InterceptorInvokerHelper.handleException(e);
        }
    }

    @Override
    public void setString(int parameterIndex, String x) throws SQLException {
        targetStatement.setString(parameterIndex, x);
        try {
            SqlTraceMetaData.checkAndAddParameter(sqlMetaData, parameterIndex, x);
        } catch (Throwable e) {
            InterceptorInvokerHelper.handleException(e);
        }
    }

    @Override
    public void setTime(int parameterIndex, Time x) throws SQLException {
        targetStatement.setTime(parameterIndex, x);
        try {
            SqlTraceMetaData.checkAndAddParameter(sqlMetaData, parameterIndex, x.toGMTString());
        } catch (Throwable e) {
            InterceptorInvokerHelper.handleException(e);
        }
    }

    @Override
    public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException {
        targetStatement.setTime(parameterIndex, x, cal);
        try {
            SqlTraceMetaData.checkAndAddParameter(sqlMetaData, parameterIndex, x.toGMTString() + " " + cal.toString());
        } catch (Throwable e) {
            InterceptorInvokerHelper.handleException(e);
        }
    }

    @Override
    public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException {
        targetStatement.setTimestamp(parameterIndex, x);
        try {
            SqlTraceMetaData.checkAndAddParameter(sqlMetaData, parameterIndex, x.toGMTString());
        } catch (Throwable e) {
            InterceptorInvokerHelper.handleException(e);
        }
    }

    @Override
    public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException {
        targetStatement.setTimestamp(parameterIndex, x, cal);
        try {
            SqlTraceMetaData.checkAndAddParameter(sqlMetaData, parameterIndex, x.toGMTString() + " " + cal.toString());
        } catch (Throwable e) {
            InterceptorInvokerHelper.handleException(e);
        }
    }

    @Override
    public void setURL(int parameterIndex, URL x) throws SQLException {
        targetStatement.setURL(parameterIndex, x);
        try {
            SqlTraceMetaData.checkAndAddParameter(sqlMetaData, parameterIndex, x.toExternalForm());
        } catch (Throwable e) {
            InterceptorInvokerHelper.handleException(e);
        }
    }

    @Override
    @Deprecated
    public void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException {
        targetStatement.setUnicodeStream(parameterIndex, x, length);
        try {
            SqlTraceMetaData.checkAndAddParameter(sqlMetaData, parameterIndex, "UnicodeStream");
        } catch (Throwable e) {
            InterceptorInvokerHelper.handleException(e);
        }
    }

    @Override
    public void setRowId(int parameterIndex, RowId x) throws SQLException {
        targetStatement.setRowId(parameterIndex, x);
        try {
            SqlTraceMetaData.checkAndAddParameter(sqlMetaData, parameterIndex, "RowId");
        } catch (Throwable e) {
            InterceptorInvokerHelper.handleException(e);
        }
    }

    @Override
    public void setNString(int parameterIndex, String value) throws SQLException {
        targetStatement.setNString(parameterIndex, value);
        try {
            SqlTraceMetaData.checkAndAddParameter(sqlMetaData, parameterIndex, value);
        } catch (Throwable e) {
            InterceptorInvokerHelper.handleException(e);
        }
    }

    @Override
    public void setNCharacterStream(int parameterIndex, Reader value, long length) throws SQLException {
        targetStatement.setNCharacterStream(parameterIndex, value, length);
        try {
            SqlTraceMetaData.checkAndAddParameter(sqlMetaData, parameterIndex, "NCharacterStream");
        } catch (Throwable e) {
            InterceptorInvokerHelper.handleException(e);
        }
    }

    @Override
    public void setNClob(int parameterIndex, NClob value) throws SQLException {
        targetStatement.setNClob(parameterIndex, value);
        try {
            SqlTraceMetaData.checkAndAddParameter(sqlMetaData, parameterIndex, "NClob");
        } catch (Throwable e) {
            InterceptorInvokerHelper.handleException(e);
        }
    }

    @Override
    public void setClob(int parameterIndex, Reader reader, long length) throws SQLException {
        targetStatement.setClob(parameterIndex, reader, length);
        try {
            SqlTraceMetaData.checkAndAddParameter(sqlMetaData, parameterIndex, "Clob");
        } catch (Throwable e) {
            InterceptorInvokerHelper.handleException(e);
        }

    }

    @Override
    public void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException {
        targetStatement.setBlob(parameterIndex, inputStream, length);
        try {
            SqlTraceMetaData.checkAndAddParameter(sqlMetaData, parameterIndex, "Blob");
        } catch (Throwable e) {
            InterceptorInvokerHelper.handleException(e);
        }
    }

    @Override
    public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException {
        targetStatement.setNClob(parameterIndex, reader, length);
        try {
            SqlTraceMetaData.checkAndAddParameter(sqlMetaData, parameterIndex, "NClob");
        } catch (Throwable e) {
            InterceptorInvokerHelper.handleException(e);
        }
    }

    @Override
    public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException {
        targetStatement.setSQLXML(parameterIndex, xmlObject);
        try {
            SqlTraceMetaData.checkAndAddParameter(sqlMetaData, parameterIndex, "SQLXML");
        } catch (Throwable e) {
            InterceptorInvokerHelper.handleException(e);
        }
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException {
        targetStatement.setAsciiStream(parameterIndex, x, length);
        try {
            SqlTraceMetaData.checkAndAddParameter(sqlMetaData, parameterIndex, "AsciiStream");
        } catch (Throwable e) {
            InterceptorInvokerHelper.handleException(e);
        }
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException {
        targetStatement.setBinaryStream(parameterIndex, x, length);
        try {
            SqlTraceMetaData.checkAndAddParameter(sqlMetaData, parameterIndex, "BinaryStream");
        } catch (Throwable e) {
            InterceptorInvokerHelper.handleException(e);
        }
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException {
        targetStatement.setCharacterStream(parameterIndex, reader, length);
        try {
            SqlTraceMetaData.checkAndAddParameter(sqlMetaData, parameterIndex, "CharacterStream");
        } catch (Throwable e) {
            InterceptorInvokerHelper.handleException(e);
        }
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {
        targetStatement.setAsciiStream(parameterIndex, x);
        try {
            SqlTraceMetaData.checkAndAddParameter(sqlMetaData, parameterIndex, "AsciiStream");
        } catch (Throwable e) {
            InterceptorInvokerHelper.handleException(e);
        }
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {
        targetStatement.setBinaryStream(parameterIndex, x);
        try {
            SqlTraceMetaData.checkAndAddParameter(sqlMetaData, parameterIndex, "BinaryStream");
        } catch (Throwable e) {
            InterceptorInvokerHelper.handleException(e);
        }
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException {
        targetStatement.setCharacterStream(parameterIndex, reader);
        try {
            SqlTraceMetaData.checkAndAddParameter(sqlMetaData, parameterIndex, "CharacterStream");
        } catch (Throwable e) {
            InterceptorInvokerHelper.handleException(e);
        }
    }

    @Override
    public void setNCharacterStream(int parameterIndex, Reader value) throws SQLException {
        targetStatement.setNCharacterStream(parameterIndex, value);
        try {
            SqlTraceMetaData.checkAndAddParameter(sqlMetaData, parameterIndex, "NCharacterStream");
        } catch (Throwable e) {
            InterceptorInvokerHelper.handleException(e);
        }
    }

    @Override
    public void setClob(int parameterIndex, Reader reader) throws SQLException {
        targetStatement.setClob(parameterIndex, reader);
        try {
            SqlTraceMetaData.checkAndAddParameter(sqlMetaData, parameterIndex, "Clob");
        } catch (Throwable e) {
            InterceptorInvokerHelper.handleException(e);
        }
    }

    @Override
    public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException {
        targetStatement.setBlob(parameterIndex, inputStream);
        try {
            SqlTraceMetaData.checkAndAddParameter(sqlMetaData, parameterIndex, "Blob");
        } catch (Throwable e) {
            InterceptorInvokerHelper.handleException(e);
        }
    }

    @Override
    public void setNClob(int parameterIndex, Reader reader) throws SQLException {
        targetStatement.setNClob(parameterIndex, reader);
        try {
            SqlTraceMetaData.checkAndAddParameter(sqlMetaData, parameterIndex, "NClob");
        } catch (Throwable e) {
            InterceptorInvokerHelper.handleException(e);
        }
    }

    @Override
    public void addBatch(String sql) throws SQLException {
        this.targetStatement.addBatch(sql);
        try {
            sqlMetaData.setSql(sql);
        } catch (Throwable e) {
            InterceptorInvokerHelper.handleException(e);
        }
    }

    @Override
    public void cancel() throws SQLException {
        this.targetStatement.cancel();
    }

    @Override
    public void clearBatch() throws SQLException {
        this.targetStatement.clearBatch();
    }

    @Override
    public void clearWarnings() throws SQLException {
        this.targetStatement.clearWarnings();
    }

    @Override
    public void close() throws SQLException {
        this.targetStatement.close();
        this.sqlMetaData = null;
        this.isClosed = true;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return this.targetStatement.getConnection();
    }

    @Override
    public int getFetchDirection() throws SQLException {
        return this.targetStatement.getFetchDirection();
    }

    @Override
    public int getFetchSize() throws SQLException {
        return this.targetStatement.getFetchSize();
    }

    @Override
    public ResultSet getGeneratedKeys() throws SQLException {
        return this.targetStatement.getGeneratedKeys();
    }

    @Override
    public int getMaxFieldSize() throws SQLException {
        return this.targetStatement.getMaxFieldSize();
    }

    @Override
    public int getMaxRows() throws SQLException {
        return this.targetStatement.getMaxRows();
    }

    @Override
    public boolean getMoreResults() throws SQLException {
        return this.targetStatement.getMoreResults();
    }

    @Override
    public boolean getMoreResults(int current) throws SQLException {
        return this.targetStatement.getMoreResults(current);
    }

    @Override
    public int getQueryTimeout() throws SQLException {
        return this.targetStatement.getQueryTimeout();
    }

    @Override
    public ResultSet getResultSet() throws SQLException {
        return this.targetStatement.getResultSet();
    }

    @Override
    public int getResultSetConcurrency() throws SQLException {
        return this.targetStatement.getResultSetConcurrency();
    }

    @Override
    public int getResultSetHoldability() throws SQLException {
        return this.targetStatement.getResultSetHoldability();
    }

    @Override
    public int getResultSetType() throws SQLException {
        return this.targetStatement.getResultSetType();
    }

    @Override
    public int getUpdateCount() throws SQLException {
        return this.targetStatement.getUpdateCount();
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        return this.targetStatement.getWarnings();
    }

    @Override
    public void setCursorName(String name) throws SQLException {
        this.targetStatement.setCursorName(name);
    }

    @Override
    public void setEscapeProcessing(boolean enable) throws SQLException {
        this.targetStatement.setEscapeProcessing(enable);
    }

    @Override
    public void setFetchDirection(int direction) throws SQLException {
        this.targetStatement.setFetchDirection(direction);
    }

    @Override
    public void setFetchSize(int rows) throws SQLException {
        this.targetStatement.setFetchSize(rows);
    }

    @Override
    public void setMaxFieldSize(int max) throws SQLException {
        this.targetStatement.setMaxFieldSize(max);
    }

    @Override
    public void setMaxRows(int max) throws SQLException {
        this.targetStatement.setMaxRows(max);
    }

    @Override
    public void setQueryTimeout(int seconds) throws SQLException {
        this.targetStatement.setQueryTimeout(seconds);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return iface.isAssignableFrom(this.getClass());
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T unwrap(Class<T> iface) throws SQLException {
        try {
            return targetStatement.unwrap(iface);
        } catch (Throwable e) {
            return (T) this;
        }
    }

    @Override
    public boolean isClosed() throws SQLException {
        try {
            return this.targetStatement.isClosed();
        } catch (AbstractMethodError e) {
        } catch (NoSuchMethodError e) {
        }
        return this.isClosed;
    }

    @Override
    public void setPoolable(boolean poolable) throws SQLException {
        try {
            this.targetStatement.setPoolable(poolable);
        } catch (AbstractMethodError e) {
        } catch (NoSuchMethodError e) {
        }
    }

    @Override
    public boolean isPoolable() throws SQLException {
        try {
            return this.targetStatement.isPoolable();
        } catch (AbstractMethodError e) {
        } catch (NoSuchMethodError e) {
        }
        return false;
    }

    public void closeOnCompletion() throws SQLException {
        try {
            this.targetStatement.closeOnCompletion();
        } catch (AbstractMethodError e) {
        } catch (NoSuchMethodError e) {
        }
    }

    public boolean isCloseOnCompletion() throws SQLException {
        try {
            return this.targetStatement.isCloseOnCompletion();
        } catch (AbstractMethodError e) {
        } catch (NoSuchMethodError e) {
        }
        return false;
    }
}
