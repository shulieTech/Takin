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

package com.pamirs.tro.entity.domain.entity;

import java.util.Date;

public class TShadowTableDataSource extends BaseEntity {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_shadow_table_datasource.ID
     *
     * @mbggenerated
     */
    private Long shadowDatasourceId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_shadow_table_datasource.APPLICATION_ID
     *
     * @mbggenerated
     */
    private Long applicationId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_shadow_table_datasource.DATABASE_IPPORT
     *
     * @mbggenerated
     */
    private String databaseIpport;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_shadow_table_datasource.DATABASE_NAME
     *
     * @mbggenerated
     */
    private String databaseName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_shadow_table_datasource.USE_SHADOW_TABLE
     *
     * @mbggenerated
     */
    private Integer useShadowTable;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_shadow_table_datasource.CREATE_TIME
     *
     * @mbggenerated
     */
    private Date createTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_shadow_table_datasource.UPDATE_TIME
     *
     * @mbggenerated
     */
    private Date updateTime;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_shadow_table_datasource
     *
     * @mbggenerated
     */
    public TShadowTableDataSource(Long shadowDatasourceId, Long applicationId, String databaseIpport,
        String databaseName, Integer useShadowTable, Date createTime, Date updateTime) {
        this.shadowDatasourceId = shadowDatasourceId;
        this.applicationId = applicationId;
        this.databaseIpport = databaseIpport;
        this.databaseName = databaseName;
        this.useShadowTable = useShadowTable;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_shadow_table_datasource
     *
     * @mbggenerated
     */
    public TShadowTableDataSource() {
        super();
    }

    public Long getShadowDatasourceId() {
        return shadowDatasourceId;
    }

    public void setShadowDatasourceId(Long shadowDatasourceId) {
        this.shadowDatasourceId = shadowDatasourceId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_shadow_table_datasource.APPLICATION_ID
     *
     * @return the value of t_shadow_table_datasource.APPLICATION_ID
     * @mbggenerated
     */
    public Long getApplicationId() {
        return applicationId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_shadow_table_datasource.APPLICATION_ID
     *
     * @param applicationId the value for t_shadow_table_datasource.APPLICATION_ID
     * @mbggenerated
     */
    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_shadow_table_datasource.DATABASE_IPPORT
     *
     * @return the value of t_shadow_table_datasource.DATABASE_IPPORT
     * @mbggenerated
     */
    public String getDatabaseIpport() {
        return databaseIpport;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_shadow_table_datasource.DATABASE_IPPORT
     *
     * @param databaseIpport the value for t_shadow_table_datasource.DATABASE_IPPORT
     * @mbggenerated
     */
    public void setDatabaseIpport(String databaseIpport) {
        this.databaseIpport = databaseIpport == null ? null : databaseIpport.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_shadow_table_datasource.DATABASE_NAME
     *
     * @return the value of t_shadow_table_datasource.DATABASE_NAME
     * @mbggenerated
     */
    public String getDatabaseName() {
        return databaseName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_shadow_table_datasource.DATABASE_NAME
     *
     * @param databaseName the value for t_shadow_table_datasource.DATABASE_NAME
     * @mbggenerated
     */
    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName == null ? null : databaseName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_shadow_table_datasource.USE_SHADOW_TABLE
     *
     * @return the value of t_shadow_table_datasource.USE_SHADOW_TABLE
     * @mbggenerated
     */
    public Integer getUseShadowTable() {
        return useShadowTable;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_shadow_table_datasource.USE_SHADOW_TABLE
     *
     * @param useShadowTable the value for t_shadow_table_datasource.USE_SHADOW_TABLE
     * @mbggenerated
     */
    public void setUseShadowTable(Integer useShadowTable) {
        this.useShadowTable = useShadowTable;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_shadow_table_datasource.CREATE_TIME
     *
     * @return the value of t_shadow_table_datasource.CREATE_TIME
     * @mbggenerated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_shadow_table_datasource.CREATE_TIME
     *
     * @param createTime the value for t_shadow_table_datasource.CREATE_TIME
     * @mbggenerated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_shadow_table_datasource.UPDATE_TIME
     *
     * @return the value of t_shadow_table_datasource.UPDATE_TIME
     * @mbggenerated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_shadow_table_datasource.UPDATE_TIME
     *
     * @param updateTime the value for t_shadow_table_datasource.UPDATE_TIME
     * @mbggenerated
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}