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

import com.pamirs.pradar.pressurement.datasource.SqlParser;
import com.shulie.instrument.simulator.api.util.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * sql 追踪元数据库
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/12/22 10:32 上午
 */
public class SqlTraceMetaData implements Serializable {
    private final static long serialVersionUID = 1L;
    private String url;
    private String username;
    private String host;
    private String dbName;
    private String port;
    private String dbType;
    private String sql;
    private List<String> parameters;
    private List<String> tables = new ArrayList<String>();

    public static void checkAndAddParameter(SqlTraceMetaData sqlMetaData, int index, Object parameter) {
        if (null != sqlMetaData) {
            sqlMetaData.addParameter(index, parameter);
        }
    }

    private void addParameter(int index, Object parameter) {
        if (this.parameters == null) {
            this.parameters = new LinkedList<String>();
        }
        try {
            this.parameters.add(parameter == null ? null : parameter.toString());
        } catch (Throwable e) {
            //ignore
        }
    }

    public void clearParameters() {
        if (parameters != null) {
            parameters = null;
        }
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
        if (StringUtils.isNotBlank(sql)) {
            this.tables = SqlParser.getTables(sql, dbType).getTables();
        }
    }

    public List<String> getParameters() {
        return parameters;
    }

    public List<String> getTables() {
        return Collections.unmodifiableList(tables);
    }

    public String getTableNames() {
        if (CollectionUtils.isEmpty(tables)) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (String table : tables) {
            builder.append(table).append(',');
        }
        if (builder.length() > 0) {
            builder.deleteCharAt(builder.length() - 1);
        }
        return builder.toString();
    }

    public void setParameters(List<String> parameters) {
        this.parameters = parameters;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getDbType() {
        return dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }
}
