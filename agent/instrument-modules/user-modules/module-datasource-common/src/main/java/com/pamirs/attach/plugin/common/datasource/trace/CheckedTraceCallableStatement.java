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

import com.pamirs.pradar.interceptor.InterceptorInvokerHelper;
import com.pamirs.pradar.pressurement.datasource.util.SqlMetaData;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Calendar;
import java.util.Map;

/**
 * 带压测检测功能的可追踪的 PreparedStatement
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/12/22 10:26 上午
 */
public class CheckedTraceCallableStatement extends CheckedTracePreparedStatement implements CallableStatement {
    protected CallableStatement targetStatement;

    public CheckedTraceCallableStatement(CallableStatement callableStatement, String sql, String url, String username, String dbType, boolean isPressureConnection, SqlMetaData sqlMetaData) {
        this(callableStatement,sql,url,username,dbType,isPressureConnection,true,sqlMetaData);
    }

    public CheckedTraceCallableStatement(CallableStatement callableStatement, String sql, String url, String username, String dbType,boolean isPressureConnection,boolean isChecked,SqlMetaData sqlMetaData) {
        super(callableStatement, sql, url, username, dbType, isPressureConnection, isChecked, sqlMetaData);
        this.targetStatement = callableStatement;
        if (this.sqlMetaData != null) {
            this.sqlMetaData.setSql(sql);
        }
    }


    @Override
    public void registerOutParameter(int parameterIndex, int sqlType) throws SQLException {
        targetStatement.registerOutParameter(parameterIndex, sqlType);
    }

    @Override
    public void registerOutParameter(int parameterIndex, int sqlType, int scale) throws SQLException {
        targetStatement.registerOutParameter(parameterIndex, sqlType, scale);
    }

    @Override
    public boolean wasNull() throws SQLException {
        return targetStatement.wasNull();
    }

    @Override
    public String getString(int parameterIndex) throws SQLException {
        return targetStatement.getString(parameterIndex);
    }

    @Override
    public boolean getBoolean(int parameterIndex) throws SQLException {
        return targetStatement.getBoolean(parameterIndex);
    }

    @Override
    public byte getByte(int parameterIndex) throws SQLException {
        return targetStatement.getByte(parameterIndex);
    }

    @Override
    public short getShort(int parameterIndex) throws SQLException {
        return targetStatement.getShort(parameterIndex);
    }

    @Override
    public int getInt(int parameterIndex) throws SQLException {
        return targetStatement.getInt(parameterIndex);
    }

    @Override
    public long getLong(int parameterIndex) throws SQLException {
        return targetStatement.getLong(parameterIndex);
    }

    @Override
    public float getFloat(int parameterIndex) throws SQLException {
        return targetStatement.getFloat(parameterIndex);
    }

    @Override
    public double getDouble(int parameterIndex) throws SQLException {
        return targetStatement.getDouble(parameterIndex);
    }

    @Override
    public BigDecimal getBigDecimal(int parameterIndex, int scale) throws SQLException {
        return targetStatement.getBigDecimal(parameterIndex, scale);
    }

    @Override
    public byte[] getBytes(int parameterIndex) throws SQLException {
        return targetStatement.getBytes(parameterIndex);
    }

    @Override
    public Date getDate(int parameterIndex) throws SQLException {
        return targetStatement.getDate(parameterIndex);
    }

    @Override
    public Time getTime(int parameterIndex) throws SQLException {
        return targetStatement.getTime(parameterIndex);
    }

    @Override
    public Timestamp getTimestamp(int parameterIndex) throws SQLException {
        return targetStatement.getTimestamp(parameterIndex);
    }

    @Override
    public Object getObject(int parameterIndex) throws SQLException {
        return targetStatement.getObject(parameterIndex);
    }

    @Override
    public BigDecimal getBigDecimal(int parameterIndex) throws SQLException {
        return targetStatement.getBigDecimal(parameterIndex);
    }

    @Override
    public Object getObject(int parameterIndex, Map<String, Class<?>> map) throws SQLException {
        return targetStatement.getObject(parameterIndex, map);
    }

    @Override
    public Ref getRef(int parameterIndex) throws SQLException {
        return targetStatement.getRef(parameterIndex);
    }

    @Override
    public Blob getBlob(int parameterIndex) throws SQLException {
        return targetStatement.getBlob(parameterIndex);
    }

    @Override
    public Clob getClob(int parameterIndex) throws SQLException {
        return targetStatement.getClob(parameterIndex);
    }

    @Override
    public Array getArray(int parameterIndex) throws SQLException {
        return targetStatement.getArray(parameterIndex);
    }

    @Override
    public Date getDate(int parameterIndex, Calendar cal) throws SQLException {
        return targetStatement.getDate(parameterIndex, cal);
    }

