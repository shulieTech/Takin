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

package com.pamirs.tro.entity.domain.query;

import java.util.ArrayList;
import java.util.List;

import com.pamirs.tro.entity.domain.entity.TAbstractData;

/**
 * 说明: 数据库抽数查询类
 *
 * @author shulie
 * @version v1.0
 * @Date: Create in 2018/9/4 14:25
 */
public class TConf {

    private static final long serialVersionUID = 1L;

    /**
     * 　主键id
     */
    private long tdcId;

    /**
     * 　字典id
     */
    private String dictType;

    /**
     * 数据库类型
     */
    private String dbType;

    /**
     * 数据来源
     */
    private String dataSource;

    /**
     * 数据流转
     */
    private String dataTurn;

    /**
     * 数据库连接地址
     */
    private String url;

    /**
     * 数据库登陆用户名
     */
    private String username;

    /**
     * 数据库登陆密码
     */
    private String passwd;

    /**
     * 数据库驱动
     */
    private String driverClassName;

    /**
     * 数据库IP
     */
    private String databaseIp;

    /**
     * 数据库名称
     */
    private String databaseName;

    /**
     * 抽数状态
     */
    private String loadStatus;

    /**
     * 公钥
     */
    private String publicKey;

    /**
     * 抽数信息集合
     */
    private List<TAbstractData> tAbstractDataList = new ArrayList<>();

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    /**
     * Gets the value of tdcId.
     *
     * @return the value of tdcId
     * @author shulie
     * @version 1.0
     */
    public long getTdcId() {
        return tdcId;
    }

    /**
     * Sets the tdcId.
     *
     * <p>You can use getTdcId() to get the value of tdcId</p>
     *
     * @param tdcId tdcId
     * @author shulie
     * @version 1.0
     */
    public void setTdcId(long tdcId) {
        this.tdcId = tdcId;
    }

    /**
     * Gets the value of dictType.
     *
     * @return the value of dictType
     * @author shulie
     * @version 1.0
     */
    public String getDictType() {
        return dictType;
    }

    /**
     * Sets the dictType.
     *
     * <p>You can use getDictType() to get the value of dictType</p>
     *
     * @param dictType dictType
     * @author shulie
     * @version 1.0
     */
    public void setDictType(String dictType) {
        this.dictType = dictType;
    }

    /**
     * Gets the value of dbType.
     *
     * @return the value of dbType
     * @author shulie
     * @version 1.0
     */
    public String getDbType() {
        return dbType;
    }

    /**
     * Sets the dbType.
     *
     * <p>You can use getDbType() to get the value of dbType</p>
     *
     * @param dbType dbType
     * @author shulie
     * @version 1.0
     */
    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    /**
     * Gets the value of dataSource.
     *
     * @return the value of dataSource
     * @author shulie
     * @version 1.0
     */
    public String getDataSource() {
        return dataSource;
    }

    /**
     * Sets the dataSource.
     *
     * <p>You can use getDataSource() to get the value of dataSource</p>
     *
     * @param dataSource dataSource
     * @author shulie
     * @version 1.0
     */
    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Gets the value of dataTurn.
     *
     * @return the value of dataTurn
     * @author shulie
     * @version 1.0
     */
    public String getDataTurn() {
        return dataTurn;
    }

    /**
     * Sets the dataTurn.
     *
     * <p>You can use getDataTurn() to get the value of dataTurn</p>
     *
     * @param dataTurn dataTurn
     * @author shulie
     * @version 1.0
     */
    public void setDataTurn(String dataTurn) {
        this.dataTurn = dataTurn;
    }

    /**
     * Gets the value of url.
     *
     * @return the value of url
     * @author shulie
     * @version 1.0
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets the url.
     *
     * <p>You can use getUrl() to get the value of url</p>
     *
     * @param url url
     * @author shulie
     * @version 1.0
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Gets the value of username.
     *
     * @return the value of username
     * @author shulie
     * @version 1.0
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username.
     *
     * <p>You can use getUsername() to get the value of username</p>
     *
     * @param username username
     * @author shulie
     * @version 1.0
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the value of passwd.
     *
     * @return the value of passwd
     * @author shulie
     * @version 1.0
     */
    public String getPasswd() {
        return passwd;
    }

    /**
     * Sets the passwd.
     *
     * <p>You can use getPasswd() to get the value of passwd</p>
     *
     * @param passwd passwd
     * @author shulie
     * @version 1.0
     */
    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    /**
     * Gets the value of driverClassName.
     *
     * @return the value of driverClassName
     * @author shulie
     * @version 1.0
     */
    public String getDriverClassName() {
        return driverClassName;
    }

    /**
     * Sets the driverClassName.
     *
     * <p>You can use getDriverClassName() to get the value of driverClassName</p>
     *
     * @param driverClassName driverClassName
     * @author shulie
     * @version 1.0
     */
    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    /**
     * Gets the value of databaseIp.
     *
     * @return the value of databaseIp
     * @author shulie
     * @version 1.0
     */
    public String getDatabaseIp() {
        return databaseIp;
    }

    /**
     * Sets the databaseIp.
     *
     * <p>You can use getDatabaseIp() to get the value of databaseIp</p>
     *
     * @param databaseIp databaseIp
     * @author shulie
     * @version 1.0
     */
    public void setDatabaseIp(String databaseIp) {
        this.databaseIp = databaseIp;
    }

    /**
     * Gets the value of databaseName.
     *
     * @return the value of databaseName
     * @author shulie
     * @version 1.0
     */
    public String getDatabaseName() {
        return databaseName;
    }

    /**
     * Sets the databaseName.
     *
     * <p>You can use getDatabaseName() to get the value of databaseName</p>
     *
     * @param databaseName databaseName
     * @author shulie
     * @version 1.0
     */
    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    /**
     * Gets the value of loadStatus.
     *
     * @return the value of loadStatus
     * @author shulie
     * @version 1.0
     */
    public String getLoadStatus() {
        return loadStatus;
    }

    /**
     * Sets the loadStatus.
     *
     * <p>You can use getLoadStatus() to get the value of loadStatus</p>
     *
     * @param loadStatus loadStatus
     * @author shulie
     * @version 1.0
     */
    public void setLoadStatus(String loadStatus) {
        this.loadStatus = loadStatus;
    }

    public List<TAbstractData> gettAbstractDataList() {
        return tAbstractDataList;
    }

    public void settAbstractDataList(List<TAbstractData> tAbstractDataList) {
        this.tAbstractDataList = tAbstractDataList;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    @Override
    public String toString() {
        return "TConf{" +
            "tdcId=" + tdcId +
            ", dictType='" + dictType + '\'' +
            ", dbType='" + dbType + '\'' +
            ", dataSource='" + dataSource + '\'' +
            ", dataTurn='" + dataTurn + '\'' +
            ", url='" + url + '\'' +
            ", username='" + username + '\'' +
            ", passwd='" + passwd + '\'' +
            ", driverClassName='" + driverClassName + '\'' +
            ", databaseIp='" + databaseIp + '\'' +
            ", databaseName='" + databaseName + '\'' +
            ", loadStatus='" + loadStatus + '\'' +
            ", tAbstractDataList=" + tAbstractDataList +
            '}';
    }

}
