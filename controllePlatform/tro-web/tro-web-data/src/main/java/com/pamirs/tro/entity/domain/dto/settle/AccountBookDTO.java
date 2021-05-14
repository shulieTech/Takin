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

package com.pamirs.tro.entity.domain.dto.settle;

import java.io.Serializable;
import java.math.BigDecimal;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @ClassName AccountBookDTO
 * @Description
 * @Author qianshui
 * @Date 2020/5/12 下午7:56
 */
@Data
@ApiModel("流量账本")
public class AccountBookDTO implements Serializable {

    private static final long serialVersionUID = -1791348292712405730L;

    private BigDecimal balance;

    private BigDecimal lockBalance;

    private BigDecimal totalBalance;
}
