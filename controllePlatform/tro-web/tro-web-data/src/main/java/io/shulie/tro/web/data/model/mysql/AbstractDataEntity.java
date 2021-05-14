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

package io.shulie.tro.web.data.model.mysql;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 抽数表
 */
@Data
@TableName(value = "t_abstract_data")
public class AbstractDataEntity {
    /**
     * 抽数表id
     */
    @TableId(value = "TAD_ID", type = IdType.AUTO)
    private Long tadId;

    /**
     * 数据库表配置id
     */
    @TableField(value = "TDC_ID")
    private Long tdcId;

    /**
     * 数据表名
     */
    @TableField(value = "TABLE_NAME")
    private String tableName;

    /**
     * 建表语句
     */
    @TableField(value = "SQL_DDl")
    private String sqlDdl;

    /**
     * 取数逻辑sql(存储文件路径或者sql语句)
     */
    @TableField(value = "ABSTRACT_SQL")
    private String abstractSql;

    /**
     * 处理数据逻辑sql(存储文件路径或者sql语句)
     */
    @TableField(value = "DEAL_SQL")
    private String dealSql;

    /**
     * 负责人工号
     */
    @TableField(value = "PRINCIPAL_NO")
    private String principalNo;

    /**
     * 0表示sql类型为纯sql输入,1表示为文本类型
     */
    @TableField(value = "SQL_TYPE")
    private Integer sqlType;

    /**
     * 数据状态(0代表删除,1代表使用,默认为1)
     */
    @TableField(value = "DB_STATUS")
    private Integer dbStatus;

    /**
     * 该表抽数启用状态(0表示未启用,1表示启用,默认启用)
     */
    @TableField(value = "USE_YN")
    private Integer useYn;

    /**
     * 插入时间
     */
    @TableField(value = "CREATE_TIME")
    private LocalDateTime createTime;

    /**
     * 变更时间
     */
    @TableField(value = "UPDATE_TIME")
    private LocalDateTime updateTime;

    /**
     * 抽数状态(0表示未开始,1表示正在运行,2表示运行成功,3停止运行,4表示运行失败)
     */
    @TableField(value = "LOAD_STATUS")
    private Integer loadStatus;
}
