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
 * 数据库配置表
 */
@Data
@TableName(value = "t_database_conf")
public class DatabaseConfEntity {
    /**
     * 数据库配置主键
     */
    @TableId(value = "TDC_ID", type = IdType.AUTO)
    private Long tdcId;

    /**
     * 基础链路id
     */
    @TableField(value = "BASIC_LINK_ID")
    private Long basicLinkId;

    /**
     * 数据库类型
     */
    @TableField(value = "DB_TYPE")
    private Integer dbType;

    /**
     * 影子库所属系统
     */
    @TableField(value = "DATA_SOURCE")
    private String dataSource;

    /**
     * 数据流转(默认SPT,为菜鸟阿斯旺)
     */
    @TableField(value = "DATA_TURN")
    private String dataTurn;

    /**
     * 数据库连接地址
     */
    @TableField(value = "URL")
    private String url;

    /**
     * 数据库登录用户名
     */
    @TableField(value = "USERNAME")
    private String username;

    /**
     * 数据库登录密码
     */
    @TableField(value = "PASSWD")
    private String passwd;

    /**
     * 数据库驱动
     */
    @TableField(value = "DRIVER_CLASS_NAME")
    private String driverClassName;

    /**
     * 数据库ip
     */
    @TableField(value = "DATABASE_IP")
    private String databaseIp;

    /**
     * 数据库名称
     */
    @TableField(value = "DATABASE_NAME")
    private String databaseName;

    /**
     * 数据状态(0代表删除,1代表使用,默认为1)
     */
    @TableField(value = "DB_STATUS")
    private Integer dbStatus;

    /**
     * 抽数状态(0表示未开始,1表示正在运行,2表示运行结束)
     */
    @TableField(value = "LOAD_STATUS")
    private Integer loadStatus;

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
     * 数据字典类型（ID值）
     */
    @TableField(value = "DICT_TYPE")
    private String dictType;

    /**
     * 数据库公钥
     */
    @TableField(value = "PUBLIC_KEY")
    private String publicKey;

    public static final String COL_TDC_ID = "TDC_ID";

    public static final String COL_BASIC_LINK_ID = "BASIC_LINK_ID";

    public static final String COL_DB_TYPE = "DB_TYPE";

    public static final String COL_DATA_SOURCE = "DATA_SOURCE";

    public static final String COL_DATA_TURN = "DATA_TURN";

    public static final String COL_URL = "URL";

    public static final String COL_USERNAME = "USERNAME";

    public static final String COL_PASSWD = "PASSWD";

    public static final String COL_DRIVER_CLASS_NAME = "DRIVER_CLASS_NAME";

    public static final String COL_DATABASE_IP = "DATABASE_IP";

    public static final String COL_DATABASE_NAME = "DATABASE_NAME";

    public static final String COL_DB_STATUS = "DB_STATUS";

    public static final String COL_LOAD_STATUS = "LOAD_STATUS";

    public static final String COL_CREATE_TIME = "CREATE_TIME";

    public static final String COL_UPDATE_TIME = "UPDATE_TIME";

    public static final String COL_DICT_TYPE = "DICT_TYPE";

    public static final String COL_PUBLIC_KEY = "PUBLIC_KEY";
}
