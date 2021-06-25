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

package com.pamirs.tro.entity.domain.vo;

/**
 * @author 298403
 * 影子库数据源
 */
public class TShadowTableDatasourceVo {

    /**
     * 影子数据源id
     */
    private String shadowDatasourceId;

    /**
     * 应用id
     */
    private String applicationId;

    /**
     * 数据库ip端口
     */
    private String databaseIpport;

    /**
     * 数据库库名
     */
    private String databaseName;

    /**
     * 是否使用影子表 1 使用 0不使用
     */
    private Integer useShadowTable;

    public String getShadowDatasourceId() {
        return shadowDatasourceId;
    }

    public void setShadowDatasourceId(String shadowDatasourceId) {
        this.shadowDatasourceId = shadowDatasourceId;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getDatabaseIpport() {
        return databaseIpport;
    }

    public void setDatabaseIpport(String databaseIpport) {
        this.databaseIpport = databaseIpport;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public Integer getUseShadowTable() {
        return useShadowTable;
    }

    public void setUseShadowTable(Integer useShadowTable) {
        this.useShadowTable = useShadowTable;
    }

    @Override
    public String toString() {
        return "TShadowTableDatasourceVo{" +
            "shadowDatasourceId='" + shadowDatasourceId + '\'' +
            ", applicationId='" + applicationId + '\'' +
            ", databaseIpport='" + databaseIpport + '\'' +
            ", databaseName='" + databaseName + '\'' +
            ", useShadowTable=" + useShadowTable +
            '}';
    }

}
