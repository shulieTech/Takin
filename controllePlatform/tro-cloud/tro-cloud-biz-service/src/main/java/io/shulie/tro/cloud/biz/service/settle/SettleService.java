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

package io.shulie.tro.cloud.biz.service.settle;

import java.math.BigDecimal;

import com.pamirs.tro.entity.domain.vo.settle.AccountTradeRequest;

/**
 * @Author 莫问
 * @Date 2020-05-14
 */
public interface SettleService {

    /**
     * 冻结账户余额
     */
    void lockAccount(AccountTradeRequest request);

    /**
     * 释放账户余额
     *
     * @param uid
     * @param outerId
     */
    void unLockAccount(Long uid, String outerId);

    /**
     * 结算
     *
     * @param request
     */
    BigDecimal settle(AccountTradeRequest request);

}
