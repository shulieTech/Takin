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

package io.shulie.tro.web.data.result.application;

import java.util.Date;

import lombok.Data;

/**
 * @Author: fanxx
 * @Date: 2020/11/27 10:49 上午
 * @Description:
 */
@Data
public class ApplicationDsResult {
    private Long id;

    /**
     * 应用主键
     */
    private Long applicationId;

    /**
     * 应用名称
     */
    private String applicationName;

    /**
     * 存储类型 0:数据库 1:缓存
     */
    private Integer dbType;

    /**
     * 方案类型 0:影子库 1:影子表 2:影子server
     */
    private Integer dsType;

    /**
     * 数据库url,影子表需填
     */
    private String url;

    /**
     * xml配置
     */
    private String config;

    /**
     * 解析后配置
     */
    private String parseConfig;

    /**
     * 状态 0:启用；1:禁用
     */
    private Integer status;

    /**
     * 租户id
     */
    private Long customerId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否有效 0:有效;1:无效
     */
    private Integer isDeleted;
}
