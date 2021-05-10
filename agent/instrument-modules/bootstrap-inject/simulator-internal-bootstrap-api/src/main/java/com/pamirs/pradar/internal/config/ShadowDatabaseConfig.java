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
package com.pamirs.pradar.internal.config;

import java.sql.SQLException;
import java.util.Collections;
import java.util.Map;

/**
 * 影子数据源配置
 *
 * @author xiaobin.zfb | xiaobin@shulie.io
 * @since 2020/9/10 12:24 下午
 */
public class ShadowDatabaseConfig {
    public static final String JTDS = "jtds";

    public static final String MOCK = "mock";

    public static final String HSQL = "hsql";

    public static final String DB2 = "db2";

    public static final String DB2_DRIVER = "COM.ibm.db2.jdbc.app.DB2Driver";

    public static final String POSTGRESQL = "postgresql";

    public static final String SYBASE = "sybase";

    public static final String SQL_SERVER = "sqlserver";

    public static final String ORACLE = "oracle";
    public static final String ORACLE_DRIVER = "oracle.jdbc.driver.OracleDriver";

    public static final String ALI_ORACLE = "AliOracle";
    public static final String ALI_ORACLE_DRIVER = "com.alibaba.jdbc.AlibabaDriver";

    public static final String MYSQL = "mysql";
    public static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";

    public static final String MARIADB = "mariadb";
    public static final String MARIADB_DRIVER = "org.mariadb.jdbc.Driver";

    public static final String DERBY = "derby";

    public static final String HBASE = "hbase";

    public static final String HIVE = "hive";

    public static final String H2 = "h2";

    public static final String H2_DRIVER = "org.h2.Driver";

    /**
     * 阿里云odps
     */
    public static final String ODPS = "odps";

    /**
     * Log4JDBC
     */
    public static final String LOG4JDBC = "log4jdbc";
    public static final String LOG4JDBC_DRIVER = "net.sf.log4jdbc.DriverSpy";
    /**
     * 应用名称
     */
    private String applicationName;

    /**
     * 数据源类型 0:影子库 1:影子表
     */
    private int dsType;

    /**
     * 业务库url
     */
    private String url;

    /**
     * 业务库用户名
     */
    private String username;

    /**
     * 业务库的schema
     */
    private String schema;

    /**
     * 影子库url
     */
    private String shadowUrl;

    /**
     * 影子库驱动名称
     */
    private String shadowDriverClassName;

    /**
     * 影子库用户名
     */
    private String shadowUsername;

    /**
     * 影子库密码
     */
    private String shadowPassword;

    /**
     * 影子库schema
     */
    private String shadowSchema;

    /**
     * 业务表影子表配置，配置业务表与影子表的映射关系
     * key: 业务表名称
     * value: 影子表名称
     */
    private Map<String, String> businessShadowTables = Collections.EMPTY_MAP;

    /**
     * 其他配置信息
     */
    private Map<String, String> properties;

    public int getDsType() {
        return dsType;
    }

