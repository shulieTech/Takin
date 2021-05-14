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

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pamirs.tro.common.util.LongToStringFormatSerialize;
import com.pamirs.tro.entity.domain.entity.TDBConf;

/**
 * 继承了TDBConf类，增加数据表信息字段
 *
 * @author shulie
 * @description
 * @create 2018-09-08 16:31:55
 */
public class TDBconfVo extends TDBConf {

    /**
     * 抽数表id
     */
    @JsonSerialize(using = LongToStringFormatSerialize.class)
    private long tadId;

    /**
     * 数据表名
     */
    private String tableName;

    /**
     * 建表语句
     */
    private String sqlDdl;

    /**
     * sql类型: 0表示sql类型为纯sql输入,1表示为文本类型
     */
    private String sqlType;

    /**
     * 取数逻辑sql(存储文件路径或者sql语句)
     */
    private String abstractSql;

    /**
     * 处理数据逻辑sql(存储文件路径或者sql语句)
     */
    private String dealSql;

    /**
     * 负责人工号
     */
    private String principalNo;

    /**
     * 是否可用
     */
    private String useYn;

    /**
     * Gets the value of tadId.
     *
     * @return the value of tadId
     * @author shulie
     * @version 1.0
     */
    public long getTadId() {
        return tadId;
    }

    /**
     * Sets the tadId.
     *
     * <p>You can use getTadId() to get the value of tadId</p>
     *
     * @param tadId tadId
     * @author shulie
     * @version 1.0
     */
    public void setTadId(long tadId) {
        this.tadId = tadId;
    }

    /**
     * Gets the value of tableName.
     *
     * @return the value of tableName
     * @author shulie
     * @version 1.0
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * Sets the tableName.
     *
     * <p>You can use getTableName() to get the value of tableName</p>
     *
     * @param tableName tableName
     * @author shulie
     * @version 1.0
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * Gets the value of sqlDdl.
     *
     * @return the value of sqlDdl
     * @author shulie
     * @version 1.0
     */
    public String getSqlDdl() {
        return sqlDdl;
    }

    /**
     * Sets the sqlDdl.
     *
     * <p>You can use getSqlDdl() to get the value of sqlDdl</p>
     *
     * @param sqlDdl sqlDdl
     * @author shulie
     * @version 1.0
     */
    public void setSqlDdl(String sqlDdl) {
        this.sqlDdl = sqlDdl;
    }

    /**
     * Gets the value of sqlType.
     *
     * @return the value of sqlType
     * @author shulie
     * @version 1.0
     */
    public String getSqlType() {
        return sqlType;
    }

    /**
     * Sets the sqlType.
     *
     * <p>You can use getSqlType() to get the value of sqlType</p>
     *
     * @param sqlType sqlType
     * @author shulie
     * @version 1.0
     */
    public void setSqlType(String sqlType) {
        this.sqlType = sqlType;
    }

    /**
     * Gets the value of abstractSql.
     *
     * @return the value of abstractSql
     * @author shulie
     * @version 1.0
     */
    public String getAbstractSql() {
        return abstractSql;
    }

    /**
     * Sets the abstractSql.
     *
     * <p>You can use getAbstractSql() to get the value of abstractSql</p>
     *
     * @param abstractSql abstractSql
     * @author shulie
     * @version 1.0
     */
    public void setAbstractSql(String abstractSql) {
        this.abstractSql = abstractSql;
    }

    /**
     * Gets the value of dealSql.
     *
     * @return the value of dealSql
     * @author shulie
     * @version 1.0
     */
    public String getDealSql() {
        return dealSql;
    }

    /**
     * Sets the dealSql.
     *
     * <p>You can use getDealSql() to get the value of dealSql</p>
     *
     * @param dealSql dealSql
     * @author shulie
     * @version 1.0
     */
    public void setDealSql(String dealSql) {
        this.dealSql = dealSql;
    }

    /**
     * Gets the value of principalNo.
     *
     * @return the value of principalNo
     * @author shulie
     * @version 1.0
     */
    public String getPrincipalNo() {
        return principalNo;
    }

    /**
     * Sets the principalNo.
     *
     * <p>You can use getPrincipalNo() to get the value of principalNo</p>
     *
     * @param principalNo principalNo
     * @author shulie
     * @version 1.0
     */
    public void setPrincipalNo(String principalNo) {
        this.principalNo = principalNo;
    }

    /**
     * Gets the value of useYn.
     *
     * @return the value of useYn
     * @author shulie
     * @version 1.0
     */
    public String getUseYn() {
        return useYn;
    }

    /**
     * Sets the useYn.
     *
     * <p>You can use getUseYn() to get the value of useYn</p>
     *
     * @param useYn useYn
     * @author shulie
     * @version 1.0
     */
    public void setUseYn(String useYn) {
        this.useYn = useYn;
    }
}
