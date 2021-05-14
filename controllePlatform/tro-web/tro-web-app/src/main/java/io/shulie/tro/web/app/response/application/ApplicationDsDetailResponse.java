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

package io.shulie.tro.web.app.response.application;

import lombok.Data;

/**
 * @Author: fanxx
 * @Date: 2020/11/27 2:34 下午
 * @Description:
 */
@Data
public class ApplicationDsDetailResponse {
    /**
     * 配置id
     */
    private Long id;

    /**
     * 应用id
     */
    private Long applicationId;

    /**
     * 应用名称
     */
    private String applicationName;

    /**
     * 存储类型，0：数据库 1：缓存
     */
    private Integer dbType;

    /**
     * 方案类型，0：影子库 1：影子表 2:影子server
     */
    private Integer dsType;

    /**
     * 服务器地址
     */
    private String url;

    /**
     * 用户名
     */
    private String userName ;

    /**
     * 配置
     */
    private String config;

    /**
     * 影子库地址
     */
    private String shadowDbUrl ;

    /**
     * 影子库用户名
     */
    private String shadowDbUserName ;

    /**
     * 影子库密码
     */
    private String shadowDbPassword ;

    /**
     * 最小空闲连接数
     */
    private String shadowDbMinIdle ;

    /**
     *最大数据库连接数
     */
    private String shadowDbMaxActive ;
}
