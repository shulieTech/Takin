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
package com.pamirs.pradar.pressurement.datasource.util;

import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * @author fabing.zhaofb
 */
public class SqlMetaData implements Serializable {
    static final private long serialVersionUID = 1L;
    private String url;
    private String host;
    private String port;
    private String sql;
    private DbType dbType;
    private String dbName;
    private List<String> parameters;

    public static SqlMetaData copy(SqlMetaData sqlMetaData) {
        SqlMetaData copy = new SqlMetaData();
        if (null == sqlMetaData) {
            return copy;
        }
        copy.setUrl(sqlMetaData.getUrl());
        copy.setHost(sqlMetaData.getHost());
        copy.setPort(sqlMetaData.getPort());
        copy.setDbType(sqlMetaData.getDbType());
        copy.setDbName(sqlMetaData.getDbName());
        return copy;
    }

    public String getUrl() {
        return url;
    }


    public void setUrl(String url) {
        this.url = url;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public DbType getDbType() {
        return dbType;
    }

    public void setDbType(DbType dbType) {
        this.dbType = dbType;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getSql() {
        return sql;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setSql(String sql) {
        try {
            if (StringUtils.isBlank(sql)) {
                return;
            }
            sql = StringUtils.trim(sql.toUpperCase());
            if (StringUtils.equals(this.sql, sql)) {
                return;
            }
            this.sql = sql;
        } catch (Exception e) {
        }
    }

    public String getSimpleSql(int length) {
        if (this.sql.length() < length || !this.sql.startsWith("SELECT")) {
            return this.sql;
        }

        String simpleSql = "SELECT * " + this.sql.substring(this.sql.indexOf("FROM"));
        if (simpleSql.length() + 3 > length) {
            return simpleSql.substring(0, length - 3) + "[S]";
        }
        return simpleSql + "[S]";
    }

    public List<String> getParameters() {
        return parameters;
    }

    public static void checkAndAddParameter(SqlMetaData sqlMetaData, int index, Object parameter) {
        if (null != sqlMetaData) {
            sqlMetaData.addParameter(index, parameter);
        }
    }

    private void addParameter(int index, Object parameter) {
        if (this.parameters == null) {
            this.parameters = new LinkedList<String>();
        }
        try {
            if (null == parameter) {
                parameter = "null";
            }
            this.parameters.add(String.valueOf(parameter));
        } catch (Throwable e) {
            //ignore
        }
    }

    @Override
    public String toString() {
        return "SqlMetaData{" +
                "url='" + url + '\'' +
                "host='" + host + '\'' +
                ", port='" + port + '\'' +
                ", sql='" + sql + '\'' +
                ", parameters=" + parameters +
                ", dbType=" + dbType +
                ", dbName='" + dbName + '\'' +
                '}';
    }
}