    @Override
    public Time getTime(int parameterIndex, Calendar cal) throws SQLException {
        return targetStatement.getTime(parameterIndex, cal);
    }

    @Override
    public Timestamp getTimestamp(int parameterIndex, Calendar cal) throws SQLException {
        return targetStatement.getTimestamp(parameterIndex, cal);
    }

    @Override
    public void registerOutParameter(int parameterIndex, int sqlType, String typeName) throws SQLException {
        targetStatement.registerOutParameter(parameterIndex, sqlType, typeName);
    }

    @Override
    public void registerOutParameter(String parameterName, int sqlType) throws SQLException {
        targetStatement.registerOutParameter(parameterName, sqlType);
    }

    @Override
    public void registerOutParameter(String parameterName, int sqlType, int scale) throws SQLException {
        targetStatement.registerOutParameter(parameterName, sqlType, scale);
    }

    @Override
    public void registerOutParameter(String parameterName, int sqlType, String typeName) throws SQLException {
        targetStatement.registerOutParameter(parameterName, sqlType, typeName);
    }

    @Override
    public URL getURL(int parameterIndex) throws SQLException {
        return targetStatement.getURL(parameterIndex);
    }

    @Override
    public void setURL(String parameterName, URL val) throws SQLException {
        targetStatement.setURL(parameterName, val);
    }

    @Override
    public void setNull(String parameterName, int sqlType) throws SQLException {
        targetStatement.setNull(parameterName, sqlType);
    }

    @Override
    public void setBoolean(String parameterName, boolean x) throws SQLException {
        targetStatement.setBoolean(parameterName, x);
    }

    @Override
    public void setByte(String parameterName, byte x) throws SQLException {
        targetStatement.setByte(parameterName, x);
    }

    @Override
    public void setShort(String parameterName, short x) throws SQLException {
        targetStatement.setShort(parameterName, x);
    }

    @Override
    public void setInt(String parameterName, int x) throws SQLException {
        targetStatement.setInt(parameterName, x);
    }

    @Override
    public void setLong(String parameterName, long x) throws SQLException {
        targetStatement.setLong(parameterName, x);
    }

    @Override
    public void setFloat(String parameterName, float x) throws SQLException {
        targetStatement.setFloat(parameterName, x);
    }

    @Override
    public void setDouble(String parameterName, double x) throws SQLException {
        targetStatement.setDouble(parameterName, x);
    }

    @Override
    public void setBigDecimal(String parameterName, BigDecimal x) throws SQLException {
        targetStatement.setBigDecimal(parameterName, x);
    }

    @Override
    public void setString(String parameterName, String x) throws SQLException {
        targetStatement.setString(parameterName, x);
    }

    @Override
    public void setBytes(String parameterName, byte[] x) throws SQLException {
        targetStatement.setBytes(parameterName, x);
    }

    @Override
    public void setDate(String parameterName, Date x) throws SQLException {
        targetStatement.setDate(parameterName, x);
    }

    @Override
    public void setTime(String parameterName, Time x) throws SQLException {
        targetStatement.setTime(parameterName, x);
    }

    @Override
    public void setTimestamp(String parameterName, Timestamp x) throws SQLException {
        targetStatement.setTimestamp(parameterName, x);
    }

    @Override
    public void setAsciiStream(String parameterName, InputStream x, int length) throws SQLException {
        targetStatement.setAsciiStream(parameterName, x, length);
    }

    @Override
    public void setBinaryStream(String parameterName, InputStream x, int length) throws SQLException {
        targetStatement.setBinaryStream(parameterName, x, length);
    }

    @Override
    public void setObject(String parameterName, Object x, int targetSqlType, int scale) throws SQLException {
        targetStatement.setObject(parameterName, x, targetSqlType, scale);
    }

    @Override
    public void setObject(String parameterName, Object x, int targetSqlType) throws SQLException {
        targetStatement.setObject(parameterName, x, targetSqlType);
    }

    @Override
    public void setObject(String parameterName, Object x) throws SQLException {
        targetStatement.setObject(parameterName, x);
    }

    @Override
    public void setCharacterStream(String parameterName, Reader reader, int length) throws SQLException {
        targetStatement.setCharacterStream(parameterName, reader, length);
    }

    @Override
    public void setDate(String parameterName, Date x, Calendar cal) throws SQLException {
        targetStatement.setDate(parameterName, x, cal);
    }

    @Override
    public void setTime(String parameterName, Time x, Calendar cal) throws SQLException {
        targetStatement.setTime(parameterName, x, cal);
    }

    @Override
    public void setTimestamp(String parameterName, Timestamp x, Calendar cal) throws SQLException {
        targetStatement.setTimestamp(parameterName, x, cal);
    }

