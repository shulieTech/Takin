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

package com.pamirs.tro.entity.domain.entity.cloudserver;

import java.util.Date;

import lombok.Data;

@Data
public class CloudAccount {
    private Long id;

    private Long platformId;

    private String platformName;

    private String account;

    /**
     * true:启用 ；false： 禁用
     */
    private Boolean status;

    private Boolean isDelete;

    private Date gmtCreate;

    private Date gmtUpdate;

    private String authorizeParam;

}
