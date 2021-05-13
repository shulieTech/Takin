/*
 * Copyright 2021 Shulie Technology, Co.Ltd
 * Email: shulie@shulie.io
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.shulie.tro.web.config.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import io.shulie.tro.web.config.enums.ShadowDSType;

/**
 * @author shiyajian
 * create: 2020-09-15
 */
public class ShadowDB implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 业务的数据库地址
     */
    private String bizJdbcUrl;

    /**
     * 业务的用户名
     */
    private String bizUserName;

    /**
     * 影子库还是影子表
     */
    private ShadowDSType type;

    /**
     * 如果是影子表，这是影子表的配置
     */
    private ShadowTableConfig shadowTableConfig;

    /**
     * 如果是影子库模式，这是影子库的 DataSource 配置
     */
    private ShadowSchemaConfig shadowSchemaConfig;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ShadowDSType getType() {
        return type;
    }

    public void setType(ShadowDSType type) {
        this.type = type;
    }

    public String getBizJdbcUrl() {
        return bizJdbcUrl;
    }

    public void setBizJdbcUrl(String bizJdbcUrl) {
        this.bizJdbcUrl = bizJdbcUrl;
    }

    public String getBizUserName() {
        return bizUserName;
    }

    public void setBizUserName(String bizUserName) {
        this.bizUserName = bizUserName;
    }

    public ShadowTableConfig getShadowTableConfig() {
        return shadowTableConfig;
    }

    public void setShadowTableConfig(ShadowTableConfig shadowTableConfig) {
        this.shadowTableConfig = shadowTableConfig;
    }

    public ShadowSchemaConfig getShadowSchemaConfig() {
        return shadowSchemaConfig;
    }

    public void setShadowSchemaConfig(ShadowSchemaConfig shadowSchemaConfig) {
        this.shadowSchemaConfig = shadowSchemaConfig;
    }

    public static class ShadowTableConfig {
        private List<String> tableNames;

        public List<String> getTableNames() {
            return tableNames;
        }

        public void setTableNames(List<String> tableNames) {
            this.tableNames = tableNames;
        }
    }

    public static class ShadowSchemaConfig {
        private String driverClassName;
        private String url;
        private String username;
        private String schema;
        private String password;
        private Map<String, String> properties;

        public String getDriverClassName() {
            return driverClassName;
        }

        public void setDriverClassName(String driverClassName) {
            this.driverClassName = driverClassName;
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

        public String getSchema() {
            return schema;
        }

        public void setSchema(String schema) {
            this.schema = schema;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public Map<String, String> getProperties() {
            return properties;
        }

        public void setProperties(Map<String, String> properties) {
            this.properties = properties;
        }
    }
}