    @Override
    public void setNull(String parameterName, int sqlType, String typeName) throws SQLException {
        targetStatement.setNull(parameterName, sqlType, typeName);
    }

    @Override
    public String getString(String parameterName) throws SQLException {
        return targetStatement.getString(parameterName);
    }

    @Override
    public boolean getBoolean(String parameterName) throws SQLException {
        return targetStatement.getBoolean(parameterName);
    }

    @Override
    public byte getByte(String parameterName) throws SQLException {
        return targetStatement.getByte(parameterName);
    }

    @Override
    public short getShort(String parameterName) throws SQLException {
        return targetStatement.getShort(parameterName);
    }

    @Override
    public int getInt(String parameterName) throws SQLException {
        return targetStatement.getInt(parameterName);
    }

    @Override
    public long getLong(String parameterName) throws SQLException {
        return targetStatement.getLong(parameterName);
    }

    @Override
    public float getFloat(String parameterName) throws SQLException {
        return targetStatement.getFloat(parameterName);
    }

    @Override
    public double getDouble(String parameterName) throws SQLException {
        return targetStatement.getDouble(parameterName);
    }

    @Override
    public byte[] getBytes(String parameterName) throws SQLException {
        return targetStatement.getBytes(parameterName);
    }

    @Override
    public Date getDate(String parameterName) throws SQLException {
        return targetStatement.getDate(parameterName);
    }

    @Override
    public Time getTime(String parameterName) throws SQLException {
        return targetStatement.getTime(parameterName);
    }

    @Override
    public Timestamp getTimestamp(String parameterName) throws SQLException {
        return targetStatement.getTimestamp(parameterName);
    }

    @Override
    public Object getObject(String parameterName) throws SQLException {
        return targetStatement.getObject(parameterName);
    }

    @Override
    public BigDecimal getBigDecimal(String parameterName) throws SQLException {
        return targetStatement.getBigDecimal(parameterName);
    }

    @Override
    public Object getObject(String parameterName, Map<String, Class<?>> map) throws SQLException {
        return targetStatement.getObject(parameterName, map);
    }

    @Override
    public Ref getRef(String parameterName) throws SQLException {
        return targetStatement.getRef(parameterName);
    }

    @Override
    public Blob getBlob(String parameterName) throws SQLException {
        return targetStatement.getBlob(parameterName);
    }

    @Override
    public Clob getClob(String parameterName) throws SQLException {
        return targetStatement.getClob(parameterName);
    }

    @Override
    public Array getArray(String parameterName) throws SQLException {
        return targetStatement.getArray(parameterName);
    }

    @Override
    public Date getDate(String parameterName, Calendar cal) throws SQLException {
        return targetStatement.getDate(parameterName, cal);
    }

    @Override
    public Time getTime(String parameterName, Calendar cal) throws SQLException {
        return targetStatement.getTime(parameterName, cal);
    }

    @Override
    public Timestamp getTimestamp(String parameterName, Calendar cal) throws SQLException {
        return targetStatement.getTimestamp(parameterName, cal);
    }

    @Override
    public URL getURL(String parameterName) throws SQLException {
        return targetStatement.getURL(parameterName);
    }

    @Override
    public RowId getRowId(int parameterIndex) throws SQLException {
        return targetStatement.getRowId(parameterIndex);
    }

    @Override
    public RowId getRowId(String parameterName) throws SQLException {
        return targetStatement.getRowId(parameterName);
    }

    @Override
    public void setRowId(String parameterName, RowId x) throws SQLException {
        targetStatement.setRowId(parameterName, x);
    }

    @Override
    public void setNString(String parameterName, String value) throws SQLException {
        targetStatement.setNString(parameterName, value);
    }

    @Override
    public void setNCharacterStream(String parameterName, Reader value, long length) throws SQLException {
        targetStatement.setNCharacterStream(parameterName, value, length);
    }

    @Override
    public void setNClob(String parameterName, NClob value) throws SQLException {
        targetStatement.setNClob(parameterName, value);
    }

    @Override
    public void setClob(String parameterName, Reader reader, long length) throws SQLException {
        targetStatement.setClob(parameterName, reader, length);
    }

    @Override
    public void setBlob(String parameterName, InputStream inputStream, long length) throws SQLException {
        targetStatement.setBlob(parameterName, inputStream, length);
    }

    @Override
    public void setNClob(String parameterName, Reader reader, long length) throws SQLException {
        targetStatement.setNClob(parameterName, reader, length);
    }

    @Override
    public NClob getNClob(int parameterIndex) throws SQLException {
        return targetStatement.getNClob(parameterIndex);
    }

    @Override
    public NClob getNClob(String parameterName) throws SQLException {
        return targetStatement.getNClob(parameterName);
    }

