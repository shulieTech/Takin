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

import com.pamirs.pradar.Pradar;
import com.pamirs.pradar.exception.PressureMeasureError;
import com.pamirs.pradar.pressurement.datasource.SqlParser;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Calendar;
import java.util.Map;

/**
 * <p>{@code } instances should NOT be constructed in
 * standard programming. </p>
 *
 * <p>This constructor is public to permit tools that require a JavaBean
 * instance to operate.</p>
 */
public class PressureCallableStatement implements CallableStatement {
    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    CallableStatement callableStatement;
    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    Connection connection;

    private String dbConnectionKey;
    private String dbType;

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public PressureCallableStatement(CallableStatement callableStatement, Connection connection, String dbConnectionKey, String dbType) {
        this.callableStatement = callableStatement;
        this.connection = connection;
        this.dbConnectionKey = dbConnectionKey;
        this.dbType = dbType;
    }

    private void check() {
        if (!Pradar.isClusterTest()) {
            throw new PressureMeasureError("pressure connection get a business request.");
        }
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public void addBatch() throws SQLException {
        callableStatement.addBatch();
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    @Override
    public void clearParameters() throws SQLException {
        callableStatement.clearParameters();
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    @Override
    public boolean execute() throws SQLException {
        check();
        return callableStatement.execute();
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    @Override
    public ResultSet executeQuery() throws SQLException {
        return callableStatement.executeQuery();
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    @Override
    public int executeUpdate() throws SQLException {
        check();
        return callableStatement.executeUpdate();
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    @Override
    public ResultSetMetaData getMetaData() throws SQLException {
        return callableStatement.getMetaData();
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    @Override
    public ParameterMetaData getParameterMetaData() throws SQLException {
        return callableStatement.getParameterMetaData();
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    @Override
    public void setArray(int arg0, Array arg1) throws SQLException {
        callableStatement.setArray(arg0, arg1);

    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    @Override
    public void setAsciiStream(int arg0, InputStream arg1) throws SQLException {
        callableStatement.setAsciiStream(arg0, arg1);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    @Override
    public void setAsciiStream(int arg0, InputStream arg1, int arg2)
            throws SQLException {
        callableStatement.setAsciiStream(arg0, arg1, arg2);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    @Override
    public void setAsciiStream(int arg0, InputStream arg1, long arg2)
            throws SQLException {
        callableStatement.setAsciiStream(arg0, arg1, arg2);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    @Override
    public void setBigDecimal(int arg0, BigDecimal arg1) throws SQLException {
        callableStatement.setBigDecimal(arg0, arg1);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    @Override
    public void setBinaryStream(int arg0, InputStream arg1) throws SQLException {
        callableStatement.setBinaryStream(arg0, arg1);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    @Override
    public void setBinaryStream(int arg0, InputStream arg1, int arg2)
            throws SQLException {
        callableStatement.setBinaryStream(arg0, arg1, arg2);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    @Override
    public void setBinaryStream(int arg0, InputStream arg1, long arg2)
            throws SQLException {
        callableStatement.setBinaryStream(arg0, arg1, arg2);

    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    @Override
    public void setBlob(int arg0, Blob arg1) throws SQLException {
        callableStatement.setBlob(arg0, arg1);

    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    @Override
    public void setBlob(int arg0, InputStream arg1) throws SQLException {
        callableStatement.setBlob(arg0, arg1);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    @Override
    public void setBlob(int arg0, InputStream arg1, long arg2)
            throws SQLException {
        callableStatement.setBlob(arg0, arg1, arg2);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    @Override
    public void setBoolean(int arg0, boolean arg1) throws SQLException {
        callableStatement.setBoolean(arg0, arg1);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    @Override
    public void setByte(int arg0, byte arg1) throws SQLException {
        callableStatement.setByte(arg0, arg1);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    @Override
    public void setBytes(int arg0, byte[] arg1) throws SQLException {
        callableStatement.setBytes(arg0, arg1);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    @Override
    public void setCharacterStream(int arg0, Reader arg1) throws SQLException {
        callableStatement.setCharacterStream(arg0, arg1);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    @Override
    public void setCharacterStream(int arg0, Reader arg1, int arg2)
            throws SQLException {
        callableStatement.setCharacterStream(arg0, arg1, arg2);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    @Override
    public void setCharacterStream(int arg0, Reader arg1, long arg2)
            throws SQLException {
        callableStatement.setCharacterStream(arg0, arg1, arg2);

    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    @Override
    public void setClob(int arg0, Clob arg1) throws SQLException {
        callableStatement.setClob(arg0, arg1);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    @Override
    public void setClob(int arg0, Reader arg1) throws SQLException {
        callableStatement.setClob(arg0, arg1);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    @Override
    public void setClob(int arg0, Reader arg1, long arg2) throws SQLException {
        callableStatement.setClob(arg0, arg1, arg2);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    @Override
    public void setDate(int arg0, Date arg1) throws SQLException {
        callableStatement.setDate(arg0, arg1);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    @Override
    public void setDate(int arg0, Date arg1, Calendar arg2) throws SQLException {
        callableStatement.setDate(arg0, arg1, arg2);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    @Override
    public void setDouble(int arg0, double arg1) throws SQLException {
        callableStatement.setDouble(arg0, arg1);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    @Override
    public void setFloat(int arg0, float arg1) throws SQLException {
        callableStatement.setFloat(arg0, arg1);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    @Override
    public void setInt(int arg0, int arg1) throws SQLException {
        callableStatement.setInt(arg0, arg1);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    @Override
    public void setLong(int arg0, long arg1) throws SQLException {
        callableStatement.setLong(arg0, arg1);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    @Override
    public void setNCharacterStream(int arg0, Reader arg1) throws SQLException {
        callableStatement.setNCharacterStream(arg0, arg1);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    @Override
    public void setNCharacterStream(int arg0, Reader arg1, long arg2)
            throws SQLException {
        callableStatement.setNCharacterStream(arg0, arg1, arg2);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    @Override
    public void setNClob(int arg0, NClob arg1) throws SQLException {
        callableStatement.setNClob(arg0, arg1);

    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    @Override
    public void setNClob(int arg0, Reader arg1) throws SQLException {
        callableStatement.setNClob(arg0, arg1);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    @Override
    public void setNClob(int arg0, Reader arg1, long arg2) throws SQLException {
        callableStatement.setNClob(arg0, arg1, arg2);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    @Override
    public void setNString(int arg0, String arg1) throws SQLException {
        callableStatement.setNString(arg0, arg1);

    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    @Override
    public void setNull(int arg0, int arg1) throws SQLException {
        callableStatement.setNull(arg0, arg1);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    @Override
    public void setNull(int arg0, int arg1, String arg2) throws SQLException {
        callableStatement.setNull(arg0, arg1, arg2);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    @Override
    public void setObject(int arg0, Object arg1) throws SQLException {
        callableStatement.setObject(arg0, arg1);

    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    @Override
    public void setObject(int arg0, Object arg1, int arg2) throws SQLException {
        callableStatement.setObject(arg0, arg1, arg2);

    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    @Override
    public void setObject(int arg0, Object arg1, int arg2, int arg3)
            throws SQLException {
        callableStatement.setObject(arg0, arg1, arg2, arg3);

    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    @Override
    public void setRef(int arg0, Ref arg1) throws SQLException {
        callableStatement.setRef(arg0, arg1);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    @Override
    public void setRowId(int arg0, RowId arg1) throws SQLException {
        callableStatement.setRowId(arg0, arg1);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public void setSQLXML(int arg0, SQLXML arg1) throws SQLException {
        callableStatement.setSQLXML(arg0, arg1);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public void setShort(int arg0, short arg1) throws SQLException {
        callableStatement.setShort(arg0, arg1);

    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public void setString(int arg0, String arg1) throws SQLException {
        callableStatement.setString(arg0, arg1);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public void setTime(int arg0, Time arg1) throws SQLException {
        callableStatement.setTime(arg0, arg1);

    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public void setTime(int arg0, Time arg1, Calendar arg2) throws SQLException {
        callableStatement.setTime(arg0, arg1, arg2);

    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public void setTimestamp(int arg0, Timestamp arg1) throws SQLException {
        callableStatement.setTimestamp(arg0, arg1);

    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public void setTimestamp(int arg0, Timestamp arg1, Calendar arg2)
            throws SQLException {
        callableStatement.setTimestamp(arg0, arg1, arg2);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public void setURL(int arg0, URL arg1) throws SQLException {
        callableStatement.setURL(arg0, arg1);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public void setUnicodeStream(int arg0, InputStream arg1, int arg2)
            throws SQLException {
        callableStatement.setUnicodeStream(arg0, arg1, arg2);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public void addBatch(String arg0) throws SQLException {
        arg0 = SqlParser.replaceSchema(arg0, this.dbConnectionKey, this.dbType);
        callableStatement.addBatch(arg0);

    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public void cancel() throws SQLException {
        callableStatement.cancel();
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public void clearBatch() throws SQLException {
        callableStatement.clearBatch();
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public void clearWarnings() throws SQLException {
        callableStatement.clearWarnings();
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public void close() throws SQLException {
        callableStatement.close();
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public void closeOnCompletion() throws SQLException {
        try {
            callableStatement.closeOnCompletion();
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
    public boolean execute(String arg0) throws SQLException {
        check();
        arg0 = SqlParser.replaceSchema(arg0, this.dbConnectionKey, this.dbType);
        return callableStatement.execute(arg0);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public boolean execute(String arg0, int arg1) throws SQLException {
        check();
        arg0 = SqlParser.replaceSchema(arg0, this.dbConnectionKey, this.dbType);
        return callableStatement.execute(arg0, arg1);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public boolean execute(String arg0, int[] arg1) throws SQLException {
        check();
        arg0 = SqlParser.replaceSchema(arg0, this.dbConnectionKey, this.dbType);
        return callableStatement.execute(arg0, arg1);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public boolean execute(String arg0, String[] arg1) throws SQLException {
        check();
        arg0 = SqlParser.replaceSchema(arg0, this.dbConnectionKey, this.dbType);
        return callableStatement.execute(arg0, arg1);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public int[] executeBatch() throws SQLException {
        check();
        return callableStatement.executeBatch();
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public ResultSet executeQuery(String arg0) throws SQLException {
        check();
        arg0 = SqlParser.replaceSchema(arg0, this.dbConnectionKey, this.dbType);
        return callableStatement.executeQuery(arg0);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public int executeUpdate(String arg0) throws SQLException {
        check();
        arg0 = SqlParser.replaceSchema(arg0, this.dbConnectionKey, this.dbType);
        return callableStatement.executeUpdate(arg0);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public int executeUpdate(String arg0, int arg1) throws SQLException {
        check();
        arg0 = SqlParser.replaceSchema(arg0, this.dbConnectionKey, this.dbType);
        return callableStatement.executeUpdate(arg0, arg1);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public int executeUpdate(String arg0, int[] arg1) throws SQLException {
        check();
        arg0 = SqlParser.replaceSchema(arg0, this.dbConnectionKey, this.dbType);
        return callableStatement.executeUpdate(arg0, arg1);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public int executeUpdate(String arg0, String[] arg1) throws SQLException {
        check();
        arg0 = SqlParser.replaceSchema(arg0, this.dbConnectionKey, this.dbType);
        return callableStatement.executeUpdate(arg0, arg1);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public Connection getConnection() throws SQLException {
        return callableStatement.getConnection();
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public int getFetchDirection() throws SQLException {
        return callableStatement.getFetchDirection();
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public int getFetchSize() throws SQLException {
        return callableStatement.getFetchSize();
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public ResultSet getGeneratedKeys() throws SQLException {
        return callableStatement.getGeneratedKeys();
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public int getMaxFieldSize() throws SQLException {
        return callableStatement.getMaxFieldSize();
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public int getMaxRows() throws SQLException {
        return callableStatement.getMaxRows();
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public boolean getMoreResults() throws SQLException {
        return callableStatement.getMoreResults();
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public boolean getMoreResults(int arg0) throws SQLException {
        return callableStatement.getMoreResults();
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public int getQueryTimeout() throws SQLException {
        return callableStatement.getQueryTimeout();
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public ResultSet getResultSet() throws SQLException {
        return callableStatement.getResultSet();
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public int getResultSetConcurrency() throws SQLException {
        return callableStatement.getResultSetConcurrency();
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public int getResultSetHoldability() throws SQLException {
        return callableStatement.getResultSetHoldability();
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public int getResultSetType() throws SQLException {
        return callableStatement.getResultSetType();
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public int getUpdateCount() throws SQLException {
        return callableStatement.getUpdateCount();
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public SQLWarning getWarnings() throws SQLException {
        return callableStatement.getWarnings();
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public boolean isCloseOnCompletion() throws SQLException {
        try {
            return callableStatement.isCloseOnCompletion();
        } catch (AbstractMethodError e) {
        } catch (NoSuchMethodError e) {
        }
        return false;
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public boolean isClosed() throws SQLException {
        return callableStatement.isClosed();
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public boolean isPoolable() throws SQLException {
        return callableStatement.isPoolable();
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public void setCursorName(String arg0) throws SQLException {
        callableStatement.setCursorName(arg0);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public void setEscapeProcessing(boolean arg0) throws SQLException {
        callableStatement.setEscapeProcessing(arg0);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public void setFetchDirection(int arg0) throws SQLException {
        callableStatement.setFetchDirection(arg0);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public void setFetchSize(int arg0) throws SQLException {
        callableStatement.setFetchSize(arg0);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public void setMaxFieldSize(int arg0) throws SQLException {
        callableStatement.setMaxFieldSize(arg0);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public void setMaxRows(int arg0) throws SQLException {
        callableStatement.setMaxRows(arg0);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public void setPoolable(boolean arg0) throws SQLException {
        callableStatement.setPoolable(arg0);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public void setQueryTimeout(int arg0) throws SQLException {
        callableStatement.setQueryTimeout(arg0);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public boolean isWrapperFor(Class<?> arg0) throws SQLException {
        return callableStatement.isWrapperFor(arg0);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public <T> T unwrap(Class<T> arg0) throws SQLException {
        return callableStatement.unwrap(arg0);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public Array getArray(int arg0) throws SQLException {
        return callableStatement.getArray(arg0);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public Array getArray(String arg0) throws SQLException {
        return callableStatement.getArray(arg0);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public BigDecimal getBigDecimal(int arg0) throws SQLException {
        return callableStatement.getBigDecimal(arg0);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public BigDecimal getBigDecimal(String arg0) throws SQLException {
        return callableStatement.getBigDecimal(arg0);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public BigDecimal getBigDecimal(int arg0, int arg1) throws SQLException {
        return callableStatement.getBigDecimal(arg0, arg1);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public Blob getBlob(int arg0) throws SQLException {
        return callableStatement.getBlob(arg0);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public Blob getBlob(String arg0) throws SQLException {
        return callableStatement.getBlob(arg0);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public boolean getBoolean(int arg0) throws SQLException {
        return callableStatement.getBoolean(arg0);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public boolean getBoolean(String arg0) throws SQLException {
        return callableStatement.getBoolean(arg0);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public byte getByte(int arg0) throws SQLException {
        return callableStatement.getByte(arg0);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public byte getByte(String arg0) throws SQLException {
        return callableStatement.getByte(arg0);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public byte[] getBytes(int arg0) throws SQLException {
        return callableStatement.getBytes(arg0);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public byte[] getBytes(String arg0) throws SQLException {
        return callableStatement.getBytes(arg0);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public Reader getCharacterStream(int arg0) throws SQLException {
        return callableStatement.getCharacterStream(arg0);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public Reader getCharacterStream(String arg0) throws SQLException {
        return callableStatement.getCharacterStream(arg0);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public Clob getClob(int arg0) throws SQLException {
        return callableStatement.getClob(arg0);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public Clob getClob(String arg0) throws SQLException {
        return callableStatement.getClob(arg0);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public Date getDate(int arg0) throws SQLException {
        return callableStatement.getDate(arg0);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public Date getDate(String arg0) throws SQLException {
        return callableStatement.getDate(arg0);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public Date getDate(int arg0, Calendar arg1) throws SQLException {
        return callableStatement.getDate(arg0, arg1);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public Date getDate(String arg0, Calendar arg1) throws SQLException {
        return callableStatement.getDate(arg0, arg1);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public double getDouble(int arg0) throws SQLException {
        return callableStatement.getDouble(arg0);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public double getDouble(String arg0) throws SQLException {
        return callableStatement.getDouble(arg0);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public float getFloat(int arg0) throws SQLException {
        return callableStatement.getFloat(arg0);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public float getFloat(String arg0) throws SQLException {
        return callableStatement.getFloat(arg0);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public int getInt(int arg0) throws SQLException {
        return callableStatement.getInt(arg0);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public int getInt(String arg0) throws SQLException {
        return callableStatement.getInt(arg0);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public long getLong(int arg0) throws SQLException {
        return callableStatement.getLong(arg0);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public long getLong(String arg0) throws SQLException {
        return callableStatement.getLong(arg0);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public Reader getNCharacterStream(int arg0) throws SQLException {
        return callableStatement.getNCharacterStream(arg0);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public Reader getNCharacterStream(String arg0) throws SQLException {
        return callableStatement.getNCharacterStream(arg0);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public NClob getNClob(int arg0) throws SQLException {
        return callableStatement.getNClob(arg0);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public NClob getNClob(String arg0) throws SQLException {
        return callableStatement.getNClob(arg0);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public String getNString(int arg0) throws SQLException {
        return callableStatement.getNString(arg0);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public String getNString(String arg0) throws SQLException {
        return callableStatement.getNString(arg0);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public Object getObject(int arg0) throws SQLException {
        return callableStatement.getObject(arg0);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public Object getObject(String arg0) throws SQLException {
        return callableStatement.getObject(arg0);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public Object getObject(int arg0, Map<String, Class<?>> arg1)
            throws SQLException {
        return callableStatement.getObject(arg0, arg1);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public Object getObject(String arg0, Map<String, Class<?>> arg1)
            throws SQLException {
        return callableStatement.getObject(arg0, arg1);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public <T> T getObject(int arg0, Class<T> arg1) throws SQLException {
        try {
            return callableStatement.getObject(arg0, arg1);
        } catch (AbstractMethodError e) {
        } catch (NoSuchMethodError e) {
        }
        return null;
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public <T> T getObject(String arg0, Class<T> arg1) throws SQLException {
        try {
            callableStatement.getObject(arg0, arg1);
        } catch (AbstractMethodError e) {
        } catch (NoSuchMethodError e) {
        }
        return null;
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public Ref getRef(int arg0) throws SQLException {
        return callableStatement.getRef(arg0);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public Ref getRef(String arg0) throws SQLException {
        return callableStatement.getRef(arg0);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public RowId getRowId(int arg0) throws SQLException {
        return callableStatement.getRowId(arg0);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public RowId getRowId(String arg0) throws SQLException {
        return callableStatement.getRowId(arg0);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public SQLXML getSQLXML(int arg0) throws SQLException {
        return callableStatement.getSQLXML(arg0);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public SQLXML getSQLXML(String arg0) throws SQLException {
        return callableStatement.getSQLXML(arg0);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public short getShort(int arg0) throws SQLException {
        return callableStatement.getShort(arg0);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public short getShort(String arg0) throws SQLException {
        return callableStatement.getShort(arg0);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public String getString(int arg0) throws SQLException {
        return callableStatement.getString(arg0);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public String getString(String arg0) throws SQLException {
        return callableStatement.getString(arg0);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public Time getTime(int arg0) throws SQLException {
        return callableStatement.getTime(arg0);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public Time getTime(String arg0) throws SQLException {
        return callableStatement.getTime(arg0);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public Time getTime(int arg0, Calendar arg1) throws SQLException {
        return callableStatement.getTime(arg0, arg1);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public Time getTime(String arg0, Calendar arg1) throws SQLException {
        return callableStatement.getTime(arg0, arg1);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public Timestamp getTimestamp(int arg0) throws SQLException {
        return callableStatement.getTimestamp(arg0);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public Timestamp getTimestamp(String arg0) throws SQLException {
        return callableStatement.getTimestamp(arg0);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public Timestamp getTimestamp(int arg0, Calendar arg1) throws SQLException {
        return callableStatement.getTimestamp(arg0, arg1);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public Timestamp getTimestamp(String arg0, Calendar arg1)
            throws SQLException {
        return callableStatement.getTimestamp(arg0, arg1);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public URL getURL(int arg0) throws SQLException {
        return callableStatement.getURL(arg0);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public URL getURL(String arg0) throws SQLException {
        return callableStatement.getURL(arg0);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public void registerOutParameter(int arg0, int arg1) throws SQLException {
        callableStatement.registerOutParameter(arg0, arg1);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public void registerOutParameter(String arg0, int arg1) throws SQLException {
        callableStatement.registerOutParameter(arg0, arg1);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public void registerOutParameter(int arg0, int arg1, int arg2)
            throws SQLException {
        callableStatement.registerOutParameter(arg0, arg1, arg2);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public void registerOutParameter(int arg0, int arg1, String arg2)
            throws SQLException {
        callableStatement.registerOutParameter(arg0, arg1, arg2);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public void registerOutParameter(String arg0, int arg1, int arg2)
            throws SQLException {
        callableStatement.registerOutParameter(arg0, arg1, arg2);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public void registerOutParameter(String arg0, int arg1, String arg2)
            throws SQLException {
        callableStatement.registerOutParameter(arg0, arg1, arg2);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public void setAsciiStream(String arg0, InputStream arg1)
            throws SQLException {
        callableStatement.setAsciiStream(arg0, arg1);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public void setAsciiStream(String arg0, InputStream arg1, int arg2)
            throws SQLException {
        callableStatement.setAsciiStream(arg0, arg1, arg2);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public void setAsciiStream(String arg0, InputStream arg1, long arg2)
            throws SQLException {
        callableStatement.setAsciiStream(arg0, arg1, arg2);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public void setBigDecimal(String arg0, BigDecimal arg1) throws SQLException {
        callableStatement.setBigDecimal(arg0, arg1);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public void setBinaryStream(String arg0, InputStream arg1)
            throws SQLException {
        callableStatement.setBinaryStream(arg0, arg1);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public void setBinaryStream(String arg0, InputStream arg1, int arg2)
            throws SQLException {
        callableStatement.setBinaryStream(arg0, arg1, arg2);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public void setBinaryStream(String arg0, InputStream arg1, long arg2)
            throws SQLException {
        callableStatement.setBinaryStream(arg0, arg1, arg2);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public void setBlob(String arg0, Blob arg1) throws SQLException {
        callableStatement.setBlob(arg0, arg1);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public void setBlob(String arg0, InputStream arg1) throws SQLException {
        callableStatement.setBlob(arg0, arg1);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public void setBlob(String arg0, InputStream arg1, long arg2)
            throws SQLException {
        callableStatement.setBlob(arg0, arg1, arg2);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public void setBoolean(String arg0, boolean arg1) throws SQLException {
        callableStatement.setBoolean(arg0, arg1);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public void setByte(String arg0, byte arg1) throws SQLException {
        callableStatement.setByte(arg0, arg1);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public void setBytes(String arg0, byte[] arg1) throws SQLException {
        callableStatement.setBytes(arg0, arg1);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public void setCharacterStream(String arg0, Reader arg1)
            throws SQLException {
        callableStatement.setCharacterStream(arg0, arg1);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public void setCharacterStream(String arg0, Reader arg1, int arg2)
            throws SQLException {
        callableStatement.setCharacterStream(arg0, arg1, arg2);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public void setCharacterStream(String arg0, Reader arg1, long arg2)
            throws SQLException {
        callableStatement.setCharacterStream(arg0, arg1, arg2);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public void setClob(String arg0, Clob arg1) throws SQLException {
        callableStatement.setClob(arg0, arg1);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public void setClob(String arg0, Reader arg1) throws SQLException {
        callableStatement.setClob(arg0, arg1);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public void setClob(String arg0, Reader arg1, long arg2)
            throws SQLException {
        callableStatement.setClob(arg0, arg1, arg2);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public void setDate(String arg0, Date arg1) throws SQLException {
        callableStatement.setDate(arg0, arg1);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public void setDate(String arg0, Date arg1, Calendar arg2)
            throws SQLException {
        callableStatement.setDate(arg0, arg1, arg2);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public void setDouble(String arg0, double arg1) throws SQLException {
        callableStatement.setDouble(arg0, arg1);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public void setFloat(String arg0, float arg1) throws SQLException {
        callableStatement.setFloat(arg0, arg1);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public void setInt(String arg0, int arg1) throws SQLException {
        callableStatement.setInt(arg0, arg1);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public void setLong(String arg0, long arg1) throws SQLException {
        callableStatement.setLong(arg0, arg1);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public void setNCharacterStream(String arg0, Reader arg1)
            throws SQLException {
        callableStatement.setNCharacterStream(arg0, arg1);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public void setNCharacterStream(String arg0, Reader arg1, long arg2)
            throws SQLException {
        callableStatement.setNCharacterStream(arg0, arg1, arg2);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public void setNClob(String arg0, NClob arg1) throws SQLException {
        callableStatement.setNClob(arg0, arg1);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public void setNClob(String arg0, Reader arg1) throws SQLException {
        callableStatement.setNClob(arg0, arg1);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public void setNClob(String arg0, Reader arg1, long arg2)
            throws SQLException {
        callableStatement.setNClob(arg0, arg1, arg2);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public void setNString(String arg0, String arg1) throws SQLException {
        callableStatement.setNString(arg0, arg1);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public void setNull(String arg0, int arg1) throws SQLException {
        callableStatement.setNull(arg0, arg1);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public void setNull(String arg0, int arg1, String arg2) throws SQLException {
        callableStatement.setNull(arg0, arg1, arg2);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public void setObject(String arg0, Object arg1) throws SQLException {
        callableStatement.setObject(arg0, arg1);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public void setObject(String arg0, Object arg1, int arg2)
            throws SQLException {
        callableStatement.setObject(arg0, arg1, arg2);

    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public void setObject(String arg0, Object arg1, int arg2, int arg3)
            throws SQLException {
        callableStatement.setObject(arg0, arg1, arg2, arg3);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public void setRowId(String arg0, RowId arg1) throws SQLException {
        callableStatement.setRowId(arg0, arg1);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public void setSQLXML(String arg0, SQLXML arg1) throws SQLException {
        callableStatement.setSQLXML(arg0, arg1);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public void setShort(String arg0, short arg1) throws SQLException {
        callableStatement.setShort(arg0, arg1);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public void setString(String arg0, String arg1) throws SQLException {
        callableStatement.setString(arg0, arg1);

    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public void setTime(String arg0, Time arg1) throws SQLException {
        callableStatement.setTime(arg0, arg1);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public void setTime(String arg0, Time arg1, Calendar arg2)
            throws SQLException {
        callableStatement.setTime(arg0, arg1, arg2);

    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public void setTimestamp(String arg0, Timestamp arg1) throws SQLException {
        callableStatement.setTimestamp(arg0, arg1);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public void setTimestamp(String arg0, Timestamp arg1, Calendar arg2)
            throws SQLException {
        callableStatement.setTimestamp(arg0, arg1, arg2);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public void setURL(String arg0, URL arg1) throws SQLException {
        callableStatement.setURL(arg0, arg1);
    }

    /**
     * <p>{@code } instances should NOT be constructed in
     * standard programming. </p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public boolean wasNull() throws SQLException {
        try {
            return callableStatement.wasNull();
        } catch (AbstractMethodError e) {
        } catch (NoSuchMethodError e) {
        }
        return false;
    }

}
