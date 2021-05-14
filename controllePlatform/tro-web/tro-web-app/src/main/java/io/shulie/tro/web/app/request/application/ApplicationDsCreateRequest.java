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

package io.shulie.tro.web.app.request.application;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @Author: fanxx
 * @Date: 2020/11/27 3:19 下午
 * @Description:
 */
@Data
@Valid
@ApiModel(value = "ApplicationDsCreateRequest", description = "应用数据源入参model")
public class ApplicationDsCreateRequest {

    /**
     * 是否是老版本
     */
    private boolean isOldVersion;

    /**
     * 应用id
     */
    @NotNull
    @ApiModelProperty(name = "applicationId", value = "应用id")
    private Long applicationId;

    /**
     * 应用名称
     */
    @ApiModelProperty(name = "applicationName", value = "应用名称")
    private String applicationName;

    /**
     * 存储类型，0：数据库 1：缓存
     */
    @NotNull
    @ApiModelProperty(name = "dbType", value = "存储类型")
    private Integer dbType;

    /**
     * 方案类型，0：影子库 1：影子表 2：影子server
     */
    @NotNull
    @ApiModelProperty(name = "dsType", value = "方案类型")
    private Integer dsType;

    /**
     * 数据库url
     */
    @ApiModelProperty(name = "url", value = "业务数据库地址")
    private String url;

    /**
     * 影子表、或其他字符型 配置
     */

    @ApiModelProperty(name = "config", value = "影子表配置")
    private String config;

    @ApiModelProperty(name = "userName", value = "用户名")
    private String userName;

    /**
     * 影子库地址
     */
    @ApiModelProperty(name = "shadowDbUrl", value = "影子库地址")
    private String shadowDbUrl;

    /**
     * 影子库用户名
     */
    @ApiModelProperty(name = "shadowDbUserName", value = "影子库用户名")
    private String shadowDbUserName;

    /**
     * 影子库密码
     */
    @ApiModelProperty(name = "shadowDbPassword", value = "影子库密码")
    private String shadowDbPassword;

    /**
     * 最小空闲连接数
     */
    @ApiModelProperty(name = "shadowDbMinIdle", value = "最小空闲连接数")
    private String shadowDbMinIdle;

    /**
     * 最大数据库连接数
     */
    @ApiModelProperty(name = "shadowDbMaxActive", value = "最大连接数")
    private String shadowDbMaxActive;

    /**
     * 状态, 0 启用, 1 禁用
     */
    @ApiModelProperty(hidden = true)
    private Integer status;

}