    @Override
    public void setSQLXML(String parameterName, SQLXML xmlObject) throws SQLException {
        targetStatement.setSQLXML(parameterName, xmlObject);
    }

    @Override
    public SQLXML getSQLXML(int parameterIndex) throws SQLException {
        return targetStatement.getSQLXML(parameterIndex);
    }

    @Override
    public SQLXML getSQLXML(String parameterName) throws SQLException {
        return targetStatement.getSQLXML(parameterName);
    }

    @Override
    public String getNString(int parameterIndex) throws SQLException {
        return targetStatement.getNString(parameterIndex);
    }

    @Override
    public String getNString(String parameterName) throws SQLException {
        return targetStatement.getNString(parameterName);
    }

    @Override
    public Reader getNCharacterStream(int parameterIndex) throws SQLException {
        return targetStatement.getNCharacterStream(parameterIndex);
    }

    @Override
    public Reader getNCharacterStream(String parameterName) throws SQLException {
        return targetStatement.getNCharacterStream(parameterName);
    }

    @Override
    public Reader getCharacterStream(int parameterIndex) throws SQLException {
        return targetStatement.getCharacterStream(parameterIndex);
    }

    @Override
    public Reader getCharacterStream(String parameterName) throws SQLException {
        return targetStatement.getCharacterStream(parameterName);
    }

    @Override
    public void setBlob(String parameterName, Blob x) throws SQLException {
        targetStatement.setBlob(parameterName, x);
    }

    @Override
    public void setClob(String parameterName, Clob x) throws SQLException {
        targetStatement.setClob(parameterName, x);
    }

    @Override
    public void setAsciiStream(String parameterName, InputStream x, long length) throws SQLException {
        targetStatement.setAsciiStream(parameterName, x, length);
    }

    @Override
    public void setBinaryStream(String parameterName, InputStream x, long length) throws SQLException {
        targetStatement.setBinaryStream(parameterName, x, length);
    }

    @Override
    public void setCharacterStream(String parameterName, Reader reader, long length) throws SQLException {
        targetStatement.setCharacterStream(parameterName, reader, length);
    }

    @Override
    public void setAsciiStream(String parameterName, InputStream x) throws SQLException {
        targetStatement.setAsciiStream(parameterName, x);
    }

    @Override
    public void setBinaryStream(String parameterName, InputStream x) throws SQLException {
        targetStatement.setBinaryStream(parameterName, x);
    }

    @Override
    public void setCharacterStream(String parameterName, Reader reader) throws SQLException {
        targetStatement.setCharacterStream(parameterName, reader);
    }

    @Override
    public void setNCharacterStream(String parameterName, Reader value) throws SQLException {
        targetStatement.setNCharacterStream(parameterName, value);
    }

    @Override
    public void setClob(String parameterName, Reader reader) throws SQLException {
        targetStatement.setClob(parameterName, reader);
    }

    @Override
    public void setBlob(String parameterName, InputStream inputStream) throws SQLException {
        targetStatement.setBlob(parameterName, inputStream);
    }

    @Override
    public void setNClob(String parameterName, Reader reader) throws SQLException {
        targetStatement.setNClob(parameterName, reader);
    }

    public <T> T getObject(int parameterIndex, Class<T> type) throws SQLException {
        try {
            return targetStatement.getObject(parameterIndex, type);
        } catch (AbstractMethodError e) {
        } catch (NoSuchMethodError e) {
        }
        return null;
    }

    public <T> T getObject(String parameterName, Class<T> type) throws SQLException {
        try {
            return targetStatement.getObject(parameterName, type);
        } catch (AbstractMethodError e) {
        } catch (NoSuchMethodError e) {
        }
        return null;
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
            return targetStatement instanceof CheckedTraceCallableStatement;
        } catch (Throwable e) {
            InterceptorInvokerHelper.handleException(e);
            return true;
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

    private boolean isPradarCallableStatement() {
        try {
            return targetStatement instanceof CheckedTraceCallableStatement;
        } catch (Throwable e) {
            return true;
        }
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
            return (T) this;
        } catch (Throwable e) {
            throw new SQLException(e);
        }
    }

    @Override
    public boolean isClosed() throws SQLException {
        try {
            return this.targetStatement.isClosed();
        } catch (AbstractMethodError e) {
            return this.isClosed;
        }
    }

    @Override
    public void setPoolable(boolean poolable) throws SQLException {
        try {
            this.targetStatement.setPoolable(poolable);
        } catch (AbstractMethodError e) {
        }
    }

    @Override
    public boolean isPoolable() throws SQLException {
        try {
            return this.targetStatement.isPoolable();
        } catch (AbstractMethodError e) {
            return false;
        }
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