    public void setDsType(int dsType) {
        this.dsType = dsType;
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

    public String getShadowUrl() {
        return shadowUrl;
    }

    public void setShadowUrl(String shadowUrl) {
        this.shadowUrl = shadowUrl;
        try {
            this.shadowDriverClassName = getDriverClassName(this.shadowUrl);
        } catch (SQLException e) {
        }
    }

    private static String getDriverClassName(String rawUrl) throws SQLException {
        if (rawUrl.startsWith("jdbc:derby:")) {
            return "org.apache.derby.jdbc.EmbeddedDriver";
        } else if (rawUrl.startsWith("jdbc:mysql:")) {
            return MYSQL_DRIVER;
        } else if (rawUrl.startsWith("jdbc:log4jdbc:")) {
            return LOG4JDBC_DRIVER;
        } else if (rawUrl.startsWith("jdbc:mariadb:")) {
            return MARIADB_DRIVER;
        } else if (rawUrl.startsWith("jdbc:oracle:") //
                || rawUrl.startsWith("JDBC:oracle:")
        ) {
            return ORACLE_DRIVER;
        } else if (rawUrl.startsWith("jdbc:alibaba:oracle:")) {
            return ALI_ORACLE_DRIVER;
        } else if (rawUrl.startsWith("jdbc:microsoft:")) {
            return "com.microsoft.jdbc.sqlserver.SQLServerDriver";
        } else if (rawUrl.startsWith("jdbc:sqlserver:")) {
            return "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        } else if (rawUrl.startsWith("jdbc:sybase:Tds:")) {
            return "com.sybase.jdbc2.jdbc.SybDriver";
        } else if (rawUrl.startsWith("jdbc:jtds:")) {
            return "net.sourceforge.jtds.jdbc.Driver";
        } else if (rawUrl.startsWith("jdbc:fake:") || rawUrl.startsWith("jdbc:mock:")) {
            return "com.alibaba.druid.mock.MockDriver";
        } else if (rawUrl.startsWith("jdbc:postgresql:")) {
            return "org.postgresql.Driver";
        } else if (rawUrl.startsWith("jdbc:hsqldb:")) {
            return "org.hsqldb.jdbcDriver";
        } else if (rawUrl.startsWith("jdbc:db2:")) {
            return DB2_DRIVER;
        } else if (rawUrl.startsWith("jdbc:sqlite:")) {
            return "org.sqlite.JDBC";
        } else if (rawUrl.startsWith("jdbc:ingres:")) {
            return "com.ingres.jdbc.IngresDriver";
        } else if (rawUrl.startsWith("jdbc:h2:")) {
            return H2_DRIVER;
        } else if (rawUrl.startsWith("jdbc:mckoi:")) {
            return "com.mckoi.JDBCDriver";
        } else if (rawUrl.startsWith("jdbc:cloudscape:")) {
            return "COM.cloudscape.core.JDBCDriver";
        } else if (rawUrl.startsWith("jdbc:informix-sqli:")) {
            return "com.informix.jdbc.IfxDriver";
        } else if (rawUrl.startsWith("jdbc:timesten:")) {
            return "com.timesten.jdbc.TimesTenDriver";
        } else if (rawUrl.startsWith("jdbc:as400:")) {
            return "com.ibm.as400.access.AS400JDBCDriver";
        } else if (rawUrl.startsWith("jdbc:sapdb:")) {
            return "com.sap.dbtech.jdbc.DriverSapDB";
        } else if (rawUrl.startsWith("jdbc:JSQLConnect:")) {
            return "com.jnetdirect.jsql.JSQLDriver";
        } else if (rawUrl.startsWith("jdbc:JTurbo:")) {
            return "com.newatlanta.jturbo.driver.Driver";
        } else if (rawUrl.startsWith("jdbc:firebirdsql:")) {
            return "org.firebirdsql.jdbc.FBDriver";
        } else if (rawUrl.startsWith("jdbc:interbase:")) {
            return "interbase.interclient.Driver";
        } else if (rawUrl.startsWith("jdbc:pointbase:")) {
            return "com.pointbase.jdbc.jdbcUniversalDriver";
        } else if (rawUrl.startsWith("jdbc:edbc:")) {
            return "ca.edbc.jdbc.EdbcDriver";
        } else if (rawUrl.startsWith("jdbc:mimer:multi1:")) {
            return "com.mimer.jdbc.Driver";
        } else {
            throw new SQLException("unkow jdbc driver : " + rawUrl);
        }
    }

    public String getShadowDriverClassName() {
        return shadowDriverClassName;
    }

    public void setShadowDriverClassName(String shadowDriverClassName) {
        this.shadowDriverClassName = shadowDriverClassName;
    }

    public String getShadowUsername() {
        return shadowUsername;
    }

    public void setShadowUsername(String shadowUsername) {
        this.shadowUsername = shadowUsername;
    }

    public String getShadowPassword() {
        return shadowPassword;
    }

    public void setShadowPassword(String shadowPassword) {
        this.shadowPassword = shadowPassword;
    }

    public Map<String, String> getBusinessShadowTables() {
        return businessShadowTables;
    }

    public void setBusinessShadowTables(Map<String, String> businessShadowTables) {
        this.businessShadowTables = businessShadowTables;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getShadowSchema() {
        return shadowSchema;
    }

    public void setShadowSchema(String shadowSchema) {
        this.shadowSchema = shadowSchema;
    }

    public boolean isShadowDatabase() {
        return dsType == 0;
    }

    public boolean isShadowTable() {
        return dsType == 1;
    }

    public String getProperty(String key) {
        if (properties == null) {
            return null;
        }
        String value = properties.get(key);
        return value == null ? null : value.trim();
    }

    public Boolean getBooleanProperty(String key) {
        String property = getProperty(key);
        if (property == null) {
            return null;
        }
        return Boolean.valueOf(property);
    }

    public Boolean getBooleanProperty(String key, Boolean defaultValue) {
        String property = getProperty(key);
        if (property == null) {
            return defaultValue;
        }
        return Boolean.valueOf(property);
    }

    public Integer getIntProperty(String key) {
        String property = getProperty(key);
        if (property == null) {
            return null;
        }
        if (isDigits(property)) {
            return Integer.valueOf(property);
        }
        return null;
    }

    private static boolean isDigits(final String cs) {
        if (cs == null || "".equals(cs.trim())) {
            return false;
        }
        final int sz = cs.length();
        for (int i = 0; i < sz; i++) {
            if (!Character.isDigit(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public Integer getIntProperty(String key, Integer defaultValue) {
        String property = getProperty(key);
        if (property == null) {
            return defaultValue;
        }
        if (isDigits(property)) {
            return Integer.valueOf(property);
        }
        return defaultValue;
    }

    public Long getLongProperty(String key) {
        String property = getProperty(key);
        if (property == null) {
            return null;
        }
        if (isDigits(property)) {
            return Long.valueOf(property);
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ShadowDatabaseConfig that = (ShadowDatabaseConfig) o;

        if (dsType != that.dsType) return false;
        if (applicationName != null ? !applicationName.equals(that.applicationName) : that.applicationName != null)
            return false;
        if (url != null ? !url.equals(that.url) : that.url != null) return false;
        if (username != null ? !username.equals(that.username) : that.username != null) return false;
        if (schema != null ? !schema.equals(that.schema) : that.schema != null) return false;
        if (shadowUrl != null ? !shadowUrl.equals(that.shadowUrl) : that.shadowUrl != null) return false;
        if (shadowDriverClassName != null ? !shadowDriverClassName.equals(that.shadowDriverClassName) : that.shadowDriverClassName != null)
            return false;
        if (shadowUsername != null ? !shadowUsername.equals(that.shadowUsername) : that.shadowUsername != null)
            return false;
        if (shadowPassword != null ? !shadowPassword.equals(that.shadowPassword) : that.shadowPassword != null)
            return false;
        if (shadowSchema != null ? !shadowSchema.equals(that.shadowSchema) : that.shadowSchema != null) return false;
        if (businessShadowTables != null ? !businessShadowTables.equals(that.businessShadowTables) : that.businessShadowTables != null)
            return false;
        return properties != null ? properties.equals(that.properties) : that.properties == null;
    }

    @Override
    public int hashCode() {
        int result = applicationName != null ? applicationName.hashCode() : 0;
        result = 31 * result + dsType;
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (schema != null ? schema.hashCode() : 0);
        result = 31 * result + (shadowUrl != null ? shadowUrl.hashCode() : 0);
        result = 31 * result + (shadowDriverClassName != null ? shadowDriverClassName.hashCode() : 0);
        result = 31 * result + (shadowUsername != null ? shadowUsername.hashCode() : 0);
        result = 31 * result + (shadowPassword != null ? shadowPassword.hashCode() : 0);
        result = 31 * result + (shadowSchema != null ? shadowSchema.hashCode() : 0);
        result = 31 * result + (businessShadowTables != null ? businessShadowTables.hashCode() : 0);
        result = 31 * result + (properties != null ? properties.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ShadowDatabaseConfig{" +
                "applicationName='" + applicationName + '\'' +
                ", dsType=" + dsType +
                ", url='" + url + '\'' +
                ", username='" + username + '\'' +
                ", schema='" + schema + '\'' +
                ", shadowUrl='" + shadowUrl + '\'' +
                ", shadowDriverClassName='" + shadowDriverClassName + '\'' +
                ", shadowUsername='" + shadowUsername + '\'' +
                ", shadowPassword='" + shadowPassword + '\'' +
                ", shadowSchema='" + shadowSchema + '\'' +
                ", businessShadowTables=" + businessShadowTables +
                ", properties=" + properties +
                '}';
    }
}

