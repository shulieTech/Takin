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

package io.shulie.tro.web.data.result.datasource;

import java.util.Date;

import lombok.Data;

/**
 * @Author: fanxx
 * @Date: 2020/12/30 10:11 上午
 * @Description:
 */
@Data
public class DataSourceResult {
    private Long id;

    /**
     * 0:MYSQL
     */
    private Integer type;

    /**
     * 数据源名称
     */
    private String name;

    /**
     * 数据源地址
     */
    private String jdbcUrl;

    /**
     * 数据源用户
     */
    private String username;

    /**
     * 数据源密码
     */
    private String password;

    /**
     * 扩展字段，k-v形式存在
     */
    private String features;

    /**
     * 租户id
     */
    private Long customerId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 是否有效 0:有效;1:无效
     */
    private Boolean isDeleted;
}
