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

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * t_application_api_manage
 *
 * @author
 */
@Data
public class ApplicationApiManage implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    private Long id;
    /**
     * api
     */
    private String api;
    /**
     * 应用主键
     */
    private Long applicationId;
    /**
     * 应用名称
     */
    private String applicationName;
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
    private Byte isDeleted;

    private Long customerId;

    private Long userId;

    private String requestMethod;

    private Integer isAgentRegiste;
}
