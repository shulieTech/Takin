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

package com.pamirs.tro.entity.domain.entity.settle;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class AccountBook {
    private Long id;

    private Long uid;

    private Long accId;

    private Long parentBookId;

    private BigDecimal balance;

    private BigDecimal lockBalance;

    private BigDecimal totalBalance;

    private Integer subject;

    private Integer direct;

    private String rule;

    private BigDecimal ruleBalance;

    private Date startTime;

    private Date endTime;

    private Integer status;

    private Integer version;

    private Integer isDeleted;

    private Long tags;

    private Date gmtCreate;

    private Date gmtUpdate;

    private String features;
}
