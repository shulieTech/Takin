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
public class AccountBalance {
    private Long id;

    private Long accId;

    private Long bookId;

    private BigDecimal amount;

    private BigDecimal balance;

    private BigDecimal lockBalance;

    private Integer subject;

    private Integer direct;

    private String remark;

    private Long parentBookId;

    private String sceneCode;

    private Integer status;

    private Date accTime;

    private String outerId;

    private Integer isDeleted;

    private Long tags;

    private Date gmtCreate;

    private Date gmtUpdate;

    private String features;
}
